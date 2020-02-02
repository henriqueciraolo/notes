package br.com.hciraolo.notes

import android.app.Application
import android.content.Context
import androidx.room.Room
import br.com.hciraolo.notes.notes.repository.data.NoteDto
import br.com.hciraolo.notes.notes.repository.data.Priority
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()
        appDatabase = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "hciraolo-notas.db"
        ).build()
        sharedPreferencesRepository = SharedPreferencesRepository(this)
    }

    companion object {
        var firebaseAuth: FirebaseAuth? = null
        var appDatabase: AppDatabase? = null
        var sharedPreferencesRepository: SharedPreferencesRepository? = null
    }
}