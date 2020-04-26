package entrainement.timer.p7_go4lunch.api.restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entrainement.timer.p7_go4lunch.model.Place;

public  abstract class ListPlaceGenerator {

    public static List<Place> PLACE_GEN = Arrays.asList(
    );

    public static List<entrainement.timer.p7_go4lunch.model.Place> generatePlace() {
        return new ArrayList<>(PLACE_GEN);
    }
}

