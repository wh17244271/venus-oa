package com.suypower.venus.utils;

import com.suypower.venus.common.ConstantChatRoomType;
import com.suypower.venus.common.SpringContextHolder;
import com.suypower.venus.entity.ChatRoom;
import com.suypower.venus.entity.User;
import com.suypower.venus.entity.UserChatRoom;
import com.suypower.venus.service.IUserChatRoomService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatRoomUtils {
    private static IUserChatRoomService userChatRoomService = SpringContextHolder.getBean(IUserChatRoomService.class);

    /**
     * 获取用户聊天室信息
     *
     * @param userId
     * @param roomId
     * @return
     */
    public static UserChatRoom getUserChatRoom(String userId, String roomId) {
        return userChatRoomService.getUserChatRoomByUserIdAndRoomId(userId, roomId);
    }

    /**
     * 获取聊天室信息
     *
     * @param roomId
     * @return
     */
    public static ChatRoom getChatRoom(String roomId) {
        return userChatRoomService.getChatRoomInfoByRoomId(roomId);
    }

    /**
     * 判断用户是否已经左滑删除了
     *
     * @param userId
     * @param roomId
     * @return
     */
    public static boolean isLeftDel(String userId, String roomId) {
        UserChatRoom userChatRoom = ChatRoomUtils.getUserChatRoom(userId, roomId);
        if (null == userChatRoom) {
            return true;
        } else {
            return userChatRoom.isLeftDel();
        }
    }

    /**
     * 判断用户聊天室是否激活
     *
     * @param userId
     * @param roomId
     * @return
     */
    public static boolean isActive(String userId, String roomId) {
        UserChatRoom userChatRoom = ChatRoomUtils.getUserChatRoom(userId, roomId);
        if (null == userChatRoom) {
            return false;
        } else {
            return userChatRoom.isActive();
        }
    }


    /**
     * 更改用户聊天室左滑删除状态
     *
     * @param userId
     * @param roomId
     * @param leftDelStatus 0：没有左滑删除  1：左滑删除
     * @return
     */
    public static boolean editLeftDelStatus(String userId, String roomId, boolean leftDelStatus) {
        return userChatRoomService.editLeftDelStatus(userId, roomId, leftDelStatus);

    }

    /**
     * 编辑用户聊天室窗口激活状态
     *
     * @param userId
     * @param roomId
     * @param activeStatus
     * @return
     */
    public static boolean editActiveStatus(String userId, String roomId, boolean activeStatus) {
        return userChatRoomService.editActiveStatus(userId, roomId, activeStatus);

    }

    /**
     * 编辑用户所有聊天室窗口激活状态
     *
     * @param userId
     * @param activeStatus
     * @return
     */
    public static void editAllUserChatRoomActiveStatus(String userId, boolean activeStatus) {
        List<UserChatRoom> userChatRoomList = userChatRoomService.getUserChatRoomList(userId);
        if (null != userChatRoomList && userChatRoomList.size() > 0) {
            for (UserChatRoom userChatRoom : userChatRoomList) {
                String roomId = userChatRoom.getRoomId();
                ChatRoomUtils.editActiveStatus(userId, roomId, activeStatus);
            }
        }

    }

    /**
     * 更改用户聊天室踢出状态
     *
     * @param userId
     * @param roomId
     * @param kickoutStatus
     */
    public static void editkickoutStatus(String userId, String roomId, boolean kickoutStatus) {
        userChatRoomService.editkickoutStatus(userId, roomId, kickoutStatus);

    }

    /**
     * 更新消息条数
     *
     * @param userId
     * @param roomId
     * @param newMsgCount
     * @return
     */
    public static void editNewMsgCount(String userId, String roomId, int newMsgCount) {
        userChatRoomService.editNewMsgCount(userId, roomId, newMsgCount);
    }

    /**
     * 编辑用户聊天室置顶状态
     *
     * @param userId
     * @param roomId
     * @param topStatus
     */
    public static void editTopStatus(String userId, String roomId, boolean topStatus) {
        userChatRoomService.editTopStatus(userId, roomId, topStatus);
    }

    /**
     * 编辑用户聊天室更新时间
     *
     * @param userId
     * @param roomId
     */
    public static void editChatRoomUpdateTime(String userId, String roomId) {
        userChatRoomService.editChatRoomUpdateTime(userId, roomId);
    }

    /**
     * 更新聊天室名字
     *
     * @param roomId
     * @param roomName
     */
    public static void editChatRoomName(String roomId, String roomName) {
        userChatRoomService.editChatRoomName(roomId, roomName);
    }

    /**
     * 查找聊天室所有成员
     *
     * @param userId
     * @param roomId
     * @param isKicked 是否包含已经被踢出的人
     * @return
     */
    public static List<User> getUserListByRoomId(String userId, String roomId, boolean isKicked) {

        List<String> userIdIsKicked = userChatRoomService.getUserIdIsKicked(roomId);
        if(isKicked){
            return ChatRoomUtils.getUserListByRoomIdExcept(userId, roomId, userIdIsKicked);
        }else{
            return userChatRoomService.getUserListByRoomId(userId, roomId);
        }


    }


    /**
     * 查找聊天室成员，除了某些用户
     *
     * @param userId
     * @param roomId
     * @return
     */
    public static List<User> getUserListByRoomIdExcept(String userId, String roomId, List<String> userIds) {
        return userChatRoomService.getUserListByRoomIdExcept(userId, roomId, userIds);
    }

    /**
     * 获取聊天室中被踢出的用户
     *
     * @param roomId
     * @return
     */
    public static List<String> getUserIdIsKicked(String roomId) {
        return userChatRoomService.getUserIdIsKicked(roomId);
    }

    public static String creatGroupChatRoom(String userId, String[] pullers) {
        if (pullers == null || pullers.length < 2) throw new RuntimeException("创建群聊，拉取人数不得少于2人");
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomType(ConstantChatRoomType.chat_type_group);
        chatRoom.setRoomName("群聊");
        chatRoom.setOwnerId(userId);
        userChatRoomService.creatChatRoom(chatRoom);//创建公共聊天室
        String roomId = chatRoom.getRoomId();

        UserChatRoom owin = new UserChatRoom(userId, roomId, false);
        userChatRoomService.creatUserChatRoom(owin);
        for (String puller : pullers) {
            UserChatRoom userChatRoom = new UserChatRoom(puller, roomId, true);
            userChatRoomService.creatUserChatRoom(userChatRoom);
        }
        return roomId;
    }

    /**
     * 拉人聊天
     *
     * @param userId
     * @param roomId
     * @param pullers
     */
    public static  void pullToGroup(String userId, String roomId, String[] pullers) {


        if (null != pullers) {
            for (String puller : pullers) {
                UserChatRoom userChatRoom = ChatRoomUtils.getUserChatRoom(puller, roomId);
                if (userChatRoom != null) {
                    //不等于 null,又能被拉，说明被踢过
                    //将此用户重新激活
                    userChatRoomService.editkickoutStatus(puller, roomId, false);
                    continue;
                }
                //创建用户群聊室
                userChatRoom = new UserChatRoom(puller, roomId, true);
                userChatRoomService.creatUserChatRoom(userChatRoom);
                //历史记录不可看
                MessageUtils.delUserChatRoomMsg(puller, roomId);
            }
        }

    }

    /**
     * 删除用户聊天室
     *
     * @param userId
     * @param roomId
     */
    public static void delUserChatRoom(String userId, String roomId) {
        userChatRoomService.delUserChatRoom(userId, roomId);
    }


}
