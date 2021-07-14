package com.etocrm.memcached.sensitive;


import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class SensitiveWord {

    private volatile static SensitiveWord mInstance; // 单例模式
    private SensitiveWordFilter filterProvider; // 功能接口，提供算法功能

    private String encoding = "UTF-8";
    // 初始的装载替换字符的字符串，使用字符串的目的是为了可以随意截取任意长度的替换字符串
    // 例如要获取****直接replaceAll.subString(0,4)即可完成
    private StringBuilder replaceAll;
    private String replaceStr = "*";
    private String fileName = "CensorWords.txt"; // 默认文件位置
    private String punctuationRegex = "\\pP|\\pS| ";// 特殊字符正则表达式
    private Pattern punctuationPattern;

    private SensitiveWord() {
        punctuationPattern = Pattern.compile(punctuationRegex);
        replaceAll = new StringBuilder(500);
        for (int x = 0; x < 500; x++) {
            replaceAll.append(replaceStr);
        }
//        filterProvider = new DFASensitiveWordFilter();
    }

    /**
     * 如果修改了类的属性，而想让这些属性生效的话，请调用这个方法
     */
    public void initiliazation() {
        filterProvider.prepare();
    }

    public static SensitiveWord getInstance() {
        if (mInstance == null) {
            synchronized (SensitiveWord.class) {
                if (mInstance == null) {
                    mInstance = new SensitiveWord();
                }
            }
        }
        return mInstance;
    }

    /**
     * @param str 将要被过滤的信息
     * @return 过滤后的信息
     */
    public String filterInfo(String str) {

        if (str == null) {
            return "";
        }
        // 将原始字符串去除掉标点符号
        String replaceStr = str.replaceAll(punctuationRegex, "");

        String filterString = filterProvider.doFilter(replaceStr, replaceAll);

        // 将字符串还原回去
        Map<Integer, String> punctuationIndexMap = getPunctuationIndexMap(str);
        return convertResString(filterString, punctuationIndexMap);
    }

    public void setFilterProvider(SensitiveWordFilter filterProvider) {
        this.filterProvider = null;
        this.filterProvider = filterProvider;
        this.filterProvider.prepare();
    }

    /**
     * 将过滤后的字符串还原成带标点符号的过滤字符串
     *
     * @param filterString        过滤后的字符串
     * @param punctuationIndexMap 根据PunctuationIndexMap获取到的特殊字符位置内容信息
     * @return 还原后的字符串
     */
    private String convertResString(String filterString, Map<Integer, String> punctuationIndexMap) {
        StringBuilder builder = new StringBuilder(filterString);
        for (Integer integer : punctuationIndexMap.keySet()) {
            builder.insert(integer, punctuationIndexMap.get(integer));
        }
        return builder.toString();
    }

    /**
     * 获取到标点符号在字符串中的位置和标点符号内容的Map
     * 这个Map主要是用来对敏感词屏蔽后的字符串进行恢复标点符号
     *
     * @param str 原始的字符串
     * @return 包含特殊字符位置和内容的Map
     */
    private Map<Integer, String> getPunctuationIndexMap(String str) {
        String[] split = punctuationPattern.split(str);
        Map<Integer, String> pStrIndexMap = new TreeMap<>();
        int fromIndex = 0;
        int preEnd = 0;
        for (String s : split) {
            int start = str.indexOf(s, fromIndex);
            if (preEnd != 0) {
                String pStr = str.substring(preEnd, start);// 找到那个特殊字符
                pStrIndexMap.put(preEnd, pStr);
            } else {
                // 找到开始的字符是不是特殊字符
                if (start != 0) {
                    pStrIndexMap.put(0, str.substring(0, start));
                }
            }
            preEnd = start + s.length();
            fromIndex = preEnd;
        }
        // 找到最后一个标点符号
        if (split.length > 0) {
            if (preEnd < str.length()) {
                pStrIndexMap.put(preEnd, str.substring(preEnd, str.length()));
            }
        }
//        System.out.println(pStrIndexMap);
        return pStrIndexMap;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getReplaceStr() {
        return replaceStr;
    }

    public void setReplaceStr(String replaceStr) {
        this.replaceStr = replaceStr;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isContainSensitiveWord(String text) {
        String replaceStr = text.replaceAll(punctuationRegex, "");
        return filterProvider.isContainsSensitiveWord(replaceStr);
    }

    public Set<String> getSensitiveWord(String text) {
        String replaceStr = text.replaceAll(punctuationRegex, "");
        return filterProvider.getSensitiveWord(replaceStr);
    }

}
