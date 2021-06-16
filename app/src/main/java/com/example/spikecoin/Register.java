package com.example.spikecoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText registerFullName,registerEmail,registerPassword,registerConfPass;
    Button registerUserBtn,gotoLogin;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    String bal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerFullName = findViewById(R.id.WalletID);
        registerEmail = findViewById(R.id.Amount);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfPass = findViewById(R.id.registerConfPass);
        registerUserBtn = findViewById(R.id.registerUserBtn);
        gotoLogin = findViewById(R.id.gotoLogin);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login.class));
                finish();
            }
        });

        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nolasa aizpildiitos datus

                String fullName = registerFullName.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();
                String confPass = registerConfPass.getText().toString();
                String bal = "0";

                if(fullName.isEmpty()){
                    registerFullName.setError("Full name is required!");
                    return;
                }
                if(email.isEmpty()){
                    registerEmail.setError("Email is required!");
                    return;
                }
                if(password.isEmpty()){
                    registerPassword.setError("Password is required!");
                    return;
                }
                if(confPass.isEmpty()){
                    registerConfPass.setError("Confirmation password is required!");
                    return;
                }
                if(!password.equals(confPass)){
                    registerConfPass.setError("Passwords do not match!");
                    return;
                }
                //Dati ir apstrādāti
                //lietotāja reģistrēšana firebase sistēmā

                Toast.makeText(Register.this, "Data Validated", Toast.LENGTH_SHORT).show();

                fAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //nosūta lietotāju uz jaunu lapu
                        userID = fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        Map<String,Object> user = new HashMap<>();
                        user.put("fullName",fullName);
                        user.put("email",email);
                        user.put("bal",bal);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: user profile is created for"+ userID);
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                        });
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });
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