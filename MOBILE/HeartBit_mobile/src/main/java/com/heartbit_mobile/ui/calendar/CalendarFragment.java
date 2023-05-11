package com.heartbit_mobile.ui.calendar;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.heartbit_mobile.R;
import com.heartbit_mobile.ui.logare.Register;
import com.heartbit_mobile.ui.logare.User;

import java.text.ParseException;


public class CalendarFragment extends Fragment {
    private CalendarViewModel calendarViewModel;
    private LinearLayout programareLayout;
    private LinearLayout recomandariLayout;


    private Button pastBtn;
    private Button futureBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        programareLayout = view.findViewById(R.id.programareLayout);
        recomandariLayout = view.findViewById(R.id.recomandariLayout);
        pastBtn = view.findViewById(R.id.pastBtn);
        futureBtn = view.findViewById(R.id.futureBtn);
        programareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLayoutProgramareClick();
            }
        });

        recomandariLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLayoutRecomandariClick();
            }
        });

        pastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPastBtnClick();
            }
        });

        futureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFutureBtnClick();
            }
        });
        return view;
    }

    private void onLayoutProgramareClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_custom_programare, null);
        builder.setView(dialogView);

        Button cancelButton = dialogView.findViewById(R.id.returnFromProgramareBtn);
        Button confirmButton = dialogView.findViewById(R.id.sendCerereBtn);
        AlertDialog dialog = builder.create();
        // Setarea listenerilor de click pentru butoane
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cod pentru anularea acțiunii
                dialog.dismiss();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cod pentru confirmarea acțiunii
                TextInputEditText specialitateTxt, dataTxt, medicTxt;
                Spinner locatiaSpinner;
                specialitateTxt = dialogView.findViewById(R.id.Specialitate);
                dataTxt = dialogView.findViewById(R.id.Data);
                locatiaSpinner = dialogView.findViewById(R.id.locatie_spinner);
                medicTxt = dialogView.findViewById(R.id.Medic);

                String specialitatea, data, locatia, medic;
                specialitatea = specialitateTxt.getText().toString();
                data = dataTxt.getText().toString();
                locatia = locatiaSpinner.getSelectedItem().toString();
                medic = medicTxt.getText().toString();

                if (TextUtils.isEmpty(specialitatea)) {
                    Toast.makeText(getContext(), "Introduceţi specialitatea", Toast.LENGTH_SHORT).show();
                    specialitateTxt.setError("Specialitatea necompletata");
                    specialitateTxt.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(data)) {
                    Toast.makeText(getContext(), "Introduceţi data", Toast.LENGTH_SHORT).show();
                    dataTxt.setError("Data necompletata");
                    dataTxt.requestFocus();
                    return;
                }

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = currentUser.getUid();
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                ValueEventListener userListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Extrageți datele din dataSnapshot și le asignați la un obiect User
                        Programare programare = null;
                        User user = dataSnapshot.getValue(User.class);
                        String nume = user.getNume();
                        String prenume = user.getPrenume();

                        if (TextUtils.isEmpty(medic)) {
                            programare = new Programare(specialitatea, data, locatia, nume, prenume);
                        } else {
                            programare = new Programare(specialitatea, data, locatia, medic, nume, prenume);
                        }

                        FirebaseDatabase.getInstance().getReference("path/to/Programari")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(programare)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Dacă programarea a fost salvată cu succes, afișați un mesaj și închideți dialogul
                                            Toast.makeText(getContext(), "Programarea a fost înregistrată cu succes!", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        } else {
                                            // În caz contrar, afișați un mesaj de eroare
                                            Toast.makeText(getContext(), "Eroare la salvarea programării! Încercați din nou mai târziu.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // În cazul în care apariție o eroare, afișați un mesaj corespunzător
                                        Toast.makeText(getContext(), "Eroare la salvarea programării! Încercați din nou mai târziu.", Toast.LENGTH_SHORT).show();
                                        Log.w(TAG, "Eroare la salvarea programării", e);
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // În cazul în care apariție o eroare, afișați un mesaj corespunzător
                        Toast.makeText(getContext(), "Eroare la obținerea datelor utilizatorului! Încercați din nou mai târziu.", Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "Eroare la obținerea datelor utilizatorului", databaseError.toException());
                    }
                };

// atașați listenerul la referința bazei de date
                databaseRef.addListenerForSingleValueEvent(userListener);

                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void onLayoutRecomandariClick() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.hide(this);
        transaction.commit();

        FragmentTransaction transaction2 = getParentFragmentManager().beginTransaction();
        transaction2.add(R.id.frame_layout, new RecomandariFragment());
        transaction2.commit();
    }

    private void onPastBtnClick() {
        pastBtn.setClickable(false);
        pastBtn.setBackgroundColor(Color.WHITE);
        futureBtn.setClickable(true);
        futureBtn.setBackgroundColor(Color.GRAY);
        afisareDateIstoric();
    }

    private void onFutureBtnClick() {
        pastBtn.setClickable(true);
        pastBtn.setBackgroundColor(Color.GRAY);
        futureBtn.setClickable(false);
        futureBtn.setBackgroundColor(Color.WHITE);
        afisareDateViitoare();
    }

    private void afisareDateIstoric() {

    }

    private void afisareDateViitoare() {

    }
}