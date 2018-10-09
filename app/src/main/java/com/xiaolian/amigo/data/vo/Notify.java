package com.xiaolian.amigo.data.vo;

import lombok.Data;

/**
 * 通知模型
 *
 * @author zcd
 * @date 17/9/22
 */
@Data
public class Notify {
    private String content;
    private Long createTime;
    private Long id;
    /**
     * 1 紧急公告 2 系统公告 3 客服消息
     */
    private Integer type;
    /**
     * 1 未读 2 已读
     */
    private Integer readStatus;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }
}
