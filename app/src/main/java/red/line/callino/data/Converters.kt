package red.line.callino.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromThemeType(value: ThemeType): String = value.name

    @TypeConverter
    fun toThemeType(value: String): ThemeType = try {
        ThemeType.valueOf(value)
    } catch (e: Exception) {
        ThemeType.IMAGE
    }
}
