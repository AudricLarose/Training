package entrainement.timer.listeentraniement;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface interfaceDao {
    @Insert
    void insertData (item item);

    @Update
    void updateData(item item);

    @Delete
    void deleteData (item item);

    @Query("DELETE FROM tab1")
    void deleteAllData();

    @Query("SELECT * FROM tab1 BY ORDER id ASC")
    LiveData<List<item>> selectData();

}
