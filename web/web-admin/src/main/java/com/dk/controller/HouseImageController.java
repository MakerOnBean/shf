package com.dk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dk.entity.HouseImage;
import com.dk.result.Result;
import com.dk.service.HouseImageService;
import com.dk.util.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/houseImage")
public class HouseImageController {

    @Reference
    private HouseImageService houseImageService;

    private static final String PAGE_UPLOAD = "house/upload";
    private static final String PAGE_SUCCESS="common/successPage";

    /**
     * 去上传图片页面
     */
    @RequestMapping("/uploadShow/{houseId}/{type}")
    public String goUploadPage(@PathVariable("houseId") Long houseId, @PathVariable("type") Integer type, Model model) {
        model.addAttribute("houseId", houseId);
        model.addAttribute("type", type);
        return PAGE_UPLOAD;
    }

    /**
     * 上传房源或房产图片
     */
    @ResponseBody
    @RequestMapping("/upload/{houseId}/{type}")
    public Result upload(@PathVariable("houseId") Long houseId, @PathVariable("type") Integer type,
                         @RequestParam("file") MultipartFile[] files) {
        try {
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    //获取字节数组
                    byte[] bytes = file.getBytes();
                    //获取图片名字
                    String originalFilename = file.getOriginalFilename();
                    //通过UUID随机生成字符串作为图片名字，上传到七牛云
                    String newFileName = UUID.randomUUID().toString();
                    //通过QiniuUtil工具类上传图片到七牛云
                    QiniuUtils.upload2Qiniu(bytes,newFileName);

                    //创建houseImage对象
                    HouseImage houseImage = new HouseImage();
                    houseImage.setHouseId(houseId);
                    houseImage.setType(type);
                    //设置图片名字
                    houseImage.setImageName(originalFilename);
                    //设置图片路径，路径格式：http://七牛云域名/随机生成的图片名字
                    houseImage.setImageUrl("http://rfyz3qaei.hn-bkt.clouddn.com/"+newFileName);
                    houseImageService.insert(houseImage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId")Long houseId,@PathVariable("id")Long id){
        houseImageService.delete(id);
        return "redirect:/house/"+houseId;
    }
}
