package com.suypower.venus.service;

import com.suypower.venus.entity.ChatFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileService {
    /**
     * 新增文件
     * @param chatFile
     */
    void addFile(ChatFile chatFile);

    /**
     * 上传文件
     * @param fileType
     * @param uploadPath
     * @param flodUrl
     * @param file
     * @return
     */
    ChatFile uploadFile(String fileType,String uploadPath,String flodUrl, MultipartFile file) throws IOException;

    /**
     * 获取文件信息
     * @param fileId
     * @return
     */
    ChatFile getFileInfoByFileId(String fileId);
}
