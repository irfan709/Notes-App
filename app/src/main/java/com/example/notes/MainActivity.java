package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.Adapter.NotesAdapter;
import com.example.notes.Model.NotesModel;
import com.example.notes.ViewModel.NotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton main_fab;
    TextView nofilter, hightolow, lowtohigh;
    NotesViewModel notesViewModel;
    RecyclerView recyclerView;
    NotesAdapter notesAdapter;
    List<NotesModel> filteredNoteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_fab = findViewById(R.id.main_fab);
        recyclerView = findViewById(R.id.recyclerView);
        nofilter = findViewById(R.id.nofilter);
        hightolow = findViewById(R.id.hightolow);
        lowtohigh = findViewById(R.id.lowtohigh);

//        Default unselected filter
        nofilter.setBackgroundResource(R.drawable.filter_unselected);

//        Change filter according to clicked on item
        nofilter.setOnClickListener(v -> {
            loadData(0);
            nofilter.setBackgroundResource(R.drawable.filter_selected);
            hightolow.setBackgroundResource(R.drawable.filter_unselected);
            lowtohigh.setBackgroundResource(R.drawable.filter_unselected);
        });
        hightolow.setOnClickListener(v -> {
            loadData(1);
            nofilter.setBackgroundResource(R.drawable.filter_unselected);
            hightolow.setBackgroundResource(R.drawable.filter_selected);
            lowtohigh.setBackgroundResource(R.drawable.filter_unselected);
        });
        lowtohigh.setOnClickListener(v -> {
            loadData(2);
            nofilter.setBackgroundResource(R.drawable.filter_unselected);
            hightolow.setBackgroundResource(R.drawable.filter_unselected);
            lowtohigh.setBackgroundResource(R.drawable.filter_selected);
        });

        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        notesViewModel.getAllNotes.observe(this, notesModels -> {
            filteredNoteItem = notesModels;
            setAdapter(notesModels);
        });

        main_fab.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddNote.class)));

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                // Get the position of the swiped item
//                int position = viewHolder.getBindingAdapterPosition();
//                NotesModel deletedNote = filteredNoteItem.get(position); // Retrieve the note to be deleted
//                int id = deletedNote.id;
//                notesViewModel.deleteNote(id);
//                Toast.makeText(MainActivity.this, "Note deleted...", Toast.LENGTH_SHORT).show();
//
//                // Remove the item from your data source
//                // Notify the adapter of the removed item
//            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Get the position of the swiped item
                int position = viewHolder.getBindingAdapterPosition();
                NotesModel deletedNote = filteredNoteItem.get(position); // Retrieve the note to be deleted

                // Backup the deleted note in case of undo
                final NotesModel backupNote = deletedNote; // No need to create a new instance

                // Delete the note using your ViewModel
                notesViewModel.deleteNote(deletedNote.id);

                // Show Snackbar with undo option
                Snackbar snackbar = Snackbar.make(recyclerView, "Note deleted", Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", v -> {
                    // Restore the deleted note using your ViewModel
                    notesViewModel.insertNote(backupNote);
                    Toast.makeText(MainActivity.this, "Note restored", Toast.LENGTH_SHORT).show();
                });
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                            // Snackbar was dismissed without clicking Undo, note was permanently deleted
                            // You might want to handle this scenario, e.g., logging
                            Toast.makeText(MainActivity.this, "Note deleted permanently...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                snackbar.show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void loadData(int filterType) {
        LiveData<List<NotesModel>> notesLiveData;

        // Remove all observers before observing new LiveData
        notesViewModel.getAllNotes.removeObservers(this);
        notesViewModel.highToLow.removeObservers(this);
        notesViewModel.lowToHigh.removeObservers(this);

        switch (filterType) {
            case 0:
                notesLiveData = notesViewModel.getAllNotes;
                break;
            case 1:
                notesLiveData = notesViewModel.highToLow;
                break;
            case 2:
                notesLiveData = notesViewModel.lowToHigh;
                break;
            default:
                notesLiveData = notesViewModel.getAllNotes;
                break;
        }

        notesLiveData.observe(this, notesModels -> {
            setAdapter(notesModels);
            filteredNoteItem = notesModels;
        });
    }

    public void setAdapter(List<NotesModel> notesModels) {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        notesAdapter = new NotesAdapter(MainActivity.this, notesModels);
        recyclerView.setAdapter(notesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_note, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Note...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredNote(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void filteredNote(String newText) {
        ArrayList<NotesModel> filter = new ArrayList<>();
        if (filteredNoteItem != null) {
            String searchText = newText.toLowerCase(); // Convert search text to lowercase
            for (NotesModel note : filteredNoteItem) {
                if (note.title.toLowerCase().contains(searchText) ||
                        note.note.toLowerCase().contains(searchText)) {
                    filter.add(note);
                }
            }
        }
        this.notesAdapter.searchNote(filter);
    }
}