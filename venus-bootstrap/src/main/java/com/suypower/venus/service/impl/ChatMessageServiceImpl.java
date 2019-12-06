package com.suypower.venus.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.suypower.venus.dao.ChatMessageDao;
import com.suypower.venus.entity.ChatMessage;
import com.suypower.venus.entity.User;
import com.suypower.venus.service.IChatMessageService;
import com.suypower.venus.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service( "chatMessageService" )
public class ChatMessageServiceImpl implements IChatMessageService {

    @Autowired
    private ChatMessageDao chatMessageDao;

    @Override
    public PageInfo<ChatMessage> getChatMessageListByUserNotDel(String userId, String roomId,int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ChatMessage> chatMsgList = chatMessageDao.getChatMessageListByUserNotDel(userId, roomId);
        //颠倒
        Collections.reverse(chatMsgList);
        PageInfo<ChatMessage> userPageInfo = new PageInfo<>(chatMsgList);
        return userPageInfo;

//        return chatMessageDao.getChatMessageListByUserNotDel(userId, roomId);
    }

    @Override
    public ChatMessage addNewChatMessage(ChatMessage chatMessage) {
        String roomId = chatMessage.getRoomId();
        if (StringUtils.isEmpty(roomId)) {
            throw new RuntimeException(roomId + "不能为空");
        }
        String msgType = chatMessage.getMsgType();
        if (StringUtils.isEmpty(msgType)) {
            throw new RuntimeException(msgType + "不能为空");
        }
        String msg = chatMessage.getMsg();
        if (StringUtils.isEmpty(msg)) {
            throw new RuntimeException(msg + "不能为空");
        }
        String from = chatMessage.getFrom();
        if (StringUtils.isEmpty(from)) {
            throw new RuntimeException(from + "不能为空");
        }
         chatMessageDao.addNewChatMessage(chatMessage);
        return chatMessage;

    }

    @Override
    public boolean clearChatRoomMsg(String userId, String roomId) {
        return chatMessageDao.clearChatRoomMsg(userId, roomId);
    }

    @Override
    public boolean delUserChatRoomMsg(String userId, String roomId) {
        //必须先清空，在重新放入
        chatMessageDao.clearChatRoomMsg(userId, roomId);
        return chatMessageDao.delUserChatRoomMsg(userId, roomId);

    }

    @Override
    public void delChatMessage(String msgId, String userId, String roomId) {
        chatMessageDao.delChatMessage(msgId, userId, roomId);
    }

    @Override
    public int withdrawMessage(String msgId, Long overtime) {
        return chatMessageDao.withdrawMessage(msgId, overtime);
    }
}
