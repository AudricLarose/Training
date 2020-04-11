package entrainement.timer.p7_go4lunch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Location implements Serializable {
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public int getDistance() {
        float[] result = new float[1];
        Double latitude = Double.parseDouble(String.valueOf(getLat()));
        Double longitude = Double.parseDouble(String.valueOf(getLng()));
        android.location.Location.distanceBetween(Me.getMy_latitude(), Me.getMy_longitude(), latitude, longitude, result);
        int round = Math.round(result[0]);
        return round;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
