package com.suypower.venus.dao;

import com.suypower.venus.entity.ChatSysMsg;
import com.suypower.venus.entity.Friend;
import com.suypower.venus.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FriendDao {
    /**
     * 获取用户的所有朋友信息
     *
     * @param userId
     * @return
     */
    List<Friend> getFriendList(@Param( "userId" ) String userId,
                               @Param( "searchInfo" ) String searchInfo);
    /**
     * 获取用户当前非朋友列表
     * @param userId
     * @return
     */
    List<User> getNotFriendList(String userId);
    /**
     * 判断是否有好友申请
     * @param sender
     * @param rcpt
     * @return
     */
    String judgeFriendApply(@Param( "sender" ) String sender,
                         @Param( "rcpt" ) String rcpt);

    /**
     * 更改好友申请时间为最新时间
     * @param sysMsgId
     */
    void editMakeFriendMsgTime(String sysMsgId);
    /**
     * 获取朋友信息
     * @param userId
     * @param fUserId
     * @return
     */
    Friend getFriend(@Param( "userId" )String userId,
                     @Param( "fUserId" )String fUserId);

    /**
     * 修改好友备注
     *
     * @param userId
     * @param fUId
     * @param friendRemark
     */
    void editFriendRemark(@Param( "userId" ) String userId, @Param( "fUId" ) String fUId,
                          @Param( "friendRemark" ) String friendRemark);

    /**
     * 更改好友申请状态
     * @param sysMsgId
     * @param status  1:正常申请 ；2：同意  ；3：拒绝
     */
    void editMakeFriendMsgStatus(@Param( "sysMsgId" ) String sysMsgId,
                                 @Param( "status" ) String status);

    /**
     * 获取添加好友信息列表
     *
     * @param userId
     * @return
     */
    List<ChatSysMsg> getMakeFriendMsgList(String userId);

    /**
     * 添加好友申请
     *
     * @param sender
     * @param rcpt
     * @param content
     */
    void addMakeFriendMsg(@Param( "sender" ) String sender,
                          @Param( "rcpt" ) String rcpt,
                          @Param( "content" ) String content);

    /**
     * 添加好友
     *
     * @param userId
     * @param fUserId
     * @param remark
     */
    void addFriend(@Param( "userId" ) String userId,
                   @Param( "fUserId" ) String fUserId,
                   @Param( "remark" ) String remark);

}
