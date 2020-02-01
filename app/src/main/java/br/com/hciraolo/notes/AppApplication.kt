package br.com.hciraolo.notes

import android.app.Application
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        firebaseAuth = FirebaseAuth.getInstance()
        /*appDatabase = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "hciraolo-notas"
        ).build()*/
    }

    companion object {
        var firebaseAuth: FirebaseAuth? = null
        /*var appDatabase: AppDatabase? = null*/
    }
}