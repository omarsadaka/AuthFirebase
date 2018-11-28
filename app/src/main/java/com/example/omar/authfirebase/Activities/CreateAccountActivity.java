package com.example.omar.authfirebase.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.omar.authfirebase.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private Button createAct;
    private StorageReference firebaseStorage;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private  Uri resultUri = null;
    private ImageButton profilePic;
    private static final int GALLERY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_create_account );

        mAuth = FirebaseAuth.getInstance ( );
        firebaseStorage = FirebaseStorage.getInstance ().getReference ().child ( "MBlog_profile_pics" );
        mFirebaseDatabase = FirebaseDatabase.getInstance ( );
        mDatabaseReference = mFirebaseDatabase.getReference ( ).child ( "MUsers" );
        progressDialog = new ProgressDialog ( this );

        firstName = (EditText) findViewById ( R.id.firstNameAct );
        lastName = (EditText) findViewById ( R.id.lastNameAct );
        email = (EditText) findViewById ( R.id.emailAct );
        password = (EditText) findViewById ( R.id.passwordAct );
        createAct = (Button) findViewById ( R.id.createAct );
        profilePic = (ImageButton) findViewById ( R.id.profilePic );


        createAct.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                createNewAccount ( );
            }
        } );
        profilePic.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent galleryintent = new Intent ( );
                galleryintent.setAction ( Intent.ACTION_GET_CONTENT );
                galleryintent.setType ( "image/*" );
                startActivityForResult ( galleryintent, GALLERY_CODE );

            }
        } );


    }

    private void createNewAccount() {
        final String name = firstName.getText ( ).toString ( ).trim ( );
        final String Lname = lastName.getText ( ).toString ( ).trim ( );
        String Email = email.getText ( ).toString ( ).trim ( );
        String pwd = password.getText ( ).toString ( ).trim ( );
        if (!TextUtils.isEmpty ( name ) && !TextUtils.isEmpty ( Lname ) && !TextUtils.isEmpty ( Email ) && !TextUtils.isEmpty ( pwd )) {
            progressDialog.setMessage ( "Creating Account..." );
            progressDialog.show ( );
            mAuth.createUserWithEmailAndPassword ( Email, pwd ).addOnSuccessListener ( new OnSuccessListener<AuthResult> ( ) {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if (authResult != null) {
                        StorageReference imagePath = firebaseStorage.child ( "MBlog_profile_pics" ).child ( resultUri.getLastPathSegment ());
                        imagePath.putFile ( resultUri ).addOnSuccessListener ( new OnSuccessListener<UploadTask.TaskSnapshot> ( ) {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                String userId = mAuth.getCurrentUser ( ).getUid ( );
                                DatabaseReference currentUserDB = mDatabaseReference.child ( userId );
                                currentUserDB.child ( "firstname" ).setValue ( name );
                                currentUserDB.child ( "lastname" ).setValue ( Lname );
                                currentUserDB.child ( "image" ).setValue ( resultUri.toString () );

                                progressDialog.dismiss ( );
                                Intent intent = new Intent ( CreateAccountActivity.this, PostListActivity.class );
                                intent.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                startActivity ( intent );
                            }
                        } );



                    }
                }
            } );
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            Uri mImageUri = data.getData ( );
            CropImage.activity ( mImageUri )
                    .setAspectRatio ( 1,1 )
                    .setGuidelines ( CropImageView.Guidelines.ON )
                    .start ( this );

        }

        {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult ( data );
                if (resultCode == RESULT_OK) {
                     resultUri = result.getUri ( );
                    profilePic.setImageURI ( resultUri );
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError ( );
                }
            }
        }
    }
}
