package entrainement.timer.p7_go4lunch.api.restaurant;

import android.content.Context;
import com.google.android.gms.maps.GoogleMap;
import java.util.List;
import entrainement.timer.p7_go4lunch.model.Collegue;
import entrainement.timer.p7_go4lunch.model.Results;

public interface InterfacePlace {
    void unsaveMyPlace(Results results);
    void GetApiPlace(Context context, GoogleMap mMap);

    List<Collegue> CompareCollegueNPlace(Results results, Context context, ExtendedServicePlace.Increment... increments);

    void iLike(Results results);
    void unLike(Results results);
    void saveMyPlace(Results results);

}
