package red.line.callino.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CallinoDao {
    // Call Themes
    @Query("SELECT * FROM call_themes")
    fun getAllThemesFlow(): Flow<List<CallTheme>>

    @Query("SELECT * FROM call_themes")
    suspend fun getAllThemes(): List<CallTheme>

    @Query("SELECT * FROM call_themes WHERE id = :themeId LIMIT 1")
    suspend fun getThemeById(themeId: String): CallTheme?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThemes(themes: List<CallTheme>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheme(theme: CallTheme)

    @Query("DELETE FROM call_themes WHERE id = :themeId")
    suspend fun deleteTheme(themeId: String)

    // Contact Themes
    @Query("SELECT * FROM contact_themes ORDER BY createdAt DESC")
    fun getAllContactThemesFlow(): Flow<List<ContactTheme>>

    @Query("SELECT * FROM contact_themes WHERE contactNumber = :number OR contactId = :number LIMIT 1")
    suspend fun getContactThemeForNumber(number: String): ContactTheme?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContactTheme(contactTheme: ContactTheme)

    @Query("DELETE FROM contact_themes WHERE contactId = :contactId")
    suspend fun deleteContactTheme(contactId: String)

    // User Media
    @Query("SELECT * FROM user_media ORDER BY addedAt DESC")
    fun getAllUserMediaFlow(): Flow<List<UserMedia>>

    @Query("SELECT * FROM user_media WHERE id = :id LIMIT 1")
    suspend fun getUserMediaById(id: String): UserMedia?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserMedia(media: UserMedia)

    @Update
    suspend fun updateUserMedia(media: UserMedia)

    @Query("DELETE FROM user_media WHERE id = :id")
    suspend fun deleteUserMedia(id: String)
}