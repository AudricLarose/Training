package entrainement.recycleview.un;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModel extends AndroidViewModel {
    private LiveData<List<ExempleItem>> liste;
    private Repository repository;


    public ViewModel(@NonNull Application application) {
        super(application);
        repository= new Repository(application);
        liste=repository.selectData();
    }

    public void insertData (ExempleItem exempleItem){
        repository.insertdate(exempleItem);
    }
    public void updateData (ExempleItem exempleItem) {
        repository.update(exempleItem);
    }
    public void deleteData (ExempleItem exempleItem) {
        repository.deletData(exempleItem);
    }
    public void deleteAllData () {
        repository.deletAllData();
    }
    public LiveData<List<ExempleItem>> SelectAllThoseDatas (){
        return liste;
    }
}
