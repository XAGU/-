package com.xiaolian.amigo.data.network.model.dto.response;

import java.util.List;

import lombok.Data;

/**
 * 学校列表DTO
 * @author zcd
 */
@Data
public class QueryBriefSchoolListRespDTO {

    private int total;
    private List<SchoolsBean> schools;

    public static class SchoolsBean {
        /**
         * id : 0
         * schoolName : string
         */

        private int id;
        private String schoolName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }
    }
}
