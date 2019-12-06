package com.suypower.venus.service.impl;

import com.suypower.venus.dao.UserChatRoomDao;
import com.suypower.venus.dao.UserDao;
import com.suypower.venus.entity.*;
import com.suypower.venus.service.IChatMessageService;
import com.suypower.venus.service.IUserChatRoomService;
import com.suypower.venus.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service( "userChatRoomService" )
public class UserChatRoomServiceImpl implements IUserChatRoomService {

    @Autowired
    private UserChatRoomDao userChatRoomDao;


    @Override
    public List<ChatList> getChatList(String userId) {
        return userChatRoomDao.getChatList(userId);
    }

    @Override
    public List<ChatMessage> searchChatContent(String roomId, String userId, String msgType, String content) {
        return userChatRoomDao.searchChatContent(roomId, userId, msgType, content);
    }

    @Override
    public ChatRoom getChatRoomInfoByRoomId(String roomId) {
        return userChatRoomDao.getChatRoomInfoByRoomId(roomId);
    }

    @Override
    public List<User> getUserListByRoomId(String userId, String roomId) {
        return userChatRoomDao.getUserListByRoomId(userId, roomId);
    }

    @Override
    public List<User> getUserListByRoomIdExcept(String userId, String roomId, List<String> userIds) {
        List<User> userListByRoomId = this.getUserListByRoomId(userId, roomId);
        if (null == userIds || userIds.size() == 0 || null == userListByRoomId) return userListByRoomId;

        for (String uId : userIds) {
            Iterator<User> iterator = userListByRoomId.iterator();
            while (iterator.hasNext()) {
                User next = iterator.next();
                String userIdIn = next.getUserId();
                if (userIdIn.equals(uId)) {
                    iterator.remove();
                    break;
                }

            }
        }
        return userListByRoomId;

    }

    @Override
    public UserChatRoom getUserChatRoomByUserIdAndRoomId(String userId, String roomId) {
        return userChatRoomDao.getUserChatRoomByUserIdAndRoomId(userId, roomId);
    }

    @Override
    public List<UserChatRoom> getUserChatRoomList(String userId) {
        return userChatRoomDao.getUserChatRoomList(userId);
    }

    @Override
    public boolean editLeftDelStatus(String userId,String roomId,boolean leftDelStatus) {
        return userChatRoomDao.editLeftDelStatus(userId,roomId, leftDelStatus);
    }

    @Override
    public void editChatRoomUpdateTime(String userId, String roomId) {
        userChatRoomDao.editChatRoomUpdateTime(userId, roomId);
    }

    @Override
    public boolean editActiveStatus(String userId, String roomId, boolean activeStatus) {
        return userChatRoomDao.editActiveStatus(userId, roomId, activeStatus);
    }

    @Override
    public void editTopStatus(String userId, String roomId, boolean topStatus) {
        userChatRoomDao.editTopStatus(userId, roomId, topStatus);
    }

    @Override
    public void editkickoutStatus(String userId, String roomId, boolean kickoutStatus) {
        userChatRoomDao.editkickoutStatus(userId, roomId, kickoutStatus);
    }

    @Override
    public void editChatRoomName(String roomId, String roomName) {
        userChatRoomDao.editChatRoomName(roomId, roomName);
    }

    @Override
    public void editNewMsgCount(String userId, String roomId, int newMsgCount) {
        userChatRoomDao.editNewMsgCount(userId, roomId, newMsgCount);
    }

    @Override
    public ChatRoom judgeRoomIsExist(String oneUId, String otherUId, String roomType) {
        return userChatRoomDao.judgeRoomIsExist(oneUId, otherUId, roomType);
    }

    @Override
    public int creatChatRoom(ChatRoom chatRoom) {
        return userChatRoomDao.creatChatRoom(chatRoom);
    }

    @Override
    public int creatUserChatRoom(UserChatRoom userChatRoom) {
        return userChatRoomDao.creatUserChatRoom(userChatRoom);
    }

    /**
     * 创建单人聊天室
     * @param fromUserId
     * @param toUserId
     * @param roomType
     * @return
     */
    @Override
    public ChatRoom creatSingleChatRoom(String fromUserId, String toUserId, String roomType) {
        ChatRoom chatRoom = this.judgeRoomIsExist(fromUserId, toUserId, roomType);
        //1)判断是否存在聊天室
        if(chatRoom!=null && chatRoom.getRoomId()!=null){
            //说明存在私聊聊天室,直接返回聊天室信息
            return chatRoom;
        }
        //2)创建chatRoom
        chatRoom = new ChatRoom();
        chatRoom.setRoomType(roomType);
        chatRoom.setOwnerId(fromUserId);
        this.creatChatRoom(chatRoom);
        //3)创建用户聊天室
        UserChatRoom fromUserChatRoom = new UserChatRoom(fromUserId, chatRoom.getRoomId(), true);
        this.creatUserChatRoom(fromUserChatRoom);
        UserChatRoom toUserChatRoom = new UserChatRoom(toUserId, chatRoom.getRoomId(), true);
        this.creatUserChatRoom(toUserChatRoom);

        return chatRoom;
    }

    @Override
    public List<String> getUserIdIsKicked(String roomId) {
        return userChatRoomDao.getUserIdIsKicked(roomId);
    }

    @Autowired
    private IChatMessageService chatMessageService;
    @Override
    public void delUserChatRoom(String userId, String roomId) {
        //1)删除用户聊天室
        userChatRoomDao.delUserChatRoom(userId, roomId);
        //2)清空聊天室内容(删除没必要的垃圾)
        chatMessageService.clearChatRoomMsg(userId, roomId);
    }
}
