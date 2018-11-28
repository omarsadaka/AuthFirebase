package com.example.omar.authfirebase.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.omar.authfirebase.Data.BlogRecyclerAdapter;
import com.example.omar.authfirebase.Model.MyBlog;
import com.example.omar.authfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PostListActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private List<MyBlog> blogList;
    private BlogRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_post_list );
        mAuth = FirebaseAuth.getInstance ();
        mUser = mAuth.getCurrentUser ();
        firebaseDatabase = FirebaseDatabase.getInstance ();
        databaseReference = firebaseDatabase.getReference ().child ( "MBlog" );
        databaseReference.keepSynced ( true );

        blogList = new ArrayList<> ( );
        recyclerView = (RecyclerView)findViewById ( R.id.recyclerview );
        recyclerView.setHasFixedSize ( true );
        recyclerView.setLayoutManager ( new LinearLayoutManager ( this ) );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.main_menu , menu );
        return super.onCreateOptionsMenu ( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId ()){

            case R.id.add:
                if (mUser != null && mAuth != null){
                    startActivity ( new Intent ( PostListActivity.this , AddPostActivity.class ) );
                    finish ();
                }
                break;
            case R.id.singnOut:
                if (mUser != null && mAuth != null){
                    mAuth.signOut ();
                    startActivity ( new Intent ( PostListActivity.this , MainActivity.class ) );
                    finish ();
                }
                break;




        }
        return super.onOptionsItemSelected ( item );
    }

    @Override
    protected void onStart() {
        super.onStart ( );
        databaseReference.addChildEventListener ( new ChildEventListener ( ) {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                  // blogList.clear ();
                   MyBlog blog = dataSnapshot.getValue (MyBlog.class);
                   blogList.add ( blog );

                   Collections.reverse ( blogList );
                   adapter = new BlogRecyclerAdapter ( PostListActivity.this , blogList );
                   recyclerView.setAdapter ( adapter );
                   adapter.notifyDataSetChanged ();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }
}
