package com.suypower.venus.dao;

import com.suypower.venus.entity.ChatMessage;
import com.suypower.venus.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChatMessageDao {
    /**
     * 获取用户没有删除的聊天信息列表
     * @param userId
     * @param roomId
     * @return
     */
    List<ChatMessage> getChatMessageListByUserNotDel(@Param("userId") String userId,
                                                     @Param("roomId") String roomId);

    /**
     * 新增消息
     * @param chatMessage
     * @return
     */
    int addNewChatMessage(ChatMessage chatMessage);

    /**
     * 删除用户聊天室聊天记录
     * @param userId
     * @param roomId
     * @return
     */
    boolean delUserChatRoomMsg(@Param("userId") String userId,
                               @Param("roomId") String roomId);


    /**
     * 清空用户聊天室原先清空的记录
     * @param userId
     * @param roomId
     * @return
     */
    boolean clearChatRoomMsg(@Param("userId") String userId,
                             @Param("roomId") String roomId);

    /**
     * 删除一条用户聊天记录
     * @param msgId
     * @param userId
     * @param roomId
     */
    void delChatMessage(@Param("msgId") String msgId,
                        @Param("userId") String userId,
                        @Param("roomId") String roomId);

    /**
     * 撤回消息
     * @param msgId
     * @param overtime
     * @return
     */
    int withdrawMessage(@Param("msgId")String msgId,
                        @Param("overtime")Long overtime);
}
