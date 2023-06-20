package com.example.firebaseppb_modul10

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseppb_modul10.Fitur.Edit
import com.example.firebaseppb_modul10.Fitur.Insert
import com.google.firebase.database.*
class MainActivity : AppCompatActivity() {
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var adapter: MahasiswaAdapter
    private lateinit var userList: ArrayList<Mahasiswa>
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val insertButton: Button = findViewById(R.id.insertButton)
        insertButton.setOnClickListener {
            insertMahasiswa()
        }
        // Initialize RecyclerView
        userRecyclerView = findViewById(R.id.recyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        // Initialize ArrayList and Adapter
        userList = ArrayList()
        adapter = MahasiswaAdapter(userList, this, ::editMahasiswa,
            ::deleteMahasiswa)
        userRecyclerView.adapter = adapter
        // Get Firebase database reference
        database =
            FirebaseDatabase.getInstance().getReference("mahasiswa")
        // Get data from Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear previous data from userList
                userList.clear()
                // Loop through each data item and add it to
                userList
                for (snapshot in dataSnapshot.children) {
                    val nim =
                        snapshot.child("nim").getValue(String::class.java)
                    val mahasiswa =
                        snapshot.getValue(Mahasiswa::class.java)
                    mahasiswa?.let {
                        it.nim = nim
                        it.nim?.let { nim -> // Check if nim is not
                            null
                            it.nim = nim
                            userList.add(it)
                        }
                    }
                }
                // Update the RecyclerView after receiving new data
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error when accessing Firebase
            }
        })
    }
    private fun editMahasiswa(mahasiswa: Mahasiswa) {
        // Open the edit activity or perform the edit operation as needed
        val intent = Intent(this, Edit::class.java)
        intent.putExtra("nama", mahasiswa.nama)
        intent.putExtra("nim", mahasiswa.nim)
        intent.putExtra("telp", mahasiswa.telp)
        startActivity(intent)
    }
    private fun deleteMahasiswa(mahasiswa: Mahasiswa) {
        // Delete the mahasiswa data from Firebase
        val userId = mahasiswa.nim
        userId?.let {
            database.child(it).removeValue()
        }
    }
    private fun insertMahasiswa() {
        val intent = Intent(this, Insert::class.java)
        startActivity(intent)
    }
}