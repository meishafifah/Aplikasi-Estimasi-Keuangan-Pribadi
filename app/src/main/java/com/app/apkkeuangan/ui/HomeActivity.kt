package com.app.apkkeuangan.ui

import SharedPref
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.apkkeuangan.R
import com.app.apkkeuangan.adapter.LaporanAdapter
import com.app.apkkeuangan.database.AppDatabase
import com.app.apkkeuangan.databinding.ActivityHomeBinding
import com.app.apkkeuangan.entity.Laporan

class HomeActivity : AppCompatActivity(), LaporanAdapter.LaporanItemClickInterface {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var database: AppDatabase
    private lateinit var sharedPref: SharedPref
    private lateinit var laporanAdapter: LaporanAdapter
    private var uid = 0
    private var list = mutableListOf<Laporan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbout)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        database = AppDatabase.getInstance(applicationContext)
        sharedPref = SharedPref(this)
        uid = sharedPref.getUid()!!.toInt()
        binding.username.text = sharedPref.getUsername().toString()

        laporanAdapter = LaporanAdapter(list,this)
        binding.rvLaporan.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            if(list.isEmpty()){
                binding.rvLaporan.visibility = View.GONE
                binding.nolp.visibility = View.VISIBLE
            }else {
                binding.rvLaporan.visibility = View.VISIBLE
                binding.nolp.visibility = View.GONE
            }
            adapter = laporanAdapter
            laporanAdapter.notifyDataSetChanged()
        }

        binding.btninputlaporan.setOnClickListener {
            val intent = Intent(this, FormLaporanActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP ; Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    fun getData(){
        list.clear()
        list.addAll(database.laporanDao().getLaporan(uid))
        if(list.isEmpty()){
            binding.rvLaporan.visibility = View.GONE
            binding.nolp.visibility = View.VISIBLE
        }else {
            binding.rvLaporan.visibility = View.VISIBLE
            binding.nolp.visibility = View.GONE
        }
        laporanAdapter.notifyDataSetChanged()
    }

    override fun onDelete(position: Int) {
        database.laporanDao().deleteLaporan(list[position])
        getData()
    }

    override fun onUpdate(position: Int) {
        val intent = Intent(this, FormLaporanActivity::class.java)
        intent.putExtra("id", list[position].laporan_id)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbarmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnLogOut -> {
                val builder = android.app.AlertDialog.Builder(this)
                builder.setTitle("Peringatan !!! ")
                    .setMessage("Apakah Anda Ingin Log Out ? ")
                    .setPositiveButton("Yes") { dialog: DialogInterface?, which: Int ->
                        sharedPref.isLogOut()
                        Toast.makeText(this, "Sign Out Berhasil", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }.setNegativeButton("No") { dialog: DialogInterface, which: Int ->
                        dialog.cancel()
                    }.show()
            }
        }
        return true
    }
}