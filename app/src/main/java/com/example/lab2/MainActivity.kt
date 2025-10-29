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
    
    private var sanPhamDangSua: SanPham? = null

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

        binding.btnThem.setOnClickListener { 
            if (sanPhamDangSua != null) {
                capNhatSP()
            } else {
                themSP()
            }
        }
        binding.rgLoai.setOnCheckedChangeListener { _, _ -> thongKe() }
    }

    private fun themSP() {
        val ten = binding.edtTen.text.toString().trim()
        val giaText = binding.edtGia.text.toString().trim()
        val giamText = binding.edtGiamGia.text.toString().trim()

        // Validate dữ liệu
        if (ten.isEmpty()) {
            binding.edtTen.error = "Vui lòng nhập tên sản phẩm!"
            binding.edtTen.requestFocus()
            return
        }
        
        if (giaText.isEmpty()) {
            binding.edtGia.error = "Vui lòng nhập giá sản phẩm!"
            binding.edtGia.requestFocus()
            return
        }

        // Kiểm tra đã chọn loại sản phẩm chưa (không phải "Tất cả")
        val loai = when (binding.rgLoai.checkedRadioButtonId) {
            R.id.rbMyPham -> "Mỹ phẩm"
            R.id.rbThoiTrang -> "Thời trang"
            R.id.rbGiaDung -> "Đồ gia dụng"
            R.id.rbTatCa -> {
                android.widget.Toast.makeText(this, "Vui lòng chọn loại sản phẩm (không chọn 'Tất cả')!", android.widget.Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                android.widget.Toast.makeText(this, "Vui lòng chọn loại sản phẩm!", android.widget.Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Tạo mã sản phẩm tự động
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
        
        // Hiển thị thông báo thành công với mã sản phẩm
        android.widget.Toast.makeText(this, "Thêm thành công! Mã SP: $ma", android.widget.Toast.LENGTH_SHORT).show()
        
        // Reset form
        resetForm()
        thongKe()
    }
    
    private fun capNhatSP() {
        val ten = binding.edtTen.text.toString().trim()
        val giaText = binding.edtGia.text.toString().trim()
        val giamText = binding.edtGiamGia.text.toString().trim()

        // Validate dữ liệu
        if (ten.isEmpty()) {
            binding.edtTen.error = "Vui lòng nhập tên sản phẩm!"
            binding.edtTen.requestFocus()
            return
        }
        
        if (giaText.isEmpty()) {
            binding.edtGia.error = "Vui lòng nhập giá sản phẩm!"
            binding.edtGia.requestFocus()
            return
        }

        // Kiểm tra đã chọn loại sản phẩm chưa (không phải "Tất cả")
        val loai = when (binding.rgLoai.checkedRadioButtonId) {
            R.id.rbMyPham -> "Mỹ phẩm"
            R.id.rbThoiTrang -> "Thời trang"
            R.id.rbGiaDung -> "Đồ gia dụng"
            R.id.rbTatCa -> {
                android.widget.Toast.makeText(this, "Vui lòng chọn loại sản phẩm (không chọn 'Tất cả')!", android.widget.Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                android.widget.Toast.makeText(this, "Vui lòng chọn loại sản phẩm!", android.widget.Toast.LENGTH_SHORT).show()
                return
            }
        }

        sanPhamDangSua?.let { sp ->
            val gia = giaText.toDoubleOrNull() ?: 0.0
            val giam = giamText.toIntOrNull() ?: 0
            val hinh = hinhArr[binding.spHinh.selectedItemPosition]

            // Cập nhật thông tin (không sửa mã sản phẩm)
            sp.tenSanPham = ten
            sp.loaiSanPham = loai
            sp.gia = gia
            sp.giamGia = giam
            sp.hinhAnh = hinh
            
            adapter.notifyDataSetChanged()
            
            // Hiển thị thông báo thành công với mã sản phẩm
            android.widget.Toast.makeText(this, "Cập nhật thành công! Mã SP: ${sp.maSanPham}", android.widget.Toast.LENGTH_SHORT).show()
            
            // Reset form
            resetForm()
            thongKe()
        }
    }
    
    private fun resetForm() {
        binding.edtTen.setText("")
        binding.edtGia.setText("")
        binding.edtGiamGia.setText("0")
        binding.spHinh.setSelection(0)
        binding.rgLoai.check(R.id.rbTatCa)
        binding.btnThem.text = "Thêm sản phẩm"
        sanPhamDangSua = null
        
        // Clear errors
        binding.edtTen.error = null
        binding.edtGia.error = null
    }

    private fun suaSP(sp: SanPham) {
        sanPhamDangSua = sp
        
        // Hiển thị thông tin sản phẩm lên form
        binding.edtTen.setText(sp.tenSanPham)
        binding.edtGia.setText(sp.gia.toString())
        binding.edtGiamGia.setText(sp.giamGia.toString())
        
        // Chọn hình ảnh
        val hinhIndex = hinhArr.indexOf(sp.hinhAnh)
        if (hinhIndex >= 0) {
            binding.spHinh.setSelection(hinhIndex)
        }
        
        // Chọn loại sản phẩm
        when (sp.loaiSanPham) {
            "Mỹ phẩm" -> binding.rgLoai.check(R.id.rbMyPham)
            "Thời trang" -> binding.rgLoai.check(R.id.rbThoiTrang)
            "Đồ gia dụng" -> binding.rgLoai.check(R.id.rbGiaDung)
        }
        
        // Thay đổi text của button
        binding.btnThem.text = "Cập nhật sản phẩm"
        
        // Hiển thị thông báo với mã sản phẩm (mã không được sửa)
        android.widget.Toast.makeText(this, "Đang sửa SP: ${sp.maSanPham} (Mã không thay đổi)", android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun xoaSP(sp: SanPham) {
        // Hiển thị hộp thoại xác nhận xóa
        android.app.AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc muốn xóa sản phẩm '${sp.tenSanPham}' (${sp.maSanPham})?")
            .setPositiveButton("Xóa") { _, _ ->
                listSP.remove(sp)
                adapter.notifyDataSetChanged()
                android.widget.Toast.makeText(this, "Đã xóa sản phẩm ${sp.maSanPham}", android.widget.Toast.LENGTH_SHORT).show()
                
                // Reset form nếu đang sửa sản phẩm này
                if (sanPhamDangSua == sp) {
                    resetForm()
                }
                
                thongKe()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun thongKe() {
        val loaiLoc = when (binding.rgLoai.checkedRadioButtonId) {
            R.id.rbMyPham -> "Mỹ phẩm"
            R.id.rbThoiTrang -> "Thời trang"
            R.id.rbGiaDung -> "Đồ gia dụng"
            R.id.rbTatCa -> "Tất cả"
            else -> "Tất cả"
        }

        val danhSachLoc = if (loaiLoc == "Tất cả") {
            listSP
        } else {
            listSP.filter { it.loaiSanPham == loaiLoc }
        }
        
        val soLuong = danhSachLoc.size
        val tongTien = danhSachLoc.sumOf { it.gia * (100 - it.giamGia) / 100 }
        
        val thongKeText = if (loaiLoc == "Tất cả") {
            "Tổng: $soLuong SP - ${String.format("%.0f", tongTien)} đ"
        } else {
            "$loaiLoc: $soLuong SP - ${String.format("%.0f", tongTien)} đ"
        }
        
        binding.tvTong.text = thongKeText
    }
}