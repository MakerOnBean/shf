package com.dk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dk.entity.Community;
import com.dk.entity.Dict;
import com.dk.service.CommunityService;
import com.dk.service.DictService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController{

    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;

    private final static String PAGE_INDEX="community/index";

    private static final String PAGE_CREATE="community/create";

    private static final String PAGE_EDIT="community/edit";

    /**
     * 分页待条件查询，并跳转到主页
     */
    @RequestMapping
    public String index(Model model, HttpServletRequest request){
        Map<String, Object> filters = getFilters(request);
        model.addAttribute("filters",filters);
        PageInfo<Community> page = communityService.findPage(filters);
        model.addAttribute("page",page);
        //二级查询第一级在此实现
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        model.addAttribute("areaList",areaList);
        return PAGE_INDEX;
    }

    /**
     * 去添加页面
     */
    @RequestMapping("/create")
    public String goCreatePage(Model model){
        //去添加页面需要携带板块和区域数据
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        model.addAttribute("areaList",areaList);
        return PAGE_CREATE;
    }

    /**
     * 保存添加的数据
     */
    @RequestMapping("/save")
    public String save(Community community){
        communityService.insert(community);
        return SUCCESS_PAGE;
    }

    /**
     * 去修改小区的页面
     */
    @RequestMapping("edit/{id}")
    public String goEditPage(@PathVariable("id")Long id, Model model){
        //去修改页面需要携带板块和区域数据
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        model.addAttribute("areaList",areaList);
        Community community = communityService.getById(id);
        model.addAttribute("community",community);
        return PAGE_EDIT;
    }

    /**
     * 更新
     */
    @RequestMapping("update")
    public String update(Community community){
        communityService.update(community);
        return SUCCESS_PAGE;
    }

    /**
     * 删除
     */
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id")Long id){
        communityService.delete(id);
        return "redirect:/community";
    }
}