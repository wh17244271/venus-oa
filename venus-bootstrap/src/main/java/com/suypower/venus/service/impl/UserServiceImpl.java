package com.suypower.venus.service.impl;

import com.suypower.venus.dao.UserDao;
import com.suypower.venus.entity.User;
import com.suypower.venus.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUser(String userId) {
        return userDao.getUserByUserId(userId);
    }

    @Override
    public User getUser(User user) {
        return userDao.getUserInfo(user);
    }

    @Override
    public String getUserPassword(User user) {

        return userDao.getUserPassword(user);
    }

    @Override
    public List<User> getUserListExcUserId(String userId) {
        return userDao.getUserListExcUserId(userId);
    }

    @Override
    public void editUserInfo(User user) {
        userDao.editUserInfo(user);
    }

}
