package entrainement.timer.maps_test.model;

public class EarthQuake {
    private String place;
    private Double magnititude;
    private Long milliseconds;
    private String detailslink;
    private String type;
    private double lat;
    private double lon;

    public EarthQuake(String place, Double magnititude, Long milliseconds, String detailslink, String type, double lat, double lon) {
        this.place = place;
        this.magnititude = magnititude;
        this.milliseconds = milliseconds;
        this.detailslink = detailslink;
        this.type = type;
        this.lat = lat;
        this.lon = lon;
    }

    public EarthQuake() {
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Double getMagnititude() {
        return magnititude;
    }

    public void setMagnititude(Double magnititude) {
        this.magnititude = magnititude;
    }

    public Long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(Long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public String getDetailslink() {
        return detailslink;
    }

    public void setDetailslink(String detailslink) {
        this.detailslink = detailslink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
