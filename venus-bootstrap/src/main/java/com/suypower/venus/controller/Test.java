package com.suypower.venus.controller;

import com.suypower.venus.common.ConstantChatRoomType;
import com.suypower.venus.entity.ChatList;
import com.suypower.venus.entity.ChatRoom;
import com.suypower.venus.entity.ChatSysMsg;
import com.suypower.venus.platform.web.response.VenusResponse;
import com.suypower.venus.platform.web.response.VenusResponseHttpCode;
import com.suypower.venus.service.IChatMessageService;
import com.suypower.venus.service.IFriendService;
import com.suypower.venus.service.IUserChatRoomService;
import com.suypower.venus.utils.ChatRoomUtils;
import com.suypower.venus.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class Test {
    @Autowired
    private IUserChatRoomService userChatRoomService;
    @Autowired
    IChatMessageService chatMessageService;
    @Autowired
    IFriendService friendService;

    @ResponseBody
    @RequestMapping( "/getChatList" )
    public VenusResponse getChatList(String userId) {
        List<ChatList> chatList = userChatRoomService.getChatList(userId);
        return new VenusResponse(VenusResponseHttpCode.OK, chatList);
    }


    @ResponseBody
    @RequestMapping( "/delUserChatRoomMsg" )
    public VenusResponse delUserChatRoomMsg(String userId,String roomId) {
        boolean b = chatMessageService.delUserChatRoomMsg(userId, roomId);
        return new VenusResponse(VenusResponseHttpCode.OK, b);
    }

    @ResponseBody
    @RequestMapping( "/creatSingleChatRoom" )
    public VenusResponse creatSingleChatRoom(String fromUserId,String toUserId) {
        ChatRoom chatRoom = userChatRoomService.creatSingleChatRoom(fromUserId, toUserId, ConstantChatRoomType.chat_type_friend);
        return new VenusResponse(VenusResponseHttpCode.OK, chatRoom);
    }

    @ResponseBody
    @RequestMapping( "/getUserIdIsKicked" )
    public VenusResponse getUserIdIsKicked(String roomId) {
        List<String> userIdIsKicked = ChatRoomUtils.getUserIdIsKicked(roomId);
        return new VenusResponse(VenusResponseHttpCode.OK, userIdIsKicked);
    }

    @ResponseBody
    @RequestMapping( "/pullToGroup" )
    public VenusResponse pullToGroup(String roomId,String[] pullers) {
        ChatRoomUtils.pullToGroup("",roomId, pullers);

    return new VenusResponse(VenusResponseHttpCode.OK, "ok");
    }

    @ResponseBody
    @RequestMapping( "/withdrawMessage" )
    public VenusResponse pullToGroup(String msgId) {
        int result = MessageUtils.withdrawMessage(msgId);

        return new VenusResponse(VenusResponseHttpCode.OK, result);
    }

    @ResponseBody
    @RequestMapping( "/getMakeFriendMsgList" )
    public VenusResponse getMakeFriendMsgList(String userId) {
        List<ChatSysMsg> makeFriendMsg = friendService.getMakeFriendMsgList(userId);

        return new VenusResponse(VenusResponseHttpCode.OK, makeFriendMsg);
    }

    @ResponseBody
    @RequestMapping( "/addMakeFriendMsg" )
    public VenusResponse addMakeFriendMsg(String sender,String rcpt,String content) {
        friendService.addMakeFriendMsg(sender, rcpt, content);

        return new VenusResponse(VenusResponseHttpCode.OK, "ok");
    }

}
