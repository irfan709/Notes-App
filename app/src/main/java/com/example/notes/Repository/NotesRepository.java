package com.example.notes.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.notes.Dao.NotesDao;
import com.example.notes.Database.NotesDatabase;
import com.example.notes.Model.NotesModel;

import java.util.List;

public class NotesRepository {
    public NotesDao notesDao;
    public LiveData<List<NotesModel>> getAllNotes;
    public LiveData<List<NotesModel>> highToLow;
    public LiveData<List<NotesModel>> lowToHigh;

    public NotesRepository(Application application) {
        NotesDatabase database = NotesDatabase.getInstance(application);
        notesDao = database.notesDao();
        getAllNotes = notesDao.getAllNotes();
        highToLow = notesDao.highToLow();
        lowToHigh = notesDao.lowToHigh();
    }

    public void insertNotes(NotesModel notesModel) {
        notesDao.insertNote(notesModel);
    }

    public void updateNotes(NotesModel notesModel) {
        notesDao.updateNote(notesModel);
    }

    public void deleteNotes(int id) {
        notesDao.deleteNote(id);
    }
}