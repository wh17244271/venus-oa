package com.suypower.venus.service;

import com.suypower.venus.entity.AppGroup;

import java.util.List;

public interface IAppService {
    /**
     * 获取app所有分组
     * @return
     */
    List<AppGroup> getWorkGroupList();
}
