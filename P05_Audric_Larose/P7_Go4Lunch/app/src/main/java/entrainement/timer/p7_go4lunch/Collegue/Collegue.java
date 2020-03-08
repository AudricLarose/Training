package entrainement.timer.p7_go4lunch.Collegue;

public class Collegue {

    private String id;
    private String nom;
    private String choix;
    private String photo;
    private String longitude;
    private String latitude;

    public Collegue(String id, String nom, String choix, String photo) {
        this.id = id;
        this.nom = nom;
        this.choix = choix;
        this.photo = photo;
    }

    public Collegue(String nom, String choix, String photo) {
        this.nom = nom;
        this.choix = choix;
        this.photo = photo;
    }

    public Collegue(String nom, String choix) {
        this.nom = nom;
        this.choix = choix;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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
}
