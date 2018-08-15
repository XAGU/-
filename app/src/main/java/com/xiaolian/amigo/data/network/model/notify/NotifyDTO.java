package com.xiaolian.amigo.data.network.model.notify;

import com.xiaolian.amigo.data.vo.Mapper;
import com.xiaolian.amigo.data.vo.Notify;

import lombok.Data;

/**
 * 通知
 *
 * @author zcd
 * @date 17/12/14
 */
@Data
public class NotifyDTO implements Mapper<Notify> {
    private String content;
    private Long createTime;
    private Long id;
    private Integer readStatus;
    private Integer type;

    @Override
    public Notify transform() {
        Notify notify = new Notify();
        notify.setContent(content);
        notify.setCreateTime(createTime);
        notify.setId(id);
        notify.setReadStatus(readStatus);
        notify.setType(type);
        return notify;
    }

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

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
