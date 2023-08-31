package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.Model.NotesModel;
import com.example.notes.ViewModel.NotesViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.Objects;

public class updateNote extends AppCompatActivity {
    EditText up_title, up_note;
    ImageView up_low, up_med, up_high;
    FloatingActionButton up_fab;
    String ititle, ipriority, inote;
    int iid;
    String priority = "1";
    NotesViewModel notesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);
        up_title = findViewById(R.id.up_title);
        up_note = findViewById(R.id.up_note);
        up_low = findViewById(R.id.up_low);
        up_med = findViewById(R.id.up_med);
        up_high = findViewById(R.id.up_high);
        up_fab = findViewById(R.id.up_fab);

////        Get note data from intent
//        iid = getIntent().getIntExtra("id", 0);
//        inote = getIntent().getStringExtra("note");
//        ipriority = getIntent().getStringExtra("priority");

        // Getting note data from intent
        iid = getIntent().getIntExtra("id", 0);
        ititle = getIntent().getStringExtra("title"); // Add this line
        inote = getIntent().getStringExtra("note");
        ipriority = getIntent().getStringExtra("priority");


//        Setting priority of selected note from intent
        switch (Objects.requireNonNull(ipriority)) {
            case "1":
                up_low.setImageResource(R.drawable.done);
                break;
            case "2":
                up_med.setImageResource(R.drawable.done);
                break;
            case "3":
                up_high.setImageResource(R.drawable.done);
                break;
        }

//        Setting note data in updatenote activity
        up_title.setText(ititle);
        up_note.setText(inote);

        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
//        Setting priority according to selected
        up_low.setOnClickListener(v -> {
            up_low.setImageResource(R.drawable.done);
            up_med.setImageResource(0);
            up_high.setImageResource(0);
            priority = "1";
        });
        up_med.setOnClickListener(v -> {
            up_low.setImageResource(0);
            up_med.setImageResource(R.drawable.done);
            up_high.setImageResource(0);
            priority = "2";
        });
        up_high.setOnClickListener(v -> {
            up_low.setImageResource(0);
            up_med.setImageResource(0);
            up_high.setImageResource(R.drawable.done);
            priority = "3";
        });
        up_fab.setOnClickListener(v -> {
            String title = up_title.getText().toString();
            String note = up_note.getText().toString();
            UpdateNote(title, note);
        });
    }

    private void UpdateNote(String title, String note) {
        Date date = new Date();
        CharSequence sequence = DateFormat.format("d MMMM yyyy", date.getTime());
        NotesModel updatedNote = new NotesModel();
        updatedNote.id = iid;
        updatedNote.title = title;
        updatedNote.note = note;
        updatedNote.priority = priority;
        updatedNote.date = sequence.toString();
        notesViewModel.updateNote(updatedNote);
        Toast.makeText(this, "Note updated successfully!!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_sheet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(updateNote.this);
            View view = LayoutInflater.from(updateNote.this).inflate(R.layout.delete_sheet,
                    (LinearLayout) findViewById(R.id.deleteSheet));
            bottomSheetDialog.setContentView(view);
            TextView tvyes, tvno;
            tvyes = view.findViewById(R.id.tvyes);
            tvno = view.findViewById(R.id.tvno);
            tvyes.setOnClickListener(v -> {
                notesViewModel.deleteNote(iid);
                finish();
            });
            tvno.setOnClickListener(v -> bottomSheetDialog.dismiss());
            bottomSheetDialog.show();
        }
        return true;
    }
}