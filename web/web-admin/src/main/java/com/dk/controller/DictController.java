package com.dk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dk.entity.Dict;
import com.dk.result.Result;
import com.dk.service.DictService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/dict")
public class DictController {

    @Reference
    private DictService dictService;

    /**
     * 展示数据字典
     */
    @RequestMapping
    public String index() {
        return "dict/index";
    }

    /**
     * 获取数据字典中的数据
     */
    @ResponseBody
    @RequestMapping("/findZnodes")
    public Result findZnodes(@RequestParam(value = "id",defaultValue = "0")Long id){
        List<Map<String, Object>> zNodes = dictService.findZnodes(id);
        return Result.ok(zNodes);
    }

    /**
     * 根据父id获取子节点
     * 二级查询第二级在此实现
     */
    @ResponseBody
    @RequestMapping("/findListByParentId/{areaId}")
    public Result findListByParentId(@PathVariable("areaId")Long id){
        List<Dict> dictList = dictService.findListByParentId(id);
        return Result.ok(dictList);
    }
}
