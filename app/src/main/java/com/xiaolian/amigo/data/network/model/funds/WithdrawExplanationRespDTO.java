package com.xiaolian.amigo.data.network.model.funds;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

@Data
public class WithdrawExplanationRespDTO  implements Parcelable{

        /**
         * explanation : string
         * match : false
         * refundUser : string
         * timeRange : string
         */

        private String explanation;
        private boolean match;
        private String refundUser;
        private String timeRange;
        private boolean isSetExplanation ;
        private boolean isAll;

    protected WithdrawExplanationRespDTO(Parcel in) {
        explanation = in.readString();
        match = in.readByte() != 0;
        refundUser = in.readString();
        timeRange = in.readString();
        isSetExplanation = in.readByte() != 0 ;
    }

    public static final Creator<WithdrawExplanationRespDTO> CREATOR = new Creator<WithdrawExplanationRespDTO>() {
        @Override
        public WithdrawExplanationRespDTO createFromParcel(Parcel in) {
            return new WithdrawExplanationRespDTO(in);
        }

        @Override
        public WithdrawExplanationRespDTO[] newArray(int size) {
            return new WithdrawExplanationRespDTO[size];
        }
    };

    public String getExplanation() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation = explanation;
        }

        public boolean isMatch() {
            return match;
        }

        public void setMatch(boolean match) {
            this.match = match;
        }

        public String getRefundUser() {
            return refundUser;
        }

        public void setRefundUser(String refundUser) {
            this.refundUser = refundUser;
        }

        public String getTimeRange() {
            return timeRange;
        }

        public void setTimeRange(String timeRange) {
            this.timeRange = timeRange;
        }

    public boolean isSetExplanation() {
        return isSetExplanation;
    }

    public void setSetExplanation(boolean setExplanation) {
        isSetExplanation = setExplanation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(explanation);
        dest.writeByte((byte) (match ? 1 : 0));
        dest.writeString(refundUser);
        dest.writeString(timeRange);
        dest.writeByte((byte) (isSetExplanation ? 1: 0));
    }
}
