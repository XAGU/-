package com.xiaolian.amigo.data.network.model.connecterror;

import java.util.List;

public class AppTradeStatisticDataReqDTO {


    /**
     * items : [{"avgTime":0,"count":0,"deviceType":"SHOWER","macAddress":"string","maxTime":0,"minTime":0,"residenceId":0,"result":"SUCCESS","supplierId":0,"target":"SERVER","time":0,"type":"SCAN"}]
     * terminalInfo : {"appVersion":"string","brand":"string","env":"USER","ip":"string","model":"string","os":"IOS","systemVersion":"string","uniqueId":"string"}
     */

    private TerminalInfoBean terminalInfo;
    private List<ItemsBean> items;

    public TerminalInfoBean getTerminalInfo() {
        return terminalInfo;
    }

    public void setTerminalInfo(TerminalInfoBean terminalInfo) {
        this.terminalInfo = terminalInfo;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class TerminalInfoBean {
        /**
         * appVersion : string
         * brand : string
         * env : USER
         * ip : string
         * model : string
         * os : IOS
         * systemVersion : string
         * uniqueId : string
         */

        private String appVersion;
        private String brand;
        private String env;
        private String ip;
        private String model;
        private String os;
        private String systemVersion;
        private String uniqueId;

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getEnv() {
            return env;
        }

        public void setEnv(String env) {
            this.env = env;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public String getSystemVersion() {
            return systemVersion;
        }

        public void setSystemVersion(String systemVersion) {
            this.systemVersion = systemVersion;
        }

        public String getUniqueId() {
            return uniqueId;
        }

        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
        }
    }

    public static class ItemsBean {
        /**
         * avgTime : 0
         * count : 0
         * deviceType : SHOWER
         * macAddress : string
         * maxTime : 0
         * minTime : 0
         * residenceId : 0
         * result : SUCCESS
         * supplierId : 0
         * target : SERVER
         * time : 0
         * type : SCAN
         */

        private int avgTime;
        private int count;
        private String deviceType;
        private String macAddress;
        private int maxTime;
        private int minTime;
        private long residenceId;
        private String result;
        private int supplierId;
        private String target;
        private long time;
        private String type;

        public int getAvgTime() {
            return avgTime;
        }

        public void setAvgTime(int avgTime) {
            this.avgTime = avgTime;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        public int getMaxTime() {
            return maxTime;
        }

        public void setMaxTime(int maxTime) {
            this.maxTime = maxTime;
        }

        public int getMinTime() {
            return minTime;
        }

        public void setMinTime(int minTime) {
            this.minTime = minTime;
        }

        public long getResidenceId() {
            return residenceId;
        }

        public void setResidenceId(long residenceId) {
            this.residenceId = residenceId;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public int getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(int supplierId) {
            this.supplierId = supplierId;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
