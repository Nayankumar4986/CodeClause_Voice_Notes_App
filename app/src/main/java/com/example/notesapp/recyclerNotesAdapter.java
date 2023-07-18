package com.example.notesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerNotesAdapter extends RecyclerView.Adapter<recyclerNotesAdapter.Viewholder> {

    Context context;
    ArrayList<Note> arrNotes = new ArrayList<>();
    DatabaseHelper databaseHelper;

    recyclerNotesAdapter(Context context, ArrayList<Note> arrNotes,DatabaseHelper databaseHelper) {
        this.context = context;
        this.arrNotes = arrNotes;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_view, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Note note = arrNotes.get(position);
        holder.txtTitle.setText(note.getTitle());
        holder.txtContent.setText(note.getContent());

        holder.llrow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

              deleteitem(position);
                return true;
            }
        });
    }

    private void deleteitem(int position) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        databaseHelper.noteDao().deleteNote(new Note(arrNotes.get(position).getId(),
                                arrNotes.get(position).getTitle(),arrNotes.get(position).getContent()));

                        ((MainActivity)context).shownotes();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    @Override
    public int getItemCount() {
        return arrNotes.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtContent;

        LinearLayout llrow;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.texttitle);
            txtContent = itemView.findViewById(R.id.textcontent);
            llrow = itemView.findViewById(R.id.llrow);

        }
    }
}
