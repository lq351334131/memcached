package com.etocrm.memcached.api;

import com.etocrm.memcached.pojo.ErrorCode;
import com.etocrm.memcached.pojo.Result;
import com.etocrm.memcached.pojo.ResultGenerator;
import com.etocrm.memcached.util.MemcachedbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/memcachedb")
public class MemcachedbApiController {


    @Autowired
    private MemcachedbClient memcachedbClient;

    /**
     * @param key
     * @param value
     * @return
     */
    @RequestMapping(value = "/set")
    public Result set(String key,  String value){
        ErrorCode set = memcachedbClient.set(key, value);
        return ResultGenerator.genDefaultFailMessage(set);
    }

    /**
     * @param key
     * @return
     */
    @RequestMapping(value = "/get")
    public Result get(String key){
        Object data = memcachedbClient.get(key);
        return ResultGenerator.genDefaultFailMessage(data);
    }
    
    /**
     * @param key
     * @return
     */
    @RequestMapping(value = "/getMany")
    public Result get(@RequestBody List<String> key){
        Object data = memcachedbClient.get(key);
        return ResultGenerator.genDefaultFailMessage(data);
    }


    /**
     * @param key
     * @param value
     * @return
     */
    @RequestMapping(value = "/add")
    public Result add(String key, String value){
        ErrorCode add = memcachedbClient.add(key,value);
        return ResultGenerator.genDefaultFailMessage(add);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    @RequestMapping(value = "/replaceData")
    public Result replaceData(String key, String value){
        ErrorCode replaceData = memcachedbClient.replaceData(key,value);
        return ResultGenerator.genDefaultFailMessage(replaceData);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    @RequestMapping(value = "/append")
    public Result append(String key, String value){
        ErrorCode append = memcachedbClient.append(key,value);
        return ResultGenerator.genDefaultFailMessage(append);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    @RequestMapping(value = "/prepend")
    public Result prepend(String key, String value){
        ErrorCode prepend = memcachedbClient.prepend(key,value);
        return ResultGenerator.genDefaultFailMessage(prepend);
    }

    /**
     * @param key
     * @param delta
     * @return
     */
    @RequestMapping(value = "/incrOrDecr")
    public Result incrOrDecr(String key, Long delta,Long  initValue){
        ErrorCode incrOrDecr = memcachedbClient.incrOrDecr(key,delta,initValue);
        return ResultGenerator.genDefaultFailMessage(incrOrDecr);
    }

    /**
     * @param key
     * @return
     */
    @RequestMapping(value = "/delete")
    public Result delete(String key){
        ErrorCode delete = memcachedbClient.delete(key);
        return ResultGenerator.genDefaultFailMessage(delete);
    }

}
