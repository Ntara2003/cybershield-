package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val fullName: String,
    val email: String,
    val passwordHash: String,
    val isTermsAccepted: Boolean,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: String,
    val difficulty: String,
    val estimatedMinutes: Int,
    val lessonsCount: Int,
    val iconName: String,
    val position: Int
)

@Entity(tableName = "lessons")
data class LessonEntity(
    @PrimaryKey val id: String,
    val courseId: String,
    val title: String,
    val summary: String,
    val scenario: String,
    val howItWorks: String,
    val redFlags: List<String>,
    val safeHabits: List<String>,
    val keyTakeaways: String,
    val position: Int
)

@Entity(tableName = "quiz_questions")
data class QuizQuestionEntity(
    @PrimaryKey val id: String,
    val courseId: String,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
    val questionType: String,
    val position: Int
)

@Entity(tableName = "quiz_attempts")
data class QuizAttemptEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val courseId: String,
    val score: Int,
    val totalQuestions: Int,
    val passed: Boolean,
    val completedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "lesson_progress",
    indices = [Index(value = ["userId", "lessonId"], unique = true)]
)
data class LessonProgressEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val lessonId: String,
    val completed: Boolean,
    val completedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "checklist_items")
data class ChecklistItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: String,
    val recommendedAction: String,
    val position: Int
)

@Entity(
    tableName = "checklist_progress",
    indices = [Index(value = ["userId", "checklistItemId"], unique = true)]
)
data class ChecklistProgressEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val checklistItemId: String,
    val completed: Boolean,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "scam_reports")
data class ScamReportEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val category: String,
    val channel: String,
    val description: String,
    val incidentDate: String,
    val screenshotPath: String?,
    val status: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
