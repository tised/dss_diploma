package DSS

class Experts {

    Integer id
    String nameExpert
    String surName
    String email


    static constraints = {
        id(max: 2147483647)
        nameExpert(size: 0..50)
        surName(size: 0..50)

    }
}
