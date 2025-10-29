package com.example.lab2

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SanPhamAdapter
    private val listSP = mutableListOf<SanPham>()

    private var demMP = 0
    private var demTT = 0
    private var demGD = 0

    private val hinhArr = arrayOf(
        R.drawable.hinh1,
        R.drawable.hinh2,
        R.drawable.hinh3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Spinner ảnh
        val spAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            arrayOf("Hình 1", "Hình 2", "Hình 3")
        )
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spHinh.adapter = spAdapter

        // RecyclerView
        adapter = SanPhamAdapter(listSP, { suaSP(it) }, { xoaSP(it) })
        binding.rvSanPham.layoutManager = LinearLayoutManager(this)
        binding.rvSanPham.adapter = adapter

        binding.btnThem.setOnClickListener { themSP() }
        binding.rgLoai.setOnCheckedChangeListener { _, _ -> thongKe() }
    }

    private fun themSP() {
        val ten = binding.edtTen.text.toString().trim()
        val giaText = binding.edtGia.text.toString().trim()
        val giamText = binding.edtGiamGia.text.toString().trim()

        if (ten.isEmpty() || giaText.isEmpty()) {
            binding.edtTen.error = "Không để trống!"
            return
        }

        val loai = when (binding.rgLoai.checkedRadioButtonId) {
            R.id.rbMyPham -> "Mỹ phẩm"
            R.id.rbThoiTrang -> "Thời trang"
            R.id.rbGiaDung -> "Đồ gia dụng"
            else -> "Mỹ phẩm"
        }

        val ma = when (loai) {
            "Mỹ phẩm" -> "MP${String.format("%03d", ++demMP)}"
            "Thời trang" -> "TT${String.format("%03d", ++demTT)}"
            "Đồ gia dụng" -> "GD${String.format("%03d", ++demGD)}"
            else -> ""
        }

        val gia = giaText.toDoubleOrNull() ?: 0.0
        val giam = giamText.toIntOrNull() ?: 0
        val hinh = hinhArr[binding.spHinh.selectedItemPosition]

        val sp = SanPham(ma, hinh, ten, loai, gia, giam)
        listSP.add(sp)
        adapter.notifyDataSetChanged()
        thongKe()
    }

    private fun suaSP(sp: SanPham) {
        binding.edtTen.setText(sp.tenSanPham)
        binding.edtGia.setText(sp.gia.toString())
        binding.edtGiamGia.setText(sp.giamGia.toString())
    }

    private fun xoaSP(sp: SanPham) {
        listSP.remove(sp)
        adapter.notifyDataSetChanged()
        thongKe()
    }

    private fun thongKe() {
        val loaiLoc = when (binding.rgLoai.checkedRadioButtonId) {
            R.id.rbMyPham -> "Mỹ phẩm"
            R.id.rbThoiTrang -> "Thời trang"
            R.id.rbGiaDung -> "Đồ gia dụng"
            else -> "Tất cả"
        }

        val danhSachLoc = if (loaiLoc == "Tất cả") listSP else listSP.filter { it.loaiSanPham == loaiLoc }
        val tong = danhSachLoc.sumOf { it.gia * (100 - it.giamGia) / 100 }
        binding.tvTong.text = "Tổng tiền: $tong"
    }
}