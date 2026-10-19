package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.CyberShieldRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object Learn : Screen()
    data class LessonDetail(val courseId: String, val lessonId: String) : Screen()
    data class Quiz(val courseId: String) : Screen()
    object Checklist : Screen()
    object ReportScam : Screen()
    object Profile : Screen()
}

data class DashboardStats(
    val overallProgressPercent: Int = 0,
    val completedLessonsCount: Int = 0,
    val totalLessonsCount: Int = 9,
    val completedCoursesCount: Int = 0,
    val latestQuizScore: Int? = null,
    val checklistCompletionPercent: Int = 0,
    val completedChecklistCount: Int = 0,
    val totalChecklistCount: Int = 15
)

class CyberShieldViewModel(application: Application) : AndroidViewModel(application) {

    val repository = CyberShieldRepository(application.applicationContext, viewModelScope)

    // Navigation Stack / Current Screen
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val screenBackStack = mutableListOf<Screen>(Screen.Home)

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            screenBackStack.add(screen)
            _currentScreen.value = screen
        }
    }

    fun handleBack(): Boolean {
        if (screenBackStack.size > 1) {
            screenBackStack.removeAt(screenBackStack.size - 1)
            _currentScreen.value = screenBackStack.last()
            return true
        } else if (_currentScreen.value !is Screen.Home) {
            _currentScreen.value = Screen.Home
            screenBackStack.clear()
            screenBackStack.add(Screen.Home)
            return true
        }
        return false
    }

    // Auth State
    val currentUser: StateFlow<User?> = repository.currentUser

    private val _isAuthDialogOpen = MutableStateFlow(false)
    val isAuthDialogOpen: StateFlow<Boolean> = _isAuthDialogOpen.asStateFlow()

    private val _authDialogTab = MutableStateFlow("LOGIN") // LOGIN or REGISTER
    val authDialogTab: StateFlow<String> = _authDialogTab.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private val _authSuccessMessage = MutableStateFlow<String?>(null)
    val authSuccessMessage: StateFlow<String?> = _authSuccessMessage.asStateFlow()

    fun openAuthDialog(initialTab: String = "LOGIN") {
        _authDialogTab.value = initialTab
        _authError.value = null
        _authSuccessMessage.value = null
        _isAuthDialogOpen.value = true
    }

    fun closeAuthDialog() {
        _isAuthDialogOpen.value = false
        _authError.value = null
    }

    fun setAuthTab(tab: String) {
        _authDialogTab.value = tab
        _authError.value = null
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _authError.value = null
            val result = repository.login(email, pass)
            result.onSuccess {
                _isAuthDialogOpen.value = false
                _authSuccessMessage.value = "Welcome back, ${it.fullName}!"
            }.onFailure {
                _authError.value = it.message ?: "Login failed. Please check your credentials."
            }
        }
    }

    fun register(fullName: String, email: String, pass: String, acceptedTerms: Boolean) {
        viewModelScope.launch {
            _authError.value = null
            val result = repository.register(fullName, email, pass, acceptedTerms)
            result.onSuccess {
                _isAuthDialogOpen.value = false
                _authSuccessMessage.value = "Account created! Welcome to CyberShield SA."
            }.onFailure {
                _authError.value = it.message ?: "Registration failed."
            }
        }
    }

    fun logout() {
        repository.logout()
        _currentScreen.value = Screen.Home
        screenBackStack.clear()
        screenBackStack.add(Screen.Home)
    }

    fun updateFullName(newName: String) {
        viewModelScope.launch {
            val result = repository.updateFullName(newName)
            result.onSuccess {
                _authSuccessMessage.value = "Display name updated."
            }.onFailure {
                _authError.value = it.message
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            repository.deleteAccount()
            _authSuccessMessage.value = "Account and associated data deleted."
        }
    }

    // Courses & Lessons
    val courses: StateFlow<List<Course>> = repository.courses.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _selectedCourse = MutableStateFlow<Course?>(null)
    val selectedCourse: StateFlow<Course?> = _selectedCourse.asStateFlow()

    private val _courseLessons = MutableStateFlow<List<Lesson>>(emptyList())
    val courseLessons: StateFlow<List<Lesson>> = _courseLessons.asStateFlow()

    private val _selectedLesson = MutableStateFlow<Lesson?>(null)
    val selectedLesson: StateFlow<Lesson?> = _selectedLesson.asStateFlow()

    val completedLessonIds: StateFlow<List<String>> = currentUser.flatMapLatest { user ->
        if (user != null) repository.getCompletedLessonIds(user.id)
        else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectCourse(course: Course) {
        _selectedCourse.value = course
        viewModelScope.launch {
            repository.getLessonsForCourse(course.id).collect {
                _courseLessons.value = it
            }
        }
    }

    fun openLesson(courseId: String, lessonId: String) {
        viewModelScope.launch {
            val lesson = repository.getLesson(lessonId)
            _selectedLesson.value = lesson
            val course = courses.value.find { it.id == courseId }
            if (course != null) _selectedCourse.value = course
            navigateTo(Screen.LessonDetail(courseId, lessonId))
        }
    }

    fun markLessonCompleted(lessonId: String) {
        val user = currentUser.value
        if (user == null) {
            openAuthDialog("LOGIN")
            return
        }
        viewModelScope.launch {
            repository.completeLesson(user.id, lessonId)
        }
    }

    // Quizzes
    private val _activeQuizQuestions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val activeQuizQuestions: StateFlow<List<QuizQuestion>> = _activeQuizQuestions.asStateFlow()

    private val _currentQuizQuestionIndex = MutableStateFlow(0)
    val currentQuizQuestionIndex: StateFlow<Int> = _currentQuizQuestionIndex.asStateFlow()

    private val _quizAnswers = MutableStateFlow<Map<String, Int>>(emptyMap())
    val quizAnswers: StateFlow<Map<String, Int>> = _quizAnswers.asStateFlow()

    private val _isAnswerSubmittedForCurrentQuestion = MutableStateFlow(false)
    val isAnswerSubmittedForCurrentQuestion: StateFlow<Boolean> = _isAnswerSubmittedForCurrentQuestion.asStateFlow()

    private val _quizAttemptResult = MutableStateFlow<QuizAttempt?>(null)
    val quizAttemptResult: StateFlow<QuizAttempt?> = _quizAttemptResult.asStateFlow()

    private val _isQuizFinished = MutableStateFlow(false)
    val isQuizFinished: StateFlow<Boolean> = _isQuizFinished.asStateFlow()

    val userQuizAttempts: StateFlow<List<QuizAttempt>> = currentUser.flatMapLatest { user ->
        if (user != null) repository.getUserAttempts(user.id)
        else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun startQuiz(courseId: String) {
        val course = courses.value.find { it.id == courseId }
        _selectedCourse.value = course
        viewModelScope.launch {
            val questions = repository.getQuestionsForCourse(courseId)
            _activeQuizQuestions.value = questions
            _currentQuizQuestionIndex.value = 0
            _quizAnswers.value = emptyMap()
            _isAnswerSubmittedForCurrentQuestion.value = false
            _quizAttemptResult.value = null
            _isQuizFinished.value = false
            navigateTo(Screen.Quiz(courseId))
        }
    }

    fun selectQuizOption(questionId: String, optionIndex: Int) {
        if (!_isAnswerSubmittedForCurrentQuestion.value) {
            val map = _quizAnswers.value.toMutableMap()
            map[questionId] = optionIndex
            _quizAnswers.value = map
        }
    }

    fun submitCurrentQuestionAnswer() {
        _isAnswerSubmittedForCurrentQuestion.value = true
    }

    fun nextQuizQuestion(courseId: String) {
        val nextIdx = _currentQuizQuestionIndex.value + 1
        if (nextIdx < _activeQuizQuestions.value.size) {
            _currentQuizQuestionIndex.value = nextIdx
            _isAnswerSubmittedForCurrentQuestion.value = false
        } else {
            // Finish quiz & calculate score
            finishQuiz(courseId)
        }
    }

    private fun finishQuiz(courseId: String) {
        viewModelScope.launch {
            val user = currentUser.value
            val userId = user?.id ?: "guest_session"
            val attempt = repository.submitQuizAttempt(userId, courseId, _quizAnswers.value)
            _quizAttemptResult.value = attempt
            _isQuizFinished.value = true
        }
    }

    // SME Checklist
    val checklistItems: StateFlow<List<ChecklistItem>> = repository.checklistItems.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val completedChecklistItemIds: StateFlow<List<String>> = currentUser.flatMapLatest { user ->
        if (user != null) repository.getCompletedChecklistItemIds(user.id)
        else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedChecklistItemForDetail = MutableStateFlow<ChecklistItem?>(null)
    val selectedChecklistItemForDetail: StateFlow<ChecklistItem?> = _selectedChecklistItemForDetail.asStateFlow()

    fun selectChecklistItem(item: ChecklistItem?) {
        _selectedChecklistItemForDetail.value = item
    }

    fun toggleChecklistItem(itemId: String, currentlyCompleted: Boolean) {
        val user = currentUser.value
        if (user == null) {
            openAuthDialog("LOGIN")
            return
        }
        viewModelScope.launch {
            repository.toggleChecklistItem(user.id, itemId, !currentlyCompleted)
        }
    }

    // Scam Reporting
    val userScamReports: StateFlow<List<ScamReport>> = currentUser.flatMapLatest { user ->
        if (user != null) repository.getUserScamReports(user.id)
        else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _scamReportSubmitResult = MutableStateFlow<Result<ScamReport>?>(null)
    val scamReportSubmitResult: StateFlow<Result<ScamReport>?> = _scamReportSubmitResult.asStateFlow()

    private val _isSubmittingScamReport = MutableStateFlow(false)
    val isSubmittingScamReport: StateFlow<Boolean> = _isSubmittingScamReport.asStateFlow()

    fun submitScamReport(
        category: String,
        channel: String,
        description: String,
        incidentDate: String,
        screenshotPath: String?
    ) {
        val user = currentUser.value
        if (user == null) {
            openAuthDialog("LOGIN")
            return
        }
        viewModelScope.launch {
            _isSubmittingScamReport.value = true
            val result = repository.submitScamReport(
                userId = user.id,
                category = category,
                channel = channel,
                description = description,
                incidentDate = incidentDate,
                screenshotPath = screenshotPath
            )
            _scamReportSubmitResult.value = result
            _isSubmittingScamReport.value = false
        }
    }

    fun clearScamReportResult() {
        _scamReportSubmitResult.value = null
    }

    // Daily Tips & Threat Alerts
    val threatAlerts: List<ThreatAlert> = repository.getThreatAlerts()
    val dailyTips: List<DailyTip> = repository.getDailyTips()

    private val _currentDailyTipIndex = MutableStateFlow(0)
    val currentDailyTipIndex: StateFlow<Int> = _currentDailyTipIndex.asStateFlow()

    fun rotateDailyTip() {
        _currentDailyTipIndex.value = (_currentDailyTipIndex.value + 1) % dailyTips.size
    }

    // Calculated Dashboard Stats
    val dashboardStats: StateFlow<DashboardStats> = combine(
        completedLessonIds,
        checklistItems,
        completedChecklistItemIds,
        userQuizAttempts
    ) { completedLessons, allChecklist, completedChecklist, attempts ->
        val totalLessons = 9 // based on seed data
        val lessonPercent = if (totalLessons > 0) (completedLessons.size * 100) / totalLessons else 0
        val checklistPercent = if (allChecklist.isNotEmpty()) (completedChecklist.size * 100) / allChecklist.size else 0
        val latestAttempt = attempts.firstOrNull()
        val latestScore = latestAttempt?.let { (it.score * 100) / (if (it.totalQuestions > 0) it.totalQuestions else 1) }

        val completedCourses = attempts.filter { it.passed }.map { it.courseId }.distinct().size

        DashboardStats(
            overallProgressPercent = ((lessonPercent + checklistPercent) / 2).coerceIn(0, 100),
            completedLessonsCount = completedLessons.size,
            totalLessonsCount = totalLessons,
            completedCoursesCount = completedCourses,
            latestQuizScore = latestScore,
            checklistCompletionPercent = checklistPercent,
            completedChecklistCount = completedChecklist.size,
            totalChecklistCount = allChecklist.size.coerceAtLeast(15)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardStats())
}
