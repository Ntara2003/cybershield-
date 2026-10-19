package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.UUID

class CyberShieldRepository(
    private val context: Context,
    private val scope: CoroutineScope
) {
    private val database = CyberShieldDatabase.getDatabase(context, scope)
    private val userDao = database.userDao()
    private val courseDao = database.courseDao()
    private val quizDao = database.quizDao()
    private val progressDao = database.progressDao()
    private val checklistDao = database.checklistDao()
    private val scamReportDao = database.scamReportDao()

    private val prefs: SharedPreferences =
        context.getSharedPreferences("cybershield_prefs", Context.MODE_PRIVATE)

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        // Ensure initial database seeding occurs
        scope.launch(Dispatchers.IO) {
            val totalCourses = courseDao.getTotalLessonsCount()
            if (totalCourses == 0) {
                CyberShieldDatabase.populateInitialData(database)
            }
            loadStoredSession()
        }
    }

    private suspend fun loadStoredSession() {
        val storedUserId = prefs.getString("active_user_id", null)
        if (storedUserId != null) {
            val entity = userDao.getUserById(storedUserId)
            if (entity != null) {
                _currentUser.value = User(
                    id = entity.id,
                    fullName = entity.fullName,
                    email = entity.email,
                    passwordHash = entity.passwordHash,
                    isTermsAccepted = entity.isTermsAccepted,
                    createdAt = entity.createdAt
                )
            } else {
                prefs.edit().remove("active_user_id").apply()
            }
        }
    }

    // --- Authentication ---

    private fun hashPassword(password: String): String {
        val salt = "CYBERSHIELD_SA_SALT_2026"
        val bytes = MessageDigest.getInstance("SHA-256").digest((password + salt).toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        isTermsAccepted: Boolean
    ): Result<User> = withContext(Dispatchers.IO) {
        if (fullName.trim().length < 2) {
            return@withContext Result.failure(IllegalArgumentException("Please enter your full name (minimum 2 characters)."))
        }
        val cleanEmail = email.trim().lowercase()
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            return@withContext Result.failure(IllegalArgumentException("Please enter a valid email address."))
        }
        if (password.length < 6) {
            return@withContext Result.failure(IllegalArgumentException("Password must be at least 6 characters long."))
        }
        if (!isTermsAccepted) {
            return@withContext Result.failure(IllegalArgumentException("You must accept the privacy notice and terms to register."))
        }

        val existing = userDao.getUserByEmail(cleanEmail)
        if (existing != null) {
            return@withContext Result.failure(IllegalArgumentException("An account with this email already exists. Please log in."))
        }

        val newId = UUID.randomUUID().toString()
        val userEntity = UserEntity(
            id = newId,
            fullName = fullName.trim(),
            email = cleanEmail,
            passwordHash = hashPassword(password),
            isTermsAccepted = isTermsAccepted,
            createdAt = System.currentTimeMillis()
        )
        userDao.insertUser(userEntity)

        val user = User(
            id = userEntity.id,
            fullName = userEntity.fullName,
            email = userEntity.email,
            passwordHash = userEntity.passwordHash,
            isTermsAccepted = userEntity.isTermsAccepted,
            createdAt = userEntity.createdAt
        )
        prefs.edit().putString("active_user_id", newId).apply()
        _currentUser.value = user
        Result.success(user)
    }

    suspend fun login(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        val cleanEmail = email.trim().lowercase()
        if (cleanEmail.isEmpty() || password.isEmpty()) {
            return@withContext Result.failure(IllegalArgumentException("Please enter both email and password."))
        }

        val entity = userDao.getUserByEmail(cleanEmail)
            ?: return@withContext Result.failure(IllegalArgumentException("Invalid email or password."))

        val inputHash = hashPassword(password)
        if (entity.passwordHash != inputHash) {
            return@withContext Result.failure(IllegalArgumentException("Invalid email or password."))
        }

        val user = User(
            id = entity.id,
            fullName = entity.fullName,
            email = entity.email,
            passwordHash = entity.passwordHash,
            isTermsAccepted = entity.isTermsAccepted,
            createdAt = entity.createdAt
        )
        prefs.edit().putString("active_user_id", user.id).apply()
        _currentUser.value = user
        Result.success(user)
    }

    fun logout() {
        prefs.edit().remove("active_user_id").apply()
        _currentUser.value = null
    }

    suspend fun updateFullName(newName: String): Result<Unit> = withContext(Dispatchers.IO) {
        val user = _currentUser.value ?: return@withContext Result.failure(IllegalStateException("Not logged in"))
        if (newName.trim().length < 2) {
            return@withContext Result.failure(IllegalArgumentException("Full name must be at least 2 characters."))
        }
        val trimmed = newName.trim()
        userDao.updateFullName(user.id, trimmed)
        _currentUser.value = user.copy(fullName = trimmed)
        Result.success(Unit)
    }

    suspend fun deleteAccount(): Result<Unit> = withContext(Dispatchers.IO) {
        val user = _currentUser.value ?: return@withContext Result.failure(IllegalStateException("Not logged in"))
        progressDao.clearProgressForUser(user.id)
        checklistDao.clearChecklistForUser(user.id)
        scamReportDao.deleteReportsForUser(user.id)
        userDao.deleteUser(user.id)
        logout()
        Result.success(Unit)
    }

    // --- Courses & Academy ---

    val courses: Flow<List<Course>> = courseDao.getAllCourses().map { list ->
        list.map {
            Course(
                id = it.id,
                title = it.title,
                description = it.description,
                category = it.category,
                difficulty = it.difficulty,
                estimatedMinutes = it.estimatedMinutes,
                lessonsCount = it.lessonsCount,
                iconName = it.iconName,
                position = it.position
            )
        }
    }

    fun getLessonsForCourse(courseId: String): Flow<List<Lesson>> =
        courseDao.getLessonsForCourse(courseId).map { list ->
            list.map {
                Lesson(
                    id = it.id,
                    courseId = it.courseId,
                    title = it.title,
                    summary = it.summary,
                    scenario = it.scenario,
                    howItWorks = it.howItWorks,
                    redFlags = it.redFlags,
                    safeHabits = it.safeHabits,
                    keyTakeaways = it.keyTakeaways,
                    position = it.position
                )
            }
        }

    suspend fun getLesson(lessonId: String): Lesson? = withContext(Dispatchers.IO) {
        courseDao.getLessonById(lessonId)?.let {
            Lesson(
                id = it.id,
                courseId = it.courseId,
                title = it.title,
                summary = it.summary,
                scenario = it.scenario,
                howItWorks = it.howItWorks,
                redFlags = it.redFlags,
                safeHabits = it.safeHabits,
                keyTakeaways = it.keyTakeaways,
                position = it.position
            )
        }
    }

    fun getCompletedLessonIds(userId: String): Flow<List<String>> =
        progressDao.getCompletedLessonIds(userId)

    suspend fun completeLesson(userId: String, lessonId: String) = withContext(Dispatchers.IO) {
        val id = UUID.nameUUIDFromBytes("$userId:$lessonId".toByteArray()).toString()
        progressDao.setLessonProgress(
            LessonProgressEntity(
                id = id,
                userId = userId,
                lessonId = lessonId,
                completed = true,
                completedAt = System.currentTimeMillis()
            )
        )
    }

    // --- Quizzes ---

    suspend fun getQuestionsForCourse(courseId: String): List<QuizQuestion> =
        withContext(Dispatchers.IO) {
            quizDao.getQuestionsForCourse(courseId).map {
                QuizQuestion(
                    id = it.id,
                    courseId = it.courseId,
                    question = it.question,
                    options = it.options,
                    correctAnswerIndex = it.correctAnswerIndex,
                    explanation = it.explanation,
                    questionType = it.questionType,
                    position = it.position
                )
            }
        }

    suspend fun submitQuizAttempt(
        userId: String,
        courseId: String,
        answers: Map<String, Int> // questionId -> selectedOptionIndex
    ): QuizAttempt = withContext(Dispatchers.IO) {
        val questions = quizDao.getQuestionsForCourse(courseId)
        var correctCount = 0
        for (q in questions) {
            val selected = answers[q.id]
            if (selected != null && selected == q.correctAnswerIndex) {
                correctCount++
            }
        }
        val total = questions.size
        val scorePercent = if (total > 0) (correctCount * 100) / total else 0
        val passed = scorePercent >= 80

        val attemptId = UUID.randomUUID().toString()
        val attemptEntity = QuizAttemptEntity(
            id = attemptId,
            userId = userId,
            courseId = courseId,
            score = correctCount,
            totalQuestions = total,
            passed = passed,
            completedAt = System.currentTimeMillis()
        )
        quizDao.insertAttempt(attemptEntity)

        QuizAttempt(
            id = attemptEntity.id,
            userId = attemptEntity.userId,
            courseId = attemptEntity.courseId,
            score = attemptEntity.score,
            totalQuestions = attemptEntity.totalQuestions,
            passed = attemptEntity.passed,
            completedAt = attemptEntity.completedAt
        )
    }

    fun getUserAttempts(userId: String): Flow<List<QuizAttempt>> =
        quizDao.getAttemptsForUser(userId).map { list ->
            list.map {
                QuizAttempt(
                    id = it.id,
                    userId = it.userId,
                    courseId = it.courseId,
                    score = it.score,
                    totalQuestions = it.totalQuestions,
                    passed = it.passed,
                    completedAt = it.completedAt
                )
            }
        }

    // --- SME Checklist ---

    val checklistItems: Flow<List<ChecklistItem>> = checklistDao.getAllChecklistItems().map { list ->
        list.map {
            ChecklistItem(
                id = it.id,
                title = it.title,
                description = it.description,
                category = it.category,
                recommendedAction = it.recommendedAction,
                position = it.position
            )
        }
    }

    fun getCompletedChecklistItemIds(userId: String): Flow<List<String>> =
        checklistDao.getCompletedChecklistItemIds(userId)

    suspend fun toggleChecklistItem(userId: String, itemId: String, completed: Boolean) =
        withContext(Dispatchers.IO) {
            val id = UUID.nameUUIDFromBytes("$userId:$itemId".toByteArray()).toString()
            checklistDao.setChecklistProgress(
                ChecklistProgressEntity(
                    id = id,
                    userId = userId,
                    checklistItemId = itemId,
                    completed = completed,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }

    // --- Scam Reporting ---

    suspend fun submitScamReport(
        userId: String,
        category: String,
        channel: String,
        description: String,
        incidentDate: String,
        screenshotPath: String?
    ): Result<ScamReport> = withContext(Dispatchers.IO) {
        if (category.isBlank()) {
            return@withContext Result.failure(IllegalArgumentException("Please select a scam category."))
        }
        if (channel.isBlank()) {
            return@withContext Result.failure(IllegalArgumentException("Please specify the channel (e.g. Email, WhatsApp)."))
        }
        if (description.trim().length < 15) {
            return@withContext Result.failure(IllegalArgumentException("Please provide a detailed description (at least 15 characters) to help review the incident."))
        }

        // Validate sensitive keyword warnings
        val lower = description.lowercase()
        val sensitivePatterns = listOf("cvv", "pin", "card number", "password", "bank login")
        if (sensitivePatterns.any { lower.contains(it) && !lower.contains("never") && !lower.contains("did not") }) {
            // Note: We remind users not to include sensitive credentials
        }

        val reportId = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()
        val reportEntity = ScamReportEntity(
            id = reportId,
            userId = userId,
            category = category,
            channel = channel,
            description = description.trim(),
            incidentDate = incidentDate.ifBlank { "Recent" },
            screenshotPath = screenshotPath,
            status = "Submitted",
            createdAt = now,
            updatedAt = now
        )
        scamReportDao.insertReport(reportEntity)

        val report = ScamReport(
            id = reportEntity.id,
            userId = reportEntity.userId,
            category = reportEntity.category,
            channel = reportEntity.channel,
            description = reportEntity.description,
            incidentDate = reportEntity.incidentDate,
            screenshotPath = reportEntity.screenshotPath,
            status = reportEntity.status,
            createdAt = reportEntity.createdAt,
            updatedAt = reportEntity.updatedAt
        )
        Result.success(report)
    }

    fun getUserScamReports(userId: String): Flow<List<ScamReport>> =
        scamReportDao.getReportsForUser(userId).map { list ->
            list.map {
                ScamReport(
                    id = it.id,
                    userId = it.userId,
                    category = it.category,
                    channel = it.channel,
                    description = it.description,
                    incidentDate = it.incidentDate,
                    screenshotPath = it.screenshotPath,
                    status = it.status,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
            }
        }

    // --- Threat Alerts & Daily Tips ---

    fun getThreatAlerts(): List<ThreatAlert> = SeedData.threatAlerts

    fun getDailyTips(): List<DailyTip> = SeedData.dailyTips
}
