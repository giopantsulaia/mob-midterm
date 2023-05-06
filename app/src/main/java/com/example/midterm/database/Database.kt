package com.example.midterm.database
import com.example.midterm.Note
import io.realm.kotlin.MutableRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RealmNote : RealmObject {
    @PrimaryKey
    var id : String = ""
    var title : String = ""
    var text : String = ""

    constructor() { }

    constructor(id: String, title: String, text: String){
        this.id = id
        this.title = title
        this.text = text
    }
}

object Database {
    val config = RealmConfiguration.create(schema = setOf(RealmNote::class));
    val realm = Realm.open(config);

    fun AddNote( note: Note ) {
        realm.writeBlocking { this.copyToRealm(RealmNote(ObjectId.create().toString(),note.Label, note.Text)) }
    }

    fun UpdateNote(note: Note) {
        val noteObject = realm.query<RealmNote>(RealmNote::class, "id == $0", note.Id).first().find();
        realm.writeBlocking {
            if(noteObject != null){
                findLatest(noteObject).also {
                    realmNote -> realmNote?.apply {
                        title = note.Label
                        text = note.Text
                    }
                }
            }
        }
    }

    fun DeleteNote(id: String) {
        val realmNote = realm.query(RealmNote::class, "id == $0", id ).first().find();
        realm.writeBlocking {
            if (realmNote != null) findLatest(realmNote)?.also {
                delete(it)
            }
        }
    }

    fun GetNote(id : String) : Note? {
        val realmNote = realm.query(RealmNote::class, "id == $0", id ).first().find() ?: return null;
        return Note(realmNote.id, realmNote.title, realmNote.text);
    }

    fun GetNotes(): List<Note> {
        return realm.query(RealmNote::class).find().map { Note(it.id, it.title, it.text) }.toList()
    }
}
