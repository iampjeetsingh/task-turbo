package `in`.thesupremeone.taskturbo

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class App : Application() {
    private lateinit var database: FirebaseDatabase
    override fun onCreate() {
        super.onCreate()
        database = FirebaseDatabase.getInstance()
        database.setPersistenceEnabled(true)
    }

    fun getDatabase(): FirebaseDatabase {
        return database
    }
}