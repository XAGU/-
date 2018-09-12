package com.xiaolian.amigo.data.network.model.lostandfound;

import java.util.List;

import lombok.Data;

@Data
public class BbsTopicListTradeRespDTO {
        private java.util.List<TopicListBean> topicList;

        public List<TopicListBean> getTopicList() {
            return topicList;
        }

        public void setTopicList(List<TopicListBean> topicList) {
            this.topicList = topicList;
        }

        public static class TopicListBean {
            /**
             * icon : string
             * topicId : 0
             * topicName : string
             */

            private String icon;
            private int topicId;
            private String topicName;

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getTopicId() {
                return topicId;
            }

            public void setTopicId(int topicId) {
                this.topicId = topicId;
            }

            public String getTopicName() {
                return topicName;
            }

            public void setTopicName(String topicName) {
                this.topicName = topicName;
            }
        }
}
