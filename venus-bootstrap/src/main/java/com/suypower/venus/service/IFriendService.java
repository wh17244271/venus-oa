package com.suypower.venus.service;

import com.suypower.venus.entity.ChatSysMsg;
import com.suypower.venus.entity.Friend;
import com.suypower.venus.entity.User;

import java.util.List;

public interface IFriendService {
    /**
     * 获取用户的所有朋友信息
     * @param userId
     * @return
     */
    List<Friend> getFriendList(String userId, String searchInfo);

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
    String judgeFriendApply(String sender,String rcpt);

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
    Friend getFriend(String userId,String fUserId);

    /**
     * 修改好友备注
     * @param userId
     * @param fUId
     * @param friendRemark
     */
    void editFriendRemark(String userId,String fUId,String friendRemark);

    /**
     * 更改好友申请状态
     * @param sysMsgId
     * @param sstatus  1:正常申请 ；2：同意  ；3：拒绝
     */
    void editMakeFriendMsgStatus(String sysMsgId,String sstatus);

    /**
     * 获取添加好友信息列表
     * @param userId
     * @return
     */
    List<ChatSysMsg> getMakeFriendMsgList(String userId);

    /**
     * 添加好友申请
     * @param sender
     * @param rcpt
     * @param content
     */
    void  addMakeFriendMsg(String sender,String rcpt,String content);

    /**
     * 添加好友
     * @param userId
     * @param fUserId
     * @param remark
     */
    void addFriend(String userId,String fUserId,String remark);
}
