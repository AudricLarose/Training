package entrainement.timer.p7_go4lunch.model;

public class Collegue {

    private String beNotified;
    private String choice;
    private String date;
    private String id;
    private String Id_mychoice;
    private String name;
    private String note_choice;
    private String photo;
    private String adresse_Choice;


    public Collegue() {
    }

    public Collegue(String nom) {
        this.name = nom;
    }

    public Collegue(String name, String choix, String photo, String Id_mychoice) {

        this.name = name;
        this.choice = choix;
        this.photo = photo;
        this.Id_mychoice = Id_mychoice;
    }

    public Collegue(String name, String choix, String photo) {
        this.name = name;
        this.choice = choix;
        this.photo = photo;
    }

    public Collegue(String name, String choix) {
        this.name = name;
        this.choice = choix;
    }

    public String getId_mychoice() {
        return Id_mychoice;
    }

    public void setId_mychoice(String id_mychoice) {
        this.Id_mychoice = id_mychoice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChoice() {
        return choice;
    }



    public void setChoice(String choice) {
        this.choice = choice;
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

    public String getNote_choice() {
        return note_choice;
    }

    public void setNote_choice(String note_choice) {
        this.note_choice = note_choice;
    }

    public String getAdresse_Choice() {
        return adresse_Choice;
    }

    public void setAdresse_Choice(String adresse_Choice) {
        this.adresse_Choice = adresse_Choice;
    }
}

