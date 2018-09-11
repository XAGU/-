package com.xiaolian.amigo.data.vo;

import lombok.Data;

@Data
public class LZTag {
    private String content ;
    private boolean isCheck ;

    public LZTag(String content ,boolean isCheck){
        this.content = content ;
        this.isCheck = isCheck ;
    }
}
