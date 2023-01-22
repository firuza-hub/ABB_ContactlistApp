package com.vholodynskyi.assignment.data.local.db.contacts

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
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
    suspend fun deleteById(uuid: Int)

    @Query("UPDATE Contact SET isDeleted = 1")
    suspend fun deleteAll()

    @Query("DELETE FROM Contact WHERE isDeleted = 0")
    suspend fun clearAllActive()
}