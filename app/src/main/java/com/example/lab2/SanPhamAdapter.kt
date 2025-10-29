package com.example.lab2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.databinding.ItemSanphamBinding

class SanPhamAdapter(
    private val list: MutableList<SanPham>,
    private val onEdit: (SanPham) -> Unit,
    private val onDelete: (SanPham) -> Unit
) : RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder>() {


    inner class SanPhamViewHolder(val binding: ItemSanphamBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SanPhamViewHolder {
        val binding = ItemSanphamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SanPhamViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: SanPhamViewHolder, position: Int) {
        val sp = list[position]
        with(holder.binding) {
            tvMa.text = sp.maSanPham
            tvTen.text = sp.tenSanPham
            tvLoai.text = sp.loaiSanPham
            tvGia.text = "Giá: ${sp.gia} (-${sp.giamGia}%)"
            imgSanPham.setImageResource(sp.hinhAnh)

            root.setOnClickListener { onEdit(sp) }
            root.setOnLongClickListener {
                onDelete(sp)
                true
            }
        }
    }
}
