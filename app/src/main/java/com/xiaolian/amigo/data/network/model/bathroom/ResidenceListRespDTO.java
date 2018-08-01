package com.xiaolian.amigo.data.network.model.bathroom;

import java.util.List;

/**
 * 洗澡地址列表
 */
public class ResidenceListRespDTO {
        /**
         * residences : [{"allBaths":false,"fullName":"string","id":0,"macAddress":"string","name":"string"}]
         * total : 0
         */

        private int total;
        private java.util.List<ResidencesBean> residences;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ResidencesBean> getResidences() {
            return residences;
        }

        public void setResidences(List<ResidencesBean> residences) {
            this.residences = residences;
        }

        public static class ResidencesBean {
            /**
             * allBaths : false
             * fullName : string
             * id : 0
             * macAddress : string
             * name : string
             */

            private boolean allBaths;  // 是否全是公共浴室
            private String fullName;
            private int id;
            private String macAddress;
            private String name;

            public boolean isAllBaths() {
                return allBaths;
            }

            public void setAllBaths(boolean allBaths) {
                this.allBaths = allBaths;
            }

            public String getFullName() {
                return fullName;
            }

            public void setFullName(String fullName) {
                this.fullName = fullName;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getMacAddress() {
                return macAddress;
            }

            public void setMacAddress(String macAddress) {
                this.macAddress = macAddress;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
}
