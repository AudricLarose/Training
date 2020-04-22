package entrainement.recycleview.un;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Repository {
    private InterfaceDao interfaceDao;
    private LiveData<List<ExempleItem>> allnotes;

    public Repository (Application application){
        BaseDeDonne baseDeDonne= BaseDeDonne.getInstance(application);
        interfaceDao = baseDeDonne.interfaceDao();
        allnotes = interfaceDao.select();
    }
 public void insertdate (ExempleItem exempleItem){
        new insertAsynchTask(interfaceDao).execute(exempleItem);
 }
 public LiveData<List<ExempleItem>> selectData (){return allnotes;}

 public void deletData (ExempleItem exempleItem){
    new deleteAsynchTask(interfaceDao).execute(exempleItem);
 }
 public void deletAllData (){
        new dalateall(interfaceDao).execute();
 }

 public void update (ExempleItem exempleItem){
        new update(interfaceDao).execute(exempleItem);
 }

    public static class insertAsynchTask extends AsyncTask<ExempleItem,Void, Void>{
        private InterfaceDao interfaceDao;

        public insertAsynchTask(InterfaceDao interfaceDao) {
            this.interfaceDao = interfaceDao;
        }
        @Override
        protected Void doInBackground(ExempleItem... exempleItems) {
            interfaceDao.insert(exempleItems[0]);
            return null;
        }
    }
    public static class update extends AsyncTask<ExempleItem,Void,Void>{
        private InterfaceDao interfaceDao;

        public update(InterfaceDao interfaceDao) {
            this.interfaceDao = interfaceDao;
        }

        @Override
        protected Void doInBackground(ExempleItem... exempleItems) {
            interfaceDao.update(exempleItems[0]);
            return null;
        }
    }
    public static class deleteAsynchTask extends AsyncTask<ExempleItem, Void, Void>{
        private InterfaceDao interfaceDao;
        public deleteAsynchTask(InterfaceDao interfaceDao) {
            this.interfaceDao = interfaceDao;
        }
        @Override
        protected Void doInBackground(ExempleItem... exempleItems) {
            interfaceDao.delete(exempleItems[0]);
            return null;
        }
    }
    public static class dalateall extends  AsyncTask<Void,Void,Void>{
        private InterfaceDao interfaceDao;
        public dalateall(InterfaceDao interfaceDao) {
            this.interfaceDao = interfaceDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            interfaceDao.deleteAll();
            return null;
        }
    }

}
