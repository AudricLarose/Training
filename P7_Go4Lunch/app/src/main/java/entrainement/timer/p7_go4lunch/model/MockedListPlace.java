package entrainement.timer.p7_go4lunch.model;

public class MockedListPlace {
    private String id;
    private String whocome;
    private String like;

    public MockedListPlace(String id, String whocome, String like) {
        this.id = id;
        this.whocome = whocome;
        this.like = like;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWhocome() {
        return whocome;
    }

    public void setWhocome(String whocome) {
        this.whocome = whocome;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }
}
