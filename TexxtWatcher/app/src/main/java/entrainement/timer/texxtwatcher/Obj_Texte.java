package entrainement.timer.texxtwatcher;

public class Obj_Texte {
    private String titre;
    private String texte;

    public Obj_Texte(String titre, String texte) {
        this.titre = titre;
        this.texte = texte;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }
}
