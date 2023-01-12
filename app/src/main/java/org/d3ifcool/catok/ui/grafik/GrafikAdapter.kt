package org.d3ifcool.catok.ui.grafik

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.catok.core.data.source.local.entities.GrafikEntity
import org.d3ifcool.catok.databinding.ItemGrafikBinding
import org.d3ifcool.catok.utils.toRupiahFormatV2
import java.text.SimpleDateFormat
import java.util.*

class GrafikAdapter : RecyclerView.Adapter<GrafikAdapter.ViewHolder>() {

    private val data = mutableListOf<GrafikEntity>()
    val formatTanggalLengkap = SimpleDateFormat("EEEE, d MMMM yyyy HH:mm", Locale("id", "ID"))

    inner class ViewHolder(val binding: ItemGrafikBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(grafikEntity: GrafikEntity) {
            with(binding){
                tanggalTextView.text = formatTanggalLengkap.format(grafikEntity.tanggal)
                totalTransaksi.text = grafikEntity.totalTransaksi.toRupiahFormatV2()
            }
        }
    }

    fun getDate(position: Int): Long {
        return data[position].tanggal
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<GrafikEntity>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGrafikBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(data[data.size-position-1])
    }
}