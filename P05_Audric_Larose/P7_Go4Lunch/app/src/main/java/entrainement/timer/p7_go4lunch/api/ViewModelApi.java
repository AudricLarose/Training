package entrainement.timer.p7_go4lunch.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.model.Collegue;

public class ViewModelApi extends androidx.lifecycle.ViewModel {
//    private final MutableLiveData<List<Collegue>> userLiveData = DI.getService().getListCollegue();
//    private final MutableLiveData<List<Place>> placeLiveData = DI.getServicePlace().getListOfPlace();
    private final MutableLiveData<List<Collegue>> whocomeLiveData = DI.getService().GetQuiVient();

//    public LiveData<List<Collegue>> getUser() {
//        return userLiveData;
//    }
//    public LiveData<List<Place>> getPlace() {
//        return placeLiveData;
//    }
    public LiveData<List<Collegue>> getwhocome() {
        return whocomeLiveData;
    }

}