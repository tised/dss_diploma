package DomainEntities

class MaiStorage {

	    static mapping = {
        table 'mai_storage'
        // version is set to false, because this isn't available by default for legacy databases
        version false
        id generator:'identity', column:'id'
    }

    Integer id
    String problem
    String criterias
    String alternatives
    Integer idUser
    Boolean isClosed

    static constraints = {

        id(max: 2147483647)
        problem(size: 0..65535)
        criterias(size: 0..65535)
        alternatives(size: 0..65535)
        idUser(max: 2147483647)
        isClosed()
    }
}
