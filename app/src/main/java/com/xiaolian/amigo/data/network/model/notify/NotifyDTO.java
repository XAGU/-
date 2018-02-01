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
}
