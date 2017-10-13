package com.xiaolian.amigo.data.network.model;

/**
 * Created by adamzfc on 9/14/17.
 */

public class Error {

    private int code;
    private String debugMessage;
    private String displayMessage;
    private Integer bleCmdType;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public Integer getBleCmdType() {
        return bleCmdType;
    }

    public void setBleCmdType(Integer bleCmdType) {
        this.bleCmdType = bleCmdType;
    }
}
