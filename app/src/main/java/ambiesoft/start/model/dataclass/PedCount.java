package ambiesoft.start.model.dataclass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Bryanyhy on 29/8/2016.
 */
public class PedCount implements Parcelable {

    private String name;
    private ArrayList<Double> count;

    public PedCount(String name, ArrayList<Double> count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Double> getCount() {
        return count;
    }

    public void setCount(ArrayList<Double> count) {
        this.count = count;
    }

    protected PedCount(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0x01) {
            count = new ArrayList<Double>();
            in.readList(count, Integer.class.getClassLoader());
        } else {
            count = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if (count == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(count);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PedCount> CREATOR = new Parcelable.Creator<PedCount>() {
        @Override
        public PedCount createFromParcel(Parcel in) {
            return new PedCount(in);
        }

        @Override
        public PedCount[] newArray(int size) {
            return new PedCount[size];
        }
    };
}
