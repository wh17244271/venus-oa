package com.suypower.venus.dao;

import com.suypower.venus.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserChatRoomDao {
    /**
     * 获取用户聊天列表
     *
     * @param userId
     * @return
     */
    List<ChatList> getChatList(String userId);
    /**
     * 通过内容搜索聊天记录
     * @param roomId
     * @param loginUserId
     * @param msgType
     * @param content
     * @return
     */
    List<ChatMessage> searchChatContent(@Param("roomId") String roomId,
                                        @Param("loginUserId")String loginUserId,
                                        @Param("msgType")String msgType,
                                        @Param("content")String content);


    /**
     * 通过房间id查找房间信息
     *
     * @param roomId
     * @return
     */
    ChatRoom getChatRoomInfoByRoomId(String roomId);

    /**
     * 查找聊天室所有成员
     *
     * @param userId
     * @param roomId
     * @return
     */
    List<User> getUserListByRoomId(@Param( "userId" ) String userId, @Param( "roomId" ) String roomId);

    /**
     * 通过用户id和roomId查找用户聊天室信息
     *
     * @param userId
     * @param roomId
     * @return
     */
    UserChatRoom getUserChatRoomByUserIdAndRoomId(@Param( "userId" ) String userId, @Param( "roomId" ) String roomId);
    /**
     * 通过用户id获取所有用户聊天室
     * @param userId
     * @return
     */
    List<UserChatRoom>   getUserChatRoomList(String userId);


    /**
     * 编辑用户聊天室左滑删除状态
     *
     * @param uRoomId
     * @param leftDelStatus
     * @return
     */
    boolean editLeftDelStatus(@Param( "uRoomId" ) String uRoomId,
                              @Param( "leftDelStatus" ) String leftDelStatus);


    /**
     * 编辑用户聊天室左滑删除状态
     *
     * @param userId
     * @param roomId
     * @param leftDelStatus
     * @return
     */
    boolean editLeftDelStatus(@Param( "userId" ) String userId,
                              @Param( "roomId" ) String roomId,
                              @Param( "leftDelStatus" ) boolean leftDelStatus);

    /**
     * 编辑用户聊天室更新时间
     * @param userId
     * @param roomId
     * @return
     */
    boolean editChatRoomUpdateTime(@Param( "userId" ) String userId,
                                   @Param( "roomId" ) String roomId);

    /**
     * 编辑用户聊天室窗口激活状态
     * @param userId
     * @param roomId
     * @param activeStatus
     * @return
     */
    boolean editActiveStatus(@Param( "userId" ) String userId,
                             @Param( "roomId" ) String roomId,
                             @Param( "activeStatus" )boolean activeStatus);

    /**
     * 编辑用户聊天室置顶状态
     * @param userId
     * @param roomId
     * @param topStatus
     */
    void editTopStatus(@Param( "userId" ) String userId,
                       @Param( "roomId" ) String roomId,
                       @Param( "topStatus" )boolean topStatus);

    /**
     * 更新用户踢出状态
     * @param userId
     * @param roomId
     * @param kickoutStatus
     */
    void editkickoutStatus(@Param( "userId" ) String userId,
                           @Param( "roomId" ) String roomId,
                           @Param( "kickoutStatus" ) boolean kickoutStatus);

    /**
     * 更新聊天室名字
     * @param roomId
     * @param roomName
     */
    void editChatRoomName(@Param( "roomId" ) String roomId,@Param( "roomName" ) String roomName);

    /**
     * 更新消息条数
     *
     * @param userId
     * @param roomId
     * @param newMsgCount
     */
    void editNewMsgCount(@Param( "userId" ) String userId,
                         @Param( "roomId" ) String roomId,
                         @Param( "newMsgCount" ) int newMsgCount);

    /**
     * 判断两个用户是否存在私人聊天室
     * @param oneUId
     * @param otherUId
     * @param roomType
     * @return
     */
    ChatRoom judgeRoomIsExist(@Param( "oneUId" )String oneUId,
                             @Param( "otherUId" )String otherUId,
                             @Param( "roomType" )String roomType);

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
    void delUserChatRoom(@Param( "userId" ) String userId, @Param( "roomId" ) String roomId);
}
