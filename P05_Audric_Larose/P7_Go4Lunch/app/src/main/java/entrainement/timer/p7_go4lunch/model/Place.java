package entrainement.timer.p7_go4lunch.model;

public class Place {
    private String id;
    private String nomPlace;
    private String Adresse;
    private String photo;
    private String note;
    private String quivient;
    private String distance;
    private String latitude;
    private String longitude;
    private String phone;
    private String horaire;
    private String site;

    public Place(String id, String nomPlace, String adresse, String photo, String note, String quivient, String distance, String latitude, String longitude, String phone, String horaire, String site) {
        this.id = id;
        this.nomPlace = nomPlace;
        Adresse = adresse;
        this.photo = photo;
        this.note = note;
        this.quivient = quivient;
        this.distance = distance;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.horaire = horaire;
        this.site = site;
    }

    public Place(String nomPlace, String Adresse, String horaire, String distance, String quivient, String note, String idPlace, String phone, String site) {
        this.nomPlace = nomPlace;
        this.Adresse = Adresse;
        this.horaire = horaire;
        this.distance = distance;
        this.quivient = quivient;
        this.note = note;
        this.id = idPlace;
    }

    public Place(String name, String Adresse, String horaire, String note, String id, String quivient) {
        this.nomPlace = nomPlace;
        this.Adresse = Adresse;
        this.horaire = horaire;
        this.note = note;
        this.quivient = quivient;
        this.id = id;
    }

    public Place() {
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
