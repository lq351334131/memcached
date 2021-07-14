package com.etocrm.memcached.api;

import com.etocrm.memcached.pojo.Result;
import com.etocrm.memcached.pojo.ResultGenerator;
import com.etocrm.memcached.sensitive.SUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sensitive")
public class SensitiveWordApiController {

    @Autowired
    private SUtils sUtils;

    @PostMapping("/filter")
    public Result filterInfo(@RequestParam String information) {
        if (information == null || information.trim().equals("")) {
            return ResultGenerator.genSuccessResult("参数不能为空！");
        }
        try{
            String info = sUtils.filterInfo(information);
            return info==null?ResultGenerator.genFailResult("服务器异常"):ResultGenerator.genSuccessResult(info);
        }catch (Exception e){
            return ResultGenerator.genFailResult("服务器异常");
        }
    }


    @PostMapping("/decide")
    public Result isContainsSensitiveWord(@RequestParam String information) {
        if (information == null || information.trim().equals("")) {
            return ResultGenerator.genSuccessResult("参数不能为空！");
        }
        try{
            boolean result = sUtils.isContainsSensitiveWord(information);
            return result?ResultGenerator.genFailResult(201,"存在敏感词！"):ResultGenerator.genAsSuccessResult("不存在敏感词！");
        }catch (Exception e){
            return ResultGenerator.genFailResult("服务器异常！");
        }
    }
}
