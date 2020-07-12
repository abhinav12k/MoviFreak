package com.apps.movifreak.Database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Created by abhinav on 5/7/20.
 */

@Database(entities = {FavMovie.class}, version = 2, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final Object Lock = new Object();
    private static final String DATABASE_NAME = "moviesDb";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context){

        if(sInstance==null){
            synchronized (Lock){
                Log.d(TAG,"Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration().build();
            }
        }
        Log.d(TAG,"getting database instance");
        return sInstance;
    }

    public abstract MovieDao movieDao();
}
