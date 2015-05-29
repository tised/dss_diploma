package DSS

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

class DiplomaDatabaseController {

    def index() {}

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
}
