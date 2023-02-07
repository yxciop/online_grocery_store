package cn.letao.online_grocery_store.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class TestController {
	@PostMapping(value="/upload")
	@ResponseBody
	public Object uploadIMG(@RequestParam(value = "file",required = false) MultipartFile file,  HttpServletRequest request) throws Exception{

		String path=request.getSession().getServletContext().getRealPath("static/uploadfiles" + File.separator + "details");
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		//重新定义过appLogo的文件名
		String fileName = df.format(new Date())+".jpg";
		//创建目标文件对象用来上传文件
		File targetFile = new File(path, fileName);
		//判断文件夹是否存在
		if (!targetFile.exists()) {
			//创建子目录
			targetFile.mkdir();
		}
		try {
			file.transferTo(targetFile);//上传文件
		}catch (IOException e){
			e.printStackTrace();
		}

		return request.getContextPath() + "/static/uploadfiles/details/" + fileName;

	}
	@ResponseBody
	@RequestMapping("/deletePics")
	public String deleteFile(HttpServletRequest request,String imgSrcs)  throws IOException{
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";
		String physicalPath=request.getSession().getServletContext().getRealPath("/");
		JSONArray jsonArray= JSON.parseArray(imgSrcs);
		for(int i=0;i<jsonArray.size();i++){
			String jsonIndex= (String) jsonArray.get(i);
			String imgSrc=jsonIndex.replace(basePath,"");
			imgSrc=imgSrc.replace("/","\\\\");
			File file=new File(physicalPath+imgSrc);
			if(file.exists()){
				file.delete();
			}
		}
		return "deleteSuccess!";
	}
}
