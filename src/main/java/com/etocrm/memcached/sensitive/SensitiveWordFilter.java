package com.etocrm.memcached.sensitive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public interface SensitiveWordFilter {

    /**
     * 服务的准备工作,在这里做一些加载数据和初始化的费时操作
     */
    void prepare();

    /**
     * 具体执行敏感词替换的方法
     *
     * @param text       要进行敏感词替换的文本数据
     * @param replaceAll 可用于替换的字符串，默认是500个*，
     *                   这个参数是程序自动控制，只需使用即可。要改变其内容，调用SensitiveWord的setReplaceStr进行设置
     * @return 替换后的文本数据
     */
    String doFilter(String text, StringBuilder replaceAll);

    /**
     * 查看文本是否含有敏感数据
     *
     * @param text 要检查的文本
     * @return 是否含有敏感数据
     */
    boolean isContainsSensitiveWord(String text);

    /**
     * 查看根本数据中含有那些敏感词
     *
     * @param text 要检查的列表
     * @return 含有的敏感词列表
     */
    Set<String> getSensitiveWord(String text);

    /**
     * 加载敏感词列表文件，返回敏感词列表
     *
     * @param fileName 要加载的文件名称
     * @param encoding 加载的编码方式
     * @return 敏感词列表
     */
    default Set<String> loadData(String fileName, String encoding) {

        // 加载词库
        Set<String> arrayList = new HashSet<>();
        InputStreamReader read = null;
        BufferedReader bufferedReader = null;
        try {
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(fileName);

            read = new InputStreamReader(resourceAsStream, encoding);
            bufferedReader = new BufferedReader(read);
            for (String txt; (txt = bufferedReader.readLine()) != null; ) {
                arrayList.add(txt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            IOStreamUtil.closeAll(bufferedReader, read);
        }
        return arrayList;
    }
}
