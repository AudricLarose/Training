package entrainement.timer.p7_go4lunch.api.restaurant;

import android.content.Context;
import android.widget.ProgressBar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

import entrainement.timer.p7_go4lunch.model.Collegue;
import entrainement.timer.p7_go4lunch.model.Place;

public interface InterfacePlace {
    List<Place> getPlace(Context context, FindCurrentPlaceRequest request, PlacesClient placesClient, GoogleMap mMap, ProgressBar progressBar);
//    void getListOfPlace();
    List<Collegue> compareCollegueNPlace(String nomduResto, String idData, Context context);
    void ilike(String resto);
    void unlike(String resto, String id);

    void getListOfPlace(Context context, GoogleMap map);

    void saveMyPlace(String nom_restaurant, String id);
    void unsaveMyPlace(String id);
}
