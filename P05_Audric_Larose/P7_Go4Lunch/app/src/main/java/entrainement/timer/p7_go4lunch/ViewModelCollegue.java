package entrainement.timer.p7_go4lunch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ViewModelCollegue extends ViewModel {
    private final MutableLiveData<List<Collegue>> userLiveData = new MutableLiveData<List<Collegue>>();
    ExtendedServiceCollegue service;

    public LiveData<List<Collegue>> getUser() {
        service=DI.getService();
        userLiveData.setValue(service.getListCollegue());
        return userLiveData;
    }
}