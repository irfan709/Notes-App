package com.example.notes.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notes.Model.NotesModel;
import com.example.notes.Repository.NotesRepository;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {
    public NotesRepository repository;
    public LiveData<List<NotesModel>> getAllNotes;
    public LiveData<List<NotesModel>> highToLow;
    public LiveData<List<NotesModel>> lowToHigh;

    public NotesViewModel(Application application) {
        super(application);
        repository = new NotesRepository(application);
        getAllNotes = repository.getAllNotes;
        highToLow = repository.highToLow;
        lowToHigh = repository.lowToHigh;
    }

    public void insertNote(NotesModel notesModel) {
        repository.insertNotes(notesModel);
    }

    public void updateNote(NotesModel notesModel) {
        repository.updateNotes(notesModel);
    }

    public void deleteNote(int id) {
        repository.deleteNotes(id);
    }
}
