package com.example.twister_pm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final int RQSTCODE = 14593;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("Apple", "Current user: " + currentUser);
    }
    public void login(View view){
        EditText emailView = findViewById(R.id.mainEmailEditText);
        EditText passwordView = findViewById(R.id.mainPasswordEditText);
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        if ("".equals(email)){
            emailView.setError("No email");
            return;
        }
        if ("".equals(password)){
            passwordView.setError("No password");
            return;
        }



        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Apple", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent userData = new Intent(MainActivity.this, MessageActivity.class);
                            userData.putExtra(MessageActivity.EMAIL, email);
                            //data.putExtra(SecondActivity.PASSWORD, password);

                            startActivity(userData);

                            TextView messageView = findViewById(R.id.messageView);
                            messageView.setText("Welcome " + user);
                            //Intent til at sende brugeren videre til næste side
                        } else {
                            Log.w("Apple", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getBaseContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            TextView messageView = findViewById(R.id.messageView);
                            messageView.setText("Sorry ..." + task.getException().getMessage());
                            // ...
                        }

                        // ...
                    }
                });

    }
    public void register(View view) {
        EditText emailView = findViewById(R.id.mainEmailEditText);
        EditText passwordView = findViewById(R.id.mainPasswordEditText);
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        if ("".equals(email)){
            emailView.setError("No email");
            return;
        }
        if ("".equals(password)){
            passwordView.setError("No password");
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Apple", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            TextView messageView = findViewById(R.id.messageView);
                            messageView.setText("Bruger er nu oprettet " + user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Apple", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getBaseContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            TextView messageView = findViewById(R.id.messageView);
                            messageView.setText("Sorry ..." + task.getException().getMessage());
                        }

                        // ...
                    }
                });
    };


}