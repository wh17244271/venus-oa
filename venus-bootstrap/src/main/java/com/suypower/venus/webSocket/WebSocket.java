package com.suypower.venus.webSocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.suypower.venus.common.ConstantChatRoomType;
import com.suypower.venus.entity.*;
import com.suypower.venus.platform.web.response.DataResponse;
import com.suypower.venus.platform.web.response.DataTypeCode;
import com.suypower.venus.platform.web.response.VenusResponseHttpCode;
import com.suypower.venus.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket类
 *
 * @ServerEndpoint: socket链接地址
 */
@ServerEndpoint( "/websocket/{userId}" )
@Component
public class WebSocket {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static Map<String, Session> client = new ConcurrentHashMap<>();
    private static Map<String, String> client_user = new ConcurrentHashMap<>();


    /**
     * 监听连接（有用户连接，立马到来执行这个方法）
     * session 发生变化
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam( "userId" ) String userId, Session session) {
        User userByUserId = UserUtils.getUserByUserId(userId);
        if (userByUserId == null) {
            logger.info("进入聊天通道，用户:" + userId + ",不存在");
            return;
        }
        System.err.println(userId + "上线了");
        client.put(userId, session);
        client_user.put(session.getId(), userId);

    }


    /**
     * 监听连接断开（有用户退出，会立马到来执行这个方法）
     */
    @OnClose
    public void onClose(Session session) {
        String id = session.getId();
        String userId = client_user.get(id);
        client.remove(userId);
        client_user.remove(session.getId());
        //下线关闭所有聊天室窗口
        ChatRoomUtils.editAllUserChatRoomActiveStatus(userId, false);
//        UserChatRoomManage.setAllUserChatRoomActive(userId, false);
        System.err.println(userId + "下线");
//        logger.info(userId + "下线");
    }


    /**
     * 监听消息（收到客户端的消息立即执行）
     *
     * @param message 消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            //处理消息
            ChatMessage chatMessage = SocketUtils.formatMsg(message);
            chatMessage.setTime(TimeUtils.getCurrentTimeMillis());
            String dataGroup = chatMessage.getDataGroup();
            String dataType = chatMessage.getDataType();
            //用户发送的信息
            String roomId = chatMessage.getRoomId();
            String fromUserId = chatMessage.getFrom();
            String toUserId = chatMessage.getTo();
            System.err.println("》》》》》》》》》》》》》》  有消息进来,分组代码:   " + dataGroup + "  ,实体代码:" + dataType);
            //######################################################   聊天组    ######################################################
            if (DataTypeCode.chat.equals(dataGroup)) {
                // ******************************单人聊天*********************************
                if (DataTypeCode.chat_chatData.equals(dataType)) {
                    //1)正常窗口聊天
                    this.saveAndSendChatMsg(chatMessage, fromUserId, toUserId);
                    //2)判断接收者是否左滑删除了，是否含有聊天窗口
                    ChatRoomUtils.editLeftDelStatus(fromUserId, roomId, false);
                    boolean leftDel = ChatRoomUtils.isLeftDel(toUserId, roomId);
                    if (leftDel) {
                        ChatRoomUtils.editLeftDelStatus(toUserId, roomId, false); //重置左滑状态
                        List<User> userList = ChatRoomUtils.getUserListByRoomIdExcept(fromUserId, roomId, new ArrayList<String>() {{
                            this.add(toUserId);
                        }});
                        ChatList chatList = new ChatList(roomId, ConstantChatRoomType.chat_type_friend, "",
                                1, userList, chatMessage);//新增一个聊天列表给他,为了渲染
                        this.sendMessageToUser(chatList, toUserId, DataTypeCode.chat, DataTypeCode.chat_newChatData);
                    }
                    //3) 更新新消息条数
                    UserChatRoom userChatRoom = ChatRoomUtils.getUserChatRoom(toUserId, roomId);
                    if (userChatRoom != null && !userChatRoom.isActive()) {
                        ChatRoomUtils.editNewMsgCount(toUserId, roomId,  userChatRoom.getNewMsgCount() + 1);
                        this.sendMessageToUser(roomId, toUserId, DataTypeCode.chat, DataTypeCode.chat_new_chat_count);
                    }


                }
                // ******************************   多人聊天  *********************************
                else if (DataTypeCode.chat_groupchatData.equals(dataType)) {
                    //获取被踢出群的人
                    List<String> userIdSetIsKicked = ChatRoomUtils.getUserIdIsKicked(roomId);
                    //1)设置踢出群的人不可发送这条消息
                    for (String kickUserId : userIdSetIsKicked) {
                        if (kickUserId.equals(fromUserId)) {
                            this.sendMessageToUser("您以被踢出群聊", fromUserId, DataTypeCode.chat, DataTypeCode.chat_group_kick);
                            return;
                        }
                    }
                    //2)插入消息
                    ChatMessage chatMessage1 = MessageUtils.addNewChatMessage(chatMessage);
                    String msgId = chatMessage1.getId();
                    //3)设置踢出群的人,不可接收这条消息
                    for (String kickUserId : userIdSetIsKicked) {
                        MessageUtils.delChatMessage(msgId, kickUserId, roomId);
                    }
                    //4)判断有没有左滑删除的用户
                    //获取聊天室里面的所有人,排除已经被踢的人,包括自己
                    List<User> groupMember = ChatRoomUtils.getUserListByRoomIdExcept(fromUserId, roomId, userIdSetIsKicked);
//                    ChatRoomUtils.isLeftDel(userId, roomId);
                    if (null != groupMember && groupMember.size() > 0) {
                        ChatRoom chatRoom = ChatRoomUtils.getChatRoom(roomId);
                        for (User user : groupMember) {
                            String userId = user.getUserId();
                            //判断有没有左滑删除的用户
                            boolean leftDel = ChatRoomUtils.isLeftDel(userId, roomId);
                            if (leftDel) {
                                ChatRoomUtils.editLeftDelStatus(userId, roomId, false); //重置左滑状态
                                ChatList chatList = new ChatList(roomId, ConstantChatRoomType.chat_type_group, chatRoom.getRoomName(),
                                        1, groupMember, chatMessage);//新增一个聊天列表给他,为了渲染
                                this.sendMessageToUser(chatList, userId, DataTypeCode.chat, DataTypeCode.chat_newChatData);
                            }
                            //更新新消息条数
                            UserChatRoom userChatRoom = ChatRoomUtils.getUserChatRoom(userId, roomId);
                            if (userChatRoom != null && !userChatRoom.isActive()) {
                                ChatRoomUtils.editNewMsgCount(userId, roomId, userChatRoom.getNewMsgCount() + 1);
                                this.sendMessageToUser(roomId, userId, DataTypeCode.chat, DataTypeCode.chat_new_chat_count);
                            }

                            //更新用户聊天室时间
                            ChatRoomUtils.editChatRoomUpdateTime(userId, roomId);

                        }
                    }
                    //5)发送给多人(包括自己）
                    this.sendMessageToGroupRoom(chatMessage, groupMember, DataTypeCode.chat, DataTypeCode.chat_groupchatData);


                }

                // ******************************拉人聊天*********************************
                else if (DataTypeCode.chat_group_pull.equals(dataType)) {
                    //1)存储数据：拉人
                    String[] pullers = StringUtils.parseArray(chatMessage.getPullers(), ",");
                    ChatRoomUtils.pullToGroup(fromUserId, roomId, pullers);

                    List<String> userIds = Arrays.asList(pullers);
                    List arrList = new ArrayList(userIds);
                    arrList.add(fromUserId);
                    List<User> oldUserList = ChatRoomUtils.getUserListByRoomIdExcept(fromUserId, roomId, arrList);

                    //3)发送给多人
                    //原班人马，告诉谁拉了谁
                    for (User user : oldUserList) {
                        String userId = user.getUserId();
                        String msg = UserUtils.whoInviteWho(user.getUserId(), fromUserId, pullers);
                        MessageUtils.addSysMessage(userId, roomId, msg);
                        this.sendMessageToUser(msg, userId, DataTypeCode.chat, DataTypeCode.chat_group_pull);
                    }
                    //告诉自己，邀请了谁
                    String myMsg = UserUtils.youInviteWho(fromUserId, pullers);
                    MessageUtils.addSysMessage(fromUserId, roomId, myMsg);
                    this.sendMessageToUser(myMsg, fromUserId, DataTypeCode.chat, DataTypeCode.chat_group_pull);

                }

                // ******************************踢人聊天*********************************
                else if (DataTypeCode.chat_group_kick.equals(dataType)) {
                    //1)更新被踢人聊天室相关信息 如：是否被踢，被踢时间
                    String[] kickers = StringUtils.parseArray(chatMessage.getKickers(), ",");
                    if (kickers == null) return;
                    for (String kicker : kickers) {
                        ChatRoomUtils.editkickoutStatus(kicker, roomId, true);
                    }
                    //2）告知该用户已被剔除
                    //3）告诉群主，用户已被剔除

                    for (String kicker : kickers) {
                        MessageUtils.addSysMessage(kicker, roomId, UserUtils.youKickedByWho(kicker, fromUserId));
                        this.sendMessageToUser(UserUtils.youKickedByWho(kicker, fromUserId), kicker, DataTypeCode.chat, DataTypeCode.chat_group_kick);
                        MessageUtils.addSysMessage(fromUserId, roomId, UserUtils.youKickWho(fromUserId, kicker));
                        this.sendMessageToUser(UserUtils.youKickWho(fromUserId, kicker), fromUserId, DataTypeCode.chat, DataTypeCode.chat_group_kick);
                    }
                }

                // ******************************主动退出群聊*********************************
                else if (DataTypeCode.chat_group_out.equals(dataType)) {
                    //1)删除整个聊天室
                    ChatRoomUtils.delUserChatRoom(fromUserId, roomId);
                    //2)发送消息告诉xxx退出了群聊
                    List<User> userList = ChatRoomUtils.getUserListByRoomId(fromUserId, roomId, true);
                    for (User user : userList) {
                        String userId = user.getUserId();
                        String sendMsg = "\"" + UserUtils.getUserRemark(userId, fromUserId) + "\"退出了群聊";
                        MessageUtils.addSysMessage(userId, roomId, sendMsg);
                        this.sendMessageToUser(sendMsg, userId, DataTypeCode.chat, DataTypeCode.chat_group_out);
                    }

                }

                // ******************************激活聊天窗口*********************************
                else if (DataTypeCode.chat_chatRoom_active.equals(dataType)) {
                    ChatRoomUtils.editActiveStatus(fromUserId, roomId, true);
                    ChatRoomUtils.editNewMsgCount(fromUserId, roomId, 0); //清零
                }
                // ******************************关闭聊天窗口*********************************
                else if (DataTypeCode.chat_chatRoom_close.equals(dataType)) {
                    ChatRoomUtils.editActiveStatus(fromUserId, roomId, false);
                }

                // ****************************** 更改群名称 *********************************
                else if (DataTypeCode.chat_edit_groupName.equals(dataType)) {
                    String roomName = String.valueOf(chatMessage.getMsg());
                    ChatRoomUtils.editChatRoomName(roomId, roomName);
                    String youMsg = "你修改群名为\"" + roomName + "\"";
                    MessageUtils.addSysMessage(fromUserId, roomId, youMsg);
                    this.sendMessageToUser(youMsg, fromUserId, DataTypeCode.chat, DataTypeCode.chat_edit_groupName);
                    List<User> otherUserList = ChatRoomUtils.getUserListByRoomIdExcept(fromUserId, roomId, new ArrayList<String>() {{
                        this.add(fromUserId);
                    }});
                    for (User user : otherUserList) {
                        String userId = user.getUserId();
                        String oherMsg = "\"" + UserUtils.getUserRemark(user.getUserId(), fromUserId) + "\"修改群名为\"" + roomName + "\"";
                        MessageUtils.addSysMessage(userId, roomId, oherMsg);
                        this.sendMessageToUser(oherMsg, userId, DataTypeCode.chat, DataTypeCode.chat_edit_groupName);
                    }

                }
                // ******************************撤回消息*********************************
                else if (DataTypeCode.chat_recall.equals(dataType)) {
                    //1）彻底删除这条聊天记录
                    String msgId = String.valueOf(chatMessage.getMsg());
                    int resultNum = MessageUtils.withdrawMessage(msgId);
                    if (resultNum < 1) {
                        this.sendMessageToUser("超时，无法撤回", fromUserId, DataTypeCode.chat, DataTypeCode.chat_recall);
                    }
                    //2）角标需要消失   : 群聊要每个人发送一遍
                    List<User> otherUsers = ChatRoomUtils.getUserListByRoomIdExcept(fromUserId, roomId, new ArrayList<String>() {{
                        this.add(fromUserId);
                    }});
                    if (null != otherUsers) {
                        for (User user : otherUsers) {
                            String userId = user.getUserId();
                            String msg = UserUtils.whoWithdraw(userId, fromUserId);
                            boolean active = ChatRoomUtils.isActive(userId, roomId);
                            this.sendMessageToUser(msg, userId, DataTypeCode.chat, DataTypeCode.chat_recall);
                            MessageUtils.addSysMessage(userId, roomId, msg);
                            if (!active) {
                                ChatRoomUtils.editNewMsgCount(userId, roomId, 1);//添加通知
                            }
                        }
                    }
                    //3) 告诉自己撤回成功
                    String msg = "你撤回了一条消息";
                    this.sendMessageToUser(msg, fromUserId, DataTypeCode.chat, DataTypeCode.chat_recall);
                    MessageUtils.addSysMessage(fromUserId, roomId, msg);
                }


            }


            //######################################################    好友组     ######################################################
            else if (DataTypeCode.friend.equals(dataGroup)) {
                // ******************************添加好友*********************************
                if (DataTypeCode.friend_add.equals(dataType)) {

                }
            }


            //######################################################连接相关组######################################################
            //连接相关组
            else if (DataTypeCode.beat.equals(dataGroup)) {
                // ******************************小分类*********************************
                if (DataTypeCode.beat_link.equals(dataType)) {

                    logger.info(fromUserId + "  进入测试心跳 ！");
                    this.sendMessageToUser("ok", fromUserId, DataTypeCode.beat, DataTypeCode.beat_link);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("发生了错误了");
//            logger.error("发生了错误了");
        }
    }


    /**
     * 保存聊天记录并发送给接收方
     *
     * @param chatMessage
     * @param fromUserId
     * @param toUserId
     */
    private void saveAndSendChatMsg(ChatMessage chatMessage, String fromUserId, String toUserId) {
        logger.info("《   " + fromUserId + "    》给《     " + toUserId + "    》发送了消息：     " + String.valueOf(chatMessage.getMsg()));
        Session session1 = client.get(toUserId);
        if (null == session1) {
            System.err.println(toUserId + "   不在线!");
        } else {
            System.err.println(toUserId + "   在线!");
        }
        Session sessionGet = client.get(fromUserId);
        if (null == sessionGet) {
            System.err.println(fromUserId + "   不在线!");
        } else {
            System.err.println(fromUserId + "   在线!");
        }


        String roomId = chatMessage.getRoomId();
        //存储消息
        MessageUtils.addNewChatMessage(chatMessage);
//        UserChatRoomManage.addMessage(fromUserId, roomId, chatMessage);

        //更新用户聊天室时间
        //4)更新用户聊天室时间
        ChatRoomUtils.editChatRoomUpdateTime(fromUserId, roomId);
        ChatRoomUtils.editChatRoomUpdateTime(toUserId, roomId);
        //发送给自己处理结果
        ChatMessage toMess = null;
        //发送给指定用户
        ChatMessage fromMess = null;
        try {
            fromMess = (ChatMessage) chatMessage.clone();
            fromMess.setUser(UserUtils.getUserByUserId(fromUserId));
            this.sendMessageToUser(fromMess, toUserId, DataTypeCode.chat, DataTypeCode.chat_chatData);

            toMess = (ChatMessage) chatMessage.clone();
            toMess.setUser(UserUtils.getUserByUserId(toUserId));
            this.sendMessageToUser(toMess, fromUserId, DataTypeCode.chat, DataTypeCode.chat_chatData);

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException("克隆 ChatMessage失败");
        }

    }


    /**
     * 给指定人发送消息
     *
     * @param objMsg
     * @param userId
     * @param dataGroup
     * @param dataType
     */
    public void sendMessageToUser(Object objMsg, String userId, String dataGroup, String dataType) {
        DataResponse dataResponse = new DataResponse(VenusResponseHttpCode.OK, objMsg, dataGroup, dataType);

        String message = "";
        try {
            message = JSON.toJSONString(dataResponse, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            System.err.println("消息格式转换失败");

        }
        try {
            client.get(userId).getBasicRemote().sendText(message);
            System.err.println("发送给:    " + userId + "   的消息成功");
        } catch (Exception e) {
            System.err.println("发给：" + userId + "------>信息失败,原因:" + userId + "不在线");
        }


    }

    /**
     * 发送消息给聊天组
     *
     * @param objMsg
     * @param userList
     * @param dataGroup
     * @param dataType
     */
    public void sendMessageToGroupRoom(Object objMsg, List<User> userList, String dataGroup, String dataType) {
        if (userList != null) {
            for (User user : userList) {
                if (user == null) continue;
                this.sendMessageToUser(objMsg, user.getUserId(), dataGroup, dataType);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.info("服务端发生了错误" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 进入聊天室 --> 项目中读取用户信息获取用户名
     */
   /* @RequestMapping( "/websocket/{username}" )
    public String webSocket(Model model, @PathVariable( "username" ) String username) {

        //定义随机时间戳名称
        String name = username + ":";
        //String  datename = new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date());
        String datename = new SimpleDateFormat("msss").format(new Date());
        name = name + datename;

        //websock链接地址+游客名-->  项目中请定义在配置文件 -->或直接读取服务器，ip 端口
        // 读取服务器,ip 端口可看：https://blog.csdn.net/qq_41463655/article/details/92002474
        String path = "ws://127.0.0.1:8000/websocket/";
        model.addAttribute("path", path);
        model.addAttribute("username", name);
        return "websocket";
    }*/
}



/*
 *  注解说明
 *  @MessageMapping(value = "/chat")   // 匹配客户端 send 消息时的URL
 *  @SendTo("/topic/getResponse")      //用于给客户端订阅广播消息
 *  @SendToUser(value = "/personal")   //用于给客户端订阅点对点消息；
 *  @Payload：使用客户端 STOMP 帧的 body 赋值
 *  @Header(“xxx”)：使用客户端 STOMP 帧的 headers 中的 xxx 赋值
 *
 **/

//    @MessageMapping(value = "/chat") // 匹配客户端 send 消息时的URL
//    @SendTo("/topic/getResponse")   //分别用于给客户端订阅广播消息
//    public String talk(@Payload String text, @Header("simpSessionId") String sessionId) throws Exception {
//        return "【" + sessionId + "】说:【" + text + "】";
//    }

/**
 * 点对点推送
 * <p>
 * 异常信息推送
 * <p>
 * 异常信息推送
 * <p>
 * 异常信息推送
 * <p>
 * 异常信息推送
 * <p>
 * 异常信息推送
 * <p>
 * 异常信息推送
 * <p>
 * 异常信息推送
 */
/*
    @MessageMapping(value = "/speak")  // 匹配客户端 send 消息时的URL
    @SendToUser(value = "/personal")   //分别用于给客户端订阅点对点消息；
    public String speak(@Payload String text, @Header("simpSessionId") String sessionId) throws Exception {
        return text;
    }
    */

/**
 * 异常信息推送
 */
/*
    @MessageExceptionHandler
    @SendToUser(value = "/errors")
    public String handleException(Throwable exception) {
        return exception.getChatMessageExceptDel();
    }*/