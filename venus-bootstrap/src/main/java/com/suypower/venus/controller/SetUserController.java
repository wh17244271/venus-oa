package com.suypower.venus.controller;

import com.suypower.venus.common.ConstantUser;
import com.suypower.venus.entity.User;
import com.suypower.venus.platform.web.response.VenusResponse;
import com.suypower.venus.platform.web.response.VenusResponseHttpCode;
import com.suypower.venus.utils.ChatRoomUtils;
import com.suypower.venus.utils.ServletUtils;
import com.suypower.venus.utils.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping( "/api/set/user" )
public class SetUserController {


    /**
     * 置顶聊天
     * @param roomId
     * @param isTop
     * @return
     */
    @ResponseBody
    @RequestMapping( "/isTop" )
    public VenusResponse isTop(String roomId,boolean isTop) {
        String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
        ChatRoomUtils.editTopStatus(userId, roomId, isTop);
        return new VenusResponse(VenusResponseHttpCode.OK, "设置成功");
    }

    /**
     * 更改用户昵称
     * @param nickName
     * @return
     */
    @ResponseBody
    @RequestMapping( "/editNickName" )
    public VenusResponse editNickName(String nickName) {
        Assert.notNull(nickName,"用户昵称不能为null");
        String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
        User userByUserId = UserUtils.getUserByUserId(userId);
        UserUtils.editUserInfo(new User(){{this.setNickname(nickName);this.setUserId(userId);}});
        return new VenusResponse(VenusResponseHttpCode.OK, "更改成功");
    }


    /**
     * 设置头像
     * @param headerUrl
     * @return
     */
    @ResponseBody
    @RequestMapping( "/editHeadPortrait" )
    public VenusResponse editHeadPortrait(String headerUrl) {
        Assert.notNull(headerUrl,"用户头像地址不能为null");
        String userId = (String) ServletUtils.getSessionAttribute(ConstantUser.login_user);
        UserUtils.editUserInfo(new User(){{this.setHeaderUrl(headerUrl);this.setUserId(userId);}});
        return new VenusResponse(VenusResponseHttpCode.OK, "更改成功");
    }


}
