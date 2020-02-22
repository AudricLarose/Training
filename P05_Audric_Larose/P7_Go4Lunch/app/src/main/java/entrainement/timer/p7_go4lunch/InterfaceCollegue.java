package entrainement.timer.p7_go4lunch;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface InterfaceCollegue {
     List<Collegue>  getListCollegue();
     void addCollegue(Context context,String id, String collegue,String photo);

}
