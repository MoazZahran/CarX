package com.example.carx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverLoginActivity extends AppCompatActivity {

    private Button btnLogin , btnSignUp;
    private EditText editTextEmail, editTextPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        mAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
                if (User != null){
                    Intent intent = new Intent(DriverLoginActivity.this, DriverMapsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        btnLogin = findViewById(R.id.Driver_Login_Login_btn);
        btnSignUp = findViewById(R.id.Driver_Login_SignUp_btn);
        editTextEmail = findViewById(R.id.Driver_Login_Email);
        editTextPassword = findViewById(R.id.Driver_Login_Password);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userEmail = editTextEmail.getText().toString();
                final String userPassword = editTextPassword.getText().toString();

                mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(DriverLoginActivity.this, "Sign Up Error", Toast.LENGTH_LONG).show();
                        } else {
                            String driverId = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_Driver_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverId);
                            current_Driver_db.setValue(true);
                        }
                    }
                });
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userEmail = editTextEmail.getText().toString();
                final String userPassword = editTextPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(DriverLoginActivity.this, "Sign In Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}
