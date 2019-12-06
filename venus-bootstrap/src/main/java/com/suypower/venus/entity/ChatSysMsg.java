package com.suypower.venus.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 系统消息
 */
public class ChatSysMsg {
    private String sysMsgId;
    private User sender;
    private User rcpt;
    private String type;
    private String content;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    private String status;

    public String getSysMsgId() {
        return sysMsgId;
    }

    public void setSysMsgId(String sysMsgId) {
        this.sysMsgId = sysMsgId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRcpt() {
        return rcpt;
    }

    public void setRcpt(User rcpt) {
        this.rcpt = rcpt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
