package DSS

class DiplomaClients {
	
	    static mapping = {
        table 'diploma_clients'
        // version is set to false, because this isn't available by default for legacy databases
        version false
        id generator:'identity', column:'id'
    }
	
    Integer id
    String name
    String surName
    String email


    static constraints = {
        id(max: 2147483647)
        name(size: 0..50)
        surName(size: 0..50)
    }
}
