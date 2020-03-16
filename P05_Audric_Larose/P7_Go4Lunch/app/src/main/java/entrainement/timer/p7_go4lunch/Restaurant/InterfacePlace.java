package entrainement.timer.p7_go4lunch.Restaurant;

import android.content.Context;
import android.widget.ProgressBar;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

import entrainement.timer.p7_go4lunch.Collegue.Collegue;

public interface InterfacePlace {
//    List<Place> getPlace(Context context, FindCurrentPlaceRequest request, PlacesClient placesClient, GoogleMap googleMap);

    List<Place> getPlace(Context context, FindCurrentPlaceRequest request, PlacesClient placesClient, GoogleMap mMap, ProgressBar progressBar);

    MutableLiveData<List<Place>> getListOfPlace();

    List<Collegue> compareCollegueNPlace(String nomduResto, String idData);

    void ilike(String resto);
    void unlike(String resto, String id);
    void saveMyPlace(String nom_restaurant, String id);
    void unsaveMyPlace(String nomCollegue, String photoCollegue,String nom_restaurant);
    List<String> getMyPlace(String nom_restaurant);

    String howManyLke(String nom_restaurant, String iDrestaurant);

    String howManyCome(String nom_restaurant, String iDrestaurant);
}
