package database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import dao.AssessmentDao;
import dao.CourseDao;
import dao.TermDao;
import entities.Assessment;
import entities.Course;
import entities.Term;

@Database(entities = {Term.class, Course.class, Assessment.class}, version = 1, exportSchema = false)     // increment version everytime change entities
public abstract class StudentProgressTrackerDatabaseBuilder extends RoomDatabase {
    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();

    private static volatile StudentProgressTrackerDatabaseBuilder INSTANCE;

    static StudentProgressTrackerDatabaseBuilder getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (StudentProgressTrackerDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), StudentProgressTrackerDatabaseBuilder.class, "StudentProgressTrackerDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
