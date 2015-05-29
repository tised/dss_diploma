package DSS

class DiplomaClients {

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
