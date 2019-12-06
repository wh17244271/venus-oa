package com.suypower.venus.service;

import com.suypower.venus.entity.User;

import java.util.List;

public interface IUserService {
    /**
     * 通过userId查找用户信息
     * @param userId
     * @return
     */
     User getUser(String userId);

    /**
     * 获取用户信息
     * @param user
     * @return
     */
     User getUser(User user);

    /**
     * 获取用户密码
     * @param user
     * @return
     */
     String getUserPassword(User user);
    /**
     * 查询所有用户信息
     * @return
     */
    List<User> getUserListExcUserId(String userId);

    /**
     * 更改用户信息
     * @param user
     */
    void editUserInfo(User user);
}
