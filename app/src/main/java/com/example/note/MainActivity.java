package com.example.note;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.example.note.adapter.NoteAdapter;
import com.example.note.entity.Note;
import com.example.note.listener.NoteListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteListener {

    List<Note> noteList ;
    NoteAdapter noteAdapter;
    RecyclerView noteRecyclerView;

private int noteClickedPosition= -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteList =  new ArrayList<>();
        noteAdapter = new NoteAdapter(noteList, this);

       ImageView imageView = findViewById((R.id.addNoteMain));
        imageView.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
            //startActivity(intent);
            createNoteLauncher.launch(intent); // test

        });

        noteRecyclerView = findViewById(R.id.listNote);
        noteRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );
        noteRecyclerView.setAdapter(noteAdapter);



    }
    private final ActivityResultLauncher<Intent> createNoteLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Note newNote = extractNoteData(result.getData());
                    noteList.add(newNote);
                    noteAdapter.notifyDataSetChanged();
                }
            }
    );
    private Note extractNoteData(Intent data) {
        Note newNote = new Note();
        newNote.setTitle(data.getStringExtra("note_title"));
        newNote.setSubtitle(data.getStringExtra("note_subtitle"));
        newNote.setNoteText(data.getStringExtra("note_text"));
        newNote.setDatetime(data.getStringExtra("note_datetime"));
        newNote.setColor(data.getStringExtra("note_color"));
        newNote.setImagePath(data.getStringExtra("note_image_path"));
        newNote.setWebLink(data.getStringExtra("note_url"));
        return newNote;

    }

    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note_title", note.getTitle());
        intent.putExtra("note_subtitle", note.getSubtitle());
        intent.putExtra("note_text", note.getNoteText());
        intent.putExtra("note_datetime", note.getDatetime());
        intent.putExtra("note_color", note.getColor());
        intent.putExtra("note_image_path", note.getImagePath());
        intent.putExtra("note_url", note.getWebLink());
        startActivity(intent);
    }


}