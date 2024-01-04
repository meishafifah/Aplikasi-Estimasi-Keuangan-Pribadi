package com.app.apkkeuangan.ui

import SharedPref
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.app.apkkeuangan.R
import com.app.apkkeuangan.database.AppDatabase
import com.app.apkkeuangan.databinding.ActivityFormLaporanBinding
import com.app.apkkeuangan.databinding.ActivityHomeBinding
import com.app.apkkeuangan.entity.Laporan
import com.app.apkkeuangan.utils.BitmapConverter

class FormLaporanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormLaporanBinding
    private lateinit var database: AppDatabase
    private lateinit var sharedPref: SharedPref
    private var uid = 0
    private var id = 0
    private lateinit var imageUri: Uri
    private var img:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getInstance(applicationContext)
        sharedPref = SharedPref(this)
        uid = sharedPref.getUid()!!.toInt()
        getData()

        binding.tvaddphoto.setOnClickListener {
            selectimage()
        }
        binding.btnsavelaporan.setOnClickListener {
            val bulan = binding.etbulan.text.toString()
            val pemasukkan = binding.etpemasukan.text.toString()
            val makan = binding.etmakan.text.toString()
            val bbm = binding.etbbm.text.toString()
            val liburan = binding.etliburan.text.toString()
            val lainnya = binding.etlain.text.toString()
            var photo = ""
            if (CheckValidation(bulan,pemasukkan,makan,bbm,liburan,lainnya)){
                val intent = intent.extras
                if (intent!=null){
                    database.laporanDao().updateLaporan(
                        Laporan(id,uid,bulan,pemasukkan.toInt(),makan.toInt(),bbm.toInt(),liburan.toInt(),lainnya.toInt(),img)
                    )
                    Toast.makeText(this, "Data Laporan Berhasil Di Update", Toast.LENGTH_SHORT).show()
                }else{
                    if(img==""){
                        photo = "null"
                        Log.e("photo",photo)
                    }else{
                        photo = img
                        Log.e("photo",photo)
                    }
                    database.laporanDao().insertLaporan(
                        Laporan(null,uid,bulan,pemasukkan.toInt(),makan.toInt(),bbm.toInt(),liburan.toInt(),lainnya.toInt(),photo)
                    )
                    Toast.makeText(this, "Data Laporan Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                }
                val i = Intent(this, HomeActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP ; Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(i)
            }
        }
    }

    private fun getData(){
        val intent = intent.extras
        if (intent!=null){
            val laporan = database.laporanDao().get(intent.getInt("id"))
            id = laporan.laporan_id!!
            binding.etbulan.setText(laporan.bulan)
            binding.etpemasukan.setText(laporan.pemasukkan.toString())
            binding.etmakan.setText(laporan.makan.toString())
            binding.etbbm.setText(laporan.bbm.toString())
            binding.etliburan.setText(laporan.liburan.toString())
            binding.etlain.setText(laporan.lainnya.toString())
            img = laporan.photo.toString()
            if (img=="null"){
                binding.ivphoto.setImageResource(R.drawable.photo)
            }else{
                val bitmap = BitmapConverter.converterStringToBitmap(img)
                binding.ivphoto.setImageBitmap(bitmap)
            }
        }
    }
    private fun selectimage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            val inputStream = this.contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val bitmapstring = BitmapConverter.converterBitmapToString(bitmap)
            binding.ivphoto.setImageBitmap(bitmap)
            img = bitmapstring
        }
    }

    private fun CheckValidation(bulan: String, pemasukan: String, makan: String, bbm: String, liburan: String, lainnya: String): Boolean {
        if(bulan.isEmpty()){
            binding.etbulan.error = "Tidak Boleh Kosong"
            binding.etbulan.requestFocus()
        } else if (pemasukan.isEmpty()) {
            binding.etpemasukan.error = "Tidak Boleh Kosong"
            binding.etpemasukan.requestFocus()
        } else if (makan.isEmpty()){
            binding.etmakan.error = "Tidak Boleh Kosong"
            binding.etmakan.requestFocus()
        } else if (bbm.isEmpty()){
            binding.etbbm.error = "Tidak Boleh Kosong"
            binding.etbbm.requestFocus()
        } else if (liburan.isEmpty()){
            binding.etliburan.error = "Tidak Boleh Kosong"
            binding.etliburan.requestFocus()
        } else if (lainnya.isEmpty()){
            binding.etlain.error = "Tidak Boleh Kosong"
            binding.etlain.requestFocus()
        }else{
            binding.etbulan.error = null
            return true
        }
        Toast.makeText(this, "Penambahan atau Update Laporan Gagal", Toast.LENGTH_SHORT).show()
        return false
    }

}