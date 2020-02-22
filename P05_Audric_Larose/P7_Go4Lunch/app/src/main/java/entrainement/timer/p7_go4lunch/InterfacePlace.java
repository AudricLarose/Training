package entrainement.timer.p7_go4lunch;

import android.content.Context;

import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

public interface InterfacePlace {
    List<Place> getPlace(Context context, FindCurrentPlaceRequest request, PlacesClient placesClient);
    List<Place> getListOfPlace();
}
