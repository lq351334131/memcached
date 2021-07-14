package com.etocrm.memcached.util;

import com.etocrm.memcached.pojo.ErrorCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static ErrorCode isBlank(String str) {
        int strLen;
        boolean blank = org.apache.commons.lang.StringUtils.isBlank(str);
        if (blank){
            return ErrorCode.NULL_KEY;
        }
        if(str.length()>250) {
            return ErrorCode.EXTRA_LONG;
        }
        if (!getcom(str)){
            return ErrorCode.ENCODE_ERROR;
        }
        return ErrorCode.SUCCESS;
    }

    public static ErrorCode isBlank(String str,Object value) {
        ErrorCode blank = isBlank(str);
        return blank==ErrorCode.SUCCESS ? (value==null ? ErrorCode.NULL_VALUE : ErrorCode.SUCCESS) : blank;
    }



    public static Object valueFormat(String key , Object value){
        //incr、decr命令最好为值为String
        if(value  instanceof  Long  ||value  instanceof  Integer) {
            value=String.valueOf(value);
        }
       /* if(value  instanceof MemCachedbData) {
            Gloab.memKey.put(key, (MemCachedbData) value);
            value= JSON.toJSONString(value);
        }*/
        return value;
    }

    /**
     * @param str:数字和英文
     * @return
     */
    public static boolean getcom(String str) {
        Pattern p = Gloab.pattern;
        Matcher m = p.matcher(str);
        boolean isValid = m.matches();
        return    isValid;
    }


    public static ErrorCode isBlankAndNumber(String key, Long delta) {
        return delta instanceof Long ? isBlank(key,delta) : ErrorCode.VALUE_TYPE_ERROR;
    }
}
