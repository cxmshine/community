package com.nowcoder.community.entity;

import java.util.Date;

/**
 * @author xuming
 * @Date 2019/11/6 22:24
 * comment表对应的实体类的定义
 */
public class Comment {
    private int id;
    private int userId;
    private int entityType;
    private int entityId;
    /** 若对评论进行回复,则targetId是指对应的评论的用户id */
    private int targetId;
    /** status值为0表示该评论有效 */
    private int status;
    private String content;
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Comment{" + "id=" + id + ", userId=" + userId + ", entityType=" + entityType + ", entityId=" + entityId
            + ", targetId=" + targetId + ", status=" + status + ", content='" + content + '\'' + ", createTime="
            + createTime + '}';
    }
}
