package kg.geektech.taskapp35.room;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import kg.geektech.taskapp35.models.NewsModel;

@Database(entities = {NewsModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract NewsDao newsDao();
}
