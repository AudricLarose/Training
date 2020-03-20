package entrainement.timer.p7_go4lunch.Collegue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public  abstract class ListCollegueGenerator {

    public static List<Collegue> COLLEGUE_GEN = Arrays.asList(
    );
    static List<Collegue> generateNeighbours() {
        return new ArrayList<>(COLLEGUE_GEN);
    }
}

