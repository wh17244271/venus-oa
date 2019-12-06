package com.suypower.venus.dao;

import com.suypower.venus.entity.AppGroup;

import java.util.List;

public interface AppDao {
    /**
     * 获取app所有分组
     * @return
     */
    List<AppGroup> getWorkGroupList();
}
