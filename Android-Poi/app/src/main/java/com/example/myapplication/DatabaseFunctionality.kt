package layout

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

const val DATABASE_NAME = "MYDB16543"
const val PERSONS_TABLE_NAME = "persons2"
const val ENTRY_TABLE_NAME = "entries2"
data class Person(val first:String?,
                  val last:String? ,
                  val location:String?,
                  val race:String?,
                  val height:String?,
                  val image_path :String? ,
                  val id: String? )

data class Entry(val label:String ,
                 val data:String,
                 val level :String ,
                 var date_string :String,
                 val id: String )

class DatabaseFunctionality(val context:Context) : SQLiteOpenHelper(context, DATABASE_NAME , null,1){
    var db = this.writableDatabase
    override fun onCreate(db: SQLiteDatabase?) {
        val create_person_table= "CREATE TABLE $PERSONS_TABLE_NAME (" +
                "" +
                "first VARCHAR(255) , " +
                "last VARCHAR(255) ," +
                "height VARCHAR(255), " +
                "location VARCHAR(255), " +
                "race VARCHAR(255) , " +
                "id VARCHAR(20), " +
                "imagepath VARCHAR(255))"
        val create_entry_table ="CREATE TABLE $ENTRY_TABLE_NAME (id VARCHAR(20) , label VARCHAR(255), data TEXT , level VARCHAR(255),date_string VARCHAR(15))"
        db?.execSQL(create_person_table)
        db?.execSQL(create_entry_table)

    }

    open fun save_new_data(values: ContentValues, name:String){

        try {
           val test =  db?.insertOrThrow(name,null,values)
        }
        catch (e:Throwable){
            Toast.makeText(context , e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    open fun delete_everything(context: Context,table_name:String){
        db.delete(table_name, null,null)
    }
    fun gather_person_entries(id:String):Cursor{

        val cursor = db.query("entries2",
            arrayOf("label", "data","id","level","date_string") , //columns
            "id=?", // where
            arrayOf(id), // where's value
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
            val level = cursor.getString(cursor.getColumnIndexOrThrow("level"))
            val date = cursor.getString(cursor.getColumnIndexOrThrow("date_string"))
            val id = cursor.getString(cursor.getColumnIndexOrThrow("id"))
            val entry = Entry(label,label_data, level,date,id)
             data.add(entry)


        }
        return data
    }
    fun gather_persons():Cursor{

        val cursor = db.query("persons2",
            arrayOf("first", "last","location", "height","race","imagepath","id") , //columns
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
            val path = cursor.getString(cursor.getColumnIndexOrThrow("imagepath"))
            val id = cursor.getString(cursor.getColumnIndexOrThrow("id"))
            val person = Person(first,last,location,race,height,path, id)
            data.add(person)

        }
        return data
    }


    //removal:  db.delete(PRESET_TABLE_NAME,"name=?" , arrayOf(name))






    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}