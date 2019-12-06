package com.suypower.venus.service.impl;

import com.suypower.venus.dao.AppDao;
import com.suypower.venus.entity.AppGroup;
import com.suypower.venus.service.IAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("appService")
public class AppServiceImpl implements IAppService {

    @Autowired
    private AppDao appDao;

    @Override
    public List<AppGroup> getWorkGroupList() {
        return appDao.getWorkGroupList();
    }
}
