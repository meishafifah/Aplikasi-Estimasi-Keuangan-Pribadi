package com.app.apkkeuangan.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Laporan(

    @PrimaryKey(autoGenerate = true)
    var laporan_id: Int? = null,

    @ColumnInfo("User_ID")
    var user_id: Int? = null,

    @ColumnInfo("Bulan")
    var bulan: String? = null,

    @ColumnInfo("Pemasukkan")
    var pemasukkan: Int? = null,

    @ColumnInfo("Pengeluaran_Makan")
    var makan: Int? = null,

    @ColumnInfo("Pengeluaran_BBM")
    var bbm: Int? = null,

    @ColumnInfo("Pengeluaran_Liburan")
    var liburan: Int? = null,

    @ColumnInfo("Pengeluaran_Lainnya")
    var lainnya: Int? = null,

    @ColumnInfo("Foto_Laporan")
    var photo: String? = null
)
