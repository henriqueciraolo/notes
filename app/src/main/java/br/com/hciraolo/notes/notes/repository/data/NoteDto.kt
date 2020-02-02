package br.com.hciraolo.notes.notes.repository.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "notes")
data class NoteDto(@PrimaryKey(autoGenerate = true) @ColumnInfo(name ="id") val id: Int?, @ColumnInfo(name ="title") val title: String?, @ColumnInfo(name ="short_description") val shortDescription: String?, @ColumnInfo(name ="description") val description: String?, @ColumnInfo(name ="priority") val priority: Priority?)

enum class Priority(val code: Int) {
    HIGHER(1),
    HIGH(2),
    MEDIUM(3),
    LOW(4),
    LOWEST(5)
}

class PriorityTypeConverter {
    @TypeConverter fun fromPriorityToInt(priority: Priority) : Int = priority.code
    @TypeConverter fun fromIntToPriority(code: Int) : Priority = Priority.values()[code - 1]
}