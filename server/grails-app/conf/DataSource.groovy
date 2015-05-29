dataSource {
    pooled = true
//    driverClassName = "org.h2.Driver"
//    username = "sa"
//    password = ""
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}

// environment specific settings
environments {
    development {
        dataSource {

            dbCreate = 'update'
            driverClassName = "com.mysql.jdbc.Driver"
            username = "root"
            password = ""
            url = "jdbc:mysql://dev-mysql:3306/diploma?useUnicode=true&characterEncoding=utf8"
//            username = "cp2cms"
//            password = "cp2cmspass"
//            url = "jdbc:mysql://192.168.100.1:3306/diploma10?useUnicode=true&characterEncoding=utf8"


        }
    }
    test {
        dataSource {
            dbCreate = 'update'
            driverClassName = "com.mysql.jdbc.Driver"
            username = "root"
            password = ""
            url = "jdbc:mysql:/localhost:3306/diploma?useUnicode=true&characterEncoding=utf8"
        }
    }
    production {
        dataSource {
            dbCreate = 'update'
            driverClassName = "com.mysql.jdbc.Driver"
            username = "diploma"
            password = "diploma2pass"
            url = "jdbc:mysql://localhost:3306/diploma?useUnicode=true&characterEncoding=utf8"
        }
    }
}