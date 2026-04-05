// NotesAdapter.java
package com.aniket.mynotes.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aniket.mynotes.model.Note;
import com.aniket.mynotes.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    List<Note> notes;
    //  1: Added a listener field — this holds the click callback from MainActivity
    OnNoteClickListener listener;

    //  2: Interface definition — one method that fires when a card is tapped
    // MainActivity implements this and decides what to do (start DetailActivity)
    public interface OnNoteClickListener { void onNoteClick(Note note);}

    //  3: Constructor now accepts the listener alongside the notes list
    public NotesAdapter(List<Note> notes, OnNoteClickListener listener) {
        this.notes = notes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.title.setText(note.title);
        holder.content.setText(note.content);
        holder.date.setText(note.date);

        //  4: When the card is tapped, fire the listener with this note
        // The Adapter doesn't know what happens next — that's MainActivity's job
        holder.itemView.setOnClickListener(v -> listener.onNoteClick(note));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    // add this method inside NotesAdapter class
    public void updateNotes(List<Note> newNotes) {
        this.notes = newNotes;
        // tells RecyclerView the data d so it redraws the list
        notifyDataSetChanged();
    }
    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, content, date;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.dateofnote);
        }
    }
}