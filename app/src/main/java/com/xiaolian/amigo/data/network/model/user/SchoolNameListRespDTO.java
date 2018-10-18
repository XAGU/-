package com.xiaolian.amigo.data.network.model.user;

import java.util.List;

public class SchoolNameListRespDTO {

        private java.util.List<SchoolNameListBean> schoolNameList;

        public List<SchoolNameListBean> getSchoolNameList() {
            return schoolNameList;
        }

        public void setSchoolNameList(List<SchoolNameListBean> schoolNameList) {
            this.schoolNameList = schoolNameList;
        }

        public static class SchoolNameListBean {
            /**
             * initials : string
             * schoolList : [{"id":0,"schoolName":"string"}]
             */

            private String initials;
            private java.util.List<SchoolListBean> schoolList;

            public String getInitials() {
                return initials;
            }

            public void setInitials(String initials) {
                this.initials = initials;
            }

            public List<SchoolListBean> getSchoolList() {
                return schoolList;
            }

            public void setSchoolList(List<SchoolListBean> schoolList) {
                this.schoolList = schoolList;
            }

            public static class SchoolListBean {
                /**
                 * id : 0
                 * schoolName : string
                 */

                private Long id;
                private String schoolName;

                public Long getId() {
                    return id;
                }

                public void setId(Long id) {
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
    }
