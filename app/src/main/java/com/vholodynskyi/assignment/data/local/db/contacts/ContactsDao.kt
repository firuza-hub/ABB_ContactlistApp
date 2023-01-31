package com.vholodynskyi.assignment.data.local.db.contacts

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {
    @Query("SELECT * FROM Contact")
    fun getContacts(): Flow<List<DbContact>>

    @Update
    suspend fun update(contact: DbContact)

    @Insert(onConflict = IGNORE)
    suspend fun addAll(contact: List<DbContact>)

    @Query("UPDATE  Contact SET isDeleted = 1 WHERE userId = (:uuid)")
    suspend fun deleteById(uuid: String)

    @Query("UPDATE  Contact SET isDeleted = 0 WHERE userId = (:uuid)")
    suspend fun repairById(uuid: String)

    @Query("UPDATE Contact SET isDeleted = 1")
    suspend fun deleteAll()

    @Query("DELETE FROM Contact WHERE isDeleted = 0")
    suspend fun clearAllActive()

    @Query("SELECT * FROM Contact where userId == (:id) and isDeleted = 0")
    fun getActiveContactById(id: String):Flow<DbContact>

    @Query("SELECT (case when count(*) = 0 then 1 else 0 end) FROM Contact ct where isDeleted = 0")
    fun isDBEmpty(): Boolean
}