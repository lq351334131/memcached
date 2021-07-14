package com.etocrm.memcached.service.impl;

import com.etocrm.memcached.dao.SensitiveWordDao;
import com.etocrm.memcached.service.SensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SensitiveWordServiceImpl implements SensitiveWordService {

    @Autowired
    private SensitiveWordDao sensitiveWordDao;

    @Override
    public Set<String> getSensitiveWord() {
        return sensitiveWordDao.getSensitiveWord();
    }

}
