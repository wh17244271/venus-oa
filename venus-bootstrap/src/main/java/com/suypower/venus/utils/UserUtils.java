package com.suypower.venus.utils;

import com.suypower.venus.common.SpringContextHolder;
import com.suypower.venus.entity.Friend;
import com.suypower.venus.entity.User;
import com.suypower.venus.service.IUserService;

import java.util.List;

public class UserUtils {

    private static IUserService userService = SpringContextHolder.getBean(IUserService.class);

    /**
     * 更新用户信息
     * @param user
     */
    public static void editUserInfo(User user){

        userService.editUserInfo(user);
    }

    /**
     * 通过用户id获取用户
     * @param userId
     * @return
     */
    public static User getUser(String userId){
        return userService.getUser(userId);
    }


    /**
     * xxx撤回了一条消息
     * @param userId
     * @param friUid
     * @return msg
     */
    public static String whoWithdraw(String userId,String friUid) {
        return "\""+UserUtils.getUserRemark(userId,friUid)+"\"撤回了一条消息";
    }

    /**
     * 你邀请了xxx、xxx加入了群聊
     * @param pullers
     * @return
     */
    public static String youInviteWho(String userId,String[] pullers) {
        String msg = "你邀请";
        String names = UserUtils.getSplicingNames(userId,pullers);
        return msg+"\""+names+"\"加入了群聊";
    }


    /**
     * xxx邀请了xxx、xxx加入了群聊
     * @param userId
     * @param pullers
     * @return
     */
    public static String whoInviteWho(String userId,String fUserId, String[] pullers) {
      return "\""+UserUtils.getUserRemark(userId,fUserId) +"\"邀请"+ UserUtils.getSplicingNames(userId,pullers)+"\"加入了群聊";
    }

    /**
     * 你将"xxx"移除了群聊
     * @param kicker 被移除人
     * @return
     */
    public static String youKickWho(String userId, String kicker) {
        return "你将\""+ UserUtils.getUserRemark(userId,kicker)+"\"移出了群聊";
    }

    /**
     * 你被xxx移出了群聊
     * @param byKicker
     * @return
     */
    public static String youKickedByWho(String userId,String byKicker) {
        return "你被\""+ UserUtils.getUserRemark(userId,byKicker)+"\"移出了群聊";
    }

    private static String getSplicingNames(String userId,String[] pullers){
        String names = "";
        if(null!=pullers&&pullers.length>0){
            for(String puller : pullers){
                String name = UserUtils.getUserRemark(userId,puller);
                if(StringUtils.isEmpty(names)){
                    names+=name;
                }else{
                    names+="、"+name;
                }
            }
        }
        return names;
    }

    /**
     * 获取用户的昵称
     * @param userId   本人的朋友
     * @param fUserId   被查找的朋友的昵称
     * @return
     */
    public static String getUserRemark(String userId,String fUserId){
        String name = "";
        Friend friend = FriendUtils.getFriend(userId, fUserId,"");
        if(friend!=null){
            name = friend.getFriendRemark();
            if(StringUtils.isEmpty(name)){
                User user = friend.getUser();
                if(null!=user){
                    name = user.getRemark();
                    if(StringUtils.isEmpty(name)){
                        name = user.getNickname();
                        if(StringUtils.isEmpty(name)){
                            name = user.getUsername();
                        }
                    }

                }
            }
        }
        return name;
    }


    /**
     * 通过userId查找用户User
     * @param userId
     * @return
     */
    public static User getUserByUserId(String userId) {
        return userService.getUser(userId);
    }

    /**
     * 通过用户名查找用户
     * @param username
     * @return
     */
    public static User getUserByUsername(String username) {
        return userService.getUser(new User(){{this.setUsername(username);}});
    }

    /**
     * 查找用户所有列表，除去自己
     * @param userId
     * @return
     */
    public static List<User> getUserListExcUserId(String userId){
        return userService.getUserListExcUserId(userId);
    }



    /**
     * 通过用户id查找用户头像
     * @param userId
     * @return
     */
    public static String getUserHeadurl(String userId) {
        User user = UserUtils.getUser(userId);
        if(null!=user)return user.getHeaderUrl();
        return "";
    }

    /**
     * 通过用户id查找用户昵称
     * @param userId
     * @return
     */
    public static String getUserNicename(String userId) {
        User user = UserUtils.getUser(userId);
        if(null!=user)return user.getNickname();
        return "";
    }


}
