package com.njcrain.android.healthtracker;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest {
    private ExerciseDao mExerciseDao;
    private AppDatabase mDb;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mExerciseDao = mDb.exerciseDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    //Would like this test to use a reflective comparison on the two objects, but probably need another dependency to do that
    @Test
    public void createAndReadExercise() throws Exception{
        Exercise exercise = new Exercise("Pushups", 15, "did pushups", "1/9/19 7:00PM");
        mExerciseDao.add(exercise);
        Exercise exerciseById = mExerciseDao.getById((long) 1);
        assertEquals(exercise.toString(), exerciseById.toString());

        Exercise exercise2 = new Exercise("Pushups", 15, "did pushups", "1/9/19 7:00PM");
        mExerciseDao.add(exercise2);
        Exercise exerciseById2 = mExerciseDao.getById((long) 2);
        assertEquals(exercise2.toString(), exerciseById2.toString());
    }
}