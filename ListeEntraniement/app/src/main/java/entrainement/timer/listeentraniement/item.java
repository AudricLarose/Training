package entrainement.timer.listeentraniement;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="tab1")
public class item {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nom;

    public item(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
