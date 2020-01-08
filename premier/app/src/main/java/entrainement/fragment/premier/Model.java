package entrainement.fragment.premier;

public class Model {
    private String nom;
    private String Prenom;
    private String appreciation;

    public Model(String nom, String prenom, String appreciation) {
        this.nom = nom;
        Prenom = prenom;
        this.appreciation = appreciation;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public String getAppreciation() {
        return appreciation;
    }
}
