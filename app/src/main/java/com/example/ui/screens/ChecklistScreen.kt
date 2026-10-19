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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChecklistItem
import com.example.ui.components.BadgeType
import com.example.ui.components.CyberCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.CyberShieldViewModel

@Composable
fun ChecklistScreen(
    viewModel: CyberShieldViewModel,
    modifier: Modifier = Modifier
) {
    val items by viewModel.checklistItems.collectAsState()
    val completedIds by viewModel.completedChecklistItemIds.collectAsState()
    val stats by viewModel.dashboardStats.collectAsState()

    var selectedCategory by remember { mutableStateOf("All") }
    var selectedItemForDetail by remember { mutableStateOf<ChecklistItem?>(null) }

    val categories = remember(items) {
        listOf("All") + items.map { it.category }.distinct()
    }

    val filteredItems = remember(items, selectedCategory) {
        if (selectedCategory == "All") items
        else items.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberNavyDark),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // SME Assessment Header
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
                            text = "SME Security Checklist",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyberTextPrimary
                            )
                        )
                        StatusBadge(
                            text = "${stats.checklistCompletionPercent}% Completed",
                            type = if (stats.checklistCompletionPercent >= 80) BadgeType.SUCCESS else BadgeType.INFO
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Practical baseline security controls for South African small businesses, freelancers, and startups.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = CyberTextSecondary,
                            lineHeight = 20.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Progress Ring / Bar
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${completedIds.size} of ${items.size} controls verified",
                                style = MaterialTheme.typography.labelMedium.copy(color = CyberTeal)
                            )
                            Text(
                                text = "${stats.checklistCompletionPercent}%",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CyberCyan
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        val progress = if (items.isNotEmpty()) completedIds.size.toFloat() / items.size.toFloat() else 0f
                        LinearProgressIndicator(
                            progress = { progress },
                            color = CyberCyan,
                            trackColor = CyberNavyElevated,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }
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
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
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

        // Checklist Items List
        items(filteredItems) { item ->
            val isChecked = completedIds.contains(item.id)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedItemForDetail = item }
                    .testTag("checklist_card_${item.id}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isChecked) CyberNavyElevated else CyberNavyCard
                ),
                border = BorderStroke(
                    1.dp,
                    if (isChecked) CyberGreen.copy(alpha = 0.5f) else CyberBorder
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {
                            viewModel.toggleChecklistItem(item.id, isChecked)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = CyberGreen,
                            checkmarkColor = CyberNavyDark
                        ),
                        modifier = Modifier.testTag("checklist_checkbox_${item.id}")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isChecked) CyberTextPrimary else CyberTextPrimary
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.category,
                            style = MaterialTheme.typography.labelSmall.copy(color = CyberTeal)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CyberTextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            ),
                            maxLines = 2
                        )
                    }
                    IconButton(onClick = { selectedItemForDetail = item }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Details",
                            tint = CyberTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Legal & Educational Disclaimer Card (Mandatory Requirement)
        item {
            CyberCard(
                borderColor = CyberAmber.copy(alpha = 0.4f),
                backgroundColor = CyberNavySurface
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Gavel,
                        contentDescription = null,
                        tint = CyberAmber,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Educational Self-Assessment Notice",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyberAmber
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "This checklist is an educational self-assessment tool designed to assist South African organizations in identifying digital hygiene priorities. It does not constitute a formal cybersecurity audit, guarantee of security, or legal certification of compliance with POPIA or other regulatory standards.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CyberTextSecondary,
                                lineHeight = 18.sp,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // Item Detail Bottom Sheet / Dialog
    if (selectedItemForDetail != null) {
        val item = selectedItemForDetail!!
        val isChecked = completedIds.contains(item.id)

        AlertDialog(
            onDismissRequest = { selectedItemForDetail = null },
            title = {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = CyberTextPrimary
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatusBadge(text = item.category, type = BadgeType.INFO)
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodyMedium.copy(color = CyberTextSecondary)
                    )
                    Surface(
                        color = CyberNavyElevated,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Recommended Action:",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CyberCyan
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.recommendedAction,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = CyberTextPrimary,
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.toggleChecklistItem(item.id, isChecked)
                        selectedItemForDetail = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isChecked) CyberAmber else CyberGreen
                    )
                ) {
                    Text(
                        text = if (isChecked) "Mark Incomplete" else "Mark Complete ✓",
                        color = CyberNavyDark,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedItemForDetail = null }) {
                    Text("Close", color = CyberTextSecondary)
                }
            },
            containerColor = CyberNavySurface
        )
    }
}
