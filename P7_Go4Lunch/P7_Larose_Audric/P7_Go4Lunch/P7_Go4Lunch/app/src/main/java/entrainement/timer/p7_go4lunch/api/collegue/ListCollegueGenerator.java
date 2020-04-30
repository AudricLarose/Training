package entrainement.timer.p7_go4lunch.api.collegue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entrainement.timer.p7_go4lunch.model.Collegue;

public  abstract class ListCollegueGenerator {

    public static List<Collegue> COLLEGUE_GEN = Arrays.asList(
    );
    public static List<Collegue> generateNeighbours() {
        return new ArrayList<>(COLLEGUE_GEN);
    }
}

