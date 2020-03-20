package entrainement.timer.p7_go4lunch.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entrainement.timer.p7_go4lunch.Collegue.Collegue;

public  abstract class ListPlaceGenerator {

    public static List<Place> PLACE_GEN = Arrays.asList(
    );

    public static List<Place> generatePlace() {
        return new ArrayList<>(PLACE_GEN);
    }
}

