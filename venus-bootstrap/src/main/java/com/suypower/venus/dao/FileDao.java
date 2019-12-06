package com.suypower.venus.dao;

import com.suypower.venus.entity.ChatFile;

public interface FileDao {
    /**
     * 新增文件
     * @param chatFile
     */
    void addFile(ChatFile chatFile);
    /**
     * 获取文件信息
     * @param fileId
     * @return
     */
    ChatFile getFileInfoByFileId(String fileId);
}
