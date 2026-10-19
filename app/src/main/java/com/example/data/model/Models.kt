package com.example.data.model

data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val passwordHash: String,
    val isTermsAccepted: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

data class Course(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val difficulty: String, // Beginner, Intermediate, Advanced
    val estimatedMinutes: Int,
    val lessonsCount: Int,
    val iconName: String,
    val position: Int
)

data class Lesson(
    val id: String,
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

data class QuizQuestion(
    val id: String,
    val courseId: String,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
    val questionType: String = "multiple_choice", // multiple_choice, true_false
    val position: Int
)

data class QuizAttempt(
    val id: String,
    val userId: String,
    val courseId: String,
    val score: Int,
    val totalQuestions: Int,
    val passed: Boolean,
    val completedAt: Long = System.currentTimeMillis()
)

data class LessonProgress(
    val id: String,
    val userId: String,
    val lessonId: String,
    val completed: Boolean,
    val completedAt: Long = System.currentTimeMillis()
)

data class ChecklistItem(
    val id: String,
    val title: String,
    val description: String,
    val category: String, // Account security, Devices and software, Data protection, Employee awareness, Incident readiness
    val recommendedAction: String,
    val position: Int
)

data class ChecklistProgress(
    val id: String,
    val userId: String,
    val checklistItemId: String,
    val completed: Boolean,
    val updatedAt: Long = System.currentTimeMillis()
)

data class ScamReport(
    val id: String,
    val userId: String,
    val category: String,
    val channel: String,
    val description: String,
    val incidentDate: String,
    val screenshotPath: String? = null,
    val status: String = "Submitted", // Submitted, Under Review, Resolved
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class ThreatAlert(
    val id: String,
    val title: String,
    val category: String,
    val severity: String, // Critical, High, Caution
    val summary: String,
    val indicatorOfCompromise: String,
    val officialAdvice: String,
    val date: String,
    val isVerifiedCurrent: Boolean = true
)

data class DailyTip(
    val id: String,
    val title: String,
    val tip: String,
    val category: String,
    val practicalStep: String
)
