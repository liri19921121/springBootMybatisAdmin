package com.admin.controller.system;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Paths;

@Controller
@RequestMapping("/show")
public class ShowPictrueController {

	private Logger log = Logger.getLogger(this.getClass());
	private final ResourceLoader resourceLoader;

	@Value("${upload.root.folder}")
	public String root_fold;
	
	@Value("${img.folder}")
	public String img_fold;
	
	@Value("${user.folder}")
	public String user_folder;

	@Autowired
	public ShowPictrueController(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	/**
	 * 显示上传根目录的图片或文件
	 * @param filename
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/upload/{filename:.+}")
	@ResponseBody
	public ResponseEntity<?> getFile(@PathVariable String filename) {
		try {
			return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(root_fold, filename).toString()));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * 显示上传的图片
	 * @param folderName
	 * @param filename
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/upload/{folderName}/{filename:.+}")
	@ResponseBody
	public ResponseEntity<?> getImg(@PathVariable("folderName") String folderName,@PathVariable("filename") String filename) {
		try {
			return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(root_fold+"/"+folderName, filename).toString()));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * 显示上传的图片
	 * @param folderName
	 * @param filename
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/upload/{folderName}/{folderName2}/{filename:.+}")
	@ResponseBody
	public ResponseEntity<String> getImg(@PathVariable("folderName") String folderName,@PathVariable("folderName2") String folderName2,@PathVariable("filename") String filename) {
		try {
			return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(root_fold+"/"+folderName+"/"+folderName2, filename).toString()).toString());
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

}
