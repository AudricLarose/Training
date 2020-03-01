package entrainement.timer.p7_go4lunch.Restaurant;

public class Place {
    private String name;
    private String adresse;
    private String horaire;
    private String distance;
    private String photo;
    private String perso;
    private String etoile;
    private String id;

    public Place(String name, String adresse, String horaire, String distance, String photo, String perso, String etoile) {
        this.name = name;
        this.adresse = adresse;
        this.horaire = horaire;
        this.distance = distance;
        this.photo = photo;
        this.perso = perso;
        this.etoile = etoile;
    }

    public Place(String name, String adresse, String horaire, String etoile, String id, String perso) {
        this.name = name;
        this.adresse = adresse;
        this.horaire = horaire;
        this.etoile = etoile;
        this.perso = perso;
        this.id=id;
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

    public String getEtoile() {
        return etoile;
    }

    public void setEtoile(String etoile) {
        this.etoile = etoile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
