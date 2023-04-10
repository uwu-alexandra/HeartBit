package com.example.proiectingineriaprogramarii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    TextInputEditText editTextCnp,editTextPassword;
    Button loginBtn;
    // FireBaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    /*
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
             Intent intent=new Intent(getApplicationContext(), MainActivity.class);
             startActivity(intent);
             finish();
        }
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextCnp=findViewById(R.id.cnp);
        editTextPassword=findViewById(R.id.password);
       loginBtn=findViewById(R.id.loginBtn);
        // mAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
        textView=findViewById(R.id.registerNow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
                finish();
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String cnp,password;
                cnp=editTextCnp.getText().toString();
                password=editTextPassword.getText().toString();

                if(TextUtils.isEmpty(cnp))
                {
                    Toast.makeText(Login.this, "Enter cnp", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*
                mAuth.signInWithEmailAndPassword(cnp, password)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information

                    Toast.makeText(EmailPasswordActivity.this, "Authentication successful.",
                            Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(Login.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
                */
            }
        });
    }
}