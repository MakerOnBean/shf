package com.dk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dk.entity.Dict;
import com.dk.result.Result;
import com.dk.service.DictService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dict")
public class DictController {

    @Reference
    private DictService dictService;

    /**
     * 根据编码获取所有子节点
     */
    @RequestMapping("/findListByDictCode/{dictCode}")
    public Result findListByDictCode(@PathVariable("dictCode")String dictCode){
        //调用DictService中根据编码获取所有子节点的方法
        List<Dict> dictList = dictService.findListByDictCode(dictCode);
        return Result.ok(dictList);
    }

    /**
     * 根据父id查询所有子节点
     */
    @RequestMapping("/findListByParentId/{areaId}")
    public Result findListByParentId(@PathVariable("areaId")Long areaId){
        //调用DictService中根据父id获取所有子节点的方法
        List<Dict> dictList = dictService.findListByParentId(areaId);
        return Result.ok(dictList);
    }
}
