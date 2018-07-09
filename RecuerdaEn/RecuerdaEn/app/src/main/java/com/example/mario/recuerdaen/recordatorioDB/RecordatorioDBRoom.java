package com.example.mario.recuerdaen.recordatorioDB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = { Recordatorio.class }, version = 1)
public abstract class RecordatorioDBRoom extends RoomDatabase {

    private static final String DB_NAME = "recordatorioDB.db";
    private static volatile RecordatorioDBRoom instance;

    public static synchronized RecordatorioDBRoom getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static RecordatorioDBRoom create(final Context context) {
        return Room.databaseBuilder(
                context,
                RecordatorioDBRoom.class,
                DB_NAME).build();
    }

    public abstract RecordatorioDAO getRecordatorioDao();
}