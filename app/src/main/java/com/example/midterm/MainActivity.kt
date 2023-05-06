package com.example.midterm
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midterm.database.Database
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var notesList: MutableList<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addButton = findViewById<FloatingActionButton>(R.id.button_new_note);
        addButton.setOnClickListener { launchCreateNoteActivity() }

        initializeNotesListView()
    }

    private fun launchEditNoteActivity(noteId: String){
        val intent = Intent(this, EditNoteActivity::class.java)
        intent.putExtra("note_id", noteId)
        startActivity(intent)
    }

    private fun launchCreateNoteActivity(){
        val intent = Intent(this, EditNoteActivity::class.java)
        startActivity(intent)
    }

    private  fun removeNote(noteId: String){
        Database.DeleteNote(noteId);
        resetList()
    }

    private fun resetList(){
        notesList = ArrayList(Database.GetNotes())
        notesRecyclerView.adapter = NotesAdapter(notesList, { launchEditNoteActivity(it) }, { removeNote(it) } )
    }

    private fun initializeNotesListView(){
        notesRecyclerView = findViewById(R.id.notes_list_view)
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        resetList()
    }

}