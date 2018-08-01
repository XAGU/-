package com.xiaolian.amigo.data.network.model.bathroom;

/**
 * 删除洗澡地址
 */
public class DeleteResidenceRespDTO {
        /**
         * residenceId : 0
         * residenceName : string
         */

        private int residenceId;
        private String residenceName;

        public int getResidenceId() {
            return residenceId;
        }

        public void setResidenceId(int residenceId) {
            this.residenceId = residenceId;
        }

        public String getResidenceName() {
            return residenceName;
        }

        public void setResidenceName(String residenceName) {
            this.residenceName = residenceName;
        }
}
