package ambiesoft.start.model.dataclass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bryanyhy on 15/8/2016.
 */
// Storing all attributes for a performance
public class Performance implements Parcelable {

    private String name;
    private String category;
    private String desc;
    private String date;
    private String sTime;
    private String eTime;
    private Double lat;
    private Double lng;

    public Performance() {}

    public Performance(String name, String category, String desc, String date, String sTime, String eTime, Double lat, Double lng) {
        this.name = name;
        this.category = category;
        this.desc = desc;
        this.date = date;
        this.sTime = sTime;
        this.eTime = eTime;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String geteTime() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    protected Performance(Parcel in) {
        name = in.readString();
        category = in.readString();
        desc = in.readString();
        date = in.readString();
        sTime = in.readString();
        eTime = in.readString();
        lat = in.readByte() == 0x00 ? null : in.readDouble();
        lng = in.readByte() == 0x00 ? null : in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(desc);
        dest.writeString(date);
        dest.writeString(sTime);
        dest.writeString(eTime);
        if (lat == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(lat);
        }
        if (lng == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(lng);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Performance> CREATOR = new Parcelable.Creator<Performance>() {
        @Override
        public Performance createFromParcel(Parcel in) {
            return new Performance(in);
        }

        @Override
        public Performance[] newArray(int size) {
            return new Performance[size];
        }
    };
}
