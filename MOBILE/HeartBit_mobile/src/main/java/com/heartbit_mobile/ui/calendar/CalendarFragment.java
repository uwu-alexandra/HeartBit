package com.heartbit_mobile.ui.calendar;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.heartbit_mobile.ui.logare.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {
    private CalendarViewModel calendarViewModel;
    private LinearLayout programareLayout;
    private LinearLayout recomandariLayout;

    private List<Programare> listaProgramariTrecute = new ArrayList<>();
    private List<Programare> listaProgramariViitoare = new ArrayList<>();
    private Button pastBtn;
    private Button futureBtn;
    private LinearLayout programariLayout;
    private int backgroundColor = Color.parseColor("#E0E0E0");
    private int strokeColor = Color.BLACK;
    private int strokeWidth = 20;

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
        programariLayout = view.findViewById(R.id.layoutProgramari);
        loadListe();
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
                TextInputEditText dataTxt, medicTxt;
                Spinner locatiaSpinner;
                dataTxt = dialogView.findViewById(R.id.Data);
                locatiaSpinner = dialogView.findViewById(R.id.locatie_spinner);
                medicTxt = dialogView.findViewById(R.id.Medic);
                String data, locatia, medic;
                data = dataTxt.getText().toString();
                locatia = locatiaSpinner.getSelectedItem().toString();
                medic = medicTxt.getText().toString();

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
                            programare = new Programare(data, locatia, nume, prenume);
                        } else {
                            programare = new Programare(data, locatia, medic, nume, prenume);
                        }

                        FirebaseDatabase.getInstance().getReference("path/to/Programari")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(programare)
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
                        Date currentDate = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        String dateString = dateFormat.format(currentDate);
                        if (programare.getData().compareTo(dateString) < 0) {
                            listaProgramariTrecute.add(programare);
                        } else {
                            listaProgramariViitoare.add(programare);
                        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_custom_recomandari, null);
        builder.setView(dialogView);

        Button cancelButton = dialogView.findViewById(R.id.returnFromRecomandariBtn);

        // Fetch recommendations from Firebase and add them dynamically to the layout
        String currentUserUUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference recomandariRef = FirebaseDatabase.getInstance().getReference("path/to/Recomandari/" + currentUserUUID);
        recomandariRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LinearLayout recomandariContainer = dialogView.findViewById(R.id.containerRecomandari);
                recomandariContainer.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String data_recomandare = snapshot.child("date").getValue(String.class);
                    String text_recomandare = snapshot.child("text").getValue(String.class);
                    Recomandare recomandare = new Recomandare(data_recomandare, text_recomandare);

                    int color = ContextCompat.getColor(getContext(), R.color.grey);
                    recomandariContainer.setBackgroundColor(color);
                    recomandariContainer.setPadding(10, 10, 10, 10);

                    TextView textView1 = new TextView(dialogView.getContext());
                    textView1.setText(recomandare.getData_recomandare());
                    textView1.setTextSize(16);

                    TextView textView2 = new TextView(dialogView.getContext());
                    textView2.setText(recomandare.getText());
                    textView2.setTextSize(16);

                    // Create a View for the black line
                    View lineView1 = new View(dialogView.getContext());
                    View lineView2 = new View(dialogView.getContext());
                    lineView1.setBackgroundColor(Color.BLACK);
                    lineView2.setBackgroundColor(Color.BLACK);

                    // Set the height and width of the line
                    int lineHeight = 1; // Adjust the line height as needed
                    LinearLayout.LayoutParams lineLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            lineHeight
                    );
                    lineView1.setLayoutParams(lineLayoutParams);
                    lineView2.setLayoutParams(lineLayoutParams);

                    recomandariContainer.addView(lineView1);
                    recomandariContainer.addView(textView1);
                    recomandariContainer.addView(textView2);
                    recomandariContainer.addView(lineView2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                Toast.makeText(getContext(), "Error retrieving recommendations", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        // Setarea listenerilor de click pentru butoane
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cod pentru anularea acțiunii
                dialog.dismiss();
            }
        });

        dialog.show();
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
        programariLayout.removeAllViews();
        sortList(listaProgramariTrecute);
        for (Programare p : listaProgramariTrecute) {
            TextView textView = new TextView(getContext());
            textView.setText(p.toString());

            ShapeDrawable shapeDrawable = new ShapeDrawable();
            shapeDrawable.getPaint().setColor(backgroundColor);
            shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
            shapeDrawable.getPaint().setStrokeJoin(Paint.Join.ROUND);
            shapeDrawable.getPaint().setStrokeCap(Paint.Cap.ROUND);
            shapeDrawable.setIntrinsicWidth(100);
            shapeDrawable.setIntrinsicHeight(50);
            shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);
            shapeDrawable.getPaint().setColor(strokeColor);
            shapeDrawable.getPaint().setStrokeWidth(strokeWidth);

            shapeDrawable.setPadding(10, 10, 10, 10);
            shapeDrawable.setShape(new RectShape());

            textView.setBackground(shapeDrawable);
            textView.setBackgroundColor(backgroundColor);
            programariLayout.addView(textView);
        }
    }

    private void afisareDateViitoare() {
        programariLayout.removeAllViews();
        sortList(listaProgramariViitoare);
        for (Programare p : listaProgramariViitoare) {
            TextView textView = new TextView(getContext());
            textView.setText(p.toString());

            ShapeDrawable shapeDrawable = new ShapeDrawable();
            shapeDrawable.getPaint().setColor(backgroundColor);
            shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
            shapeDrawable.getPaint().setStrokeJoin(Paint.Join.ROUND);
            shapeDrawable.getPaint().setStrokeCap(Paint.Cap.ROUND);
            shapeDrawable.setIntrinsicWidth(100);
            shapeDrawable.setIntrinsicHeight(50);
            shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);
            shapeDrawable.getPaint().setColor(strokeColor);
            shapeDrawable.getPaint().setStrokeWidth(strokeWidth);

            shapeDrawable.setPadding(10, 10, 10, 10);
            shapeDrawable.setShape(new RectShape());

            textView.setBackground(shapeDrawable);
            textView.setBackgroundColor(backgroundColor);
            programariLayout.addView(textView);
        }
    }

    private void loadListe() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDateStr = dateFormat.format(new Date());
        Date currentDateTemp;
        try {
            currentDateTemp = dateFormat.parse(currentDateStr);
        } catch (ParseException e) {
            // Handle the exception if the parsing fails
            currentDateTemp = null;
        }
        final Date currentDate = currentDateTemp;
        String userUUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("path/to/Programari/" + userUUID)

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Retrieve the data from the snapshot and do something with it
                            Programare programare = snapshot.getValue(Programare.class);
                            Date data;

                            try {
                                data = dateFormat.parse(programare.getData());
                            } catch (ParseException e) {
                                data = null;
                                throw new RuntimeException(e);
                            }
                            if (data.compareTo(currentDate) < 0) {
                                listaProgramariTrecute.add(programare);
                                Log.d("DATE", "Adding to listaProgramariTrecute: " + programare);
                            } else {
                                listaProgramariViitoare.add(programare);
                                Log.d("DATE", "Adding to listaProgramariTrecute: " + programare);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error
                        Toast.makeText(getActivity(), "Error retrieving data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sortList(List<Programare> lista) {
        Collections.sort(lista, new Comparator<Programare>() {
            @Override
            public int compare(Programare p1, Programare p2) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                try {
                    Date date1 = dateFormat.parse(p1.getData());
                    Date date2 = dateFormat.parse(p2.getData());
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    // Handle the exception if the parsing fails
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }
}