package com.example.firebase;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Upload extends AppCompatActivity {
    private Button signout,upload,refresh;
    private ImageView imageView;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    ActivityResultLauncher<String> Gphoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        database = FirebaseDatabase.getInstance();
        storage= FirebaseStorage.getInstance();
        Gphoto = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

                imageView.setImageURI(result);
                final StorageReference reference=storage.getReference().child("image");
                reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                database.getReference().child("image").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Image Upload", Toast.LENGTH_SHORT).show();
                                    }
                                });



                            }
                        });
                    }
                });
            }
        });
        signout = findViewById(R.id.btsig);
        upload = findViewById(R.id.btup);
        refresh = findViewById(R.id.btrf);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gphoto.launch("image/*");
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"This is Simple",Snackbar.LENGTH_LONG);
                snackbar.show();
                //startActivity(new Intent(End.this,MainActivity.class));
            }
        });



    }
}