package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Course
import com.example.ui.components.BadgeType
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.CyberShieldViewModel

@Composable
fun LearnScreen(
    viewModel: CyberShieldViewModel,
    modifier: Modifier = Modifier
) {
    val courses by viewModel.courses.collectAsState()
    val completedLessonIds by viewModel.completedLessonIds.collectAsState()
    val selectedCourse by viewModel.selectedCourse.collectAsState()
    val courseLessons by viewModel.courseLessons.collectAsState()

    var selectedCategoryFilter by remember { mutableStateOf("All") }

    val categories = remember(courses) {
        listOf("All") + courses.map { it.category }.distinct()
    }

    val filteredCourses = remember(courses, selectedCategoryFilter) {
        if (selectedCategoryFilter == "All") courses
        else courses.filter { it.category == selectedCategoryFilter }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberNavyDark),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Academy Header
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberNavySurface),
                border = BorderStroke(1.dp, CyberBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "CyberShield Academy",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyberTextPrimary
                            )
                        )
                        StatusBadge(text = "10 Courses", type = BadgeType.INFO)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Practical, scenario-based modules designed for South African conditions. Learn to detect scams, secure credentials, and protect business data.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = CyberTextSecondary,
                            lineHeight = 20.sp
                        )
                    )
                }
            }
        }

        // Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategoryFilter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategoryFilter = category },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = CyberNavyCard,
                            labelColor = if (isSelected) CyberCyan else CyberTextSecondary,
                            selectedContainerColor = CyberNavyElevated,
                            selectedLabelColor = CyberCyan
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) CyberCyan else CyberBorder
                        )
                    )
                }
            }
        }

        // Expanded Course & Lessons view if a course is selected
        if (selectedCourse != null) {
            val current = selectedCourse!!
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CyberNavyElevated),
                    border = BorderStroke(1.dp, CyberCyan),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Selected Course Lessons",
                                style = MaterialTheme.typography.labelMedium.copy(color = CyberTeal)
                            )
                            IconButton(onClick = { viewModel.selectCourse(current) }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = CyberTextSecondary
                                )
                            }
                        }
                        Text(
                            text = current.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyberTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = current.description,
                            style = MaterialTheme.typography.bodySmall.copy(color = CyberTextSecondary)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Take Quiz CTA button
                        Button(
                            onClick = { viewModel.startQuiz(current.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("start_course_quiz_button")
                        ) {
                            Icon(Icons.Default.Quiz, contentDescription = null, tint = CyberNavyDark)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Take Course Quiz (80% to Pass)",
                                color = CyberNavyDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Lessons in selected course
            if (courseLessons.isNotEmpty()) {
                item {
                    Text(
                        text = "Lessons in this course:",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = CyberTextPrimary
                        )
                    )
                }

                items(courseLessons) { lesson ->
                    val isDone = completedLessonIds.contains(lesson.id)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.openLesson(selectedCourse!!.id, lesson.id) },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = CyberNavyCard),
                        border = BorderStroke(1.dp, if (isDone) CyberGreen.copy(alpha = 0.5f) else CyberBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.PlayCircle,
                                    contentDescription = null,
                                    tint = if (isDone) CyberGreen else CyberCyan,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = lesson.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = CyberTextPrimary
                                        )
                                    )
                                    Text(
                                        text = lesson.summary,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = CyberTextSecondary,
                                            fontSize = 11.sp
                                        ),
                                        maxLines = 1
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = CyberTextMuted
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Course Catalog",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = CyberTextPrimary
                )
            )
        }

        // List of all courses
        items(filteredCourses) { course ->
            CourseCard(
                course = course,
                onClick = { viewModel.selectCourse(course) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun CourseCard(
    course: Course,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("course_card_${course.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CyberNavyCard),
        border = BorderStroke(1.dp, CyberBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(CyberNavyElevated, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getCourseIcon(course.iconName),
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = course.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyberTextPrimary
                            )
                        )
                        Text(
                            text = course.category,
                            style = MaterialTheme.typography.labelSmall.copy(color = CyberTeal)
                        )
                    }
                }

                StatusBadge(
                    text = course.difficulty,
                    type = when (course.difficulty) {
                        "Beginner" -> BadgeType.SUCCESS
                        "Intermediate" -> BadgeType.WARNING
                        else -> BadgeType.ERROR
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = course.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = CyberTextSecondary,
                    lineHeight = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = CyberTextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${course.estimatedMinutes} mins",
                        style = MaterialTheme.typography.labelSmall.copy(color = CyberTextMuted)
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = CyberTextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${course.lessonsCount} lessons",
                        style = MaterialTheme.typography.labelSmall.copy(color = CyberTextMuted)
                    )
                }

                Text(
                    text = "View Lessons & Quiz →",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = CyberCyan
                    )
                )
            }
        }
    }
}

fun getCourseIcon(iconName: String): ImageVector {
    return when (iconName) {
        "email" -> Icons.Default.Email
        "chat" -> Icons.Default.Chat
        "shopping_cart" -> Icons.Default.ShoppingCart
        "lock" -> Icons.Default.Lock
        "security" -> Icons.Default.Security
        "psychology" -> Icons.Default.Psychology
        "language" -> Icons.Default.Language
        "badge" -> Icons.Default.Badge
        "business" -> Icons.Default.Business
        "gavel" -> Icons.Default.Gavel
        else -> Icons.Default.School
    }
}
