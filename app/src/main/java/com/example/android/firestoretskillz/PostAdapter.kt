package com.example.android.firestoretskillz

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostAdapter(options: FirestoreRecyclerOptions<Students>,val listner:IPostAdapter) : FirestoreRecyclerAdapter<Students, PostAdapter.PostViewholder>(
    options
){

    class PostViewholder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val text_id:TextView = itemView.findViewById(R.id.ID)
        val text_name:TextView = itemView.findViewById(R.id.NAME)
        val del_button:ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewholder {
        val viewholder= PostViewholder(LayoutInflater.from(parent.context).inflate(R.layout.newlayout,parent,false))
        viewholder.del_button.setOnClickListener{
            listner.onLikeClicked(snapshots.getSnapshot(viewholder.adapterPosition).id)
        }
        return viewholder
    }

    override fun onBindViewHolder(holder: PostViewholder, position: Int, model: Students) {
        holder.text_id.text = model.id
        holder.text_name.text = model.name

    }


}
interface IPostAdapter{
    fun onLikeClicked(postId:String)
}