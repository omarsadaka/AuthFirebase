package com.example.omar.authfirebase.Data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.omar.authfirebase.Model.MyBlog;
import com.example.omar.authfirebase.R;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

/**
 * Created by OMAR on 11/22/2018.
 */

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.viewHolder> {

    private Context context;
    private List<MyBlog> blogList;

    public BlogRecyclerAdapter(Context context, List<MyBlog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public BlogRecyclerAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.post_row , parent , false );
        return new viewHolder ( view ,context);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogRecyclerAdapter.viewHolder holder, int position) {
        MyBlog blog = blogList.get ( position );
        String imageURL = null;
        holder.postTitle.setText ( blog.getTitle () );
        holder.postDescr.setText ( blog.getDescr () );

        java.text.DateFormat dateFormat = java.text.DateFormat.getInstance ();
        String formattedDate = dateFormat.format ( new Date ( Long.valueOf ( blog.getTimesStamp () ) ).getTime () );
        holder.postDate.setText ( formattedDate );
         imageURL = blog.getImage ();
        Picasso.get ().load ( imageURL ).into ( holder.postImage );

    }

    @Override
    public int getItemCount() {
        return blogList.size ();
    }
    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView postImage;
        TextView postTitle;
        TextView postDescr;
        TextView postDate;
        String userId;

        public viewHolder(View itemView , Context ctx) {
            super ( itemView );
            context = ctx;
            postImage = (ImageView)itemView.findViewById ( R.id.postImage );
            postTitle = (TextView)itemView.findViewById ( R.id.postTitle );
            postDescr = (TextView)itemView.findViewById ( R.id.postText );
            postDate = (TextView)itemView.findViewById ( R.id.postDate );
            userId = null;
        }
    }
}
