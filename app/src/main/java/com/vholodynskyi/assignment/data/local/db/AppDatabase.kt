package com.vholodynskyi.assignment.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vholodynskyi.assignment.data.local.db.contacts.ContactsDao
import com.vholodynskyi.assignment.data.local.db.contacts.DbContact

@Database(entities = [DbContact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): ContactsDao
}
