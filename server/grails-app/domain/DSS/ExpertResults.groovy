package DSS

class ExpertResults {

    Integer id
    Integer idExpert
    String result_vector
    Integer idProblem

    static constraints = {
        id(max: 2147483647)
        idExpert(max: 2147483647)
        result_vector(size: 0..65535)
        idProblem(max: 2147483647)
    }
}
