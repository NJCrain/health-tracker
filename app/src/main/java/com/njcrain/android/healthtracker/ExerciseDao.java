package com.njcrain.android.healthtracker;

import java.util.List;

import androidx.room.Insert;
import androidx.room.Query;

public interface ExerciseDao {
    @Query("SELECT * FROM exercise")
    List<Exercise> getAll();

    @Insert
    void add(Exercise exercise);
}
