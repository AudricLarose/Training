package entrainement.timer.p7_go4lunch;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Me {
    private static String monId;
    private static String monNOm;
    private static String maPhoto;
    private static String monMail;
    private static String mon_choix;
    private static String adressechoix;
    private static String id_monchoix;
    private static String mon_like;
    private static Double my_longitude;
    private static Double my_latitude;
    private static LatLng latlng_me;
    private static Boolean beNotified;
    private static String NoteChoix;
    private static List<String> myLikes;

    public List<String> getMyLikes() {
        return myLikes;
    }

    public void setMyLikes(List<String> myLikes) {
        Me.myLikes = myLikes;
    }

    public  String getNoteChoix() {
        return NoteChoix;
    }

    public  void setNoteChoix(String noteChoix) {
        NoteChoix = noteChoix;
    }

    public String getAdressechoix() {
        return adressechoix;
    }

    public void setAdressechoix(String adressechoix) {
        Me.adressechoix = adressechoix;
    }

    public Boolean getBeNotified() {
        return beNotified;
    }

    public void setBeNotified(Boolean beNotified) {
        Me.beNotified = beNotified;
    }

    public LatLng getLatlng_me() {
        return latlng_me;
    }

    public void setLatlng_me(LatLng latlng_me) {
        Me.latlng_me = latlng_me;
    }

    public Double getMy_longitude() {
        return my_longitude;
    }

    public String getId_monchoix() {
        return id_monchoix;
    }

    public void setId_monchoix(String id_monchoix) {
        Me.id_monchoix = id_monchoix;
    }

    public void setMy_longitude(Double my_longitude) {
        Me.my_longitude = my_longitude;
    }

    public Double getMy_latitude() {
        return my_latitude;
    }

    public void setMy_latitude(Double my_latitude) {
        Me.my_latitude = my_latitude;
    }

    public String getMonMail() {
        return monMail;
    }

    public void setMonMail(String monMail) {
        this.monMail = monMail;
    }

    public  String getMon_choix() {
        return mon_choix;
    }

    public  void setMon_choix(String mon_choix) {
        Me.mon_choix = mon_choix;
    }

    public  String getMon_like() {
        return mon_like;
    }

    public  void setMon_like(String mon_like) {
        Me.mon_like = mon_like;
    }

    public Me(String monId) {
        this.monId = monId;
    }

    public  String getMonNOm() {
        return monNOm;
    }

    public  void setMonNOm(String monNOm) {
        Me.monNOm = monNOm;
    }

    public  String getMaPhoto() {
        return maPhoto;
    }
    public   void setMaPhoto(String maPhoto) {
        Me.maPhoto = maPhoto;
    }

    public Me() {
    }

    public String getMonId() {
        return monId;
    }

    public void setMonId(String monId) {
        this.monId = monId;
    }

}

