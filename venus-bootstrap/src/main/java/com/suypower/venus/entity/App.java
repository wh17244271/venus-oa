package com.suypower.venus.entity;

import com.suypower.venus.utils.StringUtils;

public class App {
    private String appId;
    private String name ;
    private String title;
    private String style;
    private String ico;
    private int unread;
    private boolean display;
    private boolean click;
    private String appDesc;

    public App() {
    }

    public App(String name, String title, String style, String ico, int unread) {
        this.appId = StringUtils.uuid();
        this.name = name;
        this.title = title;
        this.style = style;
        this.ico = ico;
        this.unread = unread;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }
}
