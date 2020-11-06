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

public class LoginActivity extends AppCompatActivity {



    MaterialEditText usernameEditText;
    MaterialEditText passwordEditText;
    MaterialEditText emailEditText;
    Button signUpButton;

    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    DatabaseReference userRef;
    public void login(View view){
        final String password,email;
        password=passwordEditText.getText().toString().trim();
        email=emailEditText.getText().toString().trim();

        InputMethodManager imm = (InputMethodManager) LoginActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        if ((password.equals(""))||email.equals(""))
            Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override

                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.i("Login", "Success");
                                sendUserToLoginPage(email);
                            } else {
                                if (email.isEmpty())
                                    Toast.makeText(LoginActivity.this, "Email or password field is empty", Toast.LENGTH_LONG).show();
                                if (task.getException() instanceof FirebaseAuthInvalidUserException)
                                    Toast.makeText(LoginActivity.this, "Email id does not exist", Toast.LENGTH_SHORT).show();
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                                    Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
}
    }

   public void sendUserToLoginPage(String email){
        if(mAuth.getCurrentUser()!=null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        else
            Toast.makeText(this,"Error while logging in.Try again",Toast.LENGTH_SHORT).show();

    }
    public void register(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Sign in");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        signUpButton =(Button)findViewById(R.id.loginButton);
        usernameEditText=(MaterialEditText)findViewById(R.id.usernameEditText);
        emailEditText=(MaterialEditText)findViewById(R.id.emailEditText);
        passwordEditText=(MaterialEditText)findViewById(R.id.passwordEditText);


    }


}