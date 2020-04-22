package entrainement.timer.p7_go4lunch.api.restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entrainement.timer.p7_go4lunch.model.ApiforOnePlace;
import entrainement.timer.p7_go4lunch.model.Place;
import entrainement.timer.p7_go4lunch.model.Results;

public  abstract class ListPlaceArray {

    public static List<Results> PLACE_GEN = Arrays.asList(
    );

    public static List<Results> generatePlaceArray() {
        return new ArrayList<>(PLACE_GEN);
    }
    public static List<ApiforOnePlace> PLACE_GEN1 = Arrays.asList(
    );
    public static List<ApiforOnePlace> generateAPIOnePlace() {
        return new ArrayList<>(PLACE_GEN1);
    }
}

