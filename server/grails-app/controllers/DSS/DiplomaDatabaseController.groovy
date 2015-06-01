package DSS

import DomainEntities.ExpertResults
import DomainEntities.Experts
import DomainEntities.MaiStorage
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang.RandomStringUtils
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

class DiplomaDatabaseController {

    def index() {

        render "database contr"
    }

    def getAllProblems(){

        def problems = MaiStorage.getAll()

        JSONArray answer = new JSONArray()

        for (MaiStorage p in problems){

            JSONObject prob = new JSONObject()

            def valofExperts = ExpertResults.findAllByIdProblem(p.id)

            prob.put("id_problem",p.id)
            prob.put("val_of_experts",valofExperts.unique().size())
            answer.put(prob)
        }

        render(contentType: "application/json") {
            answer
        }
    }

    def checkLogin(){

        /*params:
        * login
        * password*/

        println "CHECK LOGIN PARAMS === " + params

        def experts = Experts.getAll()
        String result = "NEOK";
        for (Experts expert : experts){

            if (expert.getEmail().equals(params.email) &&
                     expert.getPassword().equals(params.password))
                result = "OK" //new JSONObject(expert.toString())
        }

        render result
     }

    def getCurrentProblem(){

        /*params:
        * id_problem*/

        def resultsForCurProblem = ExpertResults.findAllByIdProblem(Integer.valueOf(params.id_problem))
        JSONArray answer = new JSONArray()

        for(ExpertResults res in resultsForCurProblem){

            JSONObject curRes = new JSONObject()
            curRes.put("res_vector",new JSONArray(res.result_vector))
            curRes.put("id_expert",res.idExpert)
            curRes.put("val_of_answers",resultsForCurProblem.size())
            answer.put(curRes)
        }

        render(contentType: "application/json") {
            answer
        }


    }

    def setExpertResult(){

        /*
        * params:
        * alternative_priority
        * id_expert*/

        def result = new ExpertResults()
        String priority_vector = String.valueOf(params.alternative_priority);


        StringBuilder sb = new StringBuilder(priority_vector)

        sb.deleteCharAt(0)
        sb.deleteCharAt(sb.size()-1)
        String res =  sb.toString();

        result.setIdExpert(Integer.valueOf(params.id_expert))
        result.setResult_vector(res)
        //result.setIdProblem(Integer.valueOf(params.id_problem))
        result.setIdProblem(1)
        result.save(flush: true)

        render "saved result"
    }

    def addExpert(){
        /*
        * email
        * name_expert
        * sur_name
        * password*/

        println "ADD EXPERT PARAMS === " + params
        Experts expert = new Experts()
        expert.setEmail(params.email)
        expert.setNameExpert(params.name)
        expert.setSurName(params.surName)
        expert.setPassword(generatePassword())
        expert.save(flush: true)

        render "OK"
     }

    private String generatePassword(){


        String password = RandomStringUtils.randomAlphanumeric(8)

//        String host = "smtp.gmail.com";
//        Properties props = System.getProperties();
//        props.put("mail.smtp.starttls.enable",true);
//        /* mail.smtp.ssl.trust is needed in script to avoid error "Could not convert socket to TLS"  */
//        props.setProperty("mail.smtp.ssl.trust", host);
//        props.put("mail.smtp.auth", true);
//        props.put("mail.smtp.host", host);
//        props.put("mail.smtp.user", "ololo@gmail.com");
//        props.put("mail.smtp.password", "");
//        props.put("mail.smtp.port", "465");
//
//        Session session = Session.getDefaultInstance(props, null);
//        MimeMessage message = new MimeMessage(session);
//        message.setFrom(new InternetAddress("ololo@gmail.com"));
//
//        InternetAddress toAddress = new InternetAddress("pescala66@gmail.com");
//
//        message.addRecipient(Message.RecipientType.TO, toAddress);
//
//        message.setSubject("your pass");
//        message.setText(password);
//
//        Transport transport = session.getTransport("smtp");
//
//        transport.connect(host, "pescala66@gmail.com", password);
//
//        transport.sendMessage(message, message.getAllRecipients());
//        transport.close();

        return toMd5(password)
    }

    private String toMd5(String value){

        String salt = "onvoghetilop";

        return DigestUtils.md5Hex( value + salt )
    }
}
