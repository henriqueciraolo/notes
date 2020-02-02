package br.com.hciraolo.notes

import android.app.Application
import androidx.room.Room
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()
        /*appDatabase = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "hciraolo-notas"
        ).build()*/
        sharedPreferencesRepository = SharedPreferencesRepository(this)
    }

    companion object {
        var firebaseAuth: FirebaseAuth? = null
        /*var appDatabase: AppDatabase? = null*/
        var sharedPreferencesRepository: SharedPreferencesRepository? = null
    }
}