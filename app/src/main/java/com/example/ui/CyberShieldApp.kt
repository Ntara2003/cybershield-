package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AuthDialog
import com.example.ui.components.CyberShieldTopBar
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.CyberShieldViewModel
import com.example.ui.viewmodel.Screen

data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val screen: Screen
)

@Composable
fun CyberShieldApp(
    viewModel: CyberShieldViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val isAuthDialogOpen by viewModel.isAuthDialogOpen.collectAsState()

    // Handle back button on sub-screens
    BackHandler {
        viewModel.handleBack()
    }

    val navItems = listOf(
        BottomNavItem(
            route = "home",
            label = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            screen = Screen.Home
        ),
        BottomNavItem(
            route = "learn",
            label = "Academy",
            selectedIcon = Icons.Filled.School,
            unselectedIcon = Icons.Outlined.School,
            screen = Screen.Learn
        ),
        BottomNavItem(
            route = "checklist",
            label = "Checklist",
            selectedIcon = Icons.Filled.FactCheck,
            unselectedIcon = Icons.Outlined.FactCheck,
            screen = Screen.Checklist
        ),
        BottomNavItem(
            route = "report",
            label = "Report Scam",
            selectedIcon = Icons.Filled.ReportProblem,
            unselectedIcon = Icons.Outlined.ReportProblem,
            screen = Screen.ReportScam
        ),
        BottomNavItem(
            route = "profile",
            label = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            screen = Screen.Profile
        )
    )

    val activeNavIndex = when (currentScreen) {
        is Screen.Home -> 0
        is Screen.Learn, is Screen.LessonDetail, is Screen.Quiz -> 1
        is Screen.Checklist -> 2
        is Screen.ReportScam -> 3
        is Screen.Profile -> 4
    }

    val topBarTitle = when (currentScreen) {
        is Screen.Home -> "CyberShield SA"
        is Screen.Learn -> "CyberShield Academy"
        is Screen.LessonDetail -> "Lesson Guide"
        is Screen.Quiz -> "Knowledge Assessment"
        is Screen.Checklist -> "SME Security Checklist"
        is Screen.ReportScam -> "Scam Incident Report"
        is Screen.Profile -> "Profile & Security Readiness"
    }

    Scaffold(
        topBar = {
            // When inside Quiz or LessonDetail, those screens provide their own top bar with back navigation
            if (currentScreen !is Screen.LessonDetail && currentScreen !is Screen.Quiz) {
                CyberShieldTopBar(
                    title = topBarTitle,
                    subtitle = "Ntara Enterprises (Pty) Ltd",
                    userFullName = currentUser?.fullName,
                    onUserClick = {
                        if (currentUser != null) {
                            viewModel.navigateTo(Screen.Profile)
                        } else {
                            viewModel.openAuthDialog("LOGIN")
                        }
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = CyberNavySurface,
                contentColor = CyberCyan,
                tonalElevation = 4.dp,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_bottom_nav_bar")
            ) {
                navItems.forEachIndexed { index, item ->
                    val isSelected = activeNavIndex == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.navigateTo(item.screen) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label,
                                tint = if (isSelected) CyberCyan else CyberTextSecondary
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) CyberCyan else CyberTextSecondary
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CyberCyan,
                            selectedTextColor = CyberCyan,
                            unselectedIconColor = CyberTextSecondary,
                            unselectedTextColor = CyberTextSecondary,
                            indicatorColor = CyberNavyElevated
                        ),
                        modifier = Modifier.testTag("nav_item_${item.route}")
                    )
                }
            }
        },
        containerColor = CyberNavyDark
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CyberNavyDark)
        ) {
            when (val screen = currentScreen) {
                is Screen.Home -> HomeScreen(viewModel = viewModel)
                is Screen.Learn -> LearnScreen(viewModel = viewModel)
                is Screen.LessonDetail -> LessonDetailScreen(
                    courseId = screen.courseId,
                    lessonId = screen.lessonId,
                    viewModel = viewModel,
                    onBack = { viewModel.handleBack() }
                )
                is Screen.Quiz -> QuizScreen(
                    courseId = screen.courseId,
                    viewModel = viewModel,
                    onBack = { viewModel.handleBack() }
                )
                is Screen.Checklist -> ChecklistScreen(viewModel = viewModel)
                is Screen.ReportScam -> ReportScamScreen(viewModel = viewModel)
                is Screen.Profile -> ProfileScreen(viewModel = viewModel)
            }
        }
    }

    if (isAuthDialogOpen) {
        AuthDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.closeAuthDialog() }
        )
    }
}
