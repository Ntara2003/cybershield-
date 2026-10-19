package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BadgeType
import com.example.ui.components.CyberCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.CyberShieldViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun QuizScreen(
    courseId: String,
    viewModel: CyberShieldViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedCourse by viewModel.selectedCourse.collectAsState()
    val questions by viewModel.activeQuizQuestions.collectAsState()
    val currentIndex by viewModel.currentQuizQuestionIndex.collectAsState()
    val answers by viewModel.quizAnswers.collectAsState()
    val isSubmitted by viewModel.isAnswerSubmittedForCurrentQuestion.collectAsState()
    val isQuizFinished by viewModel.isQuizFinished.collectAsState()
    val attemptResult by viewModel.quizAttemptResult.collectAsState()

    val currentQuestion = questions.getOrNull(currentIndex)
    val selectedOptionIndex = currentQuestion?.let { answers[it.id] }

    Scaffold(
        topBar = {
            Surface(
                color = CyberNavySurface,
                border = BorderStroke(1.dp, CyberBorder),
                modifier = Modifier.statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = CyberCyan
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Course Quiz • ${selectedCourse?.title ?: ""}",
                            style = MaterialTheme.typography.labelSmall.copy(color = CyberTeal)
                        )
                        Text(
                            text = if (isQuizFinished) "Quiz Results" else "Question ${currentIndex + 1} of ${questions.size}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyberTextPrimary
                            )
                        )
                    }
                }
            }
        },
        containerColor = CyberNavyDark
    ) { innerPadding ->
        if (questions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = CyberCyan)
            }
        } else if (isQuizFinished && attemptResult != null) {
            // Results Screen
            val attempt = attemptResult!!
            val scorePercent = (attempt.score * 100) / (if (attempt.totalQuestions > 0) attempt.totalQuestions else 1)
            val passed = scorePercent >= 80

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Result Emblem
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(
                                if (passed) CyberGreen.copy(alpha = 0.15f) else CyberAmber.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (passed) Icons.Default.Verified else Icons.Default.Replay,
                            contentDescription = null,
                            tint = if (passed) CyberGreen else CyberAmber,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }

                item {
                    Text(
                        text = if (passed) "Course Mastered!" else "Review & Try Again",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (passed) CyberGreen else CyberAmber
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (passed)
                            "Congratulations! You achieved the 80% passing threshold for ${selectedCourse?.title}."
                        else
                            "You scored $scorePercent%. The passing standard is 80%. Review the recommendations below and retake the quiz.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = CyberTextSecondary,
                            lineHeight = 20.sp
                        )
                    )
                }

                item {
                    // Score Details Card
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CyberNavySurface),
                        border = BorderStroke(1.dp, CyberBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Correct Answers", color = CyberTextSecondary)
                                Text(
                                    "${attempt.score} of ${attempt.totalQuestions}",
                                    fontWeight = FontWeight.Bold,
                                    color = CyberTextPrimary
                                )
                            }
                            HorizontalDivider(
                                color = CyberBorder,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Final Percentage", color = CyberTextSecondary)
                                Text(
                                    "$scorePercent%",
                                    fontWeight = FontWeight.Bold,
                                    color = if (passed) CyberGreen else CyberAmber
                                )
                            }
                            HorizontalDivider(
                                color = CyberBorder,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Pass Threshold", color = CyberTextSecondary)
                                Text("80%", fontWeight = FontWeight.Bold, color = CyberTeal)
                            }
                            HorizontalDivider(
                                color = CyberBorder,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Status", color = CyberTextSecondary)
                                StatusBadge(
                                    text = if (passed) "PASSED" else "NEEDS REVIEW",
                                    type = if (passed) BadgeType.SUCCESS else BadgeType.WARNING
                                )
                            }
                        }
                    }
                }

                // Recommendations
                item {
                    CyberCard(backgroundColor = CyberNavyCard) {
                        Text(
                            text = "Recommended Next Steps",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyberCyan
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (passed)
                                "• Continue to the next Academy module.\n• Apply these defense habits to your personal email and mobile messaging.\n• Review the SME Security Checklist to protect your organization."
                            else
                                "• Revisit the lesson explanations on red flags and sender domain spoofing.\n• Remember to always navigate directly to official bank/SARS portals instead of following links.\n• Retake this quiz whenever you are ready.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CyberTextPrimary,
                                lineHeight = 20.sp
                            )
                        )
                    }
                }

                // Action CTAs
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.startQuiz(courseId) },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("quiz_retry_button")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, tint = CyberNavyDark)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Retry Quiz", color = CyberNavyDark, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.navigateTo(Screen.Learn)
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberTextPrimary),
                            border = BorderStroke(1.dp, CyberBorder),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text("Back to Course Catalog")
                        }
                    }
                }
            }
        } else if (currentQuestion != null) {
            // Active Question Screen
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Progress Bar
                item {
                    val progressRatio = (currentIndex + 1).toFloat() / questions.size.toFloat()
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Question ${currentIndex + 1} of ${questions.size}",
                                style = MaterialTheme.typography.labelMedium.copy(color = CyberTeal)
                            )
                            Text(
                                text = if (currentQuestion.questionType == "true_false") "True / False" else "Multiple Choice",
                                style = MaterialTheme.typography.labelSmall.copy(color = CyberTextMuted)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { progressRatio },
                            color = CyberCyan,
                            trackColor = CyberNavyElevated,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                        )
                    }
                }

                // Question Box
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CyberNavySurface),
                        border = BorderStroke(1.dp, CyberBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = currentQuestion.question,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CyberTextPrimary,
                                    lineHeight = 24.sp
                                )
                            )
                        }
                    }
                }

                // Answer Options
                items(currentQuestion.options.size) { optIdx ->
                    val optionText = currentQuestion.options[optIdx]
                    val isSelected = selectedOptionIndex == optIdx

                    val (containerColor, borderColor, textIconColor) = when {
                        isSubmitted && optIdx == currentQuestion.correctAnswerIndex ->
                            Triple(CyberGreen.copy(alpha = 0.15f), CyberGreen, CyberGreen)
                        isSubmitted && isSelected && optIdx != currentQuestion.correctAnswerIndex ->
                            Triple(CyberRed.copy(alpha = 0.15f), CyberRed, CyberRed)
                        isSelected ->
                            Triple(CyberNavyElevated, CyberCyan, CyberCyan)
                        else ->
                            Triple(CyberNavyCard, CyberBorder, CyberTextPrimary)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !isSubmitted) {
                                viewModel.selectQuizOption(currentQuestion.id, optIdx)
                            }
                            .testTag("quiz_option_$optIdx"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = containerColor),
                        border = BorderStroke(1.dp, borderColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(borderColor.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = ('A' + optIdx).toString(),
                                    color = textIconColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = CyberTextPrimary,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            if (isSubmitted) {
                                if (optIdx == currentQuestion.correctAnswerIndex) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Correct",
                                        tint = CyberGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Cancel,
                                        contentDescription = "Incorrect",
                                        tint = CyberRed,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // In-depth Explanation Card (shown once submitted)
                if (isSubmitted) {
                    val wasCorrect = selectedOptionIndex == currentQuestion.correctAnswerIndex
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (wasCorrect) CyberGreen.copy(alpha = 0.1f) else CyberNavyElevated
                            ),
                            border = BorderStroke(1.dp, if (wasCorrect) CyberGreen else CyberAmber),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (wasCorrect) Icons.Default.CheckCircle else Icons.Default.Info,
                                        contentDescription = null,
                                        tint = if (wasCorrect) CyberGreen else CyberAmber,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (wasCorrect) "Correct Answer!" else "Explanation:",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (wasCorrect) CyberGreen else CyberAmber
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = currentQuestion.explanation,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = CyberTextPrimary,
                                        lineHeight = 19.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // CTA Button: Submit Answer or Continue / Next
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    if (!isSubmitted) {
                        Button(
                            onClick = { viewModel.submitCurrentQuestionAnswer() },
                            enabled = selectedOptionIndex != null,
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("quiz_submit_answer_button")
                        ) {
                            Text(
                                "Lock In Answer",
                                color = CyberNavyDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Button(
                            onClick = { viewModel.nextQuizQuestion(courseId) },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("quiz_next_question_button")
                        ) {
                            Text(
                                text = if (currentIndex + 1 < questions.size) "Next Question →" else "View Final Results",
                                color = CyberNavyDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}
