package ambiesoft.start.model.dataclass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bryanyhy on 15/8/2016.
 */
// Storing the information of the artworks retrieved from MelbournePublicArtwork JSON
public class Artwork implements Parcelable {

    private String assetType;
    private String name;
    private String address;
    private String artist;
    private String artDate;
    private Double lat;
    private Double lng;

    public Artwork() { }

    public Artwork(String assetType, String name, String address, String artist, String artDate, Double lat, Double lng) {
        this.assetType = assetType;
        this.name = name;
        this.address = address;
        this.artist = artist;
        this.artDate = artDate;
        this.lat = lat;
        this.lng = lng;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtDate() {
        return artDate;
    }

    public void setArtDate(String artDate) {
        this.artDate = artDate;
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

    protected Artwork(Parcel in) {
        assetType = in.readString();
        name = in.readString();
        address = in.readString();
        artist = in.readString();
        artDate = in.readString();
        lat = in.readByte() == 0x00 ? null : in.readDouble();
        lng = in.readByte() == 0x00 ? null : in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(assetType);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(artist);
        dest.writeString(artDate);
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
    public static final Parcelable.Creator<Artwork> CREATOR = new Parcelable.Creator<Artwork>() {
        @Override
        public Artwork createFromParcel(Parcel in) {
            return new Artwork(in);
        }

        @Override
        public Artwork[] newArray(int size) {
            return new Artwork[size];
        }
    };
}
