package entrainement.timer.p7_go4lunch;

public class Place {
    private String name;
    private String adresse;
    private String horaire;
    private String distance;
    private String photo;
    private String perso;
    private double etoile;
    private int id;

    public Place(String name, String adresse, String horaire, String distance, String photo, String perso, double etoile) {
        this.name = name;
        this.adresse = adresse;
        this.horaire = horaire;
        this.distance = distance;
        this.photo = photo;
        this.perso = perso;
        this.etoile = etoile;
    }

    public Place(String name, String adresse, String horaire, double etoile) {
        this.name = name;
        this.adresse = adresse;
        this.horaire = horaire;
        this.etoile = etoile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getHoraire() {
        return horaire;
    }

    public void setHoraire(String horaire) {
        this.horaire = horaire;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPerso() {
        return perso;
    }

    public void setPerso(String perso) {
        this.perso = perso;
    }

    public double getEtoile() {
        return etoile;
    }

    public void setEtoile(double etoile) {
        this.etoile = etoile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
