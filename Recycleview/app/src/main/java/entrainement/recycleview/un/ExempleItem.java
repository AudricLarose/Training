package entrainement.recycleview.un;

import android.widget.TextView;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "traingin10")
public class ExempleItem {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private String nom;
    private String etape;

    public ExempleItem(String nom, String etape) {
        this.nom = nom;
        this.etape= etape;

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

    public String getEtape() {
        return etape;
    }

    public void setEtape(String etape) {
        this.etape = etape;
    }

}
