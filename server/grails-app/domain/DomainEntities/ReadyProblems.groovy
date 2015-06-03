package DomainEntities

class ReadyProblems {

    static mapping = {
        table 'ready_problems'
        // version is set to false, because this isn't available by default for legacy databases
        version false
        id generator:'identity', column:'id'
    }

    Integer id
    Integer idExpert
    Integer idProblem

    static constraints = {

        id(max: 2147483647)
        idExpert(max: 2147483647)
        idProblem(max: 2147483647)
    }
}
