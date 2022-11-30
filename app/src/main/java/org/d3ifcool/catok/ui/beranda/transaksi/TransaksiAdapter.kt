package org.d3ifcool.catok.ui.beranda.transaksi

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.source.local.entities.EditQuantityDialog
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity
import org.d3ifcool.catok.core.data.source.local.model.Produk
import org.d3ifcool.catok.databinding.DialogEditQuantityBinding
import org.d3ifcool.catok.databinding.ItemDataTransaksiGridBinding
import org.d3ifcool.catok.databinding.ItemDataTransaksiLinearBinding
import java.util.*


@SuppressLint("NotifyDataSetChanged")
@Suppress("UNCHECKED_CAST")
class TransaksiAdapter(private val isLinearLayoutManager: Boolean = true, private val handler: ClickHandler): ListAdapter<ProdukEntity, RecyclerView.ViewHolder>(diffCallback),
    Filterable {
    companion object {
        private var TAG = TransaksiAdapter.javaClass.name
        val selectionIds = ArrayList<Int>()
        val diffCallback = object : DiffUtil.ItemCallback<ProdukEntity>() {
            override fun areItemsTheSame(oldItem: ProdukEntity, newItem: ProdukEntity): Boolean {
                return oldItem.id_produk == newItem.id_produk
            }

            override fun areContentsTheSame(oldItem: ProdukEntity, newItem: ProdukEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
    var produkList = mutableListOf<ProdukEntity>()
    var produkFilterList : MutableList<ProdukEntity> = ArrayList()
    var produkQty = 0
//    lateinit var onItemClick: ((ProdukEntity) -> Unit)

    fun updateData(historiHistoriTransaksi: ArrayList<ProdukEntity>?){
        produkList.clear()
        if(historiHistoriTransaksi!=null){
            produkList.addAll(historiHistoriTransaksi)
            produkFilterList = historiHistoriTransaksi
        }
        submitList(historiHistoriTransaksi)
//        notifyItemRangeRemoved(0, produkList.size)

    }

    fun getAllSelection(){
        selectionIds.clear()
        for(i in produkFilterList.indices){
            val id = produkFilterList[i].id_produk
            selectionIds.add(id.toInt())
            notifyDataSetChanged()
        }
    }

    fun toggleSelection(position: Int){
        val id = getItem(position).id_produk
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

    class GridViewHolder private constructor(val binding: ItemDataTransaksiGridBinding): RecyclerView.ViewHolder(binding.root) {
        companion object{
            var produkIdList = mutableListOf<Int?>()
            var produkIdBeforeDeleted = MutableLiveData<Int?>(-1)
            var produkQtyList = mutableListOf<Int?>()
            var produkNameList = mutableListOf<String?>()
            var produkPriceList = mutableListOf<Double?>()
            fun from(parent: ViewGroup): GridViewHolder{
                return GridViewHolder(ItemDataTransaksiGridBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
        }
        fun bind(
            position: Int,
            produkData: ArrayList<ProdukEntity>,
            handler: ClickHandler,
        ){
            var counter = 0
            val dialog = EditQuantityDialog()
            var textColor = if(binding.counter.text.toString().toInt()>0) ContextCompat.getColor(itemView.context,
                R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
            val (id,barcode,namaProduk,deskripsi,modal,hargaJual,satuan,stok,tanggal) = produkData[position]
            binding.counter.setText(counter.toString())

            val textWatcher = object : TextWatcher{
                fun addDataProduk(){
                    produkIdList.add(id)
                    produkQtyList.add(produkIdList.indexOf(id),counter)
                    produkNameList.add(produkIdList.indexOf(id),namaProduk)
                    produkPriceList.add(produkIdList.indexOf(id),hargaJual*counter)
                }
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    Log.d(TAG, "afterTextChanged: = $counter")
                    if(!produkIdList.contains(id) && counter > 0){
                        addDataProduk()
                        val produk = Produk(
                            produkIdList,
                            produkNameList,
                            produkQtyList,
                            produkPriceList
                        )
                        Log.d(TAG, "bind: produkIdList = ${produkIdList}")
                        Log.d(TAG, "bind: produkNameList = ${produkNameList}")
                        Log.d(TAG, "bind: produkQtyList = ${produkQtyList}")
                        Log.d(TAG, "bind: hargaProduk = ${produkPriceList}")
                        binding.counter.setTextColor(textColor)
                        binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context,R.color.orange)
                        binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
                        handler.onPlusButtonClick(position,produkData,counter,produk)
                    }
                    if(produkIdList.contains(id) && counter > 1){
//                    produkIdList[produkIdList.indexOf(id)] = id
//                    Log.d(TAG, "id = ${}")
                        produkNameList[produkIdList.indexOf(id)] = produkData[position].namaProduk
                        produkQtyList[produkIdList.indexOf(id)] = counter
                        produkPriceList[produkIdList.indexOf(id)] = produkData[position].hargaJual*counter
                        val produk = Produk(
                            produkIdList,
                            produkNameList,
                            produkQtyList,
                            produkPriceList
                        )
                        Log.d(TAG, "bind: produkIdList = ${produkIdList}")
                        Log.d(TAG, "bind: produkNameList = ${produkNameList}")
                        Log.d(TAG, "bind: produkQtyList = ${produkQtyList}")
                        Log.d(TAG, "bind: hargaProduk = ${produkPriceList}")
                        binding.counter.setTextColor(textColor)
                        binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context,R.color.orange)
                        binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
                        handler.onPlusButtonClick(position,produkData,counter,produk)
                    }

                    if(counter == 0 && binding.counter.text.toString().toInt()==0) {
                        Log.d(TAG, "afterTextChanged: masuk = 0")
                        binding.counter.setTextColor(ContextCompat.getColorStateList(itemView.context,R.color.abuB))
                        binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context,R.color.abuB)
                        binding.min.isEnabled = false
                        if(produkIdList.contains(id)){
                            produkNameList.removeAt(produkIdList.indexOf(id))
                            produkPriceList.removeAt(produkIdList.indexOf(id))
                            produkQtyList.removeAt(produkIdList.indexOf(id))
                            produkIdList.removeAt(produkIdList.indexOf(id))
                        }else{

                        }
                        val produk = Produk(
                            produkIdList,
                            produkNameList,
                            produkQtyList,
                            produkPriceList
                        )
                        Log.d(TAG, "bind: produkIdList = ${produkIdList}")
                        Log.d(TAG, "bind: produkIdList = ${produkNameList}")
                        Log.d(TAG, "bind: produkQtyList = ${produkQtyList}")
                        Log.d(TAG, "bind: hargaProduk = ${produkPriceList}")

                        binding.rootView.background = if(binding.counter.text.toString().toInt()>=1) ContextCompat.getDrawable(itemView.context,R.drawable.item_on_long_click_bg) else ContextCompat.getDrawable(itemView.context,R.drawable.rectangle_10r)

                        binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
                        handler.onMinButtonClick(position,produkData,counter,produk)
                    }

//                    binding.counter.setTextColor(textColor)
//                    binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context,R.color.orange)

                    binding.rootView.background = if(binding.counter.text.toString().toInt()>=1) ContextCompat.getDrawable(itemView.context,R.drawable.item_on_long_click_bg) else ContextCompat.getDrawable(itemView.context,R.drawable.rectangle_10r)
                    binding.min.isEnabled = true

                }
            }
            binding.removeBtn.setOnClickListener{
                counter = 0
                binding.counter.setText("0")
                if(binding.counter.text.toString().toInt()>=1 || binding.counter.text.toString()!=""){
                    Log.d(TAG, "bind: $counter")
                    if(produkIdList.indexOf(id)!=-1){
                        produkNameList[produkIdList.indexOf(id)] = produkData[position].namaProduk
                        produkQtyList[produkIdList.indexOf(id)] = counter
                        produkPriceList[produkIdList.indexOf(id)] = produkData[position].hargaJual*counter
                    }else{
                        binding.min.isEnabled = true
                    }
                    val produk = Produk(
                        produkIdList,
                        produkNameList,
                        produkQtyList,
                        produkPriceList
                    )
                    handler.onMinButtonClick(position,produkData,counter,produk)
                    binding.counter.setText(counter.toString())
                    binding.counter.setTextColor(if(counter==0) ContextCompat.getColorStateList(itemView.context,R.color.abuB) else ContextCompat.getColorStateList(itemView.context,R.color.orange))
                    binding.min2.backgroundTintList = if(counter==0) ContextCompat.getColorStateList(itemView.context,R.color.abuB) else ContextCompat.getColorStateList(itemView.context,R.color.orange)
                    binding.rootView.background = if(binding.counter.text.toString().toInt()>=1) ContextCompat.getDrawable(itemView.context,R.drawable.item_on_long_click_bg) else ContextCompat.getDrawable(itemView.context,R.drawable.rectangle_10r)
                    binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
                }
                else{
                    binding.min.isEnabled = false
                    if(produkIdList.contains(id)){
                        produkNameList.removeAt(produkIdList.indexOf(id))
                        produkPriceList.removeAt(produkIdList.indexOf(id))
                        produkQtyList.removeAt(produkIdList.indexOf(id))
                        produkIdList.removeAt(produkIdList.indexOf(id))
                    }
                    val produk = Produk(
                        produkIdList,
                        produkNameList,
                        produkQtyList,
                        produkPriceList
                    )
                    binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context,R.color.abuB)
                    Log.d(TAG, "bind: produkIdList = ${produkIdList}")
                    Log.d(TAG, "bind: produkIdList = ${produkNameList}")
                    Log.d(TAG, "bind: produkQtyList = ${produkQtyList}")
                    Log.d(TAG, "bind: hargaProduk = ${produkPriceList}")
                    binding.counter.setText(counter.toString())
                    binding.counter.setTextColor(if(counter==0) ContextCompat.getColorStateList(itemView.context,R.color.abuB) else ContextCompat.getColorStateList(itemView.context,R.color.orange))
                    binding.rootView.background = if(binding.counter.text.toString().toInt()>=1) ContextCompat.getDrawable(itemView.context,R.drawable.item_on_long_click_bg) else ContextCompat.getDrawable(itemView.context,R.drawable.rectangle_10r)
                    binding.min2.backgroundTintList = if(counter==0) ContextCompat.getColorStateList(itemView.context,R.color.abuB) else ContextCompat.getColorStateList(itemView.context,R.color.orange)
                    binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
                    handler.onMinButtonClick(position,produkData,counter,produk)
                }
            }
            binding.editBtn.setOnClickListener{

                val inflater = LayoutInflater.from(itemView.context)
                val dialogBinding = DialogEditQuantityBinding.inflate(inflater,null, false)
                val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(itemView.context)
                builder.setTitle("Tambah Kuantitas")
                builder.setView(dialogBinding.root)
                val alertDialog: android.app.AlertDialog? = builder.create()
                builder.setPositiveButton("OK") { dialog, which ->
                    binding.counter.addTextChangedListener(textWatcher)
                    counter = dialogBinding.qty.text.toString().toInt()
                    binding.counter.setText(dialogBinding.qty.text.toString())
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    alertDialog?.dismiss()
                }
                builder.show()
            }

            binding.min.isEnabled = false
            binding.counter.addTextChangedListener(textWatcher)
            binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
            binding.namaProduk.text = namaProduk
            binding.deskripsi.text = deskripsi
            binding.harga.text = itemView.context.getString(R.string.harga_jual_arg_2,hargaJual,satuan.toString())
            binding.stok.text = itemView.context.getString(R.string.stok_arg,stok.toString())
            itemView.isSelected = selectionIds.contains(id.toInt())

            fun addDataProduk(){
                produkIdList.add(id)
                produkQtyList.add(produkIdList.indexOf(id),counter)
                produkNameList.add(produkIdList.indexOf(id),namaProduk)
                produkPriceList.add(produkIdList.indexOf(id),hargaJual*counter)
            }

            binding.plus.setOnClickListener{
                binding.counter.removeTextChangedListener(textWatcher)
                binding.counter.clearFocus()
                counter = if(binding.counter.text.toString() !="") binding.counter.text.toString().toInt()+1 else counter+1
                if(!produkIdList.contains(id) && counter > 0 || binding.counter.text.toString()==""){
                    addDataProduk()
                    val produk = Produk(
                        produkIdList,
                        produkNameList,
                        produkQtyList,
                        produkPriceList
                    )
                    Log.d(TAG, "bind: produkIdList = ${produkIdList}")
                    Log.d(TAG, "bind: produkNameList = ${produkNameList}")
                    Log.d(TAG, "bind: produkQtyList = ${produkQtyList}")
                    Log.d(TAG, "bind: hargaProduk = ${produkPriceList}")
                    binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
                    handler.onPlusButtonClick(position,produkData,counter,produk)
                }
                if(produkIdList.contains(id) && counter > 1){
//                    produkIdList[produkIdList.indexOf(id)] = id
//                    Log.d(TAG, "id = ${}")
                    produkNameList[produkIdList.indexOf(id)] = produkData[position].namaProduk
                    produkQtyList[produkIdList.indexOf(id)] = counter
                    produkPriceList[produkIdList.indexOf(id)] = produkData[position].hargaJual*counter
                    val produk = Produk(
                        produkIdList,
                        produkNameList,
                        produkQtyList,
                        produkPriceList
                    )
                    Log.d(TAG, "bind: produkIdList = ${produkIdList}")
                    Log.d(TAG, "bind: produkNameList = ${produkNameList}")
                    Log.d(TAG, "bind: produkQtyList = ${produkQtyList}")
                    Log.d(TAG, "bind: hargaProduk = ${produkPriceList}")
                    binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
                    handler.onPlusButtonClick(position,produkData,counter,produk)
                }
                binding.counter.setText(counter.toString())
                binding.counter.setTextColor(textColor)
                binding.rootView.background = if(binding.counter.text.toString().toInt()>=1) ContextCompat.getDrawable(itemView.context,R.drawable.item_on_long_click_bg) else ContextCompat.getDrawable(itemView.context,R.drawable.rectangle_10r)
                binding.min.isEnabled = true
                binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context,R.color.orange)
            }
            binding.min.setOnClickListener{
                binding.counter.removeTextChangedListener(textWatcher)
                binding.counter.clearFocus()
                if(counter == 0) binding.min.isEnabled = false
                else counter = binding.counter.text.toString().toInt() - 1
                if(binding.counter.text.toString().toInt()>=1 || binding.counter.text.toString()!=""){
                    Log.d(TAG, "bind: $counter")
                    if(produkIdList.indexOf(id)!=-1){
                        produkNameList[produkIdList.indexOf(id)] = produkData[position].namaProduk
                        produkQtyList[produkIdList.indexOf(id)] = counter
                        produkPriceList[produkIdList.indexOf(id)] = produkData[position].hargaJual*counter
                    }else{
                        binding.min.isEnabled = true
                    }
                    val produk = Produk(
                        produkIdList,
                        produkNameList,
                        produkQtyList,
                        produkPriceList
                    )
                    handler.onMinButtonClick(position,produkData,counter,produk)
                    binding.counter.setText(counter.toString())
                    binding.counter.setTextColor(if(counter==0) ContextCompat.getColorStateList(itemView.context,R.color.abuB) else ContextCompat.getColorStateList(itemView.context,R.color.orange))
                    binding.min2.backgroundTintList = if(counter==0) ContextCompat.getColorStateList(itemView.context,R.color.abuB) else ContextCompat.getColorStateList(itemView.context,R.color.orange)
                    binding.rootView.background = if(binding.counter.text.toString().toInt()>=1) ContextCompat.getDrawable(itemView.context,R.drawable.item_on_long_click_bg) else ContextCompat.getDrawable(itemView.context,R.drawable.rectangle_10r)
                    binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
                }
                else{
                    counter--
                    binding.min.isEnabled = false
                    if(produkIdList.contains(id)){
                        produkNameList.removeAt(produkIdList.indexOf(id))
                        produkPriceList.removeAt(produkIdList.indexOf(id))
                        produkQtyList.removeAt(produkIdList.indexOf(id))
                        produkIdList.removeAt(produkIdList.indexOf(id))
                    }
                    val produk = Produk(
                        produkIdList,
                        produkNameList,
                        produkQtyList,
                        produkPriceList
                    )
                    binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context,R.color.abuB)
                    Log.d(TAG, "bind: produkIdList = ${produkIdList}")
                    Log.d(TAG, "bind: produkIdList = ${produkNameList}")
                    Log.d(TAG, "bind: produkQtyList = ${produkQtyList}")
                    Log.d(TAG, "bind: hargaProduk = ${produkPriceList}")
                    binding.counter.setText(counter.toString())
                    binding.counter.setTextColor(if(counter==0) ContextCompat.getColorStateList(itemView.context,R.color.abuB) else ContextCompat.getColorStateList(itemView.context,R.color.orange))
                    binding.rootView.background = if(binding.counter.text.toString().toInt()>=1) ContextCompat.getDrawable(itemView.context,R.drawable.item_on_long_click_bg) else ContextCompat.getDrawable(itemView.context,R.drawable.rectangle_10r)
                    binding.min2.backgroundTintList = if(counter==0) ContextCompat.getColorStateList(itemView.context,R.color.abuB) else ContextCompat.getColorStateList(itemView.context,R.color.orange)
                    binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
                    handler.onMinButtonClick(position,produkData,counter,produk)
                }
            }
        }
    }
    class LinearViewHolder private constructor(val binding: ItemDataTransaksiLinearBinding): RecyclerView.ViewHolder(binding.root) {
        companion object{
            var produkIdList = mutableListOf<Int?>()
            var produkQtyList = mutableListOf<Int?>()
            var produkIdBeforeDeleted = MutableLiveData<Int?>(-1)
            var produkNameList = mutableListOf<String?>()
            var produkPriceList = mutableListOf<Double?>()
            fun from(parent: ViewGroup): LinearViewHolder{
                return LinearViewHolder(ItemDataTransaksiLinearBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
        }
        fun bind(
            position: Int,
            produkData: ArrayList<ProdukEntity>,
            handler: ClickHandler,
        ){
            var counter = 0
            val dialog = EditQuantityDialog()
            var textColor = if(binding.counter.text.toString().toInt()>0) ContextCompat.getColor(itemView.context,
                R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
            val (id,barcode,namaProduk,deskripsi,modal,hargaJual,satuan,stok,tanggal) = produkData[position]
            binding.counter.setText(counter.toString())
            val textWatcher = object : TextWatcher{
                fun addDataProduk(){
                    produkIdList.add(id)
                    produkQtyList.add(produkIdList.indexOf(id),counter)
                    produkNameList.add(produkIdList.indexOf(id),namaProduk)
                    produkPriceList.add(produkIdList.indexOf(id),hargaJual*counter)
                }
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    Log.d(TAG, "afterTextChanged: = $counter")
                    if(!produkIdList.contains(id) && counter > 0){
                        addDataProduk()
                        val produk = Produk(
                            produkIdList,
                            produkNameList,
                            produkQtyList,
                            produkPriceList
                        )
                        Log.d(TAG, "bind: produkIdList = ${produkIdList}")
                        Log.d(TAG, "bind: produkNameList = ${produkNameList}")
                        Log.d(TAG, "bind: produkQtyList = ${produkQtyList}")
                        Log.d(TAG, "bind: hargaProduk = ${produkPriceList}")
                        binding.counter.setTextColor(textColor)
                        binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context,R.color.orange)
                        binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
                        handler.onPlusButtonClick(position,produkData,counter,produk)
                    }
                    if(produkIdList.contains(id) && counter > 1){
//                    produkIdList[produkIdList.indexOf(id)] = id
//                    Log.d(TAG, "id = ${}")
                        produkNameList[produkIdList.indexOf(id)] = produkData[position].namaProduk
                        produkQtyList[produkIdList.indexOf(id)] = counter
                        produkPriceList[produkIdList.indexOf(id)] = produkData[position].hargaJual*counter
                        val produk = Produk(
                            produkIdList,
                            produkNameList,
                            produkQtyList,
                            produkPriceList
                        )
                        Log.d(TAG, "bind: produkIdList = ${produkIdList}")
                        Log.d(TAG, "bind: produkNameList = ${produkNameList}")
                        Log.d(TAG, "bind: produkQtyList = ${produkQtyList}")
                        Log.d(TAG, "bind: hargaProduk = ${produkPriceList}")
                        binding.counter.setTextColor(textColor)
                        binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context,R.color.orange)
                        binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
                        handler.onPlusButtonClick(position,produkData,counter,produk)
                    }
                   
                    if(counter == 0 && binding.counter.text.toString().toInt()==0) {
                        Log.d(TAG, "afterTextChanged: masuk = 0")
                        binding.counter.setTextColor(ContextCompat.getColorStateList(itemView.context,R.color.abuB))
                        binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context,R.color.abuB)
                        binding.min.isEnabled = false
                        if(produkIdList.contains(id)){
                            produkNameList.removeAt(produkIdList.indexOf(id))
                            produkPriceList.removeAt(produkIdList.indexOf(id))
                            produkQtyList.removeAt(produkIdList.indexOf(id))
                            produkIdList.removeAt(produkIdList.indexOf(id))
                        }else{
                            
                        }
                        val produk = Produk(
                            produkIdList,
                            produkNameList,
                            produkQtyList,
                            produkPriceList
                        )
                        Log.d(TAG, "bind: produkIdList = ${produkIdList}")
                        Log.d(TAG, "bind: produkIdList = ${produkNameList}")
                        Log.d(TAG, "bind: produkQtyList = ${produkQtyList}")
                        Log.d(TAG, "bind: hargaProduk = ${produkPriceList}")

                        binding.rootView.background = if(binding.counter.text.toString().toInt()>=1) ContextCompat.getDrawable(itemView.context,R.drawable.item_on_long_click_bg) else ContextCompat.getDrawable(itemView.context,R.drawable.rectangle_10r)

                        binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
                        handler.onMinButtonClick(position,produkData,counter,produk)
                    }

//                    binding.counter.setTextColor(textColor)
//                    binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context,R.color.orange)

                    binding.rootView.background = if(binding.counter.text.toString().toInt()>=1) ContextCompat.getDrawable(itemView.context,R.drawable.item_on_long_click_bg) else ContextCompat.getDrawable(itemView.context,R.drawable.rectangle_10r)
                    binding.min.isEnabled = true

                }
            }
            binding.removeBtn.setOnClickListener{
                counter = 0
                binding.counter.setText("0")
                if(binding.counter.text.toString().toInt()>=1 || binding.counter.text.toString()!=""){
                    Log.d(TAG, "bind: $counter")
                    if(produkIdList.indexOf(id)!=-1){
                        produkNameList[produkIdList.indexOf(id)] = produkData[position].namaProduk
                        produkQtyList[produkIdList.indexOf(id)] = counter
                        produkPriceList[produkIdList.indexOf(id)] = produkData[position].hargaJual*counter
                    }else{
                        binding.min.isEnabled = true
                    }
                    val produk = Produk(
                        produkIdList,
                        produkNameList,
                        produkQtyList,
                        produkPriceList
                    )
                    handler.onMinButtonClick(position,produkData,counter,produk)
                    binding.counter.setText(counter.toString())
                    binding.counter.setTextColor(if(counter==0) ContextCompat.getColorStateList(itemView.context,R.color.abuB) else ContextCompat.getColorStateList(itemView.context,R.color.orange))
                    binding.min2.backgroundTintList = if(counter==0) ContextCompat.getColorStateList(itemView.context,R.color.abuB) else ContextCompat.getColorStateList(itemView.context,R.color.orange)
                    binding.rootView.background = if(binding.counter.text.toString().toInt()>=1) ContextCompat.getDrawable(itemView.context,R.drawable.item_on_long_click_bg) else ContextCompat.getDrawable(itemView.context,R.drawable.rectangle_10r)
                    binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
                }
                else{
                    binding.min.isEnabled = false
                    if(produkIdList.contains(id)){
                        produkNameList.removeAt(produkIdList.indexOf(id))
                        produkPriceList.removeAt(produkIdList.indexOf(id))
                        produkQtyList.removeAt(produkIdList.indexOf(id))
                        produkIdList.removeAt(produkIdList.indexOf(id))
                    }
                    val produk = Produk(
                        produkIdList,
                        produkNameList,
                        produkQtyList,
                        produkPriceList
                    )
                    binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context,R.color.abuB)
                    Log.d(TAG, "bind: produkIdList = ${produkIdList}")
                    Log.d(TAG, "bind: produkIdList = ${produkNameList}")
                    Log.d(TAG, "bind: produkQtyList = ${produkQtyList}")
                    Log.d(TAG, "bind: hargaProduk = ${produkPriceList}")
                    binding.counter.setText(counter.toString())
                    binding.counter.setTextColor(if(counter==0) ContextCompat.getColorStateList(itemView.context,R.color.abuB) else ContextCompat.getColorStateList(itemView.context,R.color.orange))
                    binding.rootView.background = if(binding.counter.text.toString().toInt()>=1) ContextCompat.getDrawable(itemView.context,R.drawable.item_on_long_click_bg) else ContextCompat.getDrawable(itemView.context,R.drawable.rectangle_10r)
                    binding.min2.backgroundTintList = if(counter==0) ContextCompat.getColorStateList(itemView.context,R.color.abuB) else ContextCompat.getColorStateList(itemView.context,R.color.orange)
                    binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
                    handler.onMinButtonClick(position,produkData,counter,produk)
                }
            }
            binding.editBtn.setOnClickListener{

                val inflater = LayoutInflater.from(itemView.context)
                val dialogBinding = DialogEditQuantityBinding.inflate(inflater,null, false)
                val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(itemView.context)
                builder.setTitle("Tambah Kuantitas")
                builder.setView(dialogBinding.root)
                val alertDialog: android.app.AlertDialog? = builder.create()
                builder.setPositiveButton("OK") { dialog, which ->
                    binding.counter.addTextChangedListener(textWatcher)
                    counter = dialogBinding.qty.text.toString().toInt()
                    binding.counter.setText(dialogBinding.qty.text.toString())
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    alertDialog?.dismiss()
                }
                builder.show()
            }

            binding.min.isEnabled = false
            binding.counter.addTextChangedListener(textWatcher)
            binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
            binding.namaProduk.text = namaProduk
            binding.deskripsi.text = deskripsi
            binding.harga.text = itemView.context.getString(R.string.harga_jual_arg_2,hargaJual,satuan.toString())
            binding.stok.text = itemView.context.getString(R.string.stok_arg,stok.toString())
            itemView.isSelected = selectionIds.contains(id.toInt())

            fun addDataProduk(){
                produkIdList.add(id)
                produkQtyList.add(produkIdList.indexOf(id),counter)
                produkNameList.add(produkIdList.indexOf(id),namaProduk)
                produkPriceList.add(produkIdList.indexOf(id),hargaJual*counter)
            }

            binding.plus.setOnClickListener{
                binding.counter.removeTextChangedListener(textWatcher)
                binding.counter.clearFocus()
                counter = if(binding.counter.text.toString() !="") binding.counter.text.toString().toInt()+1 else counter+1
                if(!produkIdList.contains(id) && counter > 0 || binding.counter.text.toString()==""){
                    addDataProduk()
                    val produk = Produk(
                        produkIdList,
                        produkNameList,
                        produkQtyList,
                        produkPriceList
                    )
                    Log.d(TAG, "bind: produkIdList = ${produkIdList}")
                    Log.d(TAG, "bind: produkNameList = ${produkNameList}")
                    Log.d(TAG, "bind: produkQtyList = ${produkQtyList}")
                    Log.d(TAG, "bind: hargaProduk = ${produkPriceList}")
                    binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
                    handler.onPlusButtonClick(position,produkData,counter,produk)
                }
                if(produkIdList.contains(id) && counter > 1){
//                    produkIdList[produkIdList.indexOf(id)] = id
//                    Log.d(TAG, "id = ${}")
                    produkNameList[produkIdList.indexOf(id)] = produkData[position].namaProduk
                    produkQtyList[produkIdList.indexOf(id)] = counter
                    produkPriceList[produkIdList.indexOf(id)] = produkData[position].hargaJual*counter
                    val produk = Produk(
                        produkIdList,
                        produkNameList,
                        produkQtyList,
                        produkPriceList
                    )
                    Log.d(TAG, "bind: produkIdList = ${produkIdList}")
                    Log.d(TAG, "bind: produkNameList = ${produkNameList}")
                    Log.d(TAG, "bind: produkQtyList = ${produkQtyList}")
                    Log.d(TAG, "bind: hargaProduk = ${produkPriceList}")
                    binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
                    handler.onPlusButtonClick(position,produkData,counter,produk)
                }
                binding.counter.setText(counter.toString())
                binding.counter.setTextColor(textColor)
                binding.rootView.background = if(binding.counter.text.toString().toInt()>=1) ContextCompat.getDrawable(itemView.context,R.drawable.item_on_long_click_bg) else ContextCompat.getDrawable(itemView.context,R.drawable.rectangle_10r)
                binding.min.isEnabled = true
                binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context,R.color.orange)
            }
            binding.min.setOnClickListener{
                binding.counter.removeTextChangedListener(textWatcher)
                binding.counter.clearFocus()
                if(counter == 0) binding.min.isEnabled = false
                else counter = binding.counter.text.toString().toInt() - 1
                if(binding.counter.text.toString().toInt()>=1 || binding.counter.text.toString()!=""){
                    Log.d(TAG, "bind: $counter")
                    if(produkIdList.indexOf(id)!=-1){
                        produkNameList[produkIdList.indexOf(id)] = produkData[position].namaProduk
                        produkQtyList[produkIdList.indexOf(id)] = counter
                        produkPriceList[produkIdList.indexOf(id)] = produkData[position].hargaJual*counter
                    }else{
                        binding.min.isEnabled = true
                    }
                    val produk = Produk(
                        produkIdList,
                        produkNameList,
                        produkQtyList,
                        produkPriceList
                    )
                    handler.onMinButtonClick(position,produkData,counter,produk)
                    binding.counter.setText(counter.toString())
                    binding.counter.setTextColor(if(counter==0) ContextCompat.getColorStateList(itemView.context,R.color.abuB) else ContextCompat.getColorStateList(itemView.context,R.color.orange))
                    binding.min2.backgroundTintList = if(counter==0) ContextCompat.getColorStateList(itemView.context,R.color.abuB) else ContextCompat.getColorStateList(itemView.context,R.color.orange)
                    binding.rootView.background = if(binding.counter.text.toString().toInt()>=1) ContextCompat.getDrawable(itemView.context,R.drawable.item_on_long_click_bg) else ContextCompat.getDrawable(itemView.context,R.drawable.rectangle_10r)
                    binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)

                }
                else{
                    counter--
                    binding.min.isEnabled = false
                    if(produkIdList.contains(id)){
                        produkNameList.removeAt(produkIdList.indexOf(id))
                        produkPriceList.removeAt(produkIdList.indexOf(id))
                        produkQtyList.removeAt(produkIdList.indexOf(id))
                        produkIdList.removeAt(produkIdList.indexOf(id))
                    }
                    val produk = Produk(
                        produkIdList,
                        produkNameList,
                        produkQtyList,
                        produkPriceList
                    )
                    binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context,R.color.abuB)
                    Log.d(TAG, "bind: produkIdList = ${produkIdList}")
                    Log.d(TAG, "bind: produkIdList = ${produkNameList}")
                    Log.d(TAG, "bind: produkQtyList = ${produkQtyList}")
                    Log.d(TAG, "bind: hargaProduk = ${produkPriceList}")
                    binding.counter.setText(counter.toString())
                    binding.counter.setTextColor(if(counter==0) ContextCompat.getColorStateList(itemView.context,R.color.abuB) else ContextCompat.getColorStateList(itemView.context,R.color.orange))
                    binding.rootView.background = if(binding.counter.text.toString().toInt()>=1) ContextCompat.getDrawable(itemView.context,R.drawable.item_on_long_click_bg) else ContextCompat.getDrawable(itemView.context,R.drawable.rectangle_10r)
                    binding.min2.backgroundTintList = if(counter==0) ContextCompat.getColorStateList(itemView.context,R.color.abuB) else ContextCompat.getColorStateList(itemView.context,R.color.orange)
                    binding.counterCardView.strokeColor = if(counter>0) ContextCompat.getColor(itemView.context,R.color.orange) else ContextCompat.getColor(itemView.context,R.color.abuB)
                    handler.onMinButtonClick(position,produkData,counter,produk)
                }
            }
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
                holder.bind(holder.absoluteAdapterPosition,produkFilterList as ArrayList<ProdukEntity>, handler)
            }
            is GridViewHolder -> {
                holder.bind(holder.absoluteAdapterPosition,produkFilterList as ArrayList<ProdukEntity>, handler)
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
                        if(
                            item.namaProduk.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            item.deskripsi.lowercase(Locale.ROOT).contains(charSearch.lowercase(
                                Locale.ROOT)) ||
                            item.hargaJual.toString().lowercase(Locale.ROOT).contains(charSearch.lowercase(
                                Locale.ROOT)) ||
                            item.stok.toString().trim().lowercase(Locale.ROOT).contains(charSearch.lowercase(
                                Locale.ROOT))
                        ){
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
//        fun afterCounterTextChange(position: Int, produk: ArrayList<ProdukEntity>, counter: Int,produkData: Produk)
        fun onPlusButtonClick(position: Int, produk: ArrayList<ProdukEntity>, counter: Int,produkData: Produk)
        fun onMinButtonClick(position: Int, produk: ArrayList<ProdukEntity>, counter: Int,produkData: Produk)
    }
}