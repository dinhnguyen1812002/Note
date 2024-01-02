package com.example.note.Utility;

import android.content.Context;
import android.widget.Toast;

public class Utility {
    static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
//    static CollectionReference getCollectionReferenceForNotes(){
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        return FirebaseFirestore.getInstance().collection("notes")
//                .document(currentUser.getUid()).collection("my_notes");
//    }

}
