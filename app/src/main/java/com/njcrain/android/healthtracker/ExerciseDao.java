package com.njcrain.android.healthtracker;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ExerciseDao {
    @Query("SELECT * FROM exercise")
    List<Exercise> getAll();

    @Query("SELECT * FROM exercise WHERE id=:id")
    Exercise getById(long id);

    @Insert
    void add(Exercise exercise);
}
