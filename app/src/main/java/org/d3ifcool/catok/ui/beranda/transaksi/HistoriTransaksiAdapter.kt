package org.d3ifcool.catok.ui.beranda.transaksi

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.source.local.entities.HistoriTransaksiEntity
import org.d3ifcool.catok.databinding.ItemDataHistoriTransaksiGridBinding
import org.d3ifcool.catok.databinding.ItemDataHistoriTransaksiLinearBinding
import org.d3ifcool.catok.utils.addUnderline
import org.d3ifcool.catok.utils.toRupiahFormat
import java.util.*

@SuppressLint("NotifyDataSetChanged")
@Suppress("UNCHECKED_CAST")
class HistoriTransaksiAdapter(private val isLinearLayoutManager: Boolean = true, private val handler: ClickHandler): ListAdapter<HistoriTransaksiEntity,RecyclerView.ViewHolder>(diffCallback),Filterable {
    companion object {
        val selectionIds = ArrayList<String>()
        val diffCallback = object : DiffUtil.ItemCallback<HistoriTransaksiEntity>() {
            override fun areItemsTheSame(oldItem: HistoriTransaksiEntity, newItem: HistoriTransaksiEntity): Boolean {
                return oldItem.id_histori == newItem.id_histori
            }

            override fun areContentsTheSame(oldItem: HistoriTransaksiEntity, newItem: HistoriTransaksiEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    var historiTransaksiList = mutableListOf<HistoriTransaksiEntity>()
    var historiHistoriTransaksiFilterList : MutableList<HistoriTransaksiEntity> = ArrayList()

    fun updateData(historiHistoriTransaksi: ArrayList<HistoriTransaksiEntity>){
        Log.d("HistoriTransaksi", "updateData: $historiHistoriTransaksi")
        historiTransaksiList.clear()
        historiTransaksiList.addAll(historiHistoriTransaksi)
        historiHistoriTransaksiFilterList = historiHistoriTransaksi
        submitList(historiHistoriTransaksi)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun resetSelection(){
        selectionIds.clear()
        notifyDataSetChanged()
    }

    class GridViewHolder private constructor(val binding: ItemDataHistoriTransaksiGridBinding): RecyclerView.ViewHolder(binding.root) {
        companion object{
            fun from(parent: ViewGroup): GridViewHolder{
                return GridViewHolder(ItemDataHistoriTransaksiGridBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
        }
        fun bind(position: Int, historiTransaksiData: ArrayList<HistoriTransaksiEntity>, handler: ClickHandler){
            val (id_transaksi,total,diskon,bayar,kembalian,catatan,produkDibeli,jumlahProdukDibeli,pembayaran,statusBayar,tanggal) = historiTransaksiData[position]
            binding.rootView.isSelected = selectionIds.contains(id_transaksi)
            itemView.isSelected = selectionIds.contains(id_transaksi)
            binding.total.text = total.toRupiahFormat().uppercase(Locale("id","ID")).addUnderline()
            binding.tanggal.text = tanggal
            binding.invoice.text = id_transaksi
            binding.jenisPembayaran.text = pembayaran.addUnderline()
            binding.statusBayar.text = itemView.context.getString(R.string.status_bayar_args,statusBayar)
            itemView.setOnClickListener {
                handler.onClick(historiTransaksiData[position])
            }
        }
    }
    class LinearViewHolder private constructor(val binding: ItemDataHistoriTransaksiLinearBinding): RecyclerView.ViewHolder(binding.root) {
        companion object{
            fun from(parent: ViewGroup): LinearViewHolder{
                return LinearViewHolder(ItemDataHistoriTransaksiLinearBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
        }
        fun bind(position: Int, historiTransaksiData: ArrayList<HistoriTransaksiEntity>, handler: ClickHandler){
            val (id_transaksi,total,diskon,bayar,kembalian,catatan,produkDibeli,jumlahProdukDibeli,pembayaran,statusBayar,tanggal) = historiTransaksiData[position]
            val textColorRes = if(statusBayar == itemView.context.getString(R.string.lunas)) ContextCompat.getColor(itemView.context,R.color.green) else ContextCompat.getColor(itemView.context,R.color.yellow)
            binding.total.text = total.toRupiahFormat().uppercase(Locale("id","ID")).addUnderline()
            binding.tanggal.text = tanggal
            binding.invoice.text = id_transaksi
            binding.jenisPembayaran.text = pembayaran.addUnderline()
            binding.statusBayar.setTextColor(textColorRes)
            binding.statusBayar.text = itemView.context.getString(R.string.status_bayar_args,statusBayar)
            itemView.isSelected = selectionIds.contains(id_transaksi)

            itemView.setOnClickListener {
                handler.onClick(historiTransaksiData[position])
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val gridLayoutManager = GridViewHolder.from(parent)
        val linearLayoutManager = LinearViewHolder.from(parent)
        return if(isLinearLayoutManager) linearLayoutManager else gridLayoutManager
    }

    override fun getItemCount(): Int {
        return historiHistoriTransaksiFilterList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is LinearViewHolder -> {
                holder.bind(holder.absoluteAdapterPosition,historiHistoriTransaksiFilterList as ArrayList<HistoriTransaksiEntity>, handler)
            }
            is GridViewHolder -> {
                holder.bind(holder.absoluteAdapterPosition,historiHistoriTransaksiFilterList as ArrayList<HistoriTransaksiEntity>, handler)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(char: CharSequence?): FilterResults {
                val charSearch = char.toString()
                historiHistoriTransaksiFilterList = if(charSearch.isEmpty()) historiTransaksiList
                else{
                    val resultList = ArrayList<HistoriTransaksiEntity>()
                    for(item in historiTransaksiList){
                        if(item.tanggal.trim().contains(charSearch.lowercase(Locale.ROOT)) ||
                            item.id_histori.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            item.statusBayar.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            item.pembayaran.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            item.total.toString().trim().lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))
                        ){
                            resultList.add(item)
                        }
                    }
                    resultList
                }
                return FilterResults().apply { values = historiHistoriTransaksiFilterList }
            }

            override fun publishResults(constraint: CharSequence?, result: FilterResults?) {
                historiHistoriTransaksiFilterList = if(result?.values == null) ArrayList()
                else result.values as ArrayList<HistoriTransaksiEntity>
                notifyDataSetChanged()

            }
        }
    }
    interface ClickHandler {
        fun onClick(transaksi: HistoriTransaksiEntity)
    }
}