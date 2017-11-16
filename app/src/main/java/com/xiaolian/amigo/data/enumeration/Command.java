package com.xiaolian.amigo.data.enumeration;

/**
 * 蓝牙操作指令
 * <p>
 * Created by caidong on 2017/10/9.
 */
public enum Command {

    UNKNOWN(0, "未知指令", ""),
    CONNECT(1, "握手", "a801"),
    PRE_CHECK(2, "预结账", "a808"),
    CHECK_OUT(3, "结账", "a807"),
    OPEN_VALVE(4, "开阀", "a803"),
    CLOSE_VALVE(5, "关阀", "a804");

    // 指令类型
    private int type;
    // 指令描述
    private String desc;
    // 响应结果前缀
    private String respPrefix;

    Command(int type, String desc, String respPrefix) {
        this.type = type;
        this.desc = desc;
        this.respPrefix = respPrefix;
    }

    // 根据指令类型查找
    public static Command getCommand(int type) {
        for (Command command : Command.values()) {
            if (command.getType() == type) {
                return command;
            }
        }
        return UNKNOWN;
    }

    // 根据指令的响应结果查找
    public static Command getCommand(String data) {
        String prefix = data.substring(0, 4);
        for (Command command : Command.values()) {
            if (command.getRespPrefix().equalsIgnoreCase(prefix)) {
                return command;
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRespPrefix() {
        return respPrefix;
    }

    public void setRespPrefix(String respPrefix) {
        this.respPrefix = respPrefix;
    }

}
