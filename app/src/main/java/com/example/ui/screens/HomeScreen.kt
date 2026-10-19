package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.data.model.ThreatAlert
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.CyberShieldViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun HomeScreen(
    viewModel: CyberShieldViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentUser.collectAsState()
    val stats by viewModel.dashboardStats.collectAsState()
    val currentTipIndex by viewModel.currentDailyTipIndex.collectAsState()
    val dailyTip = viewModel.dailyTips.getOrNull(currentTipIndex) ?: viewModel.dailyTips.first()
    val threatAlerts = viewModel.threatAlerts

    var selectedAlertForDialog by remember { mutableStateOf<ThreatAlert?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberNavyDark),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberNavySurface),
                border = BorderStroke(1.dp, CyberBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (user != null) "Welcome back, ${user?.fullName}" else "Welcome to CyberShield SA",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyberTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Ntara Enterprises (Pty) Ltd • Cybersecurity Awareness & Habits",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CyberTeal,
                                fontSize = 12.sp
                            )
                        )
                        if (user == null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Sign in to synchronize progress & submit verified incident reports.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = CyberTextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    if (user == null) {
                        Button(
                            onClick = { viewModel.openAuthDialog("LOGIN") },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("home_signin_button")
                        ) {
                            Text("Sign In", color = CyberNavyDark, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Daily Tip Card
        item {
            CyberCard(
                borderColor = CyberCyan.copy(alpha = 0.4f),
                backgroundColor = CyberNavySurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(CyberCyan.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Daily Cybersecurity Tip",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyberCyan
                            )
                        )
                    }

                    IconButton(
                        onClick = { viewModel.rotateDailyTip() },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("rotate_tip_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Next tip",
                            tint = CyberTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = dailyTip.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = CyberTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dailyTip.tip,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = CyberTextSecondary,
                        lineHeight = 20.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = CyberNavyElevated,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = CyberGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Action: ${dailyTip.practicalStep}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CyberTextPrimary,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }

        // Real Progress Stats Grid
        item {
            Text(
                text = "Your Security Readiness",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = CyberTextPrimary
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatTile(
                    title = "Overall Progress",
                    value = "${stats.overallProgressPercent}%",
                    subtitle = "Academy & Checklist",
                    icon = Icons.Default.Security,
                    iconTint = CyberCyan,
                    modifier = Modifier.weight(1f)
                )
                StatTile(
                    title = "Completed Lessons",
                    value = "${stats.completedLessonsCount} / ${stats.totalLessonsCount}",
                    subtitle = "${stats.completedCoursesCount} courses mastered",
                    icon = Icons.Default.MenuBook,
                    iconTint = CyberTeal,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatTile(
                    title = "Latest Quiz Score",
                    value = if (stats.latestQuizScore != null) "${stats.latestQuizScore}%" else "—",
                    subtitle = if (stats.latestQuizScore != null) {
                        if (stats.latestQuizScore!! >= 80) "Passed (≥80%)" else "Retake Recommended"
                    } else "No attempts yet",
                    icon = Icons.Default.Quiz,
                    iconTint = if (stats.latestQuizScore != null && stats.latestQuizScore!! >= 80) CyberGreen else CyberAmber,
                    modifier = Modifier.weight(1f)
                )
                StatTile(
                    title = "SME Checklist",
                    value = "${stats.checklistCompletionPercent}%",
                    subtitle = "${stats.completedChecklistCount}/${stats.totalChecklistCount} items checked",
                    icon = Icons.Default.FactCheck,
                    iconTint = CyberGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Quick Access Actions
        item {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = CyberTextPrimary
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionButton(
                    icon = Icons.Default.School,
                    title = "Start Academy",
                    subtitle = "Learn 10 Courses",
                    onClick = { viewModel.navigateTo(Screen.Learn) },
                    modifier = Modifier.weight(1f)
                )
                QuickActionButton(
                    icon = Icons.Default.Checklist,
                    title = "SME Checklist",
                    subtitle = "15 Security Items",
                    onClick = { viewModel.navigateTo(Screen.Checklist) },
                    modifier = Modifier.weight(1f)
                )
                QuickActionButton(
                    icon = Icons.Default.ReportProblem,
                    title = "Report Scam",
                    subtitle = "Submit Incident",
                    onClick = { viewModel.navigateTo(Screen.ReportScam) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // "Stay Alert" Live Feed
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(CyberRed)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Stay Alert — Active SA Threats",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CyberTextPrimary
                        )
                    )
                }
                StatusBadge(text = "Verified", type = BadgeType.INFO)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Curated intelligence on emerging scams targeting South African consumers and small businesses.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = CyberTextSecondary,
                    fontSize = 12.sp
                )
            )
        }

        items(threatAlerts.size) { index ->
            val alert = threatAlerts[index]
            ThreatAlertCard(
                alert = alert,
                onClick = { selectedAlertForDialog = alert }
            )
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // Detail Alert Dialog
    if (selectedAlertForDialog != null) {
        val alert = selectedAlertForDialog!!
        AlertDialog(
            onDismissRequest = { selectedAlertForDialog = null },
            title = {
                Text(
                    text = alert.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = CyberTextPrimary
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        StatusBadge(
                            text = alert.severity,
                            type = if (alert.severity == "Critical") BadgeType.ERROR else BadgeType.WARNING
                        )
                        Text(
                            text = alert.date,
                            style = MaterialTheme.typography.labelSmall.copy(color = CyberTextMuted)
                        )
                    }

                    Text(
                        text = alert.summary,
                        style = MaterialTheme.typography.bodyMedium.copy(color = CyberTextSecondary)
                    )

                    Surface(
                        color = CyberNavyElevated,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Indicators of Compromise:",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CyberCyan
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = alert.indicatorOfCompromise,
                                style = MaterialTheme.typography.bodySmall.copy(color = CyberTextPrimary)
                            )
                        }
                    }

                    Surface(
                        color = CyberGreen.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Official Defense Advice:",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CyberGreen
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = alert.officialAdvice,
                                style = MaterialTheme.typography.bodySmall.copy(color = CyberTextPrimary)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedAlertForDialog = null }) {
                    Text("Close", color = CyberCyan)
                }
            },
            containerColor = CyberNavySurface
        )
    }
}

@Composable
fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CyberNavyCard),
        border = BorderStroke(1.dp, CyberBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CyberNavyElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CyberCyan,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = CyberTextPrimary
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = CyberTextMuted,
                    fontSize = 11.sp
                )
            )
        }
    }
}

@Composable
fun ThreatAlertCard(
    alert: ThreatAlert,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CyberNavyCard),
        border = BorderStroke(
            1.dp,
            if (alert.severity == "Critical") CyberRed.copy(alpha = 0.5f) else CyberAmber.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = if (alert.severity == "Critical") CyberRed else CyberAmber,
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = alert.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = CyberTextPrimary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    StatusBadge(
                        text = alert.severity,
                        type = if (alert.severity == "Critical") BadgeType.ERROR else BadgeType.WARNING
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = alert.summary,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = CyberTextSecondary,
                        lineHeight = 18.sp
                    ),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Tap to read indicators & defense advice →",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CyberTeal,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}
