package com.suypower.venus.utils;

import com.suypower.venus.common.SpringContextHolder;
import com.suypower.venus.entity.Friend;
import com.suypower.venus.entity.User;
import com.suypower.venus.service.IFriendService;

import java.util.*;

public class FriendUtils {
    private static IFriendService friendService = SpringContextHolder.getBean(IFriendService.class);
//    public static Map<String, Set<Friend>> friendMap = new LinkedHashMap<>();


    /*static {
        List<User> users = UserController.users;
        for(User userss:users){
            String userId = userss.getUserId();
            List<User> friendUser = UserUtils.getUserListExcUserId(userId);
            if(friendUser!=null){
                for(User user:friendUser){
                    FriendUtils.creatFriend(userId,user.getUserId());
                }
            }
        }

    }
*/


    /**
     * 获取朋友列表
     * @param userId
     * @return
     */
    public static List<Friend> getFriendList(String userId,String searchInfo){

        List<Friend> friendList = friendService.getFriendList(userId,searchInfo);
        return friendList;
    }
    /**
     * 获取指定好友
     * @param userId
     * @param friendUserid
     * @return
     */
    public static Friend getFriend(String userId,String friendUserid,String searchInfo){
        List<Friend> friendList = FriendUtils.getFriendList(userId,searchInfo);
        for(Friend friend:friendList){
            User user = friend.getUser();
            if(null==user)continue;
            if(user.getUserId().equals(friendUserid)){
                return friend;
            }
        }
        return null;
    }




    /**
     * 添加朋友
     * @param userId
     * @param friendUserid
     */
   /* public static boolean addFriend(String userId,String friendUserid){
        boolean friend = FriendUtils.isFriend(userId, friendUserid);
        if(friend)return true;

        Set<Friend> friendList = FriendUtils.getFriendList(userId);
        boolean add = friendList.add(new Friend(UserUtils.getUserByUserId(friendUserid), ""));
        return add;
    }*/


    /**
     * 设置好友备注
     * @param userId
     * @param friendUserId
     * @param friendRemark
     * @return
     */
    public static boolean editFriendRemark(String userId,String friendUserId,String friendRemark){
        friendService.editFriendRemark(userId, friendUserId, friendRemark);
        return true;
    }

    /**
     * 判断两人是否是朋友
     * @param userId
     * @param friendUserid
     * @return
     */
   /* public static boolean isFriend(String userId,String friendUserid){
        if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(friendUserid) || userId.equals(friendUserid)){
            throw new RuntimeException("判断两人是否为好友异常:原因可能是自己不能加自己");
        }
        Friend friend = FriendUtils.getFriend(userId, friendUserid);
        if(null==friend){
            return false;
        }else {
            return true;
        }

    }*/




}
