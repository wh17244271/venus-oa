package com.suypower.venus.service.impl;

import com.suypower.venus.dao.FileDao;
import com.suypower.venus.entity.ChatFile;
import com.suypower.venus.service.IFileService;
import com.suypower.venus.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service( "fileService" )
public class FileServiceImpl implements IFileService {

    @Autowired
    private FileDao fileDao;

    @Override
    public void addFile(ChatFile chatFile) {
        fileDao.addFile(chatFile);
    }

    @Override
    public ChatFile uploadFile(String fileType, String uploadPath, String flodUrl, MultipartFile file) throws IOException {
        String oriName = file.getOriginalFilename();
        System.err.println("文件名:" + oriName);
        //文件后缀名
        String suffix = file.getOriginalFilename().substring(oriName.lastIndexOf("."));
        String uuid = StringUtils.uuid();
        //上传文件名
        String filename = uuid + suffix;
        long size = file.getSize(); //文件大小
        // 保存在磁盘根目录
        String saveUrl = uploadPath + flodUrl + filename;
        //除去磁盘根目录
        String fileUrl = flodUrl + filename;
        ChatFile chatFile = new ChatFile(fileType, oriName, suffix, size, fileUrl, filename);
        this.addFile(chatFile); //数据库保存文件信息

        //服务器端保存的文件对象
        File serverFile = new File(saveUrl);

        if (!serverFile.exists()) {
            //先得到文件的上级目录，并创建上级目录，在创建文件
            serverFile.getParentFile().mkdirs();
            try {
                //创建文件
                serverFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //将上传的文件写入到服务器端文件内
        file.transferTo(serverFile);


        System.err.println(oriName + "   文件上传成功");
        return chatFile;
    }

    @Override
    public ChatFile getFileInfoByFileId(String fileId) {
        return fileDao.getFileInfoByFileId(fileId);
    }
}
