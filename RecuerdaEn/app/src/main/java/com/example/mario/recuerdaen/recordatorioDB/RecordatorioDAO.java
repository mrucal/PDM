package com.example.mario.recuerdaen.recordatorioDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RecordatorioDAO {

    @Query("SELECT * FROM recordatorio_enity ORDER BY nombre, descripcion")
    List<Recordatorio> getAllRecordatorios();

    @Query("SELECT * FROM recordatorio_enity WHERE nombre=:nombre and descripcion=:descripcion")
    Recordatorio getRecordatorio(String nombre, String descripcion);

    @Insert
    void insert(Recordatorio recordatorio);

    @Update
    void update(Recordatorio recordatorio);

    @Delete
    void delete(Recordatorio recordatorio);
}