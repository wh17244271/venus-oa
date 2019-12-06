package com.suypower.venus.service;

import com.github.pagehelper.PageInfo;
import com.suypower.venus.entity.ChatMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IChatMessageService {
    /**
     * 获取用户没有删除的聊天信息列表
     * @param userId
     * @param roomId
     * @return
     */
    PageInfo<ChatMessage> getChatMessageListByUserNotDel(String userId, String roomId , int pageNum, int pageSize);

    /**
     * 新增消息
     * @param chatMessage
     * @return
     */
    ChatMessage addNewChatMessage(ChatMessage chatMessage);

    /**
     * 清空用户聊天室原先清空的记录
     * @param userId
     * @param roomId
     * @return
     */
    boolean clearChatRoomMsg(String userId, String roomId );

    /**
     * 删除用户聊天室聊天记录
     * @param userId
     * @param roomId
     * @return
     */
    boolean delUserChatRoomMsg(String userId, String roomId );

    /**
     * 删除一条用户聊天记录
     * @param msgId
     * @param userId
     * @param roomId
     */
    void delChatMessage(String msgId,
                       String userId,
                       String roomId);

    /**
     * 撤回消息
     * @param msgId
     * @param overtime
     * @return
     */
    int withdrawMessage(String msgId,Long overtime);
}
