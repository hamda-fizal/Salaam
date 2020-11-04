package com.example.salaam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText usernameEditText;
    MaterialEditText passwordEditText;
    MaterialEditText emailEditText;
    Button signUpButton;

    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    DatabaseReference userRef;
    public void signUp(View view){

        final String password,username,email;
        password=passwordEditText.getText().toString().trim();
        username=usernameEditText.getText().toString().trim();
        email=emailEditText.getText().toString().trim();

        InputMethodManager imm = (InputMethodManager) RegisterActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        if ((username.equals("")) || (password.equals(""))||email.equals(""))
            Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
        else {

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if (task.isSuccessful()) {
                        FirebaseUser user=mAuth.getCurrentUser();
                        assert user != null;
                        final String uid=user.getUid();

                        userRef=FirebaseDatabase.getInstance().getReference("users").child(uid);
                        HashMap<String,String> hashMap=new HashMap<>();
                        hashMap.put("uid",uid);
                        hashMap.put("username",username);
                        hashMap.put("imageURL","default");


                        userRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                login(uid);

                            }
                        });

                    } else {

                        if (password.length() < 6) {

                            Toast.makeText(RegisterActivity.this, "Password must contain atleast 6 characters", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w("Create User", "createUserWithEmail:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                                Toast.makeText(RegisterActivity.this, "InvalidCredentials", Toast.LENGTH_LONG).show();
                            if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                Toast.makeText(RegisterActivity.this, "Email id already exists!", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidUserException)
                                Toast.makeText(RegisterActivity.this, "Email id does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    void login(String uid){
        if(mAuth.getCurrentUser()!=null) {
            Intent intent = new Intent(this, UserListActivity.class);
            intent.putExtra("uid",uid);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        else
            Toast.makeText(this,"Error while logging in.Try again",Toast.LENGTH_SHORT).show();

    }


    public void sendUserToLoginPage(View view){

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        signUpButton =(Button)findViewById(R.id.signUpButton);
        usernameEditText=(MaterialEditText)findViewById(R.id.usernameEditText);
        emailEditText=(MaterialEditText)findViewById(R.id.emailEditText);
        passwordEditText=(MaterialEditText)findViewById(R.id.passwordEditText);


    }
}