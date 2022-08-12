package com.dk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dk.entity.HouseUser;
import com.dk.service.HouseUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/houseUser")
public class HouseUserController {
    @Reference
    private HouseUserService houseUserService;

    private static final String PAGE_CREATE="houseUser/create";
    private static final String PAGE_SUCCESS="common/successPage";
    private static final String PAGE_EDIT="houseUser/edit";

    /**
     * 去添加房东页面
     */
    @RequestMapping("/create")
    public String goCreatePage(@RequestParam("houseId")Long houseId, Model model){
        model.addAttribute("houseId",houseId);
        return PAGE_CREATE;
    }

    /**
     * 保存新增数据
     */
    @RequestMapping("/save")
    public String save(HouseUser houseUser){
        houseUserService.insert(houseUser);
        return PAGE_SUCCESS;
    }

    /**
     * 去修改页面
     */
    @RequestMapping("/edit/{id}")
    public String geEditPage(@PathVariable("id")Long id,Model model){
        HouseUser houseUser = houseUserService.getById(id);
        model.addAttribute("houseUser",houseUser);
        return PAGE_EDIT;
    }

    /**
     * 保存修改的数据
     */
    @RequestMapping("/update")
    public String update(HouseUser houseUser){
        houseUserService.update(houseUser);
        return PAGE_SUCCESS;
    }

    /**
     * 删除房东数据
     */
    @RequestMapping("/delete/{houseId}/{houseUserId}")
    public String deleted(@PathVariable("houseId")Long houseId,@PathVariable("houseUserId")Long userId){
        houseUserService.delete(userId);
        return "redirect:/house/"+houseId;
    }
}
