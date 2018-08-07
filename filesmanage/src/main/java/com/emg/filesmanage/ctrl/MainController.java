package com.emg.filesmanage.ctrl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Controller
@RequestMapping("/file.web")
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Value("${rootPath}")
	private String rootPath;

	/**
	 * 获取图片
	 * 
	 * @param model
	 * @param session
	 * @param request
	 * @param response
	 * @param recordid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, params = "action=getPic")
	private ModelAndView getPic(Model model, HttpSession session, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("filePath") String filePath) {
		logger.debug("START: " + filePath);
		ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			Path path = Paths.get(rootPath, filePath);
			response.setContentType("image/*");
			inputStream = new BufferedInputStream(new FileInputStream(path.toString()));
			outputStream = response.getOutputStream();

			int len = 0;
			byte[] buf = new byte[1024];
			while ((len = inputStream.read(buf, 0, 1024)) != -1) {
				outputStream.write(buf, 0, len);
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		logger.debug("END: " + filePath);
		return modelAndView;
	}

	/**
	 * 上传图片
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, params = "action=uploadPic")
	private ModelAndView uploadPic(Model model, HttpSession session, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("filePath") String filePath) {
		logger.debug("START: " + filePath);
		ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

			Path path = Paths.get(rootPath, filePath);

			if (!path.toFile().exists()) {
				path.toFile().mkdir();
			}

			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				MultipartFile mf = entity.getValue();
				String fileName = mf.getOriginalFilename();
				logger.debug("DOING: " + fileName);
				File uploadFile = Paths.get(path.toString(), fileName).toFile();
				FileCopyUtils.copy(mf.getBytes(), uploadFile);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.debug("END: " + filePath);
		return modelAndView;
	}
}
