package com.example.note;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.example.note.adapter.NoteAdapter;
import com.example.note.entity.Note;
import com.example.note.listener.NoteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteListener{

    List<Note> noteList ;
    private static final String TAG = "MainActivity";
    NoteAdapter noteAdapter;
    RecyclerView noteRecyclerView;
    FirebaseDatabase database;
    DatabaseReference noteRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteList =  new ArrayList<>();
        noteAdapter = new NoteAdapter(noteList, this);

       ImageView imageView = findViewById((R.id.addNoteMain));
        imageView.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
            startActivity(intent);
        });
        database = FirebaseDatabase.getInstance();
        noteRef = database.getReference("Note");
        noteRecyclerView = findViewById(R.id.listNote);
        noteRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );
        noteRecyclerView.setAdapter(noteAdapter);
        getNote();
        EditText txtSearch = findViewById(R.id.txtSearch);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                noteAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(noteList.size() != 0){
                    noteAdapter.searchNote(editable.toString());
                }
            }
        });
    }

    private void getNote(){
        noteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noteList.clear();
                for (DataSnapshot snapshot:  dataSnapshot.getChildren()){
                    Note notes = snapshot.getValue(Note.class);
                    noteList.add(notes);
                }
                displayNote();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read products from Firebase.", error.toException());
            }
        });
    }
    private void displayNote() {
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClicked(Note note, int position) {
        Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivity(intent);
    }


}