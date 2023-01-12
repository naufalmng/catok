package org.d3ifcool.catok.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream

class Converters {
    private var gson: Gson = Gson()

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray{
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream)
        return outputStream.toByteArray()
    }
    @TypeConverter
    fun toBitMap(byteArray: ByteArray): Bitmap{
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }

    @TypeConverter
    fun listIntToString(data: List<Int>): String{
        return gson.toJson(data)
    }
    @TypeConverter
    fun stringToListInt(data: String): List<Int>{
        return gson.fromJson(data,object : TypeToken<List<Int>>(){}.type)
    }

}