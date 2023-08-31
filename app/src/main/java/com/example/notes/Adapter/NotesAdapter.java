package com.example.notes.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.MainActivity;
import com.example.notes.Model.NotesModel;
import com.example.notes.R;
import com.example.notes.updateNote;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    MainActivity mainActivity;
    List<NotesModel> notesModels;
    List<NotesModel> filteredNotes; // List to hold filtered notes

    public NotesAdapter(MainActivity mainActivity, List<NotesModel> notesModels) {
        this.mainActivity = mainActivity;
        this.notesModels = notesModels;
        this.filteredNotes = new ArrayList<>(notesModels); // Initialize filteredNotes with all notes
    }

    public void searchNote(List<NotesModel> filteredNote) {
        this.filteredNotes = new ArrayList<>(filteredNote); // Update filteredNotes with new filtered notes
        notifyDataSetChanged(); // Notify adapter of data change
    }

    @NonNull
    @Override
    public NotesAdapter.NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NotesViewHolder holder, int position) {
        if (filteredNotes != null && !filteredNotes.isEmpty()) {
            NotesModel notesModel = filteredNotes.get(position);

            switch (notesModel.priority) {
                case "1":
                    holder.item_priority.setBackgroundResource(R.drawable.green_shape);
                    break;
                case "2":
                    holder.item_priority.setBackgroundResource(R.drawable.yellow_shape);
                    break;
                case "3":
                    holder.item_priority.setBackgroundResource(R.drawable.red_shape);
                    break;
            }
            holder.item_title.setText(notesModel.title);
            holder.item_note.setText(notesModel.note);
            holder.item_date.setText(notesModel.date);

//            Random color to each note
//            int colorCode = getRandomColor();
//            holder.cardContainer.setCardBackgroundColor(holder.itemView.getResources().getColor(colorCode, null));
//            holder.cardContainer.setCardBackgroundColor(holder.itemView.getResources().getColor(getRandomColor(), null));

            holder.cardContainer.setCardBackgroundColor(getRandomColor());

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mainActivity, updateNote.class);
                intent.putExtra("id", notesModel.id);
                intent.putExtra("title", notesModel.title);
                intent.putExtra("note", notesModel.note);
                intent.putExtra("priority", notesModel.priority);
                mainActivity.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return filteredNotes.size(); // Return the size of filteredNotes
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView item_title, item_note, item_date;
        View item_priority;
        CardView cardContainer;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            item_title = itemView.findViewById(R.id.item_title);
            item_note = itemView.findViewById(R.id.item_note);
            item_date = itemView.findViewById(R.id.item_date);
            item_priority = itemView.findViewById(R.id.item_priority);
            cardContainer = itemView.findViewById(R.id.cardContainer);
        }
    }

    //    private int getRandomColor() {
//        int[] colorArray = {
//                R.color.color1,
//                R.color.color2,
//                R.color.color3,
//                R.color.color4,
//                R.color.color5
//        };
//
//        Random random = new Random();
//        int randomColorIndex = random.nextInt(colorArray.length);
//        return colorArray[randomColorIndex];
//    }
    private int getRandomColor() {
        int[] colorArray = {
                R.color.color1,
                R.color.color2,
                R.color.color3,
                R.color.color4,
                R.color.color5
        };

        Random random = new Random();
        int randomColorIndex = random.nextInt(colorArray.length);
        int colorResource = colorArray[randomColorIndex];
        return ResourcesCompat.getColor(mainActivity.getResources(), colorResource, null);
    }
}