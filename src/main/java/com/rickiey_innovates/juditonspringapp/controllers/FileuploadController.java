package com.rickiey_innovates.juditonspringapp.controllers;

import com.rickiey_innovates.juditonspringapp.models.accountsmodel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FileuploadController {
	
	
	  @PostMapping(value = "/upload", produces = "application/json")
	  @ResponseBody
	  public String handleFileUpload( @RequestParam("pictureFile") MultipartFile file,@RequestParam("morepath") String morepath ) {

		  accountsmodel.dbaction("SELECT *  from (SELECT s.churchid,s.name,upload_path,\n" +
				  "                       logo,address,region,phone FROM users u\n" +
				  "                                                        inner join churches s on u.church=s.churchid\n" +
				  "                where sessionid='"+ RequestContextHolder.currentRequestAttributes().getSessionId() +"')d", 1,0,0,0);
		
		List<accountsmodel> newList = new ArrayList<accountsmodel>(accountsmodel.datanew);	
	    
		String fileName = file.getOriginalFilename();
	    
	    String uploadDir = "static/"+newList.get(0).activeProperty("upload_path").getValue()+"/"+morepath;
	    
        
	    try {
	    	
	    	FileUploadUtil.saveFile(uploadDir, fileName, file);
	    	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return "{\"querystatus\" : \""+e+"\"}";
	    } 
	   
	    return "{\"querystatus\" : \"Image uploaded successfully\",\"path\" : \""+uploadDir+"/"+fileName+"\"}";
	  }


	}
