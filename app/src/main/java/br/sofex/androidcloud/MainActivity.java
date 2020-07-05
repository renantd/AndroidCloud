package br.sofex.androidcloud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    TextView TxtDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TxtDisplay = findViewById(R.id.TvDisplay);
        db = FirebaseFirestore.getInstance();

        //addRealTimeUpdate();
        ReadContact();
    }

    private void addRealTimeUpdate() {

         DocumentReference contactListen = db.collection("AddressBook").document("1");
         contactListen.addSnapshotListener(new EventListener<DocumentSnapshot>() {
             @Override
             public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                 if( e != null){
                    Log.e("Error", e.getMessage());
                    return;
                 }
                 if(documentSnapshot != null && documentSnapshot.exists()){
                     Toast.makeText(MainActivity.this, "Current  data : "+documentSnapshot.getData(), Toast.LENGTH_SHORT).show();
                 }
             }
         });

    }


    public void deleteData(){
        db.collection("AddressBook").document("1")
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Deleted !!!", Toast.LENGTH_SHORT).show();
                    }
                })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Error:  " + e, Toast.LENGTH_SHORT).show();
                }
            });

    }

    public void UpdateData(){
        DocumentReference contact  = db.collection("AddressBook").document("1");
        contact.update("name","Eddy123").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Updated !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void readSingleContactCustomObject(){
        DocumentReference contact  = db.collection("AddressBook").document("1");
        contact.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                AddressBook readContat = documentSnapshot.toObject(AddressBook.class);
                StringBuilder data = new StringBuilder("");

                data.append("Name: ").append(readContat.getName());
                data.append("\nEmail: ").append(readContat.getEmail());
                data.append("\nPhone: ").append(readContat.getPhone());
                TxtDisplay.setText(data.toString());
            }
        });
    }

    private void ReadContact() {
        DocumentReference contact  = db.collection("AddressBook").document("1");
        contact.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    StringBuilder data = new StringBuilder("");

                    data.append("Name: ").append(doc.getString("name"));
                    data.append("\nEmail: ").append(doc.getString("email"));
                    data.append("\nPhone: ").append(doc.getString("phone"));
                    TxtDisplay.setText(data.toString());

                }
            }
        });

    }

    public void addNewContact(){
        Map<String,Object> newContact = new HashMap<>();
        newContact.put("name","EddyON");
        newContact.put("email","eddydn@gmail.com");
        newContact.put("phone","(031) 98498-0430");

        db.collection("AddressBook").document("1")
                .set(newContact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Added new contact", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Facial Map - msg", "Error : " + e);
                    }
                });
    }

}