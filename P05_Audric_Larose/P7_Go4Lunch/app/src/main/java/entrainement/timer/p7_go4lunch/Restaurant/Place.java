package entrainement.timer.p7_go4lunch.Restaurant;

public class Place {
    private String nomPlace;
    private String Adresse;
    private String horaire;
    private String distance;
    private String photo;
    private String quivient;
    private String note;
    private String id;

    public Place(String nomPlace, String Adresse, String horaire, String distance, String quivient,String note, String idPlace) {
        this.nomPlace = nomPlace;
        this.Adresse = Adresse;
        this.horaire = horaire;
        this.distance = distance;
        this.quivient = quivient;
        this.note = note;
        this.id=idPlace;
    }

    public Place(String name, String Adresse, String horaire, String note, String id, String quivient) {
        this.nomPlace = nomPlace;
        this.Adresse = Adresse;
        this.horaire = horaire;
        this.note = note;
        this.quivient = quivient;
        this.id=id;
    }

    public Place() {
    }

    public String getnomPlace() {
        return nomPlace;
    }

    public void setnomPlace(String nomPlace) {
        this.nomPlace = nomPlace;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String Adresse) {
        this.Adresse = Adresse;
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

    public String getquivient() {
        return quivient;
    }

    public void setquivient(String quivient) {
        this.quivient = quivient;
    }

    public String getnote() {
        return note;
    }

    public void setnote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
