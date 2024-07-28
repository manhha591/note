package com.example.note.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.example.note.R;
import com.example.note.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private final Context context;
    private final OnItemClickListener onClick;
    private final OnItemDeleteListener onDelete;
    private List<Note> notes = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public interface OnItemDeleteListener {
        void onItemDelete(Note note);
    }

    public NoteAdapter(Context context, OnItemClickListener onClick, OnItemDeleteListener onDelete) {
        this.context = context;
        this.onClick = onClick;
        this.onDelete = onDelete;
    }
//    public void filter(String text) {
//        groupTaskList.clear();
//        if (text.isEmpty()) {
//            groupTaskList.addAll(groupTaskListFull);
//        } else {
//            text = text.toLowerCase();
//            for (String item : groupTaskListFull) {
//                if (item.toLowerCase().contains(text)) {
//                    groupTaskList.add(item);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }


    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitle;
        private final TextView txtDes;
        private final ImageView btnDelete;
        private final ConstraintLayout layoutItem;

        public NoteViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_item_title);
            txtDes = itemView.findViewById(R.id.txt_item_des);
            btnDelete = itemView.findViewById(R.id.btn_delete_note);
            layoutItem = itemView.findViewById(R.id.layout_item);
        }

        public void onBind(Note note) {
            txtDes.setText(note.getDescription());
            txtTitle.setText(note.getTitle());

            btnDelete.setOnClickListener(v -> onDelete.onItemDelete(note));
            layoutItem.setOnClickListener(v -> onClick.onItemClick(note));
        }
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        holder.onBind(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }
}
