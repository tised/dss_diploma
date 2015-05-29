package DSS

class MaiStorage {

    Integer id
    String problem
    String criterias
    String alternatives
    Integer idUser

    static constraints = {

        id(max: 2147483647)
        problem(size: 0..65535)
        criterias(size: 0..65535)
        alternatives(size: 0..65535)
        idUser(max: 2147483647)
    }
}
