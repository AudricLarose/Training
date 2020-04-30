package entrainement.timer.p7_go4lunch.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public abstract class Me {
    private static String myId;
    private static String myName;
    private static String myPhoto;
    private static String myMail;
    private static String my_choice;
    private static String adresschoice;
    private static String id_mychoice;
    private static String my_like;
    private static Double my_longitude;
    private static Double my_latitude;
    private static LatLng latlng_me;
    private static Boolean beNotified;
    private static String NoteChoice;
    private static List<String> myLikes;
    private static List<String> getCoworker;
    private static String choicePhoto;

    public Me(String myId) {
        this.myId = myId;
    }

    public Me() {
    }

    public static String getChoicePhoto() {
        return choicePhoto;
    }

    public static void setChoicePhoto(String choicePhoto) {
        Me.choicePhoto = choicePhoto;
    }

    public static String getNoteChoice() {
        return NoteChoice;
    }

    public static void setNoteChoice(String noteChoice) {
        NoteChoice = noteChoice;
    }

    public static String getAdresschoice() {
        return adresschoice;
    }

    public static void setAdresschoice(String adresschoice) {
        Me.adresschoice = adresschoice;
    }

    public static Boolean getBeNotified() {
        return beNotified;
    }

    public static void setBeNotified(Boolean beNotified) {
        Me.beNotified = beNotified;
    }

    public static String getId_mychoice() {
        return id_mychoice;
    }

    public static void setId_mychoice(String id_mychoice) {
        Me.id_mychoice = id_mychoice;
    }

    public static String getMy_choice() {
        return my_choice;
    }

    public static void setMy_choice(String my_choice) {
        Me.my_choice = my_choice;
    }

    public static String getMyId() {
        return myId;
    }

    public static void setMyId(String myId) {
        Me.myId = myId;
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

    public static String getMyMail() {
        return myMail;
    }

    public static void setMyMail(String myMail) {
        Me.myMail = myMail;
    }

    public static String getMyName() {
        return myName;
    }

    public static void setMyName(String myName) {
        Me.myName = myName;
    }

    public static String getMyPhoto() {
        return myPhoto;
    }

    public static void setMyPhoto(String myPhoto) {
        Me.myPhoto = myPhoto;
    }

    public LatLng getLatlng_me() {
        return latlng_me;
    }

    public static void setLatlng_me(LatLng latlng_me) {
        Me.latlng_me = latlng_me;
    }

    public String getMy_like() {
        return my_like;
    }

    public void setmy_like(String my_like) {
        Me.my_like = my_like;
    }

}

