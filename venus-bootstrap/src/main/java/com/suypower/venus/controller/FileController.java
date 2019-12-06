package com.suypower.venus.controller;

import com.suypower.venus.common.ConstantUploadType;
import com.suypower.venus.common.ConstantUser;
import com.suypower.venus.entity.ChatFile;
import com.suypower.venus.platform.web.response.VenusResponse;
import com.suypower.venus.platform.web.response.VenusResponseHttpCode;
import com.suypower.venus.service.IFileService;
import com.suypower.venus.utils.ServletUtils;
import com.suypower.venus.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping( "/api/file" )
public class FileController {

    @Value( "${upload.path}" )
    private String uploadPath;

    @Autowired
    private IFileService fileService;


    /**
     * 上传文件
     * @param request
     * @param file
     * @return
     */
    @RequestMapping( "/upload/{uploadType}" )
    @ResponseBody
    public VenusResponse upload(@PathVariable("uploadType")String uploadType, HttpServletRequest request, MultipartFile[] file) {
         String initFlod = "/file/";  //基础目录在添加一层，看自己需不需要
        //上传目录地址,并且判断是那种类型
        //聊天上传
       if(ConstantUploadType.uploadChat.equals(uploadType)){
           initFlod+= ConstantUploadType.uploadChat+"/";
       }
       //用户头像上传
       else if(ConstantUploadType.uploadUserHear.equals(uploadType)){
           String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
           initFlod+= ConstantUploadType.userFolder + "/" + userId +"/" + ConstantUploadType.uploadUserHear+"/";
       }
       //系统表情上传
       else if(ConstantUploadType.uploadSysExp.equals(uploadType)){
           initFlod+=  ConstantUploadType.uploadSysExp+"/";
       }else if(ConstantUploadType.uploadUserExp.equals(uploadType)){
           String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
           initFlod+=  ConstantUploadType.uploadUserExp+"/"+userId+"/";
       }else if(ConstantUploadType.uploadFile.equals(uploadType)){
           initFlod+=  ConstantUploadType.uploadFile+"/";
       }
       else{
           return new VenusResponse(VenusResponseHttpCode.BadRequest, "上传类型出错误");
       }


        List<ChatFile> fileList = new ArrayList<>();
        try {

            //如果目录不存在，自动创建文件夹
            File dir = new File(uploadPath+initFlod);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //遍历文件数组执行上传
            for (int i = 0; i < file.length; i++) {
                if (file[i] != null) {
                    //调用上传方法
                    ChatFile chatFile = fileService.uploadFile(uploadType, uploadPath, initFlod, file[i]);
                    fileList.add(chatFile);
                }
            }
        } catch (Exception e) {
            //打印错误堆栈信息
            e.printStackTrace();
            throw new RuntimeException("上传失败"+e.getMessage());
        }
        return new VenusResponse(200, "上传成功!", fileList);
    }

    /**
     * 预览图片
     * @param fileId
     * @param response
     */
    @RequestMapping( "/showImg" )
    public void showPicturePub(@RequestParam("fileId") String fileId, HttpServletResponse response)  {
        InputStream inputStream = null;
        OutputStream os = null;
        try {
//            String path =uploadPath +fileName;
            ChatFile fileInfoByFileId = fileService.getFileInfoByFileId(fileId);
            inputStream =  new FileInputStream(uploadPath+fileInfoByFileId.getFileUrl());
            os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeStream(os, inputStream);
        }
    }

    /**
     * 下载文件
     * @param response
     */
    @RequestMapping( "/downLoad" )
    public void downLoad(@RequestParam("fileId") String fileId,HttpServletResponse response)  {
        System.err.println("开始上传文件");
        OutputStream os = null;
        InputStream inputStream = null;
        try {
            ChatFile fileInfoByFileId = fileService.getFileInfoByFileId(fileId);
           String fileName =  fileInfoByFileId.getFileName()+"."+fileInfoByFileId.getFileSuffix();
            response.reset();
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition","attachment;fileName="+ URLEncoder.encode(fileName, "UTF-8"));
//            String path = uploadPath + File.separator+fileName;
            inputStream = new FileInputStream(fileInfoByFileId.getFileUrl());
            os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeStream(os, inputStream);
        }
    }

    private void closeStream(OutputStream os, InputStream inputStream) {
        if(null!=os) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(null!=inputStream) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
