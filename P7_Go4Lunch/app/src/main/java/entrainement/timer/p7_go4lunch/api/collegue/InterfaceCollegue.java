package entrainement.timer.p7_go4lunch.api.collegue;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import entrainement.timer.p7_go4lunch.model.Collegue;

public interface InterfaceCollegue {
     void getListCollegue();
     void newCollegue(Context context,String id, String collegue,String photo, String mail);
     void getme(String id);
     MutableLiveData<List<Collegue>> GetQuiVient();
    void updateNotify();
    void addMyChoice(String id, String resto, String adresse, String idRestaurant, String notechoix, String idAncienResto);

    void twentyFourHourLast(Context context, boolean b);
}
