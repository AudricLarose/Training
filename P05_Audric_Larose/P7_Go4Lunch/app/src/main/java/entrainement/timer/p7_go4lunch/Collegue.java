package entrainement.timer.p7_go4lunch;

public class Collegue {

    private String id;
    private String nom;
    private String choix;
    private String photo;

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
