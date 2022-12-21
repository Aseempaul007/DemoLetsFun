package com.example.demoletsfun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoletsfun.models.UserRecordModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class AuthActivity extends AppCompatActivity {

    EditText name_ui;
    EditText email_ui;
    EditText password_ui;
    TextView registerToLogin;
    Button register;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        name_ui = findViewById(R.id.edit_name);
        email_ui = findViewById(R.id.edit_email);
        password_ui = findViewById(R.id.edit_password);
        registerToLogin = findViewById(R.id.register_to_login);
        register = findViewById(R.id.button_register);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(AuthActivity.this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuthentication(name_ui.getText().toString(),
                        email_ui.getText().toString(),
                        password_ui.getText().toString().trim());
            }
        });

        registerToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });

    }

    public void firebaseAuthentication(String name,String email,String password){
        dialog.show();
        auth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        dialog.cancel();
                        firestore.collection(email)
                                .document(FirebaseAuth.getInstance().getUid())
                                .set(new UserRecordModel(name,
                                        email,
                                        password));
                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.cancel();
                    }
                });

    }


    public void navigateToLogin(){
        Intent i = new Intent(AuthActivity.this,LoginActivity.class);
        startActivity(i);
    }


}