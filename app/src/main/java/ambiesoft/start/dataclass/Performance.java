package ambiesoft.start.dataclass;

/**
 * Created by Bryanyhy on 15/8/2016.
 */
public class Performance {

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
}
