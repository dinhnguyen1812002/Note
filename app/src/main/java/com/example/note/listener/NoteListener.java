package com.example.note.listener;

import com.example.note.entity.Note;

public interface NoteListener  {
    void onNoteClicked(Note note, int position);
}
