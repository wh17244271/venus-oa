package com.suypower.venus.controller;

import com.suypower.venus.common.ConstantUser;
import com.suypower.venus.entity.ChatSysMsg;
import com.suypower.venus.entity.Friend;
import com.suypower.venus.entity.User;
import com.suypower.venus.platform.web.response.VenusResponse;
import com.suypower.venus.platform.web.response.VenusResponseHttpCode;
import com.suypower.venus.service.IFriendService;
import com.suypower.venus.utils.FriendUtils;
import com.suypower.venus.utils.ServletUtils;
import com.suypower.venus.utils.StringUtils;
import com.suypower.venus.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping( "/api/friend" )
public class FriendController {

    @Autowired
    private IFriendService friendService;

    /**
     * 获取好友列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping( "/getFriendList" )
    public VenusResponse getFriendList(String searchInfo) {
        String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
        List<Friend> friendList = FriendUtils.getFriendList(userId,searchInfo);
        return new VenusResponse(VenusResponseHttpCode.OK, friendList);


    }

    /**
     * 获取通讯录列表
     * @return
     */
    @ResponseBody
    @RequestMapping( "/getNotFriendList" )
    public VenusResponse getNotFriendList() {
        String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
        List<User> notFriendList = friendService.getNotFriendList(userId);
        return new VenusResponse(VenusResponseHttpCode.OK, notFriendList);


    }

    /**
     * 修改好友备注
     *
     * @param friendUserId
     * @param friendRemark
     * @return
     */
    @ResponseBody
    @RequestMapping( "/editFriendRemark" )
    public VenusResponse editFriendRemark(String friendUserId, String friendRemark) {
        String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
        boolean b = FriendUtils.editFriendRemark(userId, friendUserId, friendRemark);
        if (b) {
            return new VenusResponse(VenusResponseHttpCode.OK, "修改好友备注成功");
        } else {
            return new VenusResponse(VenusResponseHttpCode.InternalServerError, "修改好友备注失败");
        }

    }

    /**
     * 获取添加好友信息列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping( "/getMakeFriendMsgList" )
    public VenusResponse getMakeFriendMsgList() {
        String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
        List<ChatSysMsg> makeFriendMsg = friendService.getMakeFriendMsgList(userId);
        return new VenusResponse(VenusResponseHttpCode.OK, makeFriendMsg);
    }


    /**
     * 申请添加好友
     * @param rcpt
     * @param content
     * @return
     */
    @ResponseBody
    @RequestMapping( "/addMakeFriendMsg" )
    public VenusResponse addMakeFriendMsg(String rcpt,String content) {
        String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
        User user = UserUtils.getUser(rcpt);
        if(user==null){
            return new VenusResponse(VenusResponseHttpCode.BadRequest, "用户不存在");
        }
        Friend friend = friendService.getFriend(userId, rcpt);
        if(friend!=null){
            return new VenusResponse(VenusResponseHttpCode.BadRequest, "不可再次添加好友");
        }
        //需要查看是否已经存在申请添加好友记录,存在，更新时间，不存在，添加
        String sysMsgId = friendService.judgeFriendApply(userId, rcpt);
        if(StringUtils.isEmpty(sysMsgId)){
            friendService.addMakeFriendMsg(userId, rcpt, content);
        }else{
            friendService.editMakeFriendMsgTime(sysMsgId);
        }

        return new VenusResponse(VenusResponseHttpCode.OK, "申请添加好友成功");
    }

    /**
     * 处理好友信息列表
     * @param fUserId
     * @param result
     * @return
     */
    @ResponseBody
    @RequestMapping( "/dealMakeFriendMsg" )
    public VenusResponse dealMakeFriendMsg(String sysMsgId,String fUserId,boolean result) {
        String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
        User user = UserUtils.getUser(fUserId);
        if(user==null){
            return new VenusResponse(VenusResponseHttpCode.BadRequest, "用户不存在");
        }
        //同意
        if(result){
            Friend friend = friendService.getFriend(userId, fUserId);
            if(null==friend){
                friendService.addFriend(userId, fUserId, "");
                friendService.addFriend(fUserId, userId, "");
            }
            friendService.editMakeFriendMsgStatus(sysMsgId, "2");
        }else{
            friendService.editMakeFriendMsgStatus(sysMsgId, "3");
        }
        return new VenusResponse(VenusResponseHttpCode.OK, "添加好友成功");
    }



}
