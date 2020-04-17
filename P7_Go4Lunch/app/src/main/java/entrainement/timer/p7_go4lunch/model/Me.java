package entrainement.timer.p7_go4lunch.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public abstract class Me {
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
    private static List<String> getCoworker;

    public Me(String monId) {
        this.monId = monId;
    }

    public Me() {
    }

    public static String getNoteChoix() {
        return NoteChoix;
    }

    public static void setNoteChoix(String noteChoix) {
        NoteChoix = noteChoix;
    }

    public static String getAdressechoix() {
        return adressechoix;
    }

    public static void setAdressechoix(String adressechoix) {
        Me.adressechoix = adressechoix;
    }

    public static Boolean getBeNotified() {
        return beNotified;
    }

    public static void setBeNotified(Boolean beNotified) {
        Me.beNotified = beNotified;
    }

    public static String getId_monchoix() {
        return id_monchoix;
    }

    public static void setId_monchoix(String id_monchoix) {
        Me.id_monchoix = id_monchoix;
    }

    public static String getMon_choix() {
        return mon_choix;
    }

    public static void setMon_choix(String mon_choix) {
        Me.mon_choix = mon_choix;
    }

    public static String getMonId() {
        return monId;
    }

    public static void setMonId(String monId) {
        Me.monId = monId;
    }

    public static List<String> getGetCoworker() {
        return getCoworker;
    }

    public static void setGetCoworker(List<String> getCoworker) {
        Me.getCoworker = getCoworker;
    }

    public static List<String> getMyLikes() {
        return myLikes;
    }

    public static void setMyLikes(List<String> myLikes) {
        Me.myLikes = myLikes;
    }

    public LatLng getLatlng_me() {
        return latlng_me;
    }

    public static void setLatlng_me(LatLng latlng_me) {
        Me.latlng_me = latlng_me;
    }

    public static Double getMy_longitude() {
        return my_longitude;
    }

    public static void setMy_longitude(Double my_longitude) {
        Me.my_longitude = my_longitude;
    }

    public static Double getMy_latitude() {
        return my_latitude;
    }

    public static void setMy_latitude(Double my_latitude) {
        Me.my_latitude = my_latitude;
    }

    public static String getMonMail() {
        return monMail;
    }

    public static void setMonMail(String monMail) {
        Me.monMail = monMail;
    }

    public String getMon_like() {
        return mon_like;
    }

    public void setMon_like(String mon_like) {
        Me.mon_like = mon_like;
    }

    public static String getMonNOm() {
        return monNOm;
    }

    public static void setMonNOm(String monNOm) {
        Me.monNOm = monNOm;
    }

    public static String getMaPhoto() {
        return maPhoto;
    }

    public static void setMaPhoto(String maPhoto) {
        Me.maPhoto = maPhoto;
    }

}

