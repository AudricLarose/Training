package entrainement.recycleview.un;

import android.widget.TextView;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "training5")
public class ExempleItem {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private String nom;


    public ExempleItem(String nom) {
        this.nom = nom;

    }

    public String getNom() {
        return nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

}
