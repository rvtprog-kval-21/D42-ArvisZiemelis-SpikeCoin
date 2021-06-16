package com.example.spikecoin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import android.os.Bundle;

public class send extends AppCompatActivity {
    TextView Amount2;
    EditText WalletID;
    FirebaseAuth auth2;
    AlertDialog.Builder reset_alert2;
    LayoutInflater inflater2;
    FirebaseAuth fAuth2;
    FirebaseFirestore fStore2;
    String userID2;
    Button send2;
    public static String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        auth2 = FirebaseAuth.getInstance();
        Button logout = findViewById(R.id.LogoutBtn);
        Amount2 = findViewById(R.id.Amount);
        WalletID = findViewById(R.id.WalletID);
        send2 = findViewById(R.id.send);
        fAuth2 = FirebaseAuth.getInstance();
        fStore2 = FirebaseFirestore.getInstance();




        reset_alert2 = new AlertDialog.Builder(this);
        inflater2 = this.getLayoutInflater();

        //Kad visi lauki tiek aizpildīti un tiek nospiesta poga "send"
        send2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID2 = fAuth2.getCurrentUser().getUid();
                String WalletIDD  = WalletID.getText().toString(); // nolasa maciņa lauku
                String Amountt  = Amount2.getText().toString(); // nolasa sūtāmās summas lauku
                int bal222 = Integer.parseInt(Amountt); // summas lauks tiek pārvērsts Inta lai to varētu tālāk izmantot
                int WalletIDD22 = Integer.parseInt(WalletIDD); // maciņa lauks tiek pārvērsts Inta lai to varētu tālāk izmantot
                String WalletIDD222 = String.valueOf(WalletIDD22);
                fStore2.collection("users").document(userID2) // atksiata monētas no lietotāja konta (sūtītāja)
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {

                            int bal2 = Integer.parseInt(documentSnapshot.getString("bal"));
                            int bal3 = bal2 - bal222;
                            String ball2 = String.valueOf(bal3);
                            if (bal2 >= bal222){
                            DocumentReference documentReference = fStore2.collection("users").document(userID2);
                            Map<String, Object> user = new HashMap<>();
                            user.put("bal", ball2);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Coins have been sent successfully");
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                }
                            });
                        }
                            else{
                                Amount2.setError("Not enough coins!"); // nepietiek līdzekļi kontā
                                return;
                            }
                        }
                    }
                });
                fStore2.collection("users").document(WalletIDD222) //pieskaita monētas otram lietotājam (Saņēmēja konts)
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {

                            int bal22 = Integer.parseInt(documentSnapshot.getString("bal"));
                            int bal32 = bal22 + bal222;
                            String ball22 = String.valueOf(bal32);

                            DocumentReference documentReference = fStore2.collection("users").document(WalletIDD222);
                            Map<String,Object> user = new HashMap<>();
                            user.put("bal",ball22);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            });
                        }
                    }
                });

            }
        });
        /*
        loggout poga beidz sesiju un izvada lietotāju uz pieslēgšanās klasi.
         */
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),login.class));
                finish(); //lai lietotajs nevaretu nospiest back pogu
            }
        });
    }

    }
