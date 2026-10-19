package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.data.model.ScamReport
import com.example.ui.components.BadgeType
import com.example.ui.components.CyberCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.CyberShieldViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScamScreen(
    viewModel: CyberShieldViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentUser.collectAsState()
    val reports by viewModel.userScamReports.collectAsState()
    val submitResult by viewModel.scamReportSubmitResult.collectAsState()
    val isSubmitting by viewModel.isSubmittingScamReport.collectAsState()

    var activeTab by remember { mutableStateOf("FORM") } // FORM or MY_REPORTS

    // Form fields
    var selectedCategory by remember { mutableStateOf("Phishing & Email Scam") }
    var selectedChannel by remember { mutableStateOf("Email") }
    var description by remember { mutableStateOf("") }
    var incidentDate by remember { mutableStateOf("Today") }
    var simulatedScreenshotName by remember { mutableStateOf<String?>(null) }
    var formValidationError by remember { mutableStateOf<String?>(null) }

    val categories = listOf(
        "Phishing & Email Scam",
        "SMS / Banking Smishing",
        "WhatsApp Impersonation",
        "Fake Job Offer / Task Scam",
        "Online Shopping / Marketplace",
        "Fake Proof of Payment (PoP)",
        "Ransomware / Malicious File",
        "Other Suspicious Activity"
    )

    val channels = listOf("Email", "SMS", "WhatsApp", "Phone Call", "Website", "Social Media", "Physical / Other")

    var categoryExpanded by remember { mutableStateOf(false) }
    var channelExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberNavyDark),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberNavySurface),
                border = BorderStroke(1.dp, CyberBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Report Suspicious Activity",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyberTextPrimary
                            )
                        )
                        Icon(
                            imageVector = Icons.Default.ReportProblem,
                            contentDescription = null,
                            tint = CyberAmber
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Help protect the South African community by reporting emerging scams, suspicious messages, or fake websites.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = CyberTextSecondary,
                            lineHeight = 20.sp
                        )
                    )
                }
            }
        }

        // Tab Selector (Report Form vs My Reports History)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CyberNavySurface, RoundedCornerShape(10.dp))
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (activeTab == "FORM") CyberNavyElevated else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { activeTab = "FORM" }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Submit Report",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (activeTab == "FORM") CyberCyan else CyberTextSecondary
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (activeTab == "MY_REPORTS") CyberNavyElevated else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { activeTab = "MY_REPORTS" }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "My Reports",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = if (activeTab == "MY_REPORTS") CyberCyan else CyberTextSecondary
                            )
                        )
                        if (reports.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(6.dp))
                            StatusBadge(text = "${reports.size}", type = BadgeType.INFO)
                        }
                    }
                }
            }
        }

        if (activeTab == "FORM") {
            // Mandatory Security Warning Banner
            item {
                Surface(
                    color = CyberRed.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, CyberRed.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = CyberRed,
                            modifier = Modifier
                                .size(22.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "CRITICAL PRIVACY WARNING",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CyberRed
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Do not include passwords, banking PINs, card security codes (CVV), identity document numbers, or other unnecessary sensitive information in your report.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = CyberTextPrimary,
                                    lineHeight = 18.sp,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }

            // Success Card if submitted
            if (submitResult != null && submitResult!!.isSuccess) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CyberGreen.copy(alpha = 0.15f)),
                        border = BorderStroke(1.dp, CyberGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = CyberGreen,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Incident Report Submitted Successfully!",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CyberGreen
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Your report has been securely logged with status 'Submitted'. An analyst at Ntara Enterprises will review the threat indicators.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = CyberTextPrimary,
                                    lineHeight = 18.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    viewModel.clearScamReportResult()
                                    description = ""
                                    simulatedScreenshotName = null
                                    activeTab = "MY_REPORTS"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CyberGreen)
                            ) {
                                Text("View in My Reports", color = CyberNavyDark, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Form Validation Error
            if (formValidationError != null) {
                item {
                    Text(
                        text = formValidationError ?: "",
                        color = CyberRed,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }

            // Category Picker
            item {
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Scam Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = CyberBorder,
                            focusedTextColor = CyberTextPrimary,
                            unfocusedTextColor = CyberTextPrimary
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("scam_category_picker")
                    )

                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false },
                        modifier = Modifier.background(CyberNavySurface)
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat, color = CyberTextPrimary) },
                                onClick = {
                                    selectedCategory = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Channel Picker
            item {
                ExposedDropdownMenuBox(
                    expanded = channelExpanded,
                    onExpandedChange = { channelExpanded = !channelExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedChannel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Where did this activity occur? (Channel)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = channelExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = CyberBorder,
                            focusedTextColor = CyberTextPrimary,
                            unfocusedTextColor = CyberTextPrimary
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("scam_channel_picker")
                    )

                    ExposedDropdownMenu(
                        expanded = channelExpanded,
                        onDismissRequest = { channelExpanded = false },
                        modifier = Modifier.background(CyberNavySurface)
                    ) {
                        channels.forEach { ch ->
                            DropdownMenuItem(
                                text = { Text(ch, color = CyberTextPrimary) },
                                onClick = {
                                    selectedChannel = ch
                                    channelExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Approximate Date
            item {
                OutlinedTextField(
                    value = incidentDate,
                    onValueChange = { incidentDate = it },
                    label = { Text("Approximate Date / Time") },
                    placeholder = { Text("e.g. Yesterday morning, Oct 18") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("scam_date_input")
                )
            }

            // Description
            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description of Suspicious Activity") },
                    placeholder = {
                        Text("Describe what happened, the sender address/number, link details, or what they demanded. (Do NOT include your passwords or PINs).")
                    },
                    minLines = 4,
                    maxLines = 8,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("scam_description_input")
                )
                Text(
                    text = "${description.length} characters (min 15 recommended)",
                    style = MaterialTheme.typography.labelSmall.copy(color = CyberTextMuted),
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                )
            }

            // Optional Screenshot Attachment
            item {
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = CyberNavyCard),
                    border = BorderStroke(1.dp, CyberBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AttachFile,
                                contentDescription = null,
                                tint = CyberTeal
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (simulatedScreenshotName != null) simulatedScreenshotName!! else "Attach Screenshot (Optional)",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = CyberTextPrimary
                                    )
                                )
                                Text(
                                    text = "PNG, JPG up to 5MB • Stored in private local vault",
                                    style = MaterialTheme.typography.labelSmall.copy(color = CyberTextMuted)
                                )
                            }
                        }

                        if (simulatedScreenshotName == null) {
                            OutlinedButton(
                                onClick = {
                                    simulatedScreenshotName = "scam_evidence_${System.currentTimeMillis() % 10000}.jpg"
                                },
                                border = BorderStroke(1.dp, CyberCyan),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                                modifier = Modifier.testTag("attach_screenshot_button")
                            ) {
                                Text("Select")
                            }
                        } else {
                            IconButton(onClick = { simulatedScreenshotName = null }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove",
                                    tint = CyberRed
                                )
                            }
                        }
                    }
                }
            }

            // Submit Button
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {
                        if (user == null) {
                            viewModel.openAuthDialog("LOGIN")
                            return@Button
                        }
                        if (description.trim().length < 15) {
                            formValidationError = "Please provide at least 15 characters describing what occurred."
                            return@Button
                        }
                        formValidationError = null
                        viewModel.submitScamReport(
                            category = selectedCategory,
                            channel = selectedChannel,
                            description = description,
                            incidentDate = incidentDate,
                            screenshotPath = simulatedScreenshotName
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = RoundedCornerShape(10.dp),
                    enabled = !isSubmitting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_scam_report_button")
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            color = CyberNavyDark,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Icon(Icons.Default.Send, contentDescription = null, tint = CyberNavyDark)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (user != null) "Submit Confidential Report" else "Sign In & Submit Report",
                            color = CyberNavyDark,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            // MY REPORTS TAB
            if (user == null) {
                item {
                    CyberCard(backgroundColor = CyberNavySurface) {
                        Text(
                            text = "Authentication Required",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyberTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Sign in to view the status and history of your submitted incident reports.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = CyberTextSecondary)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.openAuthDialog("LOGIN") },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
                        ) {
                            Text("Sign In", color = CyberNavyDark, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else if (reports.isEmpty()) {
                item {
                    CyberCard(backgroundColor = CyberNavySurface) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inbox,
                                contentDescription = null,
                                tint = CyberTextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No Scam Reports Submitted Yet",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CyberTextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "When you encounter suspicious messages or fraudulent sites, submit a report to alert our analyst team.",
                                style = MaterialTheme.typography.bodySmall.copy(color = CyberTextSecondary)
                            )
                        }
                    }
                }
            } else {
                items(reports) { report ->
                    ScamReportHistoryCard(report = report)
                }
            }
        }

        // Official South African Reporting Channels (Mandatory Guidance)
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CyberNavyCard),
                border = BorderStroke(1.dp, CyberBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Official South African Emergency Reporting Channels",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = CyberTeal
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "CyberShield SA is an educational awareness service, not an official law-enforcement agency. If you have suffered active financial loss, please contact:",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = CyberTextSecondary,
                            lineHeight = 18.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "• Your Bank Fraud Department: Call the phone number on the back of your card immediately.\n" +
                                "• SAPS Cybercrime Reporting: Dial 10111 or visit your nearest SAPS station.\n" +
                                "• SABRIC (SA Banking Risk Information Centre): sabric.co.za\n" +
                                "• Information Regulator (POPIA Breaches): inforegulator.org.za",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = CyberTextPrimary,
                            lineHeight = 20.sp,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ScamReportHistoryCard(report: ScamReport) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CyberNavyCard),
        border = BorderStroke(1.dp, CyberBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = report.category,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = CyberTextPrimary
                    )
                )
                StatusBadge(
                    text = report.status,
                    type = when (report.status) {
                        "Resolved" -> BadgeType.SUCCESS
                        "Under Review" -> BadgeType.WARNING
                        else -> BadgeType.INFO
                    }
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Via: ${report.channel}",
                    style = MaterialTheme.typography.labelSmall.copy(color = CyberTeal)
                )
                Text(
                    text = "Date: ${report.incidentDate}",
                    style = MaterialTheme.typography.labelSmall.copy(color = CyberTextMuted)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = report.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = CyberTextSecondary,
                    lineHeight = 18.sp
                )
            )
            if (report.screenshotPath != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Attachment,
                        contentDescription = null,
                        tint = CyberCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Attachment: ${report.screenshotPath}",
                        style = MaterialTheme.typography.labelSmall.copy(color = CyberCyan)
                    )
                }
            }
        }
    }
}
