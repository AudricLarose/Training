package entrainement.timer.p7_go4lunch.Collegue;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import entrainement.timer.p7_go4lunch.Activities.ActivityDetails;

public interface InterfaceCollegue {
     MutableLiveData<List<Collegue>> getListCollegue();
     void newCollegue(Context context,String id, String collegue,String photo, String mail);
     void getme(String id);
     MutableLiveData<List<Collegue>> GetQuiVient();
    void updateNotify();
    void addmychoice(String id, String resto, String adresse, String idRestaurant, String notechoix, String idAncienResto);

    void twentyFourHourLast(Context context, boolean b);
}
