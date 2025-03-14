package es.uc3m.android.okbloomer_kotlin.datas

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class Plant_data(context: Context)
    : SQLiteOpenHelper(context, "myplants.db", null, 1) {


    override fun onCreate(db: SQLiteDatabase) {
    db.execSQL("CREATE TABLE mygarden("+
                "idplant INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "plant_nickname TEXT,"+
                "plant_specie TEXT,"+
                "watering_frequency FLOAT,"+
            "typo INTEGER)")
    }

    fun adding_new_plant(plant_nickname : String, plant_specie: String, watering_frequency : Float, typo : Int) : Long{
        val db = this.writableDatabase
        //db.execSQL("INSERT INTO ...")
        val contentValues = ContentValues().apply {
            put("plant_nickname",plant_nickname)
            put("plant_specie", plant_specie)
            put("watering_frequency", watering_frequency)
            put("typo", typo)
        }

        val autonumeric = db.insert("mygarden", null, contentValues)

        if (autonumeric == -1L) {
            Log.e("DB_ERROR", "Échec de l'insertion des données")
        } else {
            Log.d("DB_SUCCESS", "Insertion réussie, id: $autonumeric")
        }
        return autonumeric
    }

    fun deleting_a_plant(idplant : String , plant_nickname : String, plant_specie: String, watering_frequency : Float, typo : Int){
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("idplant", idplant)
            put("plant_nickname",plant_nickname)
            put("plant_specie", plant_specie)
            put("watering_frequency", watering_frequency)
            put("typo", typo)
        }

        val autonumeric = db.delete("mygarden",  idplant  + "=", arrayOf(idplant))
        db.close()

    }

    fun plantSelect(plant_data : Plant_data): Cursor{
        val db = plant_data.readableDatabase
        val sql = "SELECT * from mygarden ORDER BY idplant DESC" //selects every object from the database mygarden
        return db.rawQuery(sql, null)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

}