package com.suypower.venus.service;

import com.suypower.venus.entity.*;

import java.util.List;

public interface IUserChatRoomService {
    /**
     * 获取用户聊天列表
     * @param userId
     * @return
     */
    List<ChatList> getChatList(String userId);

    /**
     * 通过内容搜索聊天记录
     * @param roomId
     * @param userId
     * @param msgType
     * @param content
     * @return
     */
    List<ChatMessage> searchChatContent(String roomId,String userId,
                                        String msgType,String content);

    /**
     * 通过房间id查找房间信息
     * @param roomId
     * @return
     */
    ChatRoom getChatRoomInfoByRoomId(String roomId);


    /**
     * 查找聊天室所有成员
     * @param userId
     * @param roomId
     * @return
     */
    List<User> getUserListByRoomId(String userId,String roomId);

    /**
     * 查找聊天室成员，除了某些用户
     * @param userId    当前指定用户
     * @param roomId    房间号
     * @param userIds   排除的人
     * @return
     */
    List<User> getUserListByRoomIdExcept(String userId,String roomId,List<String> userIds);


    /**
     * 通过用户id和roomId查找用户聊天室信息
     * @param userId
     * @param roomId
     * @return
     */
    UserChatRoom getUserChatRoomByUserIdAndRoomId(String userId, String roomId);

    /**
     * 通过用户id获取所有用户聊天室
     * @param userId
     * @return
     */
    List<UserChatRoom>   getUserChatRoomList(String userId);

    /**
     * 编辑用户聊天室左滑删除状态
     * @param userId
     * @param roomId
     * @param leftDelStatus
     * @return
     */
    boolean editLeftDelStatus(String userId,String roomId,boolean leftDelStatus);

    /**
     * 编辑用户聊天室更新时间
     * @param userId
     * @param roomId
     * @return
     */
    void editChatRoomUpdateTime(String userId,String roomId);

    /**
     * 编辑用户聊天室窗口激活状态
     * @param userId
     * @param roomId
     * @param activeStatus
     * @return
     */
    boolean editActiveStatus(String userId,String roomId,boolean activeStatus);

    /**
     * 编辑用户聊天室置顶状态
     * @param userId
     * @param roomId
     * @param topStatus
     */
    void editTopStatus(String userId,String roomId,boolean topStatus);
    /**
     * 更新用户踢出状态
     * @param userId
     * @param roomId
     * @param kickoutStatus
     */
    void editkickoutStatus(String userId,String roomId,boolean kickoutStatus);

    /**
     * 更新聊天室名字
     * @param roomId
     * @param roomName
     */
    void editChatRoomName(String roomId,String roomName);

    /**
     * 更新消息条数
     * @param userId
     * @param roomId
     * @param newMsgCount
     */
    void editNewMsgCount(String userId,String roomId,int newMsgCount);



    /**
     * 判断两个用户是否存在私人聊天室
     * @param oneUId
     * @param otherUId
     * @param roomType
     * @return
     */
    ChatRoom judgeRoomIsExist(String oneUId,String otherUId,String roomType);

    /**
     * 新增聊天室
     * @param chatRoom
     * @return
     */
    int creatChatRoom(ChatRoom chatRoom);
    /**
     * 创建用户聊天室
     * @param userChatRoom
     * @return
     */
    int creatUserChatRoom(UserChatRoom userChatRoom);

    /**
     * 创建单人聊天室
     * @param fromUserId
     * @param toUserId
     * @param roomType
     * @return
     */
    ChatRoom creatSingleChatRoom(String fromUserId, String toUserId,String roomType);

    /**
     * 获取聊天室中被踢出的用户
     * @param roomId
     * @return
     */
    List<String> getUserIdIsKicked(String roomId);


    /**
     * 删除用户聊天室
     * @param userId
     * @param roomId
     */
    void delUserChatRoom(String userId,String roomId);
}
