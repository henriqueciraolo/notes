package br.com.hciraolo.notes

import android.content.Context

class SharedPreferencesRepository(context: Context) {
    val sharedPreferences = context.getSharedPreferences("hciraolo-notes-sp", Context.MODE_PRIVATE)

    fun putValue(key: String, value: String) {

        val sharedPrefsEditor = sharedPreferences.edit()
        sharedPrefsEditor.putString(key, value)

        sharedPrefsEditor.apply()
    }

    fun getStringValue(key: String) : String {
        return sharedPreferences.getString(key, "")!!
    }

    fun putValue(key: String, value: Boolean) {

        val sharedPrefsEditor = sharedPreferences.edit()
        sharedPrefsEditor.putBoolean(key, value)

        sharedPrefsEditor.apply()
    }

    fun getBooleanValue(key: String) : Boolean {
        return sharedPreferences.getBoolean(key, false)!!
    }
}