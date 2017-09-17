package com.xiaolian.amigo.data.network.model.dto.response;

import java.util.List;

import lombok.Data;

/**
 * 学校业务列表DTO
 * @author zcd
 */
@Data
public class QuerySchoolBizListRespDTO {

    private List<BusinessesBean> businesses;

    public static class BusinessesBean {
        /**
         * businessId : 0
         * using : true
         */

        private int businessId;
        private boolean using;

        public int getBusinessId() {
            return businessId;
        }

        public void setBusinessId(int businessId) {
            this.businessId = businessId;
        }

        public boolean isUsing() {
            return using;
        }

        public void setUsing(boolean using) {
            this.using = using;
        }
    }
}
