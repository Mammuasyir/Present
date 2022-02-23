package com.example.testingcrud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.testingcrud.room.Constant
import com.example.testingcrud.room.Note
import com.example.testingcrud.room.NoteDB
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {

    val db by lazy { NoteDB(this) }
    private var noteId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setupView()
        setupListener()
    }

    private fun setupView() {
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType) {
            Constant.TYPE_CREATE -> {

            }
            Constant.TYPE_READ -> {
                button_save.visibility = View.GONE
                getNote()
            }
        }
    }

        fun setupListener() {
            button_save.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    db.noteDao().addNote(
                        Note(
                            0,
                            edit_title.text.toString(),
                            edit_note.text.toString()
                        )
                    )
                    finish()
                }
            }
        }
    private fun getNote(){
        noteId = intent.getIntExtra("note_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.noteDao().getNote( noteId ) [0]
            edit_title.setText( notes.title )
            edit_note.setText( notes.note )
        }
    }

    }
