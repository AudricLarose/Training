package entrainement.recycleview.un;

import android.widget.TextView;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "training8")
public class ExempleItem {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private String nom;
    private int color;


    public ExempleItem(String nom, int color) {
        this.nom = nom;
        this.color= color;

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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
