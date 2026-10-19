package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        CourseEntity::class,
        LessonEntity::class,
        QuizQuestionEntity::class,
        QuizAttemptEntity::class,
        LessonProgressEntity::class,
        ChecklistItemEntity::class,
        ChecklistProgressEntity::class,
        ScamReportEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CyberShieldDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun courseDao(): CourseDao
    abstract fun quizDao(): QuizDao
    abstract fun progressDao(): ProgressDao
    abstract fun checklistDao(): ChecklistDao
    abstract fun scamReportDao(): ScamReportDao

    companion object {
        @Volatile
        private var INSTANCE: CyberShieldDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CyberShieldDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CyberShieldDatabase::class.java,
                    "cybershield_db"
                )
                    .addCallback(CyberShieldDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class CyberShieldDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(database: CyberShieldDatabase) {
            database.courseDao().insertCourses(SeedData.courses)
            database.courseDao().insertLessons(SeedData.lessons)
            database.quizDao().insertQuestions(SeedData.quizQuestions)
            database.checklistDao().insertChecklistItems(SeedData.checklistItems)
        }
    }
}
