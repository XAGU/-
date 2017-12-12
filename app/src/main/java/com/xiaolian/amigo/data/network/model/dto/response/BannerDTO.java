package com.xiaolian.amigo.data.network.model.dto.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import lombok.Data;

/**
 * banner
 * <p>
 * Created by zcd on 17/11/6.
 */
@Data
public class BannerDTO implements Parcelable {

    private String image;
    private String link;
    private Integer type;

    public BannerDTO(Integer type, String image, String link) {
        this.image = image;
        this.link = link;
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.image);
        dest.writeString(this.link);
        dest.writeValue(this.type);
    }

    protected BannerDTO(Parcel in) {
        this.image = in.readString();
        this.link = in.readString();
        this.type = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<BannerDTO> CREATOR = new Parcelable.Creator<BannerDTO>() {
        @Override
        public BannerDTO createFromParcel(Parcel source) {
            return new BannerDTO(source);
        }

        @Override
        public BannerDTO[] newArray(int size) {
            return new BannerDTO[size];
        }
    };
}
