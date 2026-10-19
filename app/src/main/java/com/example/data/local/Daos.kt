package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): UserEntity?

    @Query("UPDATE users SET fullName = :fullName WHERE id = :id")
    suspend fun updateFullName(id: String, fullName: String)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: String)
}

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses ORDER BY position ASC")
    fun getAllCourses(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE id = :id LIMIT 1")
    suspend fun getCourseById(id: String): CourseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<CourseEntity>)

    @Query("SELECT * FROM lessons WHERE courseId = :courseId ORDER BY position ASC")
    fun getLessonsForCourse(courseId: String): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE id = :id LIMIT 1")
    suspend fun getLessonById(id: String): LessonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)

    @Query("SELECT COUNT(*) FROM lessons")
    suspend fun getTotalLessonsCount(): Int
}

@Dao
interface QuizDao {
    @Query("SELECT * FROM quiz_questions WHERE courseId = :courseId ORDER BY position ASC")
    suspend fun getQuestionsForCourse(courseId: String): List<QuizQuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuizQuestionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: QuizAttemptEntity)

    @Query("SELECT * FROM quiz_attempts WHERE userId = :userId ORDER BY completedAt DESC")
    fun getAttemptsForUser(userId: String): Flow<List<QuizAttemptEntity>>

    @Query("SELECT * FROM quiz_attempts WHERE userId = :userId AND courseId = :courseId ORDER BY completedAt DESC LIMIT 1")
    suspend fun getLatestAttempt(userId: String, courseId: String): QuizAttemptEntity?

    @Query("SELECT AVG(score * 100.0 / totalQuestions) FROM quiz_attempts WHERE userId = :userId")
    suspend fun getAverageScore(userId: String): Double?
}

@Dao
interface ProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setLessonProgress(progress: LessonProgressEntity)

    @Query("SELECT lessonId FROM lesson_progress WHERE userId = :userId AND completed = 1")
    fun getCompletedLessonIds(userId: String): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM lesson_progress WHERE userId = :userId AND completed = 1")
    suspend fun getCompletedLessonsCount(userId: String): Int

    @Query("SELECT completed FROM lesson_progress WHERE userId = :userId AND lessonId = :lessonId LIMIT 1")
    suspend fun isLessonCompleted(userId: String, lessonId: String): Boolean?

    @Query("DELETE FROM lesson_progress WHERE userId = :userId")
    suspend fun clearProgressForUser(userId: String)
}

@Dao
interface ChecklistDao {
    @Query("SELECT * FROM checklist_items ORDER BY position ASC")
    fun getAllChecklistItems(): Flow<List<ChecklistItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklistItems(items: List<ChecklistItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setChecklistProgress(progress: ChecklistProgressEntity)

    @Query("SELECT checklistItemId FROM checklist_progress WHERE userId = :userId AND completed = 1")
    fun getCompletedChecklistItemIds(userId: String): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM checklist_progress WHERE userId = :userId AND completed = 1")
    suspend fun getCompletedCount(userId: String): Int

    @Query("SELECT COUNT(*) FROM checklist_items")
    suspend fun getTotalItemsCount(): Int

    @Query("DELETE FROM checklist_progress WHERE userId = :userId")
    suspend fun clearChecklistForUser(userId: String)
}

@Dao
interface ScamReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ScamReportEntity)

    @Query("SELECT * FROM scam_reports WHERE userId = :userId ORDER BY createdAt DESC")
    fun getReportsForUser(userId: String): Flow<List<ScamReportEntity>>

    @Query("SELECT * FROM scam_reports ORDER BY createdAt DESC")
    fun getAllReports(): Flow<List<ScamReportEntity>>

    @Query("UPDATE scam_reports SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateReportStatus(id: String, status: String, updatedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM scam_reports WHERE userId = :userId")
    suspend fun deleteReportsForUser(userId: String)
}
