package com.example.diary

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

class DiaryActivity : Activity() {
    private lateinit var database: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = DatabaseHelper(this)
        val session = getSharedPreferences("session", MODE_PRIVATE)
        val student = database.getStudent(session.getInt("student_id", -1))
        if (student == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
        setContentView(R.layout.activity_diary)
        findViewById<TextView>(R.id.studentText).text = getString(R.string.student_info, student.name, student.schoolClass)
        val table = findViewById<TableLayout>(R.id.subjectsTable)
        for ((index, subject) in database.getSubjects(student.id).withIndex()) {
            val row = TableRow(this)
            row.setBackgroundColor(if (index % 2 == 0) Color.WHITE else Color.rgb(242, 242, 242))
            row.addView(makeCell(subject.name, 1.3f))
            row.addView(makeCell(subject.grades, 0.9f))
            row.addView(makeCell(subject.teacher, 1.2f))
            table.addView(row)
        }
        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            session.edit().clear().apply()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun makeCell(value: String, weight: Float): TextView {
        val cell = TextView(this)
        cell.text = value
        cell.textSize = 14f
        cell.setTextColor(Color.BLACK)
        val padding = (6 * resources.displayMetrics.density).toInt()
        cell.setPadding(padding, padding, padding, padding)
        cell.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight)
        return cell
    }

    override fun onDestroy() {
        database.close()
        super.onDestroy()
    }
}
