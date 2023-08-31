package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notes.Model.NotesModel;
import com.example.notes.ViewModel.NotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class AddNote extends AppCompatActivity {
    EditText add_title, add_note;
    ImageView lowPriority, medPriority, highPriority;
    FloatingActionButton add_fab;
    String title, note;
    String priority;
    NotesViewModel notesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        add_title = findViewById(R.id.add_title);
        add_note = findViewById(R.id.add_note);
        add_fab = findViewById(R.id.add_fab);
        lowPriority = findViewById(R.id.lowPriority);
        medPriority = findViewById(R.id.medPriority);
        highPriority = findViewById(R.id.highPriority);

        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        lowPriority.setOnClickListener(v -> setPriority("1"));
        medPriority.setOnClickListener(v -> setPriority("2"));
        highPriority.setOnClickListener(v -> setPriority("3"));

        add_fab.setOnClickListener(v -> createNote());
    }

    private void setPriority(String priorityValue) {
        priority = priorityValue;
        lowPriority.setImageResource(priority.equals("1") ? R.drawable.done : 0);
        medPriority.setImageResource(priority.equals("2") ? R.drawable.done : 0);
        highPriority.setImageResource(priority.equals("3") ? R.drawable.done : 0);
    }

    //    private void createNote() {
//        title = add_title.getText().toString();
//        note = add_note.getText().toString();
//
//        if (!title.isEmpty() && !note.isEmpty() && priority != null) {
//            Date date = new Date();
//            CharSequence sequence = DateFormat.format("d MMMM yyyy", date.getTime());
//
//            NotesModel notesModel = new NotesModel();
//            notesModel.title = title;
//            notesModel.note = note;
//            notesModel.date = sequence.toString();
//            notesModel.priority = priority;
//
//            notesViewModel.insertNote(notesModel);
//
//            Toast.makeText(this, "Note saved!!", Toast.LENGTH_SHORT).show();
//            finish();
//        } else {
//            Toast.makeText(this, "Please fill in all the fields and select a priority", Toast.LENGTH_SHORT).show();
//        }
//    }
    private void createNote() {
        title = add_title.getText().toString();
        note = add_note.getText().toString();

        if (!title.isEmpty() && !note.isEmpty() && priority != null) {
            // Create a new NotesModel instance to store the note data
            NotesModel notesModel = new NotesModel();

            // Set the title, note content, and priority
            notesModel.title = title;
            notesModel.note = note;
            notesModel.priority = priority;

            // Get the current date and format it as a string
            Date date = new Date();
            CharSequence sequence = DateFormat.format("d MMMM yyyy", date.getTime());
            notesModel.date = sequence.toString();

            // Insert the note into the database using the ViewModel
            notesViewModel.insertNote(notesModel);

            // Show a toast message indicating successful note creation
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();

            // Finish the activity to return to the previous screen
            finish();
        } else {
            // Show a toast message if any of the required fields are empty
            Toast.makeText(this, "Please fill in all the fields and select a priority", Toast.LENGTH_SHORT).show();
        }
    }
}