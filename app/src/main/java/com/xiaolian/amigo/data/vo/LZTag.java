package com.xiaolian.amigo.data.vo;

import lombok.Data;

@Data
public class LZTag {
    private String content ;
    private boolean isCheck ;
    private int id ;

    public LZTag(String content ,boolean isCheck , int id ){
        this.content = content ;
        this.isCheck = isCheck ;
        this.id = id ;
    }
}
