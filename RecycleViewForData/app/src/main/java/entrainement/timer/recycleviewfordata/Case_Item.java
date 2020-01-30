package entrainement.timer.recycleviewfordata;

public class Case_Item {
    private String titre;
    private String url;
    private String textlong;

    public String getTextlong() {
        return textlong;
    }

    public void setTextlong(String textlong) {
        this.textlong = textlong;
    }

    public Case_Item(String url, String textlong) {
        this.titre = titre;
        this.url = url;
        this.textlong=textlong;
    }

    public Case_Item(String titre) {
        this.titre = titre;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
