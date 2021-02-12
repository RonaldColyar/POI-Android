package layout

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
const val DATABASE_NAME = "MYDB"
const val PERSONS_TABLE_NAME = "persons"
const val ENTRY_TABLE_NAME = "entries"
class DatabaseFunctionality(val context:Context) : SQLiteOpenHelper(context, DATABASE_NAME , null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        val create_person_table= "CREATE TABLE $PERSONS_TABLE_NAME (first VARCHAR(255) , last VARCHAR(255) , location VARCHAR(255), race VARCHAR(255) , id int)"
        val create_entry_table ="CREATE TABLE $ENTRY_TABLE_NAME (id int , label VARCHAR(255), data text)"

        db?.execSQL(create_person_table)
        db?.execSQL(create_entry_table)

    }

    fun create_person(){}
    fun create_entry(){}
    fun update_person(){}
    fun update_entry(){}
    fun delete_person(){}
    fun delete_entry(){}
    fun breached(){}
    



    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}