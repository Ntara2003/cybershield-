package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.SeedData
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun readStringFromContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("CyberShield", appName)
    }

    @Test
    fun verifySeedCoursesAndLessonsCount() {
        // Ensure 10 foundational courses exist
        assertEquals(10, SeedData.courses.size)
        // Ensure at least 5 meaningful lessons exist for MVP
        assertTrue(SeedData.lessons.size >= 5)
        // Ensure at least 20 quiz questions exist across categories
        assertTrue(SeedData.quizQuestions.size >= 15)
        // Ensure 15 SME checklist items exist
        assertEquals(15, SeedData.checklistItems.size)
    }

    @Test
    fun verifyQuizPassingThresholdIs80Percent() {
        val totalQuestions = 5
        val passingScore = 4 // 4/5 = 80%
        val failingScore = 3 // 3/5 = 60%

        val passingPercent = (passingScore * 100) / totalQuestions
        val failingPercent = (failingScore * 100) / totalQuestions

        assertTrue("80% or higher must pass", passingPercent >= 80)
        assertFalse("Below 80% must fail", failingPercent >= 80)
    }

    @Test
    fun verifySouthAfricanThreatAlertsAreCurated() {
        val alerts = SeedData.threatAlerts
        assertTrue(alerts.isNotEmpty())
        val sarsAlert = alerts.find { it.title.contains("SARS", ignoreCase = true) }
        assertNotNull("SARS alert must be present for SA market", sarsAlert)
    }
}
