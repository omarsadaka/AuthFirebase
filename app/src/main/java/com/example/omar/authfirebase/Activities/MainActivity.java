package com.example.omar.authfirebase.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.omar.authfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUser mUser;
    private Button loginBtn;
    private Button createAccBtn;
    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        loginBtn = (Button)findViewById ( R.id.loginID );
        createAccBtn = (Button)findViewById ( R.id.createAccoID );
        emailField = (EditText)findViewById ( R.id.loginEmailID );
        passwordField = (EditText)findViewById ( R.id.loginPasswordID );
        createAccBtn.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( MainActivity.this , CreateAccountActivity.class ) );
                finish ();
            }
        } );
        mAuth = FirebaseAuth.getInstance ( );
        mAuthListner = new FirebaseAuth.AuthStateListener ( ) {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser ();
                if (mUser != null){
                    Toast.makeText ( MainActivity.this, "Signed In", Toast.LENGTH_SHORT ).show ( );
                    startActivity ( new Intent ( MainActivity.this , PostListActivity.class ) );
                    finish ();
                }else {
                    Toast.makeText ( MainActivity.this, "Not Signed In", Toast.LENGTH_SHORT ).show ( );

                }

            }
        };
        loginBtn.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty ( emailField.getText ().toString () )&& !TextUtils.isEmpty ( passwordField.getText ().toString () )){
                    String email = emailField.getText ().toString ();
                    String password = passwordField.getText ().toString ();
                    login(email , password);
                }
            }

            private void login(String email, String password) {
                mAuth.signInWithEmailAndPassword ( email , password ).addOnCompleteListener ( MainActivity.this,
                        new OnCompleteListener<AuthResult> ( ) {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful ()){
                                    Toast.makeText ( MainActivity.this, "Signed In", Toast.LENGTH_SHORT ).show ( );
                                    startActivity ( new Intent ( MainActivity.this , PostListActivity.class ) );
                                }else {
                                    Toast.makeText ( MainActivity.this, "Not Signed In", Toast.LENGTH_SHORT ).show ( );

                                }

                            }
                        } );

            }
        } );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.main_menu , menu );
        return super.onCreateOptionsMenu ( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId () == R.id.singnOut) {
            mAuth.signOut ();
        }
            return super.onOptionsItemSelected ( item );
    }

    @Override
    protected void onStart() {
        super.onStart ( );
        mAuth.addAuthStateListener ( mAuthListner );
    }

    @Override
    protected void onStop() {
        super.onStop ( );
        if (mAuthListner != null){
            mAuth.removeAuthStateListener ( mAuthListner );
        }
    }
}
