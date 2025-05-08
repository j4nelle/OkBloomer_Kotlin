package es.uc3m.android.okbloomer_kotlin.datas

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.compose.foundation.layout.Row
import es.uc3m.android.okbloomer_kotlin.WaterReminderReceiver

//old version

class Plant_data(private val context: Context)
    : SQLiteOpenHelper(context, "myplants.db", null, 3) {

    override fun onCreate(db: SQLiteDatabase) {
    db.execSQL("CREATE TABLE mygarden("+
                "idplant INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "plant_nickname TEXT,"+
                "plant_specie TEXT,"+
                "watering_frequency FLOAT,"+
                //for button color change, keeps track of last watering date
                "last_watered INTEGER,"+
                "typo INTEGER,"+
                "photo_path TEXT,"+
            "plant_specie_IA TEXT)")
    }

    fun adding_new_plant(plant_nickname : String, plant_specie: String, watering_frequency : Float, typo : Int, photo_path : String, plant_specie_IA: String) : Long{
        val db = this.writableDatabase
        //db.execSQL("INSERT INTO ...")

        val currentTime = System.currentTimeMillis()

        val contentValues = ContentValues().apply {
            put("plant_nickname",plant_nickname)
            put("plant_specie", plant_specie)
            put("watering_frequency", watering_frequency)
            put("last_watered", currentTime)
            put("typo", typo)
            put("photo_path", photo_path)
            put("plant_specie_IA", plant_specie_IA)
        }

        // to see the inserted values
        Log.d("DB_INSERT", "Inserting: $plant_nickname, $plant_specie, $watering_frequency, $typo, $photo_path, $plant_specie_IA")

        val autonumeric = db.insert("mygarden", null, contentValues)

        if (autonumeric == -1L) {
            Log.e("DB_ERROR", "failed insertion in the database of the plant: $plant_nickname")
        } else {
            Log.d("DB_SUCCESS", "Inserted successfully, id: $autonumeric")
        }
        return autonumeric
    }

    //to update the last watered date
    fun plant_watered(plantID: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("last_watered", System.currentTimeMillis())
        }

        val rowsUpdated = db.update(
            "mygarden",
            contentValues,
            "idplant = ?",
            arrayOf(plantID)
        )

        db.close()
        return rowsUpdated > 0
    }

    fun deleting_a_plant(idplant : String): Boolean {
        val intent = Intent(context, WaterReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            idplant.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

        val db = this.writableDatabase
        val Row_deleted = db.delete("mygarden", "idplant = ?", arrayOf(idplant))
        db.close()

        //tests messages
        if (Row_deleted > 0) {
            Log.d("Success deletion", "Plant with ID $idplant deleted successfully")
        } else{
            Log.e("Error deletion", "Deletion failed")
        }
        return Row_deleted > 0
    }

    fun plantSelect(plant_data : Plant_data): Cursor{
        val db = plant_data.readableDatabase
        val sql = "SELECT * from mygarden ORDER BY idplant DESC" //selects every object from the database mygarden
        return db.rawQuery(sql, null)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {

            //Add new column for plant_specie_IA
            db?.execSQL("ALTER TABLE mygarden ADD COLUMN plant_specie_IA TEXT")
            Log.d("DB_UPGRADE", "New column plant_specie_IA well added")

            // Add the new column if photo path
            db?.execSQL("ALTER TABLE mygarden ADD COLUMN photo_path TEXT")
            Log.d("DB_UPGRADE", "New column added")
            if (oldVersion < 3) {
                db?.execSQL("ALTER TABLE mygarden ADD COLUMN last_watered INTEGER")
                Log.d("DB_UPGRADE", "Added column: last_watered")
            }
        }

    }

    //function to add the specie found by IA to the database
    fun updatePlantSpecieIA(idplant : Int, species: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply{
            put("plant_specie_IA", species)
        }

        val RowsAffected = db.update(
            "mygarden",
            contentValues,
            "idplant = ?",
            arrayOf(idplant.toString())
        )


        db.close()

        //Checking if the update worked in the logcat
        if(RowsAffected >0){
            Log.d("Update db", "The row was successfully updated with the IA specie, the ID modified is:$idplant")
        }
        else{
            Log.d("Update db", "No modifications were made in the db for the plant : $idplant")
        }

    }


}