package layout

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

const val DATABASE_NAME = "MYDB2"
const val PERSONS_TABLE_NAME = "persons"
const val ENTRY_TABLE_NAME = "entries"
data class Person(val first:String,val last:String , val location:String,val race:String,val height:String )
data class Entry(val label:String , val data:String)
class DatabaseFunctionality(val context:Context) : SQLiteOpenHelper(context, DATABASE_NAME , null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        val create_person_table= "CREATE TABLE $PERSONS_TABLE_NAME (first VARCHAR(255) , last VARCHAR(255) , location VARCHAR(255), race VARCHAR(255) , id int)"
        val create_entry_table ="CREATE TABLE $ENTRY_TABLE_NAME (id int , label VARCHAR(255), data text)"
        db?.execSQL(create_person_table)
        db?.execSQL(create_entry_table)

    }
    private fun save_new_data(values: ContentValues, name:String){
        val db  = this.writableDatabase
        try {
            db?.insert(name,null,values)
        }
        catch (e:Throwable){
            Toast.makeText(context , "Error 202" , Toast.LENGTH_LONG).show()
        }
    }

    open fun delete_everything(context: Context,table_name:String){
        var db = this.writableDatabase
        db.delete(table_name, null,null)
    }
    fun gather_person_entries(id:Int):Cursor{
        val db = this.readableDatabase
        val cursor = db.query("entries",
            arrayOf("label", "data") , //columns
            "id=?", // where
            arrayOf(id.toString()), // where's value
            null, //group by
            null,  // having
            null) // order by
        return cursor
    }
    fun formatt_entries(cursor:Cursor):MutableList<Entry>{
        var data = mutableListOf<Entry>()

        while (cursor.moveToNext()){ // every entry
            val label = cursor.getString(cursor.getColumnIndexOrThrow("label"))
            val label_data = cursor.getString(cursor.getColumnIndexOrThrow("data"))
            val entry = Entry(label,label_data)
            data.add(entry)


        }
        return data
    }
    fun gather_persons(id:Int):Cursor{
        val db = this.readableDatabase
        val cursor = db.query("entries",
            arrayOf("first", "last","location", "height","race") , //columns
            null, // where
             null, // where's value
            null, //group by
            null,  // having
            null) // order by
        return cursor
    }
    fun formatt_persons(cursor:Cursor) :MutableList<Person>{
        var data = mutableListOf<Person>()
        while (cursor.moveToNext()){ // every person
            val first = cursor.getString(cursor.getColumnIndexOrThrow("first"))
            val last = cursor.getString(cursor.getColumnIndexOrThrow("last"))
            val location = cursor.getString(cursor.getColumnIndexOrThrow("location"))
            val height = cursor.getString(cursor.getColumnIndexOrThrow("height"))
            val race = cursor.getString(cursor.getColumnIndexOrThrow("race"))
            val label_data = cursor.getString(cursor.getColumnIndexOrThrow("data"))
            val person = Person(first,last,location,race,height)
            data.add(person)

        }
        return data
    }


    fun create_person(){

    }
    fun create_entry(){}
    fun update_person(){}
    fun update_entry(){}
    fun delete_person(){}
    fun delete_entry(){}




    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}