package com.dobbleshop.neovision.data.local

import androidx.room.TypeConverter
import com.dobbleshop.neovision.data.model.*

/**
 * Room type converters for enum types
 */
class Converters {
    
    @TypeConverter
    fun fromSpecies(value: Species): String = value.name
    
    @TypeConverter
    fun toSpecies(value: String): Species = Species.valueOf(value)
    
    @TypeConverter
    fun fromSex(value: Sex?): String? = value?.name
    
    @TypeConverter
    fun toSex(value: String?): Sex? = value?.let { Sex.valueOf(it) }
    
    @TypeConverter
    fun fromActivityLevel(value: ActivityLevel): String = value.name
    
    @TypeConverter
    fun toActivityLevel(value: String): ActivityLevel = ActivityLevel.valueOf(value)
    
    @TypeConverter
    fun fromNutritionalGoal(value: NutritionalGoal): String = value.name
    
    @TypeConverter
    fun toNutritionalGoal(value: String): NutritionalGoal = NutritionalGoal.valueOf(value)
}
