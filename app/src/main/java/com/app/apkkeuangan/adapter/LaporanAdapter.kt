package com.app.apkkeuangan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.apkkeuangan.R
import com.app.apkkeuangan.entity.Laporan
import com.app.apkkeuangan.ui.HomeActivity
import com.app.apkkeuangan.utils.BitmapConverter
import java.text.NumberFormat
import java.util.Locale

class LaporanAdapter(private var listlaporan: List<Laporan>, private val laporanItemClickListener: HomeActivity) : RecyclerView.Adapter<LaporanAdapter.LaporanViewHolder>() {

    inner class LaporanViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val bulan = view.findViewById<TextView>(R.id.tvbulan)
        val pemasukan = view.findViewById<TextView>(R.id.tvpemasukkan)
        val pengeluaran = view.findViewById<TextView>(R.id.tvpengeluaran)
        val sisa = view.findViewById<TextView>(R.id.tvsisa)
        val photo = view.findViewById<ImageView>(R.id.ivitemphoto)
        val edit = view.findViewById<ImageView>(R.id.ivedit)
        val hapus = view.findViewById<ImageView>(R.id.ivdelete)
    }

    interface LaporanItemClickInterface {
        fun onDelete(position: Int)
        fun onUpdate(position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaporanAdapter.LaporanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_laporan, parent, false)
        return LaporanViewHolder(view)
    }

    override fun onBindViewHolder(holder: LaporanAdapter.LaporanViewHolder, position: Int) {
        holder.bulan.text = "Bulan " + listlaporan[position].bulan
        val pemasukkan = listlaporan[position].pemasukkan
        val RpPemasukkan = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(pemasukkan)
        holder.pemasukan.text = "Total Pemasukan\n$RpPemasukkan"

        val makan = listlaporan[position].makan!!
        val bbm = listlaporan[position].bbm!!
        val liburan = listlaporan[position].liburan!!
        val lainnya = listlaporan[position].lainnya!!
        val pengeluaran = makan + bbm + lainnya + liburan
        val RpPengeluaran = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(pengeluaran)
        holder.pengeluaran.text = "Total Pengeluaran\n$RpPengeluaran"

        var sisa = pemasukkan!! - pengeluaran
        if (sisa<=0){
            sisa = 0
        }else{
            sisa = sisa
        }
        val RpSisa = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(sisa)
        holder.sisa.text = "Total Sisa\n$RpSisa"

        val photo = listlaporan[position].photo
        if (photo=="null"){
            holder.photo.setImageResource(R.drawable.photo)
        }else{
            val bitmap = BitmapConverter.converterStringToBitmap(listlaporan[position].photo.toString())
            holder.photo.setImageBitmap(bitmap)
        }

        holder.hapus.setOnClickListener {
            laporanItemClickListener.onDelete(position)
        }

        holder.edit.setOnClickListener {
            laporanItemClickListener.onUpdate(position)
        }
    }

    override fun getItemCount(): Int {
        return listlaporan.size
    }
}