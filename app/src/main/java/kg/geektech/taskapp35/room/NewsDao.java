package kg.geektech.taskapp35.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import kg.geektech.taskapp35.models.NewsModel;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM NewsModel ORDER BY createdAt DESC")
    List<NewsModel> getAll();

    @Insert
    void insert(NewsModel news);

}
