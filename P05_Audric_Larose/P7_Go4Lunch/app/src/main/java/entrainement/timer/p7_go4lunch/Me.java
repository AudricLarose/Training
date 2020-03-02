package entrainement.timer.p7_go4lunch;

public class Me {
    private static String monId;
    private static String monNOm;
    private static String maPhoto;
    private static String monMail;
    private static String mon_choix;
    private static String mon_like;

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

