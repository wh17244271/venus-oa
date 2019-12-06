package com.suypower.venus.entity;

import java.util.List;

public class ChatList {
    /**
     * 用户聊天室id
     */
    private String uRoomId;

    /**
     * 聊天室id (共享)
     */
    private String roomId;
    /**
     * 聊天室类型 (共享)
     */
    private String roomType;
    /**
     * 聊天室名称 (共享)
     */
    private String roomName;
    /**
     * 置顶
     */
    private boolean top;
    /**
     * 新消息条数
     */
    private int  newMsgCount;
    /**
     * 所有成员
     */
    private List<User> member;
    private ChatMessage lastMessage;
    /**'
     * 搜索条件时，包含当前人
     */
    private String contain;

    public ChatList() {
    }

    public ChatList(String roomId, String roomType, String roomName, int newMsgCount, List<User> member, ChatMessage lastMessage) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.roomName = roomName;
        this.newMsgCount = newMsgCount;
        this.member = member;
        this.lastMessage = lastMessage;
    }

    /*  public ChatList(String roomId, String roomType,String roomName,int newMsgCount,List<User> member,ChatMessage lastMessage){


    }*/

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public int getNewMsgCount() {
        return newMsgCount;
    }

    public void setNewMsgCount(int newMsgCount) {
        this.newMsgCount = newMsgCount;
    }

    public List<User> getMember() {
        return member;
    }

    public void setMember(List<User> member) {
        this.member = member;
    }

    public ChatMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getuRoomId() {
        return uRoomId;
    }

    public void setuRoomId(String uRoomId) {
        this.uRoomId = uRoomId;
    }

    public String getContain() {
        return contain;
    }

    public void setContain(String contain) {
        this.contain = contain;
    }
}
