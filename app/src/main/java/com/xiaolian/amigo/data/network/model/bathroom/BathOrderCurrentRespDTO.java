package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

@Data
public class BathOrderCurrentRespDTO {

                /**
                 * id : 0
                 * location : string
                 * prepayAmount : 0
                 * status : 0
                 * tradeOrderId : 0
                 */

                private long id;
                private String location;
                private float prepayAmount;
                private int status;
                private long tradeOrderId;

                public long getId() {
                        return id;
                }

                public void setId(long id) {
                        this.id = id;
                }

                public String getLocation() {
                        return location;
                }

                public void setLocation(String location) {
                        this.location = location;
                }

                public float getPrepayAmount() {
                        return prepayAmount;
                }

                public void setPrepayAmount(float prepayAmount) {
                        this.prepayAmount = prepayAmount;
                }

                public int getStatus() {
                        return status;
                }

                public void setStatus(int status) {
                        this.status = status;
                }

                public long getTradeOrderId() {
                        return tradeOrderId;
                }

                public void setTradeOrderId(long tradeOrderId) {
                        this.tradeOrderId = tradeOrderId;
                }
}
