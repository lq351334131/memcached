package com.etocrm.memcached;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.etocrm.memcached.dao")
public class MemcachedProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemcachedProjectApplication.class, args);
    }

}
