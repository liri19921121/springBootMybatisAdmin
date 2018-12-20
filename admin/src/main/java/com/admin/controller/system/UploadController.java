package com.admin.controller.system;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.admin.entity.ResponseModel;
import com.admin.entity.ResultEnum;
import com.admin.util.DateUtil;
import com.admin.util.ImgUtil;
import com.admin.util.Tools;

@Controller
@RequestMapping("/upload")
public class UploadController {

	private Logger log = Logger.getLogger(this.getClass());
	
	@Value("${upload.root.folder}")
	public String root_fold;
	
	@Value("${img.folder}")
	public String img_fold;
	
	@Value("${user.folder}")
	public String user_folder;
	
	@PostMapping("/img")
	@ResponseBody
	public String imgUpload(@RequestParam(value = "file") MultipartFile file){
		if (file.isEmpty()) {
            return null;
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        log.info("上传的后缀名为：" + suffixName);
        // 文件上传后的路径
        String filePath = root_fold+img_fold+DateUtil.getDays()+"/";
        System.out.println("upload_img_folder="+root_fold);
        System.out.println("upload_folder_root="+img_fold);
        System.out.println("filePath="+filePath);
        ImgUtil.createFile(filePath, fileName);
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            System.out.println("upload  success");
        }catch (IOException e) {
        	log.error(e.getMessage(),e);
            e.printStackTrace();
            return "failed";
        }
		return filePath+fileName;
	}
	
	@RequestMapping(value="/uploadImg",method=RequestMethod.POST)
	@ResponseBody
	public Object uploadImg(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "file", required = false) MultipartFile file){
		try {
			String filePath = "/images/"+Tools.random(5)+".png";
			String resultPath = ImgUtil.uploadImg2(root_fold, filePath, file.getInputStream());
			return ResponseModel.getModel(ResultEnum.SUCCESS, resultPath);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, null);
		}
		
	}
}
