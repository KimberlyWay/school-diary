package com.example.diary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : Activity() {
    private lateinit var database: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = DatabaseHelper(this)
        val studentId = getSharedPreferences("session", MODE_PRIVATE).getInt("student_id", -1)
        if (database.getStudent(studentId) != null) {
            openDiary()
            return
        }
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.loginButton).setOnClickListener { signIn() }
        findViewById<EditText>(R.id.passwordInput).setOnEditorActionListener { _, action, _ ->
            if (action == EditorInfo.IME_ACTION_DONE) {
                signIn()
                true
            } else {
                false
            }
        }
    }

    private fun signIn() {
        val login = findViewById<EditText>(R.id.loginInput).text.toString().trim()
        val password = findViewById<EditText>(R.id.passwordInput).text.toString()
        val error = findViewById<TextView>(R.id.errorText)
        if (login.isEmpty() || password.isEmpty()) {
            error.setText(R.string.fill_fields)
            error.visibility = View.VISIBLE
            return
        }
        val student = database.login(login, password)
        if (student == null) {
            error.setText(R.string.wrong_credentials)
            error.visibility = View.VISIBLE
        } else {
            getSharedPreferences("session", MODE_PRIVATE).edit().putInt("student_id", student.id).apply()
            openDiary()
        }
    }

    private fun openDiary() {
        startActivity(Intent(this, DiaryActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        database.close()
        super.onDestroy()
    }
}
