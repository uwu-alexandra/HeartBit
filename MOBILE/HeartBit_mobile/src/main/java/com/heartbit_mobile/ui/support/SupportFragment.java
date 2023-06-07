package com.heartbit_mobile.ui.support;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.heartbit_mobile.R;

public class SupportFragment extends Fragment {
    private SupportViewModel supportViewModel;
    private LinearLayout ghidLayout;
    private LinearLayout solicitareLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportViewModel = new ViewModelProvider(this).get(SupportViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        ghidLayout = view.findViewById(R.id.ghidLayout);
        solicitareLayout = view.findViewById(R.id.solicitareLayout);
        ghidLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLayoutGhidClick();
            }
        });

        solicitareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLayoutSolicitareClick();
            }
        });
        return view;
    }

    private void onLayoutGhidClick() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_custom_ghid, null);
        builder.setView(dialogView);

        Button cancelButton = dialogView.findViewById(R.id.returnFromGhidBtn);
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

    private void onLayoutSolicitareClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_custom_solicitare, null);
        builder.setView(dialogView);

        Button cancelButton = dialogView.findViewById(R.id.returnFromSolicitareBtn);
        Button confirmButton = dialogView.findViewById(R.id.sendSolicitareBtn);
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
                EditText detaliiTxt;
                Spinner motivSpinner;
                detaliiTxt=dialogView.findViewById(R.id.Detalii);
                motivSpinner=dialogView.findViewById(R.id.motiv_spinner);
                String detalii,motiv;
                detalii=detaliiTxt.getText().toString();
                motiv=motivSpinner.getSelectedItem().toString();
                if (TextUtils.isEmpty(detalii))
                {
                    Toast.makeText(getContext(), "Introduceţi detalii", Toast.LENGTH_SHORT).show();
                    detaliiTxt.setError("Detalii necompletate");
                    detaliiTxt.requestFocus();
                    return;
                }
                Solicitare solicitare = new Solicitare(motiv, detalii);
                FirebaseDatabase.getInstance().getReference("path/to/Solicitari")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .push()
                        .setValue(solicitare)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Handle successful completion of the data insertion, if needed
                                Toast.makeText(getContext(), "Solicitare trimisă cu succes", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle the failure case
                                Toast.makeText(getContext(), "Eroare la trimiterea solicitării", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Eroare la trimiterea solicitarii", e);
                            }
                        });
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
