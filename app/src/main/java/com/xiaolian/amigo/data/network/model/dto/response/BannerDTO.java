package com.xiaolian.amigo.data.network.model.dto.response;

import java.io.Serializable;

import lombok.Data;

/**
 * banner
 * <p>
 * Created by zcd on 17/11/6.
 */
@Data
public class BannerDTO implements Serializable {
    private String image;
    private String link;
    private Integer type;

    public BannerDTO(Integer type, String image, String link) {
        this.image = image;
        this.link = link;
        this.type = type;
    }
}
