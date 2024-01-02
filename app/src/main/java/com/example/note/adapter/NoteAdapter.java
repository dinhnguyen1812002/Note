package com.example.note.adapter;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.note.R;
import com.example.note.entity.Note;
import com.example.note.listener.NoteListener;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> notes ;
    private NoteListener noteListener;


    public NoteAdapter(List<Note> notes, NoteListener noteListener) {
        this.notes = notes;
        this.noteListener = noteListener;
    }

    @NonNull
    @NotNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(
                        parent.getContext())
                        .inflate(R.layout.item_container_note
                                ,parent,
                                false)
        );

    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
      holder.setNote(notes.get(position));
      holder.layoutNote.setOnClickListener(v->{
          noteListener.onNoteClicked(notes.get(position), position);
      });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle, txtSubTitle, txtDateTime;
        LinearLayout layoutNote;
        RoundedImageView imageNote;
        public NoteViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txtTitle =  itemView.findViewById(R.id.txtTitle);
            txtSubTitle = itemView.findViewById(R.id.txtSubTitle);
            txtDateTime = itemView.findViewById(R.id.txtNoteDateTime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
            imageNote= itemView.findViewById(R.id.imageNote);

        }
        void setNote(Note note){
            txtTitle.setText(note.getTitle());
            if(note.getSubtitle().trim().isEmpty()){
                txtSubTitle.setVisibility(View.GONE);
            }else {
                txtSubTitle.setText(note.getSubtitle());
            }
            txtDateTime.setText(note.getDatetime());
            GradientDrawable gradientDrawable = ( GradientDrawable) layoutNote.getBackground();
            if(note.getColor()!= null){
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            }else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }

            if (note.getImagePath() != null ){
                imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageNote.setVisibility(View.VISIBLE);
            }else {
                imageNote.setVisibility(View.GONE);
            }
        }


    }
}
