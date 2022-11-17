package org.d3ifcool.catok.ui.beranda.produk

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity
import org.d3ifcool.catok.databinding.ItemDataProdukGridBinding
import org.d3ifcool.catok.databinding.ItemDataProdukLinearBinding
import java.util.*
import kotlin.collections.ArrayList
@SuppressLint("NotifyDataSetChanged")
@Suppress("UNCHECKED_CAST")
class DataProdukAdapter(private val isLinearLayoutManager: Boolean = true, private val handler: ClickHandler): ListAdapter<ProdukEntity,RecyclerView.ViewHolder>(diffCallback),Filterable {
    companion object {
        val selectionIds = ArrayList<Int>()
        val diffCallback = object : DiffUtil.ItemCallback<ProdukEntity>() {
            override fun areItemsTheSame(oldItem: ProdukEntity, newItem: ProdukEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProdukEntity, newItem: ProdukEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    var produkList = mutableListOf<ProdukEntity>()
    var produkFilterList : MutableList<ProdukEntity> = ArrayList()
//    lateinit var onItemClick: ((ProdukEntity) -> Unit)

    fun updateData(produkEntity: ArrayList<ProdukEntity>){
        produkList.clear()
        produkList.addAll(produkEntity)
        produkFilterList = produkEntity
        submitList(produkEntity)
//        notifyItemRangeRemoved(0, produkList.size)

    }

    fun getAllSelection(){
        selectionIds.clear()
        for(i in produkFilterList.indices){
            val id = produkFilterList[i].id
            selectionIds.add(id.toInt())
            notifyDataSetChanged()
        }
        handler.isAllItemSelected(true)

    }

    fun toggleSelection(position: Int){
        if(getSelection().size != produkFilterList.size) handler.isAllItemSelected(false)
        val id = getItem(position).id
        Log.d("DataProdukAdapter", "toggleSelection: $id")
        if(selectionIds.contains(id.toInt())) selectionIds.remove(id.toInt())
        else selectionIds.add(id.toInt())
        notifyDataSetChanged()
    }

    fun getSelection(): List<Int>{
        return selectionIds
    }

    @SuppressLint("NotifyDataSetChanged")
    fun resetSelection(){
        selectionIds.clear()
        notifyDataSetChanged()
    }

    class GridViewHolder private constructor(val binding: ItemDataProdukGridBinding): RecyclerView.ViewHolder(binding.root) {
        companion object{
            fun from(parent: ViewGroup): GridViewHolder{
                return GridViewHolder(ItemDataProdukGridBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
        }
        fun bind(position: Int, produk: ArrayList<ProdukEntity>, handler: ClickHandler){
            val (id,barcode,namaProduk,deskripsi,hargaBeli,hargaJual,satuan,stok) = produk[position]
            binding.rootView.isSelected = selectionIds.contains(id.toInt())
            itemView.isSelected = selectionIds.contains(id.toInt())
            binding.nomor.text = id.toString()
            binding.namaProduk.text = namaProduk
            binding.deskripsi.text = deskripsi
            binding.hargaBeli.text = itemView.context.getString(R.string.modal_arg_2,hargaBeli,satuan.toString())
            binding.hargaJual.text = itemView.context.getString(R.string.harga_jual_arg_2,hargaJual,satuan.toString())
            binding.stok.text = itemView.context.getString(R.string.stok_arg,stok.toString())
            itemView.setOnClickListener {
                handler.onClick(position, produk)
            }
            itemView.setOnLongClickListener {
                handler.onLongClick(position,produk)
            }
        }
    }
    class LinearViewHolder private constructor(val binding: ItemDataProdukLinearBinding): RecyclerView.ViewHolder(binding.root) {
        companion object{
            fun from(parent: ViewGroup): LinearViewHolder{
                return LinearViewHolder(ItemDataProdukLinearBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
        }
        fun bind(position: Int, produk: ArrayList<ProdukEntity>, handler: ClickHandler){
            val (id,barcode,namaProduk,deskripsi,hargaBeli,hargaJual,satuan,stok) = produk[position]
            binding.rootView.setOnClickListener{
//                    onItemClick.invoke(data[position])
            }
            itemView.isSelected = selectionIds.contains(id.toInt())
            binding.nomor.text = id.toString()
            binding.namaProduk.text = namaProduk
            binding.deskripsi.text = deskripsi
            binding.hargaBeli.text = itemView.context.getString(R.string.jumlah_modal_arg,hargaBeli,satuan.toString())
            binding.hargaJual.text = itemView.context.getString(R.string.harga_jual_arg,hargaJual,satuan.toString())
            binding.stok.text = itemView.context.getString(R.string.stok_arg,stok.toString())
            itemView.setOnClickListener {
                handler.onClick(position, produk)
            }
            itemView.setOnLongClickListener { handler.onLongClick(position,produk) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val gridLayoutManager = GridViewHolder.from(parent)
        val linearLayoutManager = LinearViewHolder.from(parent)
        return if(isLinearLayoutManager) linearLayoutManager else gridLayoutManager
    }

    override fun getItemCount(): Int {
        return produkFilterList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is LinearViewHolder -> {
                holder.bind(holder.adapterPosition,produkFilterList as ArrayList<ProdukEntity>, handler)
            }
            is GridViewHolder -> {
                holder.bind(holder.adapterPosition,produkFilterList as ArrayList<ProdukEntity>, handler)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(char: CharSequence?): FilterResults {
                val charSearch = char.toString()
                if(charSearch.isEmpty()) produkFilterList = produkList
                else{
                    val resultList = ArrayList<ProdukEntity>()
                    for(item in produkList){
                        if(item.namaProduk.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            item.deskripsi.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))){
                            resultList.add(item)
                        }
                    }
                    produkFilterList = resultList
                }
                return FilterResults().apply { values = produkFilterList }
            }

            override fun publishResults(constraint: CharSequence?, result: FilterResults?) {
                produkFilterList = if(result?.values == null) ArrayList()
                else result.values as ArrayList<ProdukEntity>
//                notifyItemRangeRemoved(0, produkFilterList.size)
                notifyDataSetChanged()

            }
        }
    }
    interface ClickHandler {
        fun onClick(position: Int, produk: ArrayList<ProdukEntity>)
        fun onLongClick(position: Int, produk: ArrayList<ProdukEntity>): Boolean
        fun isAllItemSelected(isAllItemSelected: Boolean = false)
    }
}