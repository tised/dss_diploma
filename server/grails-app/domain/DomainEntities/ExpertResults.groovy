package DomainEntities

class ExpertResults {

	    static mapping = {
        table 'expert_results'
        // version is set to false, because this isn't available by default for legacy databases
        version false
        id generator:'identity', column:'id'
    }

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
