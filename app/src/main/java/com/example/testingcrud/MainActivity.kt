package com.example.testingcrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testingcrud.room.Constant
import com.example.testingcrud.room.Note
import com.example.testingcrud.room.NoteDB
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    val db by lazy { NoteDB(this) }
    lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupListener()
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.noteDao().getNotes()
            Log.d("MainActivity", "dbResponse: $notes")
            withContext(Dispatchers.Main) {
                noteAdapter.setData(notes)
            }
        }
    }

    private fun intentEdit(intent_type: Int, note_id: Int) {
        startActivity(
            Intent(this, EditActivity::class.java)
                .putExtra("intent_type", intent_type)
                .putExtra("note_id", note_id)
        )

    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter(
            arrayListOf(), object : NoteAdapter.OnAdapterListener {
                override fun onClick(note: Note) {
//                    startActivity(
//                        Intent(applicationContext, EditActivity::class.java)
//                            .putExtra("intent_id", note.id)
//                    )
                    intentEdit(Constant.TYPE_READ, note.id)
                }
            })

        list_note.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = noteAdapter
        }
    }


    fun setupListener() {
        button_create.setOnClickListener {
//            startActivity(Intent(this, EditActivity::class.java))
            intentEdit(Constant.TYPE_CREATE, 0)
        }
    }
}