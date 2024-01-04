package com.app.apkkeuangan.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.app.apkkeuangan.entity.Laporan

@Dao
interface LaporanDao {
    @Query("SELECT * FROM Laporan  WHERE user_id = :userid")
    fun getLaporan(userid : Int): List<Laporan>

    @Query("SELECT * FROM Laporan  WHERE laporan_id = :lapid")
    fun get(lapid : Int): Laporan

    @Insert
    fun insertLaporan(laporan: Laporan)

    @Update
    fun updateLaporan(laporan: Laporan)

    @Delete
    fun deleteLaporan(laporan: Laporan)
}