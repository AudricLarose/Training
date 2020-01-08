package entrainement.recycleview.un;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface InterfaceDao {
    @Insert
    void insert(ExempleItem item);

    @Update
    void update(ExempleItem item);

    @Delete
    void delete(ExempleItem item);

    @Query("SELECT * FROM training5 ORDER BY id DESC")
    LiveData<List<ExempleItem>> select();

    @Query("DELETE FROM training5")
    void deleteAll();
}
