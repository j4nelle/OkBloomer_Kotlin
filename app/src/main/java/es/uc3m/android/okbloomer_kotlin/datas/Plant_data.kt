package es.uc3m.android.okbloomer_kotlin.datas

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class PlantData(context: Context)
    : SQLiteOpenHelper(context, "myplants.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
    db.execSQL("CREATE TABLE mygarden("+
                "idplant INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "plantnickname TEXT,"+
                "plantspecie TEXT,"+
                "wateringfrequency FLOAT,"+
            "typo INTEGER,"+
            "photopath TEXT)")
    }

    fun addingnewplant(plantnickname : String, plantspecie: String, wateringfrequency : Float, typo : Int, photopath : String) : Long{
        val db = this.writableDatabase
        //db.execSQL("INSERT INTO ...")
        val contentValues = ContentValues().apply {
            put("plant_nickname",plantnickname)
            put("plant_specie", plantspecie)
            put("watering_frequency", wateringfrequency)
            put("typo", typo)
            put("photo_path", photopath)
        }

        // to see the inserted values
        Log.d("DB_INSERT", "Inserting: $plantnickname, $plantspecie, $wateringfrequency, $typo, $photopath")

        val autonumeric = db.insert("mygarden", null, contentValues)

        if (autonumeric == -1L) {
            Log.e("DB_ERROR", "Échec de l'insertion des données")
        } else {
            Log.d("DB_SUCCESS", "Insertion réussie, id: $autonumeric")
        }
        return autonumeric
    }

    fun deletingaplant(idplant : String): Boolean {
        val db = this.writableDatabase
        val rowdeleted = db.delete("mygarden", "idplant = ?", arrayOf(idplant))
        db.close()

        //tests messages
        if (rowdeleted > 0) {
            Log.d("Success deletion", "Plant with ID $idplant deleted successfully")
        } else{
            Log.e("Error deletion", "Deletion failed")
        }
        return rowdeleted > 0
    }

    fun plantSelect(plantdata : PlantData): Cursor{
        val db = plantdata.readableDatabase
        val sql = "SELECT * from mygarden ORDER BY idplant DESC" //selects every object from the database mygarden
        return db.rawQuery(sql, null)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Add the new column
            db?.execSQL("ALTER TABLE mygarden ADD COLUMN photo_path TEXT")
            Log.d("DB_UPGRADE", "New column added")
        }
    }

}