package entrainement.timer.p7_go4lunch.Restaurant;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

import entrainement.timer.p7_go4lunch.Collegue.Collegue;

public interface InterfacePlace {
    List<Place> getPlace(Context context, FindCurrentPlaceRequest request, PlacesClient placesClient, GoogleMap googleMap);
    MutableLiveData<List<Place>> getListOfPlace();
    void ilike(String resto, String id);
    void unlike(String resto, String id);
    void saveMyPlace(String nom_restaurant, String id);
    void unsaveMyPlace(String nomCollegue, String photoCollegue,String nom_restaurant);
    List<Collegue> getMyPlace(String nom_restaurant);
    String howManyLke(String nom_restaurant);
    String howManyCome(String nom_restaurant, String iDrestaurant);

    List<Collegue> compareCollegueNPlace (String nomduResto);
}
