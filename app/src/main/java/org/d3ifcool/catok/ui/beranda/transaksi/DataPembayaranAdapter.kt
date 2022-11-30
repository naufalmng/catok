package org.d3ifcool.catok.ui.beranda.transaksi

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity
import org.d3ifcool.catok.core.data.source.local.model.DataPembayaran
import org.d3ifcool.catok.databinding.ItemDataPembayaranBinding
import org.d3ifcool.catok.utils.getCurrentDate
import org.d3ifcool.catok.utils.toRupiahFormat
import kotlin.math.abs

class DataPembayaranAdapter() : RecyclerView.Adapter<DataPembayaranAdapter.ViewHolder>() {

    private var dataPembayaran : MutableList<DataPembayaran>? = null

    fun clearData(){
        dataPembayaran?.clear()
        notifyDataSetChanged()
    }

    fun updateData(produkEntity: MutableList<DataPembayaran>){
        this.dataPembayaran?.clear()
        this.dataPembayaran = produkEntity
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemDataPembayaranBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDataPembayaranBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataPembayaran?.size?.plus(1) ?: 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rowPos = holder.bindingAdapterPosition
        val context = holder.itemView.context
        if(rowPos == 0){
            with(holder.binding){
                with(context){
                    tanggal.text = getCurrentDate()
                    layoutTanggal.visibility = View.VISIBLE
                    nomor.text = "Id"
                    produk.text = getString(org.d3ifcool.catok.R.string.produk)
                    qty.text = getString(org.d3ifcool.catok.R.string.qty)
                    harga.text = getString(org.d3ifcool.catok.R.string.harga2)
                }
            }

        }else{
            with(holder.binding){
                divider2.visibility = View.GONE
                layoutTanggal.visibility = View.GONE
                endDivider.visibility = View.GONE
                nomor.text = dataPembayaran?.get(rowPos-1)?.nomor.toString()
                produk.text = dataPembayaran?.get(rowPos-1)?.produk
                qty.text = dataPembayaran?.get(rowPos-1)?.qty.toString()
                harga.text = dataPembayaran?.get(rowPos-1)?.harga?.toRupiahFormat()
            }
        }
    }
}