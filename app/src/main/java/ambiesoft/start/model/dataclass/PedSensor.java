package ambiesoft.start.model.dataclass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bryanyhy on 29/8/2016.
 */
public class PedSensor implements Parcelable {

    private String name;
    private Double lat;
    private Double lng;

    public PedSensor(String name, Double lat, Double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    protected PedSensor(Parcel in) {
        name = in.readString();
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
    public static final Parcelable.Creator<PedSensor> CREATOR = new Parcelable.Creator<PedSensor>() {
        @Override
        public PedSensor createFromParcel(Parcel in) {
            return new PedSensor(in);
        }

        @Override
        public PedSensor[] newArray(int size) {
            return new PedSensor[size];
        }
    };
}
