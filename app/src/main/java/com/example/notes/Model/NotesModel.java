package com.example.notes.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes_db")
public class NotesModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "note_title")
    public String title;

    @ColumnInfo(name = "notes")
    public String note;

    @ColumnInfo(name = "note_priority")
    public String priority;

    @ColumnInfo(name = "note_date")
    public String date;
}