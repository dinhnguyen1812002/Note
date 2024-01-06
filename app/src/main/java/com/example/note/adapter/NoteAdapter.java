package com.example.note.adapter;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> notes ;
    private NoteListener noteListener;
    private List<Note> notesResource;
    private Timer timer;

    public NoteAdapter(List<Note> notes, NoteListener noteListener) {
        this.notes = notes;
        this.noteListener = noteListener;
        notesResource = notes;
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

    public void searchNote(final String keyword){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (keyword.trim().isEmpty()){
                    notes= notesResource;
                }else {
                    ArrayList<Note> temp= new ArrayList<>();
                    for (Note note: notesResource) {
                        if(note.getTitle().toLowerCase().contains(keyword.toLowerCase())
                        || note.getSubtitle().toLowerCase().contains(keyword.toLowerCase())
                        || note.getNoteText().toLowerCase().contains(keyword.toLowerCase())){
                            temp.add(note);
                        }
                    }
                    notes = temp;
                }
                new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
            }
        },500);
    }
    public void cancelTimer(){
        if(timer != null){
            timer.cancel();
        }
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
