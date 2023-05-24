package com.heartbit_mobile.ui.settings;

import static com.heartbit_mobile.ui.home.HomeFragment.REQUEST_BLUETOOTH_PERMISSION;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.heartbit_mobile.R;
import com.heartbit_mobile.ui.logare.Login;

public class SettingsFragment extends Fragment {

    private LinearLayout schimbareParolaLayout;
    private LinearLayout deconectareContLayout;
    private LinearLayout stergereContLayout;
    private LinearLayout permisiuneBluetoothLayout;
    private LinearLayout cadruLegalLayout;

    private TextInputEditText parolaVecheTxt,parolaNouaTxt,parolaNouaComfirmareTxt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        schimbareParolaLayout = view.findViewById(R.id.schimbareParolaLayout);
        deconectareContLayout = view.findViewById(R.id.deconectareContLayout);
        stergereContLayout = view.findViewById(R.id.stergereContLayout);
        permisiuneBluetoothLayout = view.findViewById(R.id.permisiuneBluetoothLayout);
        cadruLegalLayout = view.findViewById(R.id.cadruLegalLayout);

        schimbareParolaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickParolaLayout();
            }
        });
        deconectareContLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDeconectareContLayout();
            }
        });

        stergereContLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickStergereContLayout();
            }
        });
        permisiuneBluetoothLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBluetoothLayout();
            }
        });

        cadruLegalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCandruLegalLayout();
            }
        });

        return view;
    }

    private void onClickParolaLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_custom_schimbare_parola, null);
        builder.setView(dialogView);

        Button cancelButton = dialogView.findViewById(R.id.returnFromSchimbareParola);
        Button confirmButton = dialogView.findViewById(R.id.salvareNouaParola);
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
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = auth.getCurrentUser();
                parolaVecheTxt=dialogView.findViewById(R.id.parolaActuala);
                parolaNouaTxt=dialogView.findViewById(R.id.parolaNoua);
                parolaNouaComfirmareTxt=dialogView.findViewById(R.id.comfirmareParolaNoua);

                String oldPassword = parolaVecheTxt.getText().toString();
                String newPassword = parolaNouaTxt.getText().toString();
                String newPasswordVerificare = parolaNouaComfirmareTxt.getText().toString();

                if (TextUtils.isEmpty(oldPassword)) {
                    Toast.makeText(getContext(), "Introduceţi parola veche", Toast.LENGTH_SHORT).show();
                    parolaVecheTxt.setError("Parola veche necompetată");
                    parolaVecheTxt.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(newPassword)) {
                    Toast.makeText(getContext(), "Introduceţi parola noua", Toast.LENGTH_SHORT).show();
                    parolaNouaTxt.setError("Parola nouă necompletată");
                    parolaNouaTxt.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(newPasswordVerificare)) {
                    Toast.makeText(getContext(), "Introduceţi cnp", Toast.LENGTH_SHORT).show();
                    parolaNouaComfirmareTxt.setError("Comfirmarea parolei necompletată");
                    parolaNouaComfirmareTxt.requestFocus();
                    return;
                }

                if (newPassword.equals(newPasswordVerificare)){
                    AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), oldPassword);
                    currentUser.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Actualizeaza parola utilizatorului
                                        currentUser.updatePassword(newPassword)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getActivity(), "Parola a fost actualizata cu succes!", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(getActivity(), "Actualizarea parolei a esuat! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                        dialog.dismiss();
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(getActivity(), "Parola veche introdusa este incorecta!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else
                    Toast.makeText(getActivity(), "Parola noua si confirmarea difera! Va rugam reintroduceti parola noua", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void onClickDeconectareContLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.logout_message)
                .setTitle(R.string.logout_title)
                .setPositiveButton(R.string.logout_positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // deconectați utilizatorul aici
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.logout_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // închideți dialogul fără a face nimic
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onClickStergereContLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.stergere_message)
                .setTitle(R.string.stergere_title)
                .setPositiveButton(R.string.stergere_positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // stergeti utilizatorul aici
                    }
                })
                .setNegativeButton(R.string.stergere_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // închideți dialogul fără a face nimic
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onClickBluetoothLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_custom_bluetooth, null);
        builder.setView(dialogView);

        Button cancelButton = dialogView.findViewById(R.id.bluetooth_dialog_cancel_button);
        Button confirmButton = dialogView.findViewById(R.id.bluetooth_dialog_confirm_button);
        AlertDialog dialog = builder.create();
// Setarea listenerilor de click pentru butoane
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cod pentru confirmarea acțiunii
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                    // If we don't have the necessary permissions, request them
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH_CONNECT},
                            REQUEST_BLUETOOTH_PERMISSION);
                } else
                    Toast.makeText(getContext(), "Permisiunile sunt deja acordate", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }

        });
        dialog.show();
    }

    private void onClickCandruLegalLayout() {


        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.hide(this);
        transaction.commit();

        FragmentTransaction transaction2 = getParentFragmentManager().beginTransaction();
        transaction2.add(R.id.frame_layout, new CadruLegalFragment());
        transaction2.commit();
    }
}
