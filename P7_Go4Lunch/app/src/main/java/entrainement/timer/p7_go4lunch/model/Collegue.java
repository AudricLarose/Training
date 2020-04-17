package entrainement.timer.p7_go4lunch.model;

public class Collegue {

    private String beNotified;
    private String choix;
    private String date;
    private String id;
    private String id_monchoix;
    private String Nom;
    private String note_choix;
    private String photo;


    public Collegue() {
    }

    public Collegue(String nom) {
        this.Nom = nom;
    }

    public Collegue(String nom, String choix, String photo, String id_monchoix) {

        this.Nom = nom;
        this.choix = choix;
        this.photo = photo;
        this.id_monchoix = id_monchoix;
    }

    public Collegue(String nom, String choix, String photo) {
        this.Nom = nom;
        this.choix = choix;
        this.photo = photo;
    }

    public Collegue(String nom, String choix) {
        this.Nom = nom;
        this.choix = choix;
    }

    public String getId_monchoix() {
        return id_monchoix;
    }

    public void setId_monchoix(String id_monchoix) {
        this.id_monchoix = id_monchoix;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        this.Nom = nom;
    }

    public String getChoix() {
        return choix;
    }



    public void setChoix(String choix) {
        this.choix = choix;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBeNotified() {
        return beNotified;
    }

    public void setBeNotified(String beNotified) {
        this.beNotified = beNotified;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote_choix() {
        return note_choix;
    }

    public void setNote_choix(String note_choix) {
        this.note_choix = note_choix;
    }
}

