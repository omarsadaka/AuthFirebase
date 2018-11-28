package com.example.omar.authfirebase.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.omar.authfirebase.Model.MyBlog;
import com.example.omar.authfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {
    private ImageView mPostImage;
    private EditText mPostTitle;
    private EditText mPostDescr;
    private Button mSubmitBtn;
    private StorageReference mStorage;
    private DatabaseReference databaseReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private static final int GALLERY_CODE = 1 ;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_add_post );

        progressDialog = new ProgressDialog ( this );
        mAuth = FirebaseAuth.getInstance ();
        mUser = mAuth.getCurrentUser ();
        mStorage = FirebaseStorage.getInstance ().getReference ();

        databaseReference = FirebaseDatabase.getInstance ().getReference ().child ( "MBlog" );

        mPostImage = (ImageView)findViewById ( R.id.imageButton2 );
        mPostTitle = (EditText)findViewById ( R.id.titleET );
        mPostDescr = (EditText)findViewById ( R.id.describtionET );
        mSubmitBtn = (Button)findViewById ( R.id.submitId );

        mPostImage.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent ( Intent.ACTION_GET_CONTENT );
                galleryIntent.setType ( "image/*" );
                startActivityForResult ( galleryIntent , GALLERY_CODE );
            }
        } );

        mSubmitBtn.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        } );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            mImageUri = data.getData ();
            mPostImage.setImageURI ( mImageUri );
        }

    }

    private void startPosting() {
        progressDialog.setMessage ( "Posting to blog..." );
        progressDialog.show ();
        final String titleValue = mPostTitle.getText ().toString ().trim ();
        final String descValue = mPostDescr.getText ().toString ().trim ();
        if (!TextUtils.isEmpty ( titleValue) && !TextUtils.isEmpty ( descValue ) && mImageUri != null){

            StorageReference filePath = mStorage.child ( "MBlog_image" ).child ( mImageUri.getLastPathSegment () );
            filePath.putFile ( mImageUri).addOnSuccessListener ( new OnSuccessListener<UploadTask.TaskSnapshot> ( ) {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl ();
                    DatabaseReference newPost = databaseReference.push ();
                    Map<String , String> dataToSave = new HashMap<> ( );
                    dataToSave.put ( "title" , titleValue );
                    dataToSave.put ( "descr" , descValue );
                    dataToSave.put ( "image" , downloadUrl.toString () );
                    dataToSave.put ( "timesStamp" , String.valueOf ( System.currentTimeMillis () ) );
                    dataToSave.put ( "userId" , mUser.getUid () );

                    newPost.setValue ( dataToSave );
                    progressDialog.dismiss ();

                    startActivity ( new Intent ( AddPostActivity.this , PostListActivity.class) );
                    finish ();
                }
            } );


                }

        }
    }

