package com.example.note;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import com.example.note.entity.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText txtTitle, txtSubtitle, txtNote;
    private TextView txtDateTime, txtWebUrl;
    private LinearLayout layoutWebUrl;
    String selectNoteColor;
    private View viewSubTitleIndicator;
    private String selectImagePath;
    private ImageView imageNote ;
    private AlertDialog dialogAddUrl, dialogDelete;
    FirebaseDatabase database;
    DatabaseReference noteRef;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private Note alreadyAvailableNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        ImageView imageBack = findViewById(R.id.imageBack);
        ImageView btnDone = findViewById(R.id.imageSave);

        imageBack.setOnClickListener(v-> finish());
        txtTitle = findViewById(R.id.txtNoteTitle);
        txtSubtitle = findViewById(R.id.txtInputSubtitle);
        txtNote  = findViewById(R.id.txtInputNote);
        txtDateTime = findViewById(R.id.txtDateTime);
        txtDateTime.setText(new SimpleDateFormat(
                "EEEE, dd MMMM yyyy HH:mm a"
                , Locale.getDefault())
                .format(new Date())
        );
        imageNote = findViewById(R.id.imageNote);
        txtWebUrl = findViewById(R.id.txtWebUrl);
        layoutWebUrl = findViewById(R.id.layoutWebUrl);
        viewSubTitleIndicator= findViewById(R.id.viewSubtitleIndicator);
        btnDone.setOnClickListener(v-> saveNote());

        selectNoteColor = "#333333";
        selectImagePath= "";
        initMiscellaneous();
        setViewSubTitleIndicator();

        findViewById(R.id.imageRemoveImage).setOnClickListener(v ->{
            imageNote.setImageBitmap(null);
            imageNote.setVisibility(View.GONE);
            findViewById(R.id.imageRemoveImage).setVisibility(View.GONE);
            selectImagePath="";
        });
        findViewById(R.id.imageRemoveUrl).setOnClickListener(v->{
            txtWebUrl.setText(null);
            layoutWebUrl.setVisibility(View.GONE);

        });
        database = FirebaseDatabase.getInstance();
        noteRef = database.getReference("Note");
        if(getIntent().getBooleanExtra("isViewOrUpdate", false)){
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdate();
        }
        findViewById(R.id.imageSpeech).setOnClickListener(this::speak);

    }
    private void saveNote() {
        if (txtTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Không để trống", Toast.LENGTH_SHORT).show();
            return;
        } else if (txtSubtitle.getText().toString().trim().isEmpty()
                && txtNote.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Không để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        Note note = new Note();
        note.setTitle(txtTitle.getText().toString());
        note.setSubtitle(txtSubtitle.getText().toString());
        note.setNoteText(txtNote.getText().toString());
        note.setDatetime(txtDateTime.getText().toString());
        note.setColor(selectNoteColor);
        note.setImagePath(selectImagePath);
        if(layoutWebUrl.getVisibility()== View.VISIBLE){
            note.setWebLink(txtWebUrl.getText().toString());
        }
        String noteID;
        if (alreadyAvailableNote != null) {
            noteID = alreadyAvailableNote.getId();
        } else {
            noteID = noteRef.push().getKey();
        }
        note.setId(noteID);
        noteRef.child(noteID).setValue(note);
        finish();
    }
    private void setViewOrUpdate(){
        txtTitle.setText(alreadyAvailableNote.getTitle());
        txtSubtitle.setText(alreadyAvailableNote.getSubtitle());
        txtNote.setText(alreadyAvailableNote.getNoteText());
        txtDateTime.setText(alreadyAvailableNote.getDatetime());
        txtWebUrl.setText(alreadyAvailableNote.getWebLink());
        if(alreadyAvailableNote.getImagePath()!= null && !alreadyAvailableNote.getImagePath().trim().isEmpty()){
            imageNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath()));
            imageNote.setVisibility(View.VISIBLE);
            selectImagePath= alreadyAvailableNote.getImagePath();

        }
        if(alreadyAvailableNote.getWebLink() != null && !alreadyAvailableNote.getWebLink().trim().isEmpty()){
            txtWebUrl.setText(alreadyAvailableNote.getWebLink());
            layoutWebUrl.setVisibility(View.VISIBLE);
        }
    }

    private void initMiscellaneous(){
        LinearLayout linearLayout = findViewById(R.id.layoutMiscellaneous);
         BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        linearLayout.findViewById(R.id.txtMiscellaneous).setOnClickListener(v->{
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        final ImageView imageColor= linearLayout.findViewById(R.id.imageColor);
        final ImageView imageColor2= linearLayout.findViewById(R.id.imageColor2);
        final ImageView imageColor3= linearLayout.findViewById(R.id.imageColor3);
        final ImageView imageColor4= linearLayout.findViewById(R.id.imageColor4);
        final ImageView imageColor5= linearLayout.findViewById(R.id.imageColor5);
        linearLayout.findViewById(R.id.viewColor).setOnClickListener(v->{
            selectNoteColor = "#333333";
            imageColor.setImageResource(R.drawable.ic_done);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setViewSubTitleIndicator();
        });
        linearLayout.findViewById(R.id.viewColor2).setOnClickListener(v->{
            selectNoteColor = "#FDBE3B";
            imageColor.setImageResource(0);
            imageColor2.setImageResource(R.drawable.ic_done);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setViewSubTitleIndicator();
        });
        linearLayout.findViewById(R.id.viewColor3).setOnClickListener(v->{
            selectNoteColor = "#FF4842";
            imageColor.setImageResource(0);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(R.drawable.ic_done);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setViewSubTitleIndicator();
        });
        linearLayout.findViewById(R.id.viewColor4).setOnClickListener(v->{
            selectNoteColor = "#3A52FC";
            imageColor.setImageResource(0);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(R.drawable.ic_done);
            imageColor5.setImageResource(0);
            setViewSubTitleIndicator();
        });
        linearLayout.findViewById(R.id.viewColor5).setOnClickListener(v->{
            selectNoteColor = "#00FF00";
            imageColor.setImageResource(0);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(R.drawable.ic_done);
            setViewSubTitleIndicator();
        });
         if(alreadyAvailableNote != null
                 && alreadyAvailableNote.getColor() != null
                 && !alreadyAvailableNote.getColor().trim().isEmpty()){
             switch (alreadyAvailableNote.getColor()){
                 case "#FDBE3B" :
                     linearLayout.findViewById(R.id.viewColor2).performClick();
                     break;
                 case "#FF4842":
                     linearLayout.findViewById(R.id.viewColor3).performClick();
                     break;
                 case "#3A52FC":
                     linearLayout.findViewById(R.id.viewColor4).performClick();
                     break;
                 case "#00FF00":
                     linearLayout.findViewById(R.id.viewColor5).performClick();
                     break;
             }
         }
        linearLayout.findViewById(R.id.layoutAddImage).setOnClickListener(v->{
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if(ContextCompat.checkSelfPermission(
                    getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            )!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                        CreateNoteActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_STORAGE_PERMISSION
                );
            }
            else {
                selectImage();
            }
        });
        linearLayout.findViewById(R.id.layoutAddUrl).setOnClickListener(v->{
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            showAddUrlDialog();
        });

        linearLayout.findViewById(R.id.layoutDeleteNote).setVisibility(View.VISIBLE);
        linearLayout.findViewById(R.id.layoutDeleteNote).setOnClickListener(v->{
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            showDeleteDialog();
        });

    }
    public void showDeleteDialog() {
        if (dialogDelete == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_note,
                    findViewById(R.id.layoutDeleteNoteContainer)
            );
            builder.setView(view);
            dialogDelete = builder.create();
            if (dialogDelete.getWindow() != null) {
                dialogDelete.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            view.findViewById(R.id.btnDelete).setOnClickListener(v -> {
                if (alreadyAvailableNote != null) {
                    String noteID = alreadyAvailableNote.getId();

                    // Remove the note from Firebase
                    noteRef.child(noteID).removeValue();

                    Toast.makeText(CreateNoteActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

            view.findViewById(R.id.btnCancel).setOnClickListener(v -> {
                dialogDelete.dismiss();
            });
        }

        assert dialogDelete != null;
        dialogDelete.show();
    }

    private void setViewSubTitleIndicator(){
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubTitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectNoteColor));
    }

    private void  selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getPackageManager())!= null){
            activityResultLaunchery.launch(intent);
        }
    }

    ActivityResultLauncher<Intent> activityResultLaunchery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    if (data != null && data.getData() != null) {
                        try {
                            Uri selectedImageUri = data.getData();
                            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                            Bitmap selectedImageBitmap = BitmapFactory.decodeStream(inputStream);
                            imageNote.setImageBitmap(selectedImageBitmap);
                            imageNote.setVisibility(View.VISIBLE);
                            selectImagePath = filePath(selectedImageUri);
                            findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);
                        } catch (Exception e){
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length >0 ){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                selectImage();
            }
            else {
                Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private String filePath(Uri uriContent) {
        String filePath;
        // Cursor không phải của FireBase
        Cursor cursor = getContentResolver().query(uriContent, null, null, null, null);
        if (cursor == null) {
            filePath = uriContent.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            filePath = cursor.getString(index);
            cursor.close();
        }

        Log.d("ImagePath", "Path: " + filePath);

        return filePath;
    }

    private void showAddUrlDialog(){
        if(dialogAddUrl==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.add_url_layout,
                    (ViewGroup) findViewById(R.id.layoutAddUrlContainer)
            );
            builder.setView(view);
            dialogAddUrl = builder.create();
            if(dialogAddUrl.getWindow() !=null){
                dialogAddUrl.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            final EditText inputUrl = view.findViewById(R.id.txtInputUrl);
            inputUrl.requestFocus();
            view.findViewById(R.id.txtAddUrl).setOnClickListener(v-> {
                if(inputUrl.getText().toString().trim().isEmpty() ){
                    Toast.makeText(this, "Nhập Url", Toast.LENGTH_SHORT).show();

                } else if(!Patterns.WEB_URL.matcher(inputUrl.getText().toString()).matches()){
                    Toast.makeText(this, "Nhập đúng định dạng url", Toast.LENGTH_SHORT).show();
                }else {
                    txtWebUrl.setText(inputUrl.getText().toString());
                    layoutWebUrl.setVisibility(View.VISIBLE);
                    dialogAddUrl.dismiss();
                }
            });
            view.findViewById(R.id.txtCancel).setOnClickListener(v->{
                dialogAddUrl.dismiss();
            });
        }
        dialogAddUrl.show();
    }
    public void speak(View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Start Speaking");
        speechResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> speechResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String spokenText = text.get(0);
                    txtNote.setText(spokenText);
                }
            });

}