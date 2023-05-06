package com.example.midterm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.midterm.database.Database

class EditNoteActivity : AppCompatActivity() {

    private lateinit var saveButton: Button;
    private lateinit var titleEditText: EditText;
    private lateinit var contentEditText: EditText;

    private lateinit var noteId : String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        saveButton = findViewById(R.id.save_button);
        titleEditText = findViewById(R.id.create_title_text)
        contentEditText = findViewById(R.id.create_content_text)

        val id =  intent.extras?.getString("note_id");
        if (id.isNullOrBlank()) {
            saveButton.setOnClickListener{ addNote() }
        }
        else {
            noteId = id;

            val note = Database.GetNote(id);

            if(note == null) {
                returnToMainActivity();
                return;
            }

            titleEditText.setText(note.Label);
            contentEditText.setText(note.Text);
            saveButton.setOnClickListener{ editNote() }
        }

    }

    private fun returnToMainActivity(){
        val intent = Intent(this, MainActivity::class.java );
        startActivity(intent)
    }

    private fun editNote() {
        val note = Note(noteId, titleEditText.text.toString(), contentEditText.text.toString());
        Database.UpdateNote(note);
        returnToMainActivity()
    }

    private fun addNote() {
        val title = titleEditText.text.toString()

        if(title.trim().isNullOrBlank()){
            Toast.makeText(this, "Please enter title", Toast.LENGTH_LONG).show()
            return;
        }

        val note = Note("",titleEditText.text.toString(), contentEditText.text.toString());
        Database.AddNote(note);
        returnToMainActivity()
    }
}