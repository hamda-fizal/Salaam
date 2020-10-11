package com.example.salaam;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    EditText usernameEditText;
    EditText passwordEditText;
    boolean loginModeIsActive=false;
    Button signUpLoginButton;
    TextView toggleLoginModeTextView;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    public void signUpLogin(View view){


        String username=usernameEditText.getText().toString();
        final String password=passwordEditText.getText().toString();
        InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        //check if login possible
        try {
            if ((!username.equals("")) && (!password.equals(""))) {
                if (loginModeIsActive) {
                    mAuth.signInWithEmailAndPassword(usernameEditText.getText().toString().trim(), passwordEditText.getText().toString().trim())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override

                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //Add to database
                                            FirebaseDatabase
                                                    .getInstance()
                                                    .getReference()
                                                    .child("users").child(task.getResult().getUser().getUid()).child("email id").setValue(usernameEditText.getText().toString());
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.i("Login","Success");
                                        login();
                                    } else {
                                        if (task.getException() instanceof FirebaseAuthInvalidUserException)
                                            Toast.makeText(MainActivity.this, "Email id does not exist", Toast.LENGTH_SHORT).show();
                                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                                            Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

                //sign up user
                if (!loginModeIsActive){
                    mAuth.createUserWithEmailAndPassword(usernameEditText.getText().toString().trim(), passwordEditText.getText().toString().trim()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loginModeIsActive=true;
                                login();
                            } else {
                                if (password.length() < 6) {
                                    Toast.makeText(MainActivity.this, "Password must contain atleast 6 characters", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.w("Create User", "createUserWithEmail:failure", task.getException());
                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                                        Toast.makeText(MainActivity.this, "InvalidCredentials", Toast.LENGTH_LONG).show();
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                        Toast.makeText(MainActivity.this, "Email id already exists!", Toast.LENGTH_SHORT).show();
                                    if (task.getException() instanceof FirebaseAuthInvalidUserException)
                                        Toast.makeText(MainActivity.this, "Email id does not exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
            }
        }
        else{

                    Toast.makeText(MainActivity.this, "Username or password field is empty", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e){
            Toast.makeText(MainActivity.this,"Unable to complete action",Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }
    }
        @SuppressLint("SetTextI18n")
        public void toggleLoginSignUp(View view){

        if(loginModeIsActive) {
            usernameEditText.setText("");
            passwordEditText.setText("");
            signUpLoginButton.setText("Sign Up");
            toggleLoginModeTextView.setText("Already have an account?Login instead");
            loginModeIsActive = false;
        }else {
            usernameEditText.setText("");
            passwordEditText.setText("");
            signUpLoginButton.setText("Login");
            toggleLoginModeTextView.setText("Don't have an account?Sign up here");
            loginModeIsActive = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Users");
        signUpLoginButton =(Button)findViewById(R.id.signUpLoginButton);
        toggleLoginModeTextView=(TextView)findViewById(R.id.toggleLoginModeTextView);
        usernameEditText=(EditText)findViewById(R.id.usernameEditText);
        passwordEditText=(EditText)findViewById(R.id.passwordEditText);
      //  FirebaseStorage.getInstance().getReference().child("")

      }




                        void login() {
                            if(mAuth.getCurrentUser()!=null) {
                                Intent intent = new Intent(this, UserListActivity.class);
                            intent.putExtra("username",usernameEditText.getText().toString().trim());
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(this,"Ërror while logging in.Try again",Toast.LENGTH_SHORT).show();

                        }


}


