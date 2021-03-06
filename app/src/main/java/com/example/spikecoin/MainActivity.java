package com.example.spikecoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    TextView verifyMsg;
    TextView wallet2;
    TextView userbal;
    Button verifyEmailBtn;
    Button minebtn;
    Button sendd;
    FirebaseAuth auth;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    public static final String TAG = "TAG";
    int bal2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        minebtn = findViewById(R.id.send);
        sendd = findViewById(R.id.sendd);
        Button logout = findViewById(R.id.LogoutBtn);
        verifyMsg = findViewById(R.id.verifyEmailMsg);
        verifyEmailBtn = findViewById(R.id.verifyEmailBtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        wallet2 = (TextView)findViewById(R.id.wallet);
        userbal = (TextView)findViewById(R.id.userbal2);

        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();

        /*
        Ja lietot??js nav verific??ts tiek pasl??pti sekojo??ie objekti, kas liedz lietot??jam
        pilnv??rt??gi izmantot aplik??ciju
         */

        if(!auth.getCurrentUser().isEmailVerified()){
            verifyEmailBtn.setVisibility(View.VISIBLE);
            verifyMsg.setVisibility(View.VISIBLE);
            minebtn.setVisibility(View.GONE);
            sendd.setVisibility(View.GONE);

        }

        /*
        Ja lietot??js ir verific??ts, tad vi??am tiek sniegta pilna piek??uve vis??m darb??b??m
        K?? ar?? tiek pasl??pta verifik??cijas poga un pazi??ojums
         */
        if(auth.getCurrentUser().isEmailVerified()){
            userID = fAuth.getCurrentUser().getUid();
            fStore.collection("users").document(userID)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        wallet2.setText(documentSnapshot.getString("bal")+( " SC"));
                        userbal.setText(userID);
                    }
                }
              });
                        minebtn.setVisibility(View.VISIBLE);
                        sendd.setVisibility(View.VISIBLE);

        }

        /*
        Verifik??cijas poga autom??tiski nos??ta lietot??jam uz e-pastu linku
        p??c kura atv??r??anas lietot??js tiek verific??ts
         */
        verifyEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nosuta verifikacijas epastu
                auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                        verifyEmailBtn.setVisibility(View.GONE);    //p??c pogas nospie??anas paz??d
                        verifyMsg.setVisibility(View.GONE);         //p??c pogas nospie??anas paz??d

                    }
                });
            }
        });

        /*
        Mine poga, jeb rak??anas poga sniedz lietot??jam + 1 SC k?? ar?? at??em no kop??j?? maci??a vienu mon??tu
         */
        minebtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            userID = fAuth.getCurrentUser().getUid();
            fStore.collection("users").document(userID)         //Pieskaita pie lietot??ja maci??a +1 SC
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()) {
                        wallet2.setText(documentSnapshot.getString("bal")+( " SC"));
                        userbal.setText(userID);


                        int bal2 = Integer.parseInt(documentSnapshot.getString("bal"));
                        int bal3 = bal2 + 1;
                        String ball2 = String.valueOf(bal3);
                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        Map<String,Object> user = new HashMap<>();
                        user.put("bal",ball2);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        });
                    }
                }
            });
            fStore.collection("users").document("u6N4g4eNF8c3TpM3VHlrvRtD2iS2") // Kop??j?? maci??a mon??tas atskait????ana
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()) {

                        int bbal2 = Integer.parseInt(documentSnapshot.getString("bal"));
                        int bbal3 = bbal2 - 1;
                        String bball2 = String.valueOf(bbal3);
                        DocumentReference documentReference = fStore.collection("users").document("u6N4g4eNF8c3TpM3VHlrvRtD2iS2");
                        Map<String,Object> user = new HashMap<>();
                        user.put("bal",bball2);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }
                        });
                    }
                }
            });
        }
    });

        /*
        Nospie??ot pogu send, lietot??js tiek nos??t??ts uz send klasi, kur vi???? var t??l??k p??rsakit??t savas mon??tas citiem lietot??jiem
         */
        sendd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),send.class));
            }
        });
        /*
        loggout poga beidz sesiju un izvada lietot??ju uz piesl??g??an??s klasi.
         */
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),login.class));
                finish(); //lai lietotajs nevaretu nospiest atpaka?? pogu
            }
        });
    }


    //Izvelnes opcijas
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*Izvelnes opcij??s ir tr??s lietas
    1. update email- p??rraksta lietot??ju uz citu e-pastu (ir vajadz??gs e-pasta apstiprin??jums)
    2. Dz??st profilu- dz???? profilu no sist??mas, profila atjauno??aan nav iesp??jama
    3. Atjaunot paroli- parole tiek atjaunin??ta uz citu.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.resetUserPassword){
            startActivity(new Intent(getApplicationContext(),ResetPassword.class));
        }

        if (item.getItemId() == R.id.updateEmailMenu){
            View view = inflater.inflate(R.layout.reset_pass,null);
            reset_alert.setTitle("Update email?")
                    .setMessage("Enter Your new Email Address.")
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            EditText email = view.findViewById(R.id.rest_email_pass);
                            if(email.getText().toString().isEmpty()){
                                email.setError("Required Field");
                                return;
                            }
                            //nos??t??t linku
                            FirebaseUser user = auth.getCurrentUser();
                            user.updateEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this, "Email Updated.", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });


                        }
                    }).setNegativeButton("Cancel",null)
                    .setView(view)
                    .create().show();

        }

        if (item.getItemId() == R.id.delete_account_menu){
            reset_alert.setTitle("This will delete your Account Permanetly.")
                    .setMessage("Are You sure?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseUser user = auth.getCurrentUser();
                            user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this, "Account Deleted.", Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                    startActivity(new Intent(getApplicationContext(),login.class));
                                    finish();
                                }
                            });

                        }
                    }).setNegativeButton("Cancel", null)
                    .create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    //random wallet izveide
    private String generateString(int length){
        char[] chars ="QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for ( int i= 0; i<length;i++)
        {
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();

    }

}