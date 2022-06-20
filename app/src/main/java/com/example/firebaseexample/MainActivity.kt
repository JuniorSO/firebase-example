package com.example.firebaseexample

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Firebase.firestore
        val docRef = db.collection("basic").document("main")
        var numberupd = 0

        docRef.get().addOnSuccessListener { result ->
            val number = result.get("number")
            numberupd = number.toString().toInt()
            findViewById<TextView>(R.id.textView).text = numberupd.toString()
        }

        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            docRef
                .update("number", numberupd + 1)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        }

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val pacote = snapshot.data
                numberupd = pacote?.get("number").toString().toInt()
                findViewById<TextView>(R.id.textView).text = numberupd.toString()
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }
}