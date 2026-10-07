package com.example.diary

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Student(val id: Int, val name: String, val schoolClass: String)
data class Subject(val name: String, val grades: String, val teacher: String)

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "diary.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE students (id INTEGER PRIMARY KEY, login TEXT UNIQUE, password TEXT, name TEXT, school_class TEXT)")
        db.execSQL("CREATE TABLE subjects (id INTEGER PRIMARY KEY, student_id INTEGER, name TEXT, grades TEXT, teacher TEXT)")

        // Учебный аккаунт. Данные добавляются один раз при создании базы.
        db.execSQL("INSERT INTO students VALUES (1, 'student', '123456', 'Иванов Иван', '9 А')")
        addSubject(db, "Математика", "5, 4, 5", "Петрова А. В.")
        addSubject(db, "Русский язык", "4, 4, 5", "Смирнова Е. Н.")
        addSubject(db, "Литература", "5, 5, 4", "Смирнова Е. Н.")
        addSubject(db, "История", "4, 5, 4", "Кузнецов И. С.")
        addSubject(db, "География", "5, 4, 4", "Орлова Т. П.")
        addSubject(db, "Биология", "4, 4, 5", "Соколова М. А.")
        addSubject(db, "Физика", "3, 4, 4", "Волков Д. Н.")
        addSubject(db, "Английский язык", "5, 4, 5", "Морозова О. В.")
        addSubject(db, "Физкультура", "5, 5, 5", "Попов С. И.")
    }

    private fun addSubject(db: SQLiteDatabase, name: String, grades: String, teacher: String) {
        db.execSQL("INSERT INTO subjects (student_id, name, grades, teacher) VALUES (1, ?, ?, ?)",
            arrayOf(name, grades, teacher))
    }

    fun login(login: String, password: String): Student? {
        readableDatabase.rawQuery(
            "SELECT id, name, school_class FROM students WHERE login = ? AND password = ?",
            arrayOf(login, password)
        ).use {
            if (it.moveToFirst()) return Student(it.getInt(0), it.getString(1), it.getString(2))
        }
        return null
    }

    fun getStudent(id: Int): Student? {
        readableDatabase.rawQuery(
            "SELECT id, name, school_class FROM students WHERE id = ?", arrayOf(id.toString())
        ).use {
            if (it.moveToFirst()) return Student(it.getInt(0), it.getString(1), it.getString(2))
        }
        return null
    }

    fun getSubjects(studentId: Int): List<Subject> {
        val subjects = mutableListOf<Subject>()
        readableDatabase.rawQuery(
            "SELECT name, grades, teacher FROM subjects WHERE student_id = ? ORDER BY id",
            arrayOf(studentId.toString())
        ).use {
            while (it.moveToNext()) subjects.add(Subject(it.getString(0), it.getString(1), it.getString(2)))
        }
        return subjects
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Для первой версии обновление базы не требуется.
    }
}
