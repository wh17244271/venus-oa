package com.suypower.venus.entity;

public class ChatMessage extends ChatMessageBase implements Cloneable {
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * 消息id
     */
    private String id;

    /**
     * 发送人 userId
     */
    private String from;
    /**
     * 接收人 userId
     */
    private String to;
    /**
     * 消息类型
     */
    private String msgType;
    /**
     * 消息具体内容
     */
    private String msg;


    /**
     * 消息发送时间
     */
    private Long time;
    /**
     * 消息所在的房间号
     */
    private String roomId;
    /**
     * 接收人用户信息
     */
    private User user;
    /**
     * 最后一条信息用户备注
     */
    private String userRemark;

    /**
     * 删除人集合,只要删除，就看不到
     */
//    private Set<String> delUserIdList;
    /**
     * 被拉者（被人拉去聊天)
     */
    private String pullers;
    /**
     * 被踢者(被人剔除聊天)
     */
    private String kickers;


    public ChatMessage() {
    }

   /* public ChatMessage(String id, String from, String to, String msgType, String msg, Long time, String roomId) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.msgType = msgType;
        this.msg = msg;
        this.time = time;
        this.user = UserUtils.getUserByUserId(from);
        this.roomId = roomId;
    }
*/

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
//
//
//
//    public Set<String> getDelUserIdList() {
//        return delUserIdList;
//    }
//
//    public void setDelUserIdList(Set<String> delUserIdList) {
//        this.delUserIdList = delUserIdList;
//    }

    public String getPullers() {
        return pullers;
    }

    public void setPullers(String pullers) {
        this.pullers = pullers;
    }

    public String getKickers() {
        return kickers;
    }

    public void setKickers(String kickers) {
        this.kickers = kickers;
    }

    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }
}
