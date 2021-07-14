package com.etocrm.memcached.dao;


import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SensitiveWordDao {

    Set<String> getSensitiveWord();
}
