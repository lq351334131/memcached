package com.etocrm.memcached.sensitive;

import com.alibaba.fastjson.JSONObject;
import com.etocrm.memcached.service.SensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SUtils {

    @Autowired
    private SensitiveWordService sensitiveWordService;

    //过滤关键词
    public String filterInfo(String str) {
        SensitiveWord sensitiveWord = SensitiveWord.getInstance();
        IndexSensitiveWordFilter filterProvider = new IndexSensitiveWordFilter();
        filterProvider.setSensitiveWordService(sensitiveWordService);
        sensitiveWord.setFilterProvider(filterProvider);
        return SensitiveWord.getInstance().filterInfo(str);
    }

    //    是否有敏感词
    public boolean isContainsSensitiveWord(String str) {
        JSONObject object = new JSONObject();
        String punctuationRegex = "\\pP|\\pS| ";// 特殊字符正则表达式
        SensitiveWord sensitiveWord = SensitiveWord.getInstance();
        IndexSensitiveWordFilter filterProvider = new IndexSensitiveWordFilter();
        filterProvider.setSensitiveWordService(sensitiveWordService);
        sensitiveWord.setFilterProvider(filterProvider);
        String str1 = str.replaceAll(punctuationRegex, "");
        boolean containsSensitiveWord = filterProvider.isContainsSensitiveWord(str1);
        return containsSensitiveWord;
    }

}
