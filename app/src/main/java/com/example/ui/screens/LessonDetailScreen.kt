package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.CyberShieldViewModel

@Composable
fun LessonDetailScreen(
    courseId: String,
    lessonId: String,
    viewModel: CyberShieldViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedLesson by viewModel.selectedLesson.collectAsState()
    val selectedCourse by viewModel.selectedCourse.collectAsState()
    val completedLessonIds by viewModel.completedLessonIds.collectAsState()
    val isCompleted = selectedLesson != null && completedLessonIds.contains(selectedLesson!!.id)

    val lesson = selectedLesson

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
                            text = selectedCourse?.title ?: "CyberShield Academy",
                            style = MaterialTheme.typography.labelSmall.copy(color = CyberTeal)
                        )
                        Text(
                            text = lesson?.title ?: "Lesson Details",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyberTextPrimary
                            ),
                            maxLines = 1
                        )
                    }
                }
            }
        },
        containerColor = CyberNavyDark
    ) { innerPadding ->
        if (lesson == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = CyberCyan)
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title & Summary Card
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CyberNavySurface),
                        border = BorderStroke(1.dp, CyberBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "LESSON MODULE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CyberCyan,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                if (isCompleted) {
                                    Surface(
                                        color = CyberGreen.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(6.dp),
                                        border = BorderStroke(1.dp, CyberGreen)
                                    ) {
                                        Text(
                                            text = "Completed",
                                            color = CyberGreen,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = lesson.title,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CyberTextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = lesson.summary,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = CyberTextSecondary
                                )
                            )
                        }
                    }
                }

                // Realistic South African Scenario
                item {
                    CyberCard(
                        borderColor = CyberTeal.copy(alpha = 0.4f),
                        backgroundColor = CyberNavyCard
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Campaign,
                                contentDescription = null,
                                tint = CyberTeal,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "South African Scenario",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CyberTeal
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = lesson.scenario,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = CyberTextPrimary,
                                lineHeight = 22.sp
                            )
                        )
                    }
                }

                // How it works
                item {
                    CyberCard(backgroundColor = CyberNavySurface) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "How the Attack Works",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CyberCyan
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = lesson.howItWorks,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = CyberTextSecondary,
                                lineHeight = 21.sp
                            )
                        )
                    }
                }

                // Red Flags / Warning Signs
                item {
                    CyberCard(
                        borderColor = CyberRed.copy(alpha = 0.4f),
                        backgroundColor = CyberNavyCard
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = CyberRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Red Flags & Warning Signs",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CyberRed
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        lesson.redFlags.forEach { flag ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "•",
                                    color = CyberRed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = flag,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = CyberTextPrimary,
                                        lineHeight = 20.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // Safe Habits & Defenses
                item {
                    CyberCard(
                        borderColor = CyberGreen.copy(alpha = 0.4f),
                        backgroundColor = CyberNavyCard
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = CyberGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Safe Habits & Practical Defense",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CyberGreen
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        lesson.safeHabits.forEach { habit ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = CyberGreen,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .padding(top = 3.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = habit,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = CyberTextPrimary,
                                        lineHeight = 20.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // Key Takeaways Box
                item {
                    Surface(
                        color = CyberNavyElevated,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Key Takeaway",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = CyberCyan,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = lesson.keyTakeaways,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = CyberTextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }

                // Action Buttons
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.markLessonCompleted(lesson.id)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isCompleted) CyberNavyElevated else CyberGreen
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("mark_lesson_completed_button")
                        ) {
                            Icon(
                                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Done,
                                contentDescription = null,
                                tint = if (isCompleted) CyberGreen else CyberNavyDark
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isCompleted) "Completed ✓" else "Mark Lesson as Completed",
                                color = if (isCompleted) CyberGreen else CyberNavyDark,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = { viewModel.startQuiz(courseId) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                            border = BorderStroke(1.dp, CyberCyan),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("lesson_take_quiz_button")
                        ) {
                            Icon(Icons.Default.Quiz, contentDescription = null, tint = CyberCyan)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Test Knowledge with Quiz", fontWeight = FontWeight.Bold)
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
