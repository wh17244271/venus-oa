package com.suypower.venus.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.suypower.venus.common.ConstantChatRoomType;
import com.suypower.venus.common.ConstantUser;
import com.suypower.venus.entity.*;
import com.suypower.venus.platform.web.controller.BaseController;
import com.suypower.venus.platform.web.response.VenusResponse;
import com.suypower.venus.platform.web.response.VenusResponseHttpCode;
import com.suypower.venus.service.IChatMessageService;
import com.suypower.venus.service.IUserChatRoomService;
import com.suypower.venus.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.transform.Result;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping( "/api/chat" )
public class ChatController extends BaseController {


    @Autowired
    private IUserChatRoomService userChatRoomService;

    @Autowired
    private IChatMessageService chatMessageService;

    /**
     * 获取当前登录人的聊天列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping( "/findChatList" )
    public VenusResponse findChatList() {
        String userId = String.valueOf(ServletUtils.getSessionAttribute(ConstantUser.login_user));
        List<ChatList> chatList = userChatRoomService.getChatList(userId);
        return new VenusResponse(VenusResponseHttpCode.OK, chatList);
    }

    /**
     * 通过内容搜索聊天记录
     * @param roomId
     * @param msgType
     * @param content
     * @return
     */
    @ResponseBody
    @RequestMapping( "/searchChatContent" )
    public VenusResponse searchChatContent(@RequestParam("roomId") String roomId, @RequestParam("msgType") String msgType,
                                      @RequestParam("content") String content) {
        String userId = String.valueOf(ServletUtils.getSessionAttribute(ConstantUser.login_user));
        List<ChatMessage> messageList = userChatRoomService.searchChatContent(roomId, userId, msgType, content);
        return new VenusResponse(VenusResponseHttpCode.OK, messageList);
    }

    /**
     * 搜索聊天列表
     * @return
     */
    @ResponseBody
    @RequestMapping( "/searchChatList" )
    public VenusResponse searchChatList(String searchInfo) {
        String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
        List<Friend> friendList = FriendUtils.getFriendList(userId,searchInfo);
        List<ChatList> chatList = userChatRoomService.getChatList(userId);
        if(null!=chatList){
            Iterator<ChatList> iterator = chatList.iterator();
            while (iterator.hasNext()){
                ChatList next = iterator.next();
                List<User> member = next.getMember();
                String roomType = next.getRoomType();
                if(ConstantChatRoomType.chat_type_friend.equals(roomType)){
                    iterator.remove();
                    continue;
                }
                if(member!=null){
                    boolean flag = true;
                    for (User user:member){
                        String remark = user.getRemark();
                        if( remark.contains(searchInfo)){
                            next.setContain(remark);
                            flag = false;
                            break;
                        }
                    }
                   if(flag){
                       iterator.remove();
                   }
                }
            }
        }


        JSONObject result = new JSONObject() {{
            this.put("friend", friendList);
            this.put("group", chatList);
        }};
        return new VenusResponse(VenusResponseHttpCode.OK, result);
    }

    /**
     * 获取当前登录人某个聊天室的聊天记录列表
     *
     * @param roomId
     * @return
     */
    @ResponseBody
    @RequestMapping( "/findChatData" )
    public VenusResponse findChatData(String roomId,int pageNum, int pageSize) {
        String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);

//        ChatRoom chatRoomInfoByRoomId = userChatRoomService.getChatRoomInfoByRoomId(roomId);
        List<User> userListByRoomId = ChatRoomUtils.getUserListByRoomId(userId, roomId, false);

//        chatRoomInfoByRoomId.setUserCount(userListByRoomId.size());
        PageInfo<ChatMessage> chatMsgList = chatMessageService.getChatMessageListByUserNotDel(userId, roomId,pageNum,pageSize);
        List<ChatMessage> list = chatMsgList.getList();
        for (ChatMessage msg : list) {
            if (msg == null) continue;
            for (User user : userListByRoomId) {
                if (user == null) continue;
                if (user.getUserId().equals(msg.getFrom())) {
                    msg.setUser(user);
                }
            }
        }
//        chatRoomInfoByRoomId.setMsg(list);
        Map<String,Object> resutl = new HashMap<>();
        resutl.put("roomId",roomId);
        resutl.put("data",chatMsgList);
        List<User> realUsers = ChatRoomUtils.getUserListByRoomId(userId, roomId, true);
        resutl.put("userCount",realUsers.size());

        return new VenusResponse(VenusResponseHttpCode.OK, resutl);

    }

    /**
     * 获取指定类型聊天记录
     *
     * @param roomId
     * @param msgType
     * @return
     */
    @ResponseBody
    @RequestMapping( "/findChatRecord" )
    public VenusResponse findChatRecord(String roomId, String msgType) {
      /*  String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
        List<ChatMessage> chatRecord = UserChatRoomManage.getChatRecord(userId, roomId, msgType);

        return new VenusResponse(VenusResponseHttpCode.OK, chatRecord);*/
      return null;
    }


    /**
     * 获取群消息
     *
     * @param roomId
     * @return
     */
    @ResponseBody
    @RequestMapping( "/findGroupInfo" )
    public VenusResponse findGroupInfo(String roomId) {
        String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
      /*  UserChatRoom userChatRoom = UserChatRoomManage.getUserChatRoom(userId, roomId);
        List<User> userList = UserChatRoomManage.getUserList(roomId);
        GroupInfo groupInfo = new GroupInfo(
                roomId, userChatRoom.isGroupOwner(), userChatRoom.getChatRoom().getRoomName(),
                userList, userChatRoom.isTop()
        );
*/
        ChatRoom chatRoomInfoByRoomId = userChatRoomService.getChatRoomInfoByRoomId(roomId);
        List<User> userListByRoomId = ChatRoomUtils.getUserListByRoomId(userId, roomId, true);
        Map<String,Object> result = new HashMap<>();
        result.put("roomInfo",chatRoomInfoByRoomId);
        result.put("members",userListByRoomId);
        return new VenusResponse(VenusResponseHttpCode.OK, result);
    }


    /**
     * 删除聊天室(左滑）
     *
     * @param roomId
     * @return
     */
    @ResponseBody
    @RequestMapping( "/delChatRoom" )
    public VenusResponse delChatRoom(String roomId) {
        String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
        //左滑，删除所有聊天记录
        chatMessageService.delUserChatRoomMsg(userId, roomId);
        //用户聊天室将是否左滑字段重置为 true
        boolean b = userChatRoomService.editLeftDelStatus(userId, roomId,true);
        return new VenusResponse(VenusResponseHttpCode.OK, b);

      /*  boolean result = UserChatRoomManage.delChatRoom(userId, roomId);
        if (result) {
            return new VenusResponse(VenusResponseHttpCode.OK, result);
        } else {
            return new VenusResponse(VenusResponseHttpCode.InternalServerError, result);
        }*/

    }

    /**
     * 创建单人聊天室
     *
     * @param toUserId
     * @param roomType 聊天室类型
     * @return
     */
    @ResponseBody
    @RequestMapping( "/creatChatRoom" )
    public VenusResponse creatChatRoom(String toUserId, String roomType) {
        String fromUserId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
        ChatRoom chatRoom = userChatRoomService.creatSingleChatRoom(fromUserId, toUserId, ConstantChatRoomType.chat_type_friend);
        JSONObject result = new JSONObject() {{
            this.put("roomId", chatRoom.getRoomId());
        }};
        return new VenusResponse(VenusResponseHttpCode.OK, result);
    }

    /**
     * 创建群聊
     *
     * @param pullers
     * @return
     */
    @ResponseBody
    @RequestMapping( "/creatGroupChatRoom" )
    public VenusResponse creatGroupChatRoom(String[] pullers) {
        String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
        String roomId = ChatRoomUtils.creatGroupChatRoom(userId, pullers);
        JSONObject result = new JSONObject() {{
            this.put("roomId", roomId);
        }};

        return new VenusResponse(VenusResponseHttpCode.OK, result);
    }

    /**
     * 拉人进群
     *
     * @param roomId
     * @param pullers
     * @return
     */
    @ResponseBody
    @RequestMapping( "/pullToGroup" )
    public VenusResponse pullToGroup(String roomId, String[] pullers) {
        String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
        ChatRoomUtils.pullToGroup(userId, roomId, pullers);
        return new VenusResponse(VenusResponseHttpCode.OK, "拉人成功");
    }


    /**
     * 删除某条消息(状态删除)
     *
     * @param roomId
     * @param msgIds
     * @return
     */
    @ResponseBody
    @RequestMapping( "/delChatData" )
    public VenusResponse delChatData(String roomId, String[] msgIds) {
        String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
        if(null!=msgIds){
            for(String msgId:msgIds){
                MessageUtils.delChatMessage(msgId, userId, roomId);
            }
        }

        return new VenusResponse(VenusResponseHttpCode.OK, "修改成功");
    }


    @ResponseBody
    @RequestMapping( "/searchChatInfo" )
    public VenusResponse searchChatInfo(String roomId, String searchType) {
        String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
//        UserChatRoomManage.
        return null;
    }

}
