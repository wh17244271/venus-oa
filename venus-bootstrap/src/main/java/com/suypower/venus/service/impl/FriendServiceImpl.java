package com.suypower.venus.service.impl;

import com.suypower.venus.dao.FriendDao;
import com.suypower.venus.entity.ChatSysMsg;
import com.suypower.venus.entity.Friend;
import com.suypower.venus.entity.User;
import com.suypower.venus.service.IFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("friendService")
public class FriendServiceImpl implements IFriendService {

    @Autowired
    private FriendDao friendDao;

    @Override
    public List<Friend> getFriendList(String userId, String searchInfo) {
        return friendDao.getFriendList(userId,searchInfo);
    }

    @Override
    public List<User> getNotFriendList(String userId) {
        return friendDao.getNotFriendList(userId);
    }

    @Override
    public String judgeFriendApply(String sender, String rcpt) {
        return friendDao.judgeFriendApply(sender, rcpt);
    }

    @Override
    public void editMakeFriendMsgTime(String sysMsgId) {
        friendDao.editMakeFriendMsgTime(sysMsgId);
    }

    @Override
    public Friend getFriend(String userId, String fUserId) {
        return friendDao.getFriend(userId, fUserId);
    }

    @Override
    public void editFriendRemark(String userId, String fUId, String friendRemark) {
        friendDao.editFriendRemark(userId, fUId, friendRemark);
    }

    @Override
    public void editMakeFriendMsgStatus(String sysMsgId, String sstatus) {
        friendDao.editMakeFriendMsgStatus(sysMsgId, sstatus);
    }

    @Override
    public List<ChatSysMsg> getMakeFriendMsgList(String userId) {
        return friendDao.getMakeFriendMsgList(userId);
    }

    @Override
    public void addMakeFriendMsg(String sender, String rcpt, String content) {
        friendDao.addMakeFriendMsg(sender, rcpt, content);
    }

    @Override
    public void addFriend(String userId, String fUserId, String remark) {
        friendDao.addFriend(userId, fUserId, remark);
    }
}
