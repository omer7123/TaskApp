package kg.geektech.taskapp35;

import android.app.Application;

import androidx.room.Room;

import kg.geektech.taskapp35.room.AppDatabase;

public class App extends Application {

    public static App instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room
                .databaseBuilder(this, AppDatabase.class, "database.db")
                .allowMainThreadQueries()
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
