package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import com.example.ui.components.CyberCard
import com.example.ui.components.StatTile
import com.example.ui.theme.*
import com.example.ui.viewmodel.CyberShieldViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(
    viewModel: CyberShieldViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentUser.collectAsState()
    val stats by viewModel.dashboardStats.collectAsState()
    val quizAttempts by viewModel.userQuizAttempts.collectAsState()

    var showEditNameDialog by remember { mutableStateOf(false) }
    var editNameValue by remember { mutableStateOf("") }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }

    val formattedDate = remember(user?.createdAt) {
        val date = Date(user?.createdAt ?: System.currentTimeMillis())
        SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(date)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberNavyDark),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Header Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberNavySurface),
                border = BorderStroke(1.dp, CyberBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(CyberNavyElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (user != null) {
                        Text(
                            text = user!!.fullName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyberTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = user!!.email,
                            style = MaterialTheme.typography.bodyMedium.copy(color = CyberTeal)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Member since $formattedDate",
                            style = MaterialTheme.typography.labelSmall.copy(color = CyberTextMuted)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = {
                                editNameValue = user!!.fullName
                                showEditNameDialog = true
                            },
                            border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("edit_display_name_button")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Edit Display Name")
                        }
                    } else {
                        Text(
                            text = "Guest User",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyberTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "You are browsing CyberShield SA in guest mode.",
                            style = MaterialTheme.typography.bodySmall.copy(color = CyberTextSecondary)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.openAuthDialog("LOGIN") },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("profile_signin_button")
                        ) {
                            Text("Sign In or Register", color = CyberNavyDark, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Stats Dashboard
        item {
            Text(
                text = "Progress & Activity Overview",
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
                    title = "Overall Readiness",
                    value = "${stats.overallProgressPercent}%",
                    subtitle = "Academy & SME Checklist",
                    icon = Icons.Default.Security,
                    iconTint = CyberCyan,
                    modifier = Modifier.weight(1f)
                )
                StatTile(
                    title = "Courses Mastered",
                    value = "${stats.completedCoursesCount}",
                    subtitle = "Passed with ≥80%",
                    icon = Icons.Default.School,
                    iconTint = CyberGreen,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatTile(
                    title = "Lessons Completed",
                    value = "${stats.completedLessonsCount}",
                    subtitle = "Out of ${stats.totalLessonsCount} total",
                    icon = Icons.Default.MenuBook,
                    iconTint = CyberTeal,
                    modifier = Modifier.weight(1f)
                )
                StatTile(
                    title = "SME Controls Verified",
                    value = "${stats.completedChecklistCount}",
                    subtitle = "Out of ${stats.totalChecklistCount} items",
                    icon = Icons.Default.FactCheck,
                    iconTint = CyberAmber,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Quiz History
        if (quizAttempts.isNotEmpty()) {
            item {
                Text(
                    text = "Recent Quiz Attempts",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = CyberTextPrimary
                    )
                )
            }
            items(quizAttempts.take(3).size) { idx ->
                val attempt = quizAttempts[idx]
                val scorePercent = (attempt.score * 100) / (if (attempt.totalQuestions > 0) attempt.totalQuestions else 1)
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = CyberNavyCard),
                    border = BorderStroke(1.dp, CyberBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Course: ${attempt.courseId}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = CyberTextPrimary
                                )
                            )
                            Text(
                                text = "Score: ${attempt.score}/${attempt.totalQuestions} ($scorePercent%)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (attempt.passed) CyberGreen else CyberAmber
                                )
                            )
                        }
                        Icon(
                            imageVector = if (attempt.passed) Icons.Default.CheckCircle else Icons.Default.Replay,
                            contentDescription = null,
                            tint = if (attempt.passed) CyberGreen else CyberAmber
                        )
                    }
                }
            }
        }

        // Terms, Privacy & Legal
        item {
            CyberCard(backgroundColor = CyberNavySurface) {
                Text(
                    text = "Legal, Privacy & Compliance",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = CyberTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "CyberShield SA operates under Ntara Enterprises (Pty) Ltd in strict compliance with the Protection of Personal Information Act (POPIA).",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = CyberTextSecondary,
                        lineHeight = 18.sp
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { showTermsDialog = true }) {
                        Text("View POPIA Notice & Terms", color = CyberCyan)
                    }
                }
            }
        }

        // Account Actions (Logout, Delete Account)
        if (user != null) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.logout() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberAmber),
                        border = BorderStroke(1.dp, CyberAmber),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("logout_button")
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null, tint = CyberAmber)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sign Out", fontWeight = FontWeight.Bold)
                    }

                    TextButton(
                        onClick = { showDeleteAccountDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("request_account_deletion_button")
                    ) {
                        Text(
                            text = "Request Account & Personal Data Deletion",
                            color = CyberRed,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // Edit Name Dialog
    if (showEditNameDialog) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            title = { Text("Edit Display Name") },
            text = {
                OutlinedTextField(
                    value = editNameValue,
                    onValueChange = { editNameValue = it },
                    label = { Text("Full Name") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateFullName(editNameValue)
                        showEditNameDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
                ) {
                    Text("Save", color = CyberNavyDark, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditNameDialog = false }) {
                    Text("Cancel", color = CyberTextSecondary)
                }
            },
            containerColor = CyberNavySurface
        )
    }

    // Account Deletion Dialog (with POPIA Legal Explanation)
    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            title = {
                Text(
                    text = "Confirm Account & Data Deletion",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = CyberRed
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Are you sure you want to permanently delete your account?",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CyberTextPrimary
                        )
                    )
                    Text(
                        text = "Under the Protection of Personal Information Act (POPIA), all your personal identifiers, lesson completions, quiz histories, and submitted scam reports will be deleted or irreversibly anonymized.\n\n" +
                                "Note: Any scam reports previously verified and escalated to formal law enforcement (e.g. SAPS / SABRIC) will be retained solely as anonymized evidence records in accordance with South African statutory record-keeping obligations.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = CyberTextSecondary,
                            lineHeight = 18.sp
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteAccount()
                        showDeleteAccountDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberRed)
                ) {
                    Text("Permanently Delete", color = CyberTextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountDialog = false }) {
                    Text("Cancel", color = CyberTextSecondary)
                }
            },
            containerColor = CyberNavySurface
        )
    }

    // POPIA & Terms Dialog
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = { Text("CyberShield SA — Legal & Privacy Policy") },
            text = {
                LazyColumn(modifier = Modifier.heightIn(max = 340.dp)) {
                    item {
                        Text(
                            text = "1. Developer & Responsible Party\n" +
                                    "CyberShield SA is developed and operated by Ntara Enterprises (Pty) Ltd, registered in South Africa.\n\n" +
                                    "2. Purpose of Data Processing\n" +
                                    "We collect your name, email, and security progress solely to deliver educational cybersecurity training, measure comprehension, and review submitted threat indicators.\n\n" +
                                    "3. Scam Reports & Evidence Handling\n" +
                                    "User-submitted scam reports are reviewed privately by security analysts. We never publish your private name, email, or telephone number. Any threat intelligence shared publicly is strictly scrubbed of personal identifiers.\n\n" +
                                    "4. Educational Disclaimer\n" +
                                    "CyberShield SA provides awareness guidance. It does not replace formal legal advice, official SAPS reporting, or comprehensive enterprise penetration testing.\n\n" +
                                    "5. Data Subject Rights\n" +
                                    "Under POPIA, you have the right to request access to your records, object to processing, or demand erasure.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CyberTextSecondary,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text("Done", color = CyberCyan)
                }
            },
            containerColor = CyberNavySurface
        )
    }
}
