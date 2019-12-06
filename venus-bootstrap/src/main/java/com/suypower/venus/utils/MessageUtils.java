package com.suypower.venus.utils;

import com.suypower.venus.common.ConstantMsgType;
import com.suypower.venus.common.SpringContextHolder;
import com.suypower.venus.entity.ChatMessage;
import com.suypower.venus.service.IChatMessageService;

public class MessageUtils {

    private static IChatMessageService chatMessageService = SpringContextHolder.getBean(IChatMessageService.class);

    /**
     * 新增消息
     * @param chatMessage
     * @return
     */
    public static ChatMessage addNewChatMessage(ChatMessage chatMessage) {
        return chatMessageService.addNewChatMessage(chatMessage);
    }

    /**
     * 删除一条用户聊天记录
     * @param msgId
     * @param userId
     * @param roomId
     * @return
     */
    public static void delChatMessage(String msgId,String userId,String roomId) {
        chatMessageService.delChatMessage(msgId, userId, roomId);
    }

    /**
     *  删除用户聊天室聊天记录
     * @param userId
     * @param roomId
     * @return
     */
    public static boolean delUserChatRoomMsg(String userId,String roomId) {
        return chatMessageService.delUserChatRoomMsg(userId, roomId);
    }

    /**
     * 新增系统消息
     * @param sendUId
     * @param roomId
     * @param msg
     */
    public static void addSysMessage(String sendUId,String roomId,String msg){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMsgType(ConstantMsgType.sys);
        chatMessage.setMsg(msg);
        chatMessage.setFrom(sendUId);
        chatMessage.setRoomId(roomId);
        chatMessageService.addNewChatMessage(chatMessage);
    }

    /**
     * 撤回消息
     * @param msgId
     * @return
     */
    public static int withdrawMessage(String msgId){
        Long overtime = 1*60*1000L;
        return chatMessageService.withdrawMessage(msgId, overtime);
    }
}
