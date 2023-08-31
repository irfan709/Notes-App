package com.example.notes.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notes.Model.NotesModel;

import java.util.List;

@Dao
public interface NotesDao {
    @Insert
    void insertNote(NotesModel... notesModel);

    @Query("SELECT * FROM notes_db ORDER BY id DESC")
    LiveData<List<NotesModel>> getAllNotes();
    @Query("SELECT * FROM notes_db ORDER BY note_priority DESC, id DESC")
    LiveData<List<NotesModel>> highToLow();

    @Query("SELECT * FROM notes_db ORDER BY note_priority ASC, id DESC")
    LiveData<List<NotesModel>> lowToHigh();

    @Update
    void updateNote(NotesModel notesModel);

    @Query("DELETE FROM notes_db WHERE id = :id")
    void deleteNote(int id);
}
