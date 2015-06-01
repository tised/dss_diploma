package DomainEntities

class Experts {

	    static mapping = {
        table 'experts'
        // version is set to false, because this isn't available by default for legacy databases
        version false
        id generator:'identity', column:'id'
    }

    Integer id
    String nameExpert
    String surName
    String email
    String password


    static constraints = {
        id(max: 2147483647)
        nameExpert(size: 0..50)
        surName(size: 0..50)
        password(size: 0..32)

    }
}
