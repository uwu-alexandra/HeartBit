package com.heartbit_mobile.ui.logare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.heartbit_mobile.MainActivity;
import com.heartbit_mobile.R;

public class Register extends AppCompatActivity {

    private TextInputEditText editTextCnp, editTextPassword, editTextNume, editTextPrenume, editTextEmail, editTextId;
    private Button registerBtn;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    public boolean isValidCNP(String cnp) {
        // Verificarea lungimii
        if (cnp.length() != 13) {
            return false;
        }

        // Verificarea formatului
        if (!cnp.matches("\\d{13}")) {
            return false;
        }

        // Verificarea anului de nastere
        int birthYear = Integer.parseInt(cnp.substring(1, 3));
        if (birthYear < 1 || birthYear > 99) {
            return false;
        }

        // Verificarea lunii de nastere
        int birthMonth = Integer.parseInt(cnp.substring(3, 5));
        if (birthMonth < 1 || birthMonth > 12) {
            return false;
        }

        // Verificarea zilei de nastere
        int birthDay = Integer.parseInt(cnp.substring(5, 7));
        if (birthDay < 1 || birthDay > 31) {
            return false;
        }

        // Verificarea cifrei de sex
        int genderDigit = Integer.parseInt(cnp.substring(7, 8));
        if (genderDigit % 2 == 0) {
            return false; // Cifra de sex trebuie să fie impară pentru bărbați
        }

        // Alte verificări suplimentare pot fi adăugate aici, cum ar fi verificarea cheii de control

        return true; // CNP-ul este valid
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextEmail = findViewById(R.id.email);
        editTextCnp = findViewById(R.id.cnp);
        editTextPassword = findViewById(R.id.password);
        editTextNume = findViewById(R.id.nume);
        editTextPrenume = findViewById(R.id.prenume);
        editTextId = findViewById(R.id.cod);
        registerBtn = findViewById(R.id.registerBtn);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String cnp, password, nume, prenume, email, id;
                cnp = editTextCnp.getText().toString();
                password = editTextPassword.getText().toString();
                nume = editTextNume.getText().toString();
                prenume = editTextPrenume.getText().toString();
                email = editTextEmail.getText().toString();
                id = editTextId.getText().toString();

                if (TextUtils.isEmpty(cnp)) {
                    Toast.makeText(Register.this, "Introduceţi cnp", Toast.LENGTH_SHORT).show();
                    editTextCnp.setError("CNP necompletat");
                    editTextCnp.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Introduceţi parola", Toast.LENGTH_SHORT).show();
                    editTextPassword.setError("Parolă necompletată");
                    editTextPassword.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(nume)) {
                    Toast.makeText(Register.this, "Introduceţi numele", Toast.LENGTH_SHORT).show();
                    editTextNume.setError("Nume necompletat");
                    editTextNume.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(prenume)) {
                    Toast.makeText(Register.this, "Introduceţi prenumele", Toast.LENGTH_SHORT).show();
                    editTextNume.setError("Prenume necompletat");
                    editTextNume.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Introduceţi email", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Email necompletat");
                    editTextEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(Register.this, "Email-ul nu este valid", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Email-ul nu este valid");
                    editTextEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(id)) {
                    Toast.makeText(Register.this, "Introduceţi codul", Toast.LENGTH_SHORT).show();
                    editTextId.setError("Id necompletat");
                    editTextId.requestFocus();
                    return;
                }

                if (!(id.startsWith("1") || id.startsWith("2") || id.startsWith("3"))) {
                    // Check if 'cod' does not start with "1", "2", or "3"
                    Toast.makeText(Register.this, "Id-ul trebuie să înceapă cu 1, 2 sau 3", Toast.LENGTH_SHORT).show();
                    editTextId.setError("Id-ul nu este valid");
                    editTextId.requestFocus();
                    return;
                }

                if (!isValidCNP(cnp)) {
                    Toast.makeText(Register.this, "CNP-ul introdus nu este valid", Toast.LENGTH_SHORT).show();
                    editTextCnp.setError("CNP invalid");
                    editTextCnp.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    //we will store the additional fields in FireBase
                                    User user = new User(email, password, nume, prenume, cnp, id);
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(Register.this, "Cont creat cu succes", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(Register.this, "Probleme cu inregistrarea", Toast.LENGTH_LONG).show();
                                                        Log.e("RegistrationError", "Error registering user: " + task.getException().getMessage());
                                                    }
                                                }
                                            });
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}