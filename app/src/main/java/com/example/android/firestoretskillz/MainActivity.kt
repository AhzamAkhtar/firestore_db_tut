package com.example.android.firestoretskillz

import android.graphics.ColorSpace.get
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

import org.w3c.dom.Document
import java.lang.reflect.Array.get
import java.lang.reflect.Field
import java.nio.file.Paths.get
import java.util.*
import kotlin.collections.HashMap
import android.widget.Toast.makeText as makeText1

class MainActivity : AppCompatActivity(),IPostAdapter,IPostAdapter1 {
    //private lateinit var postForEdit:String
    //private lateinit var postForEdit_id:String
    //private lateinit var postForEdit_Name:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = FirebaseFirestore.getInstance()
        val ref:DocumentReference=db.collection("Students").document("Data")
        val newref:CollectionReference = db.collection("Students")
        val inputId:EditText = findViewById(R.id.inputId)
        val inputName:EditText  = findViewById(R.id.inputName)
        val btn:Button = findViewById(R.id.buttonSave)
        val btn2:Button = findViewById(R.id.buttonRead)
        val btn3:Button= findViewById(R.id.buttonDelete)
        val btn4:Button = findViewById(R.id.buttonAddMultiple)
        val textView:TextView = findViewById(R.id.textViewId)
        val textView2:TextView = findViewById(R.id.textViewName)




        fun realTimeUpdate(){
            ref.addSnapshotListener{querySnapshot,firebaseFirestoreException->
                firebaseFirestoreException?.let{
                    makeText1(this,it.message,Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                querySnapshot?.let{
                    textView.text = it.getString("ID")
                    textView2.text = it.getString("Name")
                }
            }
        }

        btn.setOnClickListener{

            val input_Id = inputId.text.toString().trim()
            val input_name = inputName.text.toString().trim()
            val hashMap:HashMap<String,String> = HashMap<String,String>()
            hashMap.put("ID",input_Id)
            hashMap.put("Name",input_name)

            ref.set(hashMap).addOnSuccessListener {
                makeText1(this,"Data Added",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                makeText1(this,"Error",Toast.LENGTH_SHORT).show()
            }
            realTimeUpdate()

        }

        btn2.setOnClickListener{
            ref.get().addOnSuccessListener {
               if (it!=null){
                   Log.d("tagoo", "DocumentSnapshot data: ${it.data}")
                   textView.text = it.getString("ID")
                   textView2.text = it.getString("Name")
               }else{
                   Log.d("tagooo","No Such Document")
               }
            }.addOnFailureListener{exception->
               Log.d("tagoo","get failed with",exception)
            }
        }

        btn3.setOnClickListener{
            //ref.update("ID",FieldValue.delete())
            //ref.update("Name",FieldValue.delete())
            //       OR
            ref.delete()

        }

        btn4.setOnClickListener{

            val input_Id = inputId.text.toString().trim()
            val input_name = inputName.text.toString().trim()
            val students = Students(input_Id,input_name)
            newref.add(students).addOnSuccessListener {
                makeText1(this,"data Added",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                makeText1(this,"error occured",Toast.LENGTH_SHORT).show()
            }
        }
        ReadData()



    }

    private fun ReadData() {
        val db = FirebaseFirestore.getInstance()
        val newref:CollectionReference = db.collection("Students")
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<Students>().setQuery(newref,Students::class.java).build()

        val adapter = PostAdapter(recyclerViewOption,this,this)
        val recyclerView:RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.startListening()
    }

    fun DeletePerticularData(view: View) {
        val db = FirebaseFirestore.getInstance()
        val ref1:DocumentReference=db.collection("Students").document("GIQkWE6swGNMMQRLcLUV")
        ref1.delete()
    }

    fun getPostById(postId: String): Task<DocumentSnapshot> {
        val db = FirebaseFirestore.getInstance()
        val postCollections = db.collection("Students")
        return postCollections.document(postId).get()
    }



    override fun onLikeClicked(postId: String) {
        GlobalScope.launch {
            val db = FirebaseFirestore.getInstance()
            val ref:DocumentReference=db.collection("Students").document(postId)
            ref.delete()
            val post = getPostById(postId).await().toObject(Students::class.java)

                Log.d("this", post.toString())
                Log.d("this", post?.name.toString())


        }
    }

    override fun onEditClicked(postId: String) {
        GlobalScope.launch {
            val db = FirebaseFirestore.getInstance()
            val ref_for_Edit:DocumentReference=db.collection("Students").document(postId)
            ref_for_Edit.update("name","Default")
                .addOnSuccessListener { Log.d("Edit","pass") }
                .addOnFailureListener{ Log.d("Edit","fail")}
            ref_for_Edit.update("id","000")
                .addOnSuccessListener {Log.d("Edit","pass")  }
                .addOnFailureListener{Log.d("Edit","fail")}

            /**if (postForEdit != null) {
                postForEdit.id   =" 100000"
                postForEdit.name = "default"
            }
            Log.d("testing",postForEdit.toString())*/
        }
            //val editId:EditText = findViewById(R.id.EditId)
            //val editName:EditText = findViewById(R.id.EditName)
            //editId.setText(postForEdit?.id)
            //editName.setText(post?.name)


    }



}