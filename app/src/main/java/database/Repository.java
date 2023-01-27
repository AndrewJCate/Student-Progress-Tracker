package database;

import android.app.Application;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dao.AssessmentDao;
import dao.CourseDao;
import dao.TermDao;
import entities.Assessment;
import entities.Course;
import entities.Term;

public class Repository {

    private final static int NUM_THREADS = 4;

    static final ExecutorService DATABASE_EXECUTOR = Executors.newFixedThreadPool(NUM_THREADS);

    private final TermDao       mTermDao;
    private final CourseDao     mCourseDao;
    private final AssessmentDao mAssessmentDao;

    private List<Term>       mAllTerms;
    private List<Course>     mAllCourses;
    private List<Assessment> mAllAssessments;

    public Repository(Application application) {
        StudentProgressTrackerDatabaseBuilder db = StudentProgressTrackerDatabaseBuilder.getDatabase(application);
        mTermDao       = db.termDao();
        mCourseDao     = db.courseDao();
        mAssessmentDao = db.assessmentDao();
    }

    public void insert(Term term) {
        DATABASE_EXECUTOR.execute( () -> mTermDao.insert(term));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Term term) {
        DATABASE_EXECUTOR.execute( () -> mTermDao.update(term));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Term term) {
        DATABASE_EXECUTOR.execute( () -> mTermDao.delete(term));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Term> getAllTerms() {
        DATABASE_EXECUTOR.execute( () -> mAllTerms = mTermDao.getAllTerms());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllTerms;
    }

    public void insert(Course course) {
        DATABASE_EXECUTOR.execute( () -> mCourseDao.insert(course));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Course course) {
        DATABASE_EXECUTOR.execute( () -> mCourseDao.update(course));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Course course) {
        DATABASE_EXECUTOR.execute( () -> mCourseDao.delete(course));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Course> getAllCourses() {
        DATABASE_EXECUTOR.execute( () -> mAllCourses = mCourseDao.getAllCourses());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllCourses;
    }

    public void insert(Assessment assessment) {
        DATABASE_EXECUTOR.execute( () -> mAssessmentDao.insert(assessment));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Assessment assessment) {
        DATABASE_EXECUTOR.execute( () -> mAssessmentDao.update(assessment));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Assessment assessment) {
        DATABASE_EXECUTOR.execute( () -> mAssessmentDao.delete(assessment));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Assessment> getAllAssessments() {
        DATABASE_EXECUTOR.execute( () -> mAllAssessments = mAssessmentDao.getAllAssessments());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllAssessments;
    }
}
