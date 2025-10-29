package com.example.lab2

data class SanPham(
    var maSanPham: String,
    var hinhAnh: Int,
    var tenSanPham: String,
    var loaiSanPham: String,
    var gia: Double,
    var giamGia: Int = 0
)
