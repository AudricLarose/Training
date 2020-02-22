package entrainement.timer.p7_go4lunch;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExtendedServicePlace implements InterfacePlace {
    private static final String TAG = "ExtendedServicePlace";
    private ExtendedServicePlace servicePlace;
    private List<Place> listePlace= new ArrayList<>();
    private GoogleMap mMap;

    @Override
    public List<Place> getPlace(Context context,FindCurrentPlaceRequest request, PlacesClient placesClient) {
        Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
        placeResponse.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FindCurrentPlaceResponse response = task.getResult();
                for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                    Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                   String nomPlace= placeLikelihood.getPlace().getName();
                   String idPlace= placeLikelihood.getPlace().getId();
                   String adressePlace= placeLikelihood.getPlace().getAddress();
                   String phonePlace= "15.00";

                   String horairePlace= "15.00";
                   double notePlace=15.00;
                   List photoPlace= placeLikelihood.getPlace().getPhotoMetadatas();
                   LatLng latlongPlace= placeLikelihood.getPlace().getLatLng();
                   List typePlace= placeLikelihood.getPlace().getTypes();
                   listePlace.add(new Place(nomPlace,adressePlace,horairePlace,notePlace));
                     if (latlongPlace!=null) {
                       mMap.addMarker(new MarkerOptions().position(latlongPlace).title(nomPlace).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                   }


//                                Location.distanceBetween();
                }
                Exception exception = task.getException();
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + apiException.getStatusCode());

                }
            }
        });
        return listePlace;
    }

    @Override
    public List<Place> getListOfPlace() {
        listePlace.add(new Place("eee","eeee","ee",0));

        return listePlace;
    }

}
