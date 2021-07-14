package com.etocrm.memcached.sensitive;

import com.etocrm.memcached.service.SensitiveWordService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
public class IndexSensitiveWordFilter implements SensitiveWordFilter{

    private Set<String> arrayList;

    private SensitiveWordService sensitiveWordService;

    public void setSensitiveWordService(SensitiveWordService sensitiveWordService) {
        this.sensitiveWordService = sensitiveWordService;
    }

    @Override
    public void prepare() {
        this.arrayList = sensitiveWordService.getSensitiveWord();
    }

    /**
     * index算法实现
     */
    @Override
    public String doFilter(String text, StringBuilder replaceAll) {
        StringBuilder punctuationFilterString = new StringBuilder(text);
        HashMap<Integer, Integer> hash = new HashMap<>(arrayList.size());
        String temp;
        for (String anArrayList : arrayList) {
            temp = anArrayList;
            int findIndexSize = 0;
            for (int start; (start = punctuationFilterString.indexOf(temp, findIndexSize)) > -1; ) {
                findIndexSize = start + temp.length();
                Integer mapStart = hash.get(start);
                if (mapStart == null || findIndexSize > mapStart) {
                    hash.put(start, findIndexSize);
                }
            }
        }
        Collection<Integer> values = hash.keySet();
        for (Integer value : values) {
            Integer endIndex = hash.get(value);
            punctuationFilterString.replace(value, endIndex, replaceAll.substring(0, endIndex - value));
        }
        hash.clear();
        return punctuationFilterString.toString();
    }

    @Override
    public boolean isContainsSensitiveWord(String text) {
        for (String anArrayList : arrayList) {
            if (text.contains(anArrayList)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<String> getSensitiveWord(String text) {
        Set<String> set = new HashSet<>();
        for (String s : arrayList) {
            if (text.contains(s)) {
                set.add(s);
            }
        }
        return set;
    }
}
