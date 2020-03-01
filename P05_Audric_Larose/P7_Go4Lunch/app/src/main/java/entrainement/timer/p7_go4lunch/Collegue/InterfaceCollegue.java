package entrainement.timer.p7_go4lunch.Collegue;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public interface InterfaceCollegue {
     MutableLiveData<List<Collegue>> getListCollegue();
     void newCollegue(Context context,String id, String collegue,String photo);
     void addmychoice(String id, String resto,String adresse);
     void getme(String id);
}
