package com.example.diary

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DiaryTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val context = instrumentation.targetContext

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    fun clearSession() {
        context.getSharedPreferences("session", Context.MODE_PRIVATE).edit().clear().commit()
    }

    @Test
    fun databaseChecksCredentialsAndFiltersSubjects() {
        val database = DatabaseHelper(context)
        try {
            assertEquals("Иванов Иван", database.login("student", "123456")?.name)
            assertNull(database.login("student", "wrong"))
            assertNull(database.login("' OR 1=1 --", "123456"))
            assertNull(database.login("", ""))
            assertEquals(9, database.getSubjects(1).size)
            assertEquals("Петрова А. В.", database.getSubjects(1).first().teacher)
            assertTrue(database.getSubjects(999).isEmpty())
        } finally {
            database.close()
        }
    }

    @Test
    fun emptyAndWrongPasswordShowErrors() {
        val activity = activityRule.launchActivity(Intent())
        instrumentation.runOnMainSync {
            activity.findViewById<Button>(R.id.loginButton).performClick()
            assertEquals(View.VISIBLE, activity.findViewById<TextView>(R.id.errorText).visibility)
            assertEquals(context.getString(R.string.fill_fields), activity.findViewById<TextView>(R.id.errorText).text.toString())
            activity.findViewById<EditText>(R.id.loginInput).setText("student")
            activity.findViewById<EditText>(R.id.passwordInput).setText("wrong")
            activity.findViewById<Button>(R.id.loginButton).performClick()
            assertEquals(context.getString(R.string.wrong_credentials), activity.findViewById<TextView>(R.id.errorText).text.toString())
        }
        assertEquals(-1, context.getSharedPreferences("session", Context.MODE_PRIVATE).getInt("student_id", -1))
    }

    @Test
    fun loginOpensDiaryAndLogoutClearsSession() {
        val activity = activityRule.launchActivity(Intent())
        val diaryMonitor = instrumentation.addMonitor(DiaryActivity::class.java.name, null, false)
        instrumentation.runOnMainSync {
            activity.findViewById<EditText>(R.id.loginInput).setText("student")
            activity.findViewById<EditText>(R.id.passwordInput).setText("123456")
            activity.findViewById<Button>(R.id.loginButton).performClick()
        }
        val diary = instrumentation.waitForMonitorWithTimeout(diaryMonitor, 5000)
        assertNotNull("Diary did not open", diary)
        instrumentation.removeMonitor(diaryMonitor)
        instrumentation.waitForIdleSync()
        val loginMonitor = instrumentation.addMonitor(MainActivity::class.java.name, null, false)
        instrumentation.runOnMainSync {
            assertEquals("Иванов Иван, 9 А", diary.findViewById<TextView>(R.id.studentText).text.toString())
            assertEquals(10, diary.findViewById<TableLayout>(R.id.subjectsTable).childCount)
            diary.findViewById<Button>(R.id.logoutButton).performClick()
        }
        val login = instrumentation.waitForMonitorWithTimeout(loginMonitor, 5000)
        assertNotNull("Login did not reopen", login)
        instrumentation.removeMonitor(loginMonitor)
        assertEquals(-1, context.getSharedPreferences("session", Context.MODE_PRIVATE).getInt("student_id", -1))
        instrumentation.runOnMainSync { login.finish() }
    }
}
