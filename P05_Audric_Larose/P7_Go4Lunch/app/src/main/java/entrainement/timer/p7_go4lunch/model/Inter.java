package entrainement.timer.p7_go4lunch.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Inter {
//
//        String BASE_URL = "https://maps.googleapis.com/maps/api/place";

        @GET("json")

        Call<String> getplaces(@Query("place_id") String userId,
                                       @Query("key") String key, @Query("location") String location, @Query("type") String type, @Query("radius") String radius, @Query("keyword") String keyword);
    }


