package org.d3ifcool.catok.ui.beranda.transaksi

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity
import org.d3ifcool.catok.core.data.source.local.model.Produk
import org.d3ifcool.catok.databinding.DialogEditQuantityBinding
import org.d3ifcool.catok.databinding.ItemDataTransaksiGridBinding
import org.d3ifcool.catok.databinding.ItemDataTransaksiLinearBinding
import org.d3ifcool.catok.utils.toRupiahFormat
import java.util.*


@SuppressLint("NotifyDataSetChanged")
@Suppress("UNCHECKED_CAST")
class TransaksiAdapter(
    private val isLinearLayoutManager: Boolean = true,
    private val handler: ClickHandler
) : ListAdapter<ProdukEntity, RecyclerView.ViewHolder>(diffCallback) {
    companion object {
        private var TAG = TransaksiAdapter::class.java.name
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
    var produkFilterList: MutableList<ProdukEntity> = ArrayList()

    fun setData(historiHistoriTransaksi: ArrayList<ProdukEntity>?) {
        produkList.clear()
        produkFilterList.clear()
        if (historiHistoriTransaksi != null) {
            produkList.addAll(historiHistoriTransaksi)
            produkFilterList = produkList
            submitList(produkList)
            if(LinearViewHolder.tempFirstPos!=null) LinearViewHolder.tempFirstPos = getItem(0)
            if(GridViewHolder.tempFirstPos!=null) GridViewHolder.tempFirstPos = getItem(0)

            Log.d(TAG, "observeItem: ${LinearViewHolder.tempFirstPos}")
        }
        notifyDataSetChanged()
    }

    fun updateData(filterList: ArrayList<ProdukEntity>?) {
        if (filterList != null) {
            produkFilterList.clear()
            produkFilterList.addAll(filterList)
            submitList(produkFilterList)
        }
        Log.d(TAG, "updateData: FilterListSize = ${produkFilterList.size}")
        notifyDataSetChanged()

    }

    class GridViewHolder private constructor(val binding: ItemDataTransaksiGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            var tempFirstPos: ProdukEntity? = null
            var tempProduk = mutableListOf<ProdukEntity>()
            var tempCounter = mutableListOf<Int>()
            var produkIdList = mutableListOf<Int?>()
            var produkQtyList = mutableListOf<Int?>()
            var produkSatuanPerList = mutableListOf<String?>()
            var produkNameList = mutableListOf<String?>()
            var produkPriceList = mutableListOf<Double?>()
            fun from(parent: ViewGroup): GridViewHolder {
                return GridViewHolder(
                    ItemDataTransaksiGridBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }
    }

    class LinearViewHolder private constructor(val binding: ItemDataTransaksiLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            var tempFirstPos: ProdukEntity? = null
            var tempProduk = mutableListOf<ProdukEntity>()
            var tempCounter = mutableListOf<Int>()
            var produkIdList = mutableListOf<Int?>()
            var produkQtyList = mutableListOf<Int?>()
            var produkNameList = mutableListOf<String?>()
            var produkSatuanPerList = mutableListOf<String?>()
            var produkPriceList = mutableListOf<Double?>()
            fun from(parent: ViewGroup): LinearViewHolder {
                return LinearViewHolder(
                    ItemDataTransaksiLinearBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val gridLayoutManager = GridViewHolder.from(parent)
        val linearLayoutManager = LinearViewHolder.from(parent)
        return if (isLinearLayoutManager) linearLayoutManager else gridLayoutManager
    }

    override fun getItemCount(): Int {
        return produkList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LinearViewHolder -> {
                with(holder) {
                    setIsRecyclable(true)
                    binding.min.isEnabled = false
                    val position = holder.absoluteAdapterPosition
                    val produkList = produkList as ArrayList<ProdukEntity>
                    var counter = 0
                    if(counter >= 1) binding.min.isEnabled = true
                    val textColor = if (binding.counter.text.toString().toInt() > 0 || counter > 0) ContextCompat.getColor(itemView.context, R.color.orange) else ContextCompat.getColor(itemView.context, R.color.abuB)
                    fun unselectItemView(){
                        counter = 0
                        binding.counter.setText(0.toString())
                        binding.min.isEnabled = false
                        binding.min2.isEnabled = false
                        binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context, R.color.abuB)
                        binding.counter.setTextColor(ContextCompat.getColor(itemView.context,R.color.abuB))
                        binding.rootView.background = ContextCompat.getDrawable(itemView.context, R.drawable.rectangle_10r)
                        binding.counterCardView.strokeColor = ContextCompat.getColor(itemView.context, R.color.abuB)
                    }
                    val (id, barcode, namaProduk, deskripsi, modal, hargaJual, satuan,satuanPer, stok, tanggal) = produkFilterList[position]

                    Log.d(
                        TAG,
                        "onBindViewHolder: tempId = ${LinearViewHolder.tempProduk} , counter = ${LinearViewHolder.tempCounter}"
                    )
                    Log.d(
                        TAG,
                        "onBindViewHolder: isBtnMinEnabled? = ${binding.min.isEnabled}, isBtnMin2Enabled? = ${binding.min2.isEnabled}"
                    )
                    val textWatcher = object : TextWatcher {
                        fun addDataProduk() {
                            if(!LinearViewHolder.tempProduk.contains(produkFilterList[position]) && counter > 0){
                                LinearViewHolder.tempProduk.add(produkFilterList[position])
                                LinearViewHolder.tempCounter.add(LinearViewHolder.tempProduk.indexOf(produkFilterList[position]), counter)
                            }
                            LinearViewHolder.produkIdList.add(id)
                            LinearViewHolder.produkQtyList.add(
                                LinearViewHolder.produkIdList.indexOf(
                                    id
                                ), counter
                            )
                            LinearViewHolder.produkNameList.add(
                                LinearViewHolder.produkIdList.indexOf(
                                    id
                                ), namaProduk
                            )
                            LinearViewHolder.produkSatuanPerList.add(
                                LinearViewHolder.produkIdList.indexOf(
                                    id
                                ), satuanPer
                            )
                            LinearViewHolder.produkPriceList.add(
                                LinearViewHolder.produkIdList.indexOf(
                                    id
                                ), hargaJual * counter
                            )
                        }

                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {
                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun afterTextChanged(p0: Editable?) {
                            Log.d(TAG, "afterTextChanged: = $counter")
                            if (!LinearViewHolder.produkIdList.contains(id) && binding.counter.text.toString()
                                    .toInt() > 0
                            ) {
                                addDataProduk()
                                val produk = Produk(
                                    LinearViewHolder.produkIdList,
                                    LinearViewHolder.produkNameList,
                                    LinearViewHolder.produkQtyList,
                                    LinearViewHolder.produkSatuanPerList,
                                    LinearViewHolder.produkPriceList
                                )
                                Log.d(TAG, "bind>0: produkIdList = ${LinearViewHolder.produkIdList}")
                                Log.d(
                                    TAG,
                                    "bind: produkNameList = ${LinearViewHolder.produkNameList}"
                                )
                                Log.d(
                                    TAG,
                                    "bind: produkQtyList = ${LinearViewHolder.produkQtyList}"
                                )
                                Log.d(
                                    TAG,
                                    "bind: hargaProduk = ${LinearViewHolder.produkPriceList}"
                                )
                                binding.counter.setTextColor(
                                    ContextCompat.getColor(
                                        itemView.context,
                                        R.color.orange
                                    )
                                )
                                binding.min2.backgroundTintList = ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.orange
                                )
                                binding.counterCardView.strokeColor =
                                    ContextCompat.getColor(itemView.context, R.color.orange)
                                handler.onPlusButtonClick(position, produkList, counter, produk)
                            }
                            if (LinearViewHolder.produkIdList.contains(id) && binding.counter.text.toString()
                                    .toInt() > 1
                            ) {

                                if(LinearViewHolder.tempProduk.contains(produkFilterList[position]) && counter > 1){
                                    LinearViewHolder.tempCounter[LinearViewHolder.tempProduk.indexOf(produkFilterList[position])] = counter
                                }
                                LinearViewHolder.produkNameList[LinearViewHolder.produkIdList.indexOf(
                                    id
                                )] = produkList[position].namaProduk
                                LinearViewHolder.produkQtyList[LinearViewHolder.produkIdList.indexOf(
                                    id
                                )] = counter
                                LinearViewHolder.produkSatuanPerList[LinearViewHolder.produkIdList.indexOf(id)] = produkList[position].satuanPer
                                LinearViewHolder.produkPriceList[LinearViewHolder.produkIdList.indexOf(
                                    id
                                )] = produkList[position].hargaJual * counter
                                val produk = Produk(
                                    LinearViewHolder.produkIdList,
                                    LinearViewHolder.produkNameList,
                                    LinearViewHolder.produkQtyList,
                                    LinearViewHolder.produkSatuanPerList,
                                    LinearViewHolder.produkPriceList
                                )
                                Log.d(TAG, "bind>1: produkIdList = ${LinearViewHolder.produkIdList}")
                                Log.d(
                                    TAG,
                                    "bind: produkNameList = ${LinearViewHolder.produkNameList}"
                                )
                                Log.d(
                                    TAG,
                                    "bind: produkQtyList = ${LinearViewHolder.produkQtyList}"
                                )
                                Log.d(
                                    TAG,
                                    "bind: hargaProduk = ${LinearViewHolder.produkPriceList}"
                                )
                                binding.counter.setTextColor(textColor)
                                binding.min2.backgroundTintList = ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.orange
                                )
                                binding.counterCardView.strokeColor =
                                    if (binding.counter.text.toString()
                                            .toInt() > 0
                                    ) ContextCompat.getColor(
                                        itemView.context,
                                        R.color.orange
                                    ) else ContextCompat.getColor(itemView.context, R.color.abuB)
                                handler.onPlusButtonClick(position, produkList, counter, produk)
                            }

                            binding.rootView.background =
                                if (counter >= 1) ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.item_on_long_click_bg
                                ) else ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.rectangle_10r
                                )
                            binding.min.isEnabled = binding.counter.text.toString().toInt()!=0
                        }
                    }
                    binding.counter.removeTextChangedListener(textWatcher)
                    binding.satuanPer.text = satuanPer

                    Log.d(TAG, "onBindViewHolder: ${produkFilterList[absoluteAdapterPosition]==LinearViewHolder.tempFirstPos}")
                    fun observeItem(tempProduk: List<ProdukEntity>, tempCounter: List<Int>) {
                        for(i in tempProduk){
                            if(produkFilterList[0] != LinearViewHolder.tempFirstPos){
                                unselectItemView()
                            }
                            if(produkList.contains(i)){
                                for(j in produkList){
                                    if(holder.absoluteAdapterPosition == produkList.indexOf(j)) {
                                        Log.d(TAG, "observeItem: betul = ${holder.absoluteAdapterPosition}")
                                        Log.d(TAG, "observeItem: ${produkFilterList[absoluteAdapterPosition]}")
                                        if(!tempProduk.contains(getItem(0))) {
                                            unselectItemView()
                                        }
                                        else{
                                            if(!LinearViewHolder.produkIdList.contains(getItem(position).id_produk)){
                                                unselectItemView()
                                            }else{
                                                counter = LinearViewHolder.produkQtyList[LinearViewHolder.produkIdList.indexOf(id)]!!
                                                binding.counter.setText(counter.toString())
                                                binding.min.isEnabled = true
                                                binding.min2.isEnabled = true
                                                binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context, R.color.orange)
                                                binding.counter.setTextColor(ContextCompat.getColor(itemView.context,R.color.orange))
                                                binding.rootView.background = ContextCompat.getDrawable(itemView.context, R.drawable.item_on_long_click_bg)
                                                binding.counterCardView.strokeColor = if(tempCounter[tempProduk.indexOf(j)]>0) ContextCompat.getColor(itemView.context, R.color.orange) else ContextCompat.getColor(itemView.context, R.color.abuB)
                                            }

                                        }
                                    }else {
                                        if(!tempProduk.contains(getItem(holder.absoluteAdapterPosition))){
                                            unselectItemView()
                                        } else{
                                            if(!LinearViewHolder.produkIdList.contains(getItem(position).id_produk)){
                                                unselectItemView()
                                            }else{
                                                Log.d(TAG, "observeItem: masuk sini2? $position")
                                                counter = LinearViewHolder.produkQtyList[LinearViewHolder.produkIdList.indexOf(id)]!!
                                                binding.counter.setText(counter.toString())
                                                binding.min.isEnabled = true
                                                binding.min2.isEnabled = true
                                                binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context, R.color.orange)
                                                binding.counter.setTextColor(ContextCompat.getColor(itemView.context,R.color.orange))
                                                binding.rootView.background = ContextCompat.getDrawable(itemView.context, R.drawable.item_on_long_click_bg)
                                                binding.counterCardView.strokeColor = if(tempCounter[tempProduk.indexOf(i)]>0) ContextCompat.getColor(itemView.context, R.color.orange) else ContextCompat.getColor(itemView.context, R.color.abuB)
                                            }

                                        }

                                    }
                                    return
                                }
                            }
                        }
                    }
                    observeItem(LinearViewHolder.tempProduk, LinearViewHolder.tempCounter)

                    binding.removeBtn.setOnClickListener {
                        binding.counter.removeTextChangedListener(textWatcher)
                        counter = 0
                        binding.counter.setText(itemView.context.getString(R.string.zero))
                        binding.min.isEnabled = false
                        if (LinearViewHolder.produkIdList.contains(id)) {
                            Log.d(TAG, "removeBtn: ${produkFilterList[position]}")
                            Log.d(TAG, "removeBtn: ${LinearViewHolder.produkIdList.indexOf(id)}")
                            LinearViewHolder.produkNameList.removeAt(LinearViewHolder.produkIdList.indexOf(id))
                            LinearViewHolder.produkPriceList.removeAt(LinearViewHolder.produkIdList.indexOf(id))
                            LinearViewHolder.produkQtyList.removeAt(LinearViewHolder.produkIdList.indexOf(id))
                            LinearViewHolder.produkSatuanPerList.removeAt(LinearViewHolder.produkIdList.indexOf(id))
                            LinearViewHolder.produkIdList.removeAt(LinearViewHolder.produkIdList.indexOf(id))
                        }
                        val produk = Produk(
                            LinearViewHolder.produkIdList,
                            LinearViewHolder.produkNameList,
                            LinearViewHolder.produkQtyList,
                            LinearViewHolder.produkSatuanPerList,
                            LinearViewHolder.produkPriceList
                        )
                        Log.d(TAG, "removeBtn: $produk")
                        binding.min2.backgroundTintList =
                            ContextCompat.getColorStateList(itemView.context, R.color.abuB)
                        binding.counter.setText(counter.toString())
                        binding.counter.setTextColor(
                            if (counter == 0) ContextCompat.getColorStateList(
                                itemView.context,
                                R.color.abuB
                            ) else ContextCompat.getColorStateList(
                                itemView.context,
                                R.color.orange
                            )
                        )
                        binding.rootView.background =
                            if (counter >= 1) ContextCompat.getDrawable(
                                itemView.context,
                                R.drawable.item_on_long_click_bg
                            ) else ContextCompat.getDrawable(
                                itemView.context,
                                R.drawable.rectangle_10r
                            )
                        binding.min2.backgroundTintList =
                            if (counter == 0) ContextCompat.getColorStateList(
                                itemView.context,
                                R.color.abuB
                            ) else ContextCompat.getColorStateList(
                                itemView.context,
                                R.color.orange
                            )
                        binding.counterCardView.strokeColor =
                            if (counter > 0) ContextCompat.getColor(
                                itemView.context,
                                R.color.orange
                            ) else ContextCompat.getColor(itemView.context, R.color.abuB)
                        handler.onMinButtonClick(position, produkList, counter, produk)
                    }
                    binding.editBtn.setOnClickListener {
                        val inflater = LayoutInflater.from(itemView.context)
                        val dialogBinding = DialogEditQuantityBinding.inflate(inflater, null, false)
                        val builder: android.app.AlertDialog.Builder =
                            android.app.AlertDialog.Builder(itemView.context)
                        builder.setView(dialogBinding.root)
                        val alertDialog: android.app.AlertDialog? = builder.create()
                        dialogBinding.negativeBtn.setOnClickListener {
                            alertDialog?.dismiss()
                        }
                        dialogBinding.positiveBtn.setOnClickListener {
                            Log.d(TAG, "onBindViewHolder: $stok")
                            if (dialogBinding.qty.text?.isNotBlank() == true){
                                if(dialogBinding.qty.text.toString().toInt()!=0){
                                    if (dialogBinding.qty.text.toString().toInt() == 0){
                                        alertDialog?.dismiss()
                                        return@setOnClickListener
                                    }
                                    if (dialogBinding.qty.text.toString().toInt() > 0 && stok == 0) {
                                        Toast.makeText(
                                            itemView.context,
                                            itemView.context.getString(R.string.stok_kosong_arg,namaProduk),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@setOnClickListener
                                    }
                                    if (dialogBinding.qty.text.toString().toInt() > stok) {
                                        Toast.makeText(
                                            itemView.context,
                                            itemView.context.getString(R.string.stok_tidak_cukup),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@setOnClickListener
                                    }
                                    binding.counter.addTextChangedListener(textWatcher)
                                    counter = dialogBinding.qty.text.toString().toInt()
                                    binding.counter.setText(dialogBinding.qty.text.toString())
                                    binding.counter.setTextColor(ContextCompat.getColorStateList(
                                        itemView.context,
                                        R.color.orange
                                    ))
                                } else {
                                    binding.counter.addTextChangedListener(textWatcher)
                                    counter = dialogBinding.qty.text.toString().toInt()
                                    binding.counter.setText(dialogBinding.qty.text.toString())
                                    binding.counter.setTextColor(ContextCompat.getColorStateList(
                                        itemView.context,
                                        R.color.abuB
                                    ))
                                }


                            }

                            alertDialog?.dismiss()
                        }
                        alertDialog?.show()
                    }

                    binding.namaProduk.text = namaProduk
                    binding.deskripsi.text = deskripsi
                    binding.harga.text = itemView.context.getString(R.string.harga_jual_arg_2, hargaJual.toRupiahFormat(), satuan.toString(),satuanPer)
                    binding.stok.text = itemView.context.getString(R.string.stok_arg, stok.toString())

                    fun addDataProduk() {
                        if(!LinearViewHolder.tempProduk.contains(produkFilterList[position]) && counter > 0){
                            LinearViewHolder.tempProduk.add(produkFilterList[position])
                            LinearViewHolder.tempCounter.add(LinearViewHolder.tempProduk.indexOf(produkFilterList[position]), counter)
                        }
                        LinearViewHolder.produkIdList.add(id)
                        LinearViewHolder.produkQtyList.add(
                            LinearViewHolder.produkIdList.indexOf(id),
                            counter
                        )
                        LinearViewHolder.produkNameList.add(
                            LinearViewHolder.produkIdList.indexOf(id),
                            namaProduk
                        )
                        LinearViewHolder.produkSatuanPerList.add(
                            LinearViewHolder.produkIdList.indexOf(id), satuanPer
                        )
                        LinearViewHolder.produkPriceList.add(
                            LinearViewHolder.produkIdList.indexOf(
                                id
                            ), hargaJual * counter
                        )
                    }

                    binding.plus.setOnClickListener {

                        Log.d(TAG, "onBindViewHolder: $position")
                        binding.counter.removeTextChangedListener(textWatcher)
                        binding.counter.clearFocus()
                        if (stok == 0) {
                            Toast.makeText(
                                itemView.context,
                                itemView.context.getString(R.string.stok_kosong_arg,namaProduk),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }
                        counter += 1

                        if (counter > stok) {
                            Toast.makeText(
                                itemView.context,
                                itemView.context.getString(R.string.stok_tidak_cukup),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }
                        Log.d(TAG, "counter = ${LinearViewHolder.produkIdList.contains(id)}")
                        if (!LinearViewHolder.produkIdList.contains(id) && counter > 0|| binding.counter.text.toString() == "") {
                            addDataProduk()

                            val produk = Produk(
                                LinearViewHolder.produkIdList,
                                LinearViewHolder.produkNameList,
                                LinearViewHolder.produkQtyList,
                                LinearViewHolder.produkSatuanPerList,
                                LinearViewHolder.produkPriceList
                            )
                            Log.d(TAG, "bind: tempProduk = ${LinearViewHolder.tempProduk}, tempCounter = ${LinearViewHolder.tempCounter}")
                            Log.d(TAG, "bind: produkIdList = ${LinearViewHolder.produkIdList}")
                            Log.d(TAG, "bind: produkNameList = ${LinearViewHolder.produkNameList}")
                            Log.d(TAG, "bind: produkQtyList = ${LinearViewHolder.produkQtyList}")
                            Log.d(TAG, "bind: hargaProduk = ${LinearViewHolder.produkPriceList}")
                            binding.min.isEnabled = counter >= 1

                            binding.counter.setTextColor(
                                ContextCompat.getColor(
                                    itemView.context,
                                    R.color.orange
                                )
                            )
                            binding.counterCardView.strokeColor =
                                if (counter > 0) ContextCompat.getColor(
                                    itemView.context,
                                    R.color.orange
                                ) else ContextCompat.getColor(itemView.context, R.color.abuB)
                            handler.onPlusButtonClick(position, produkList, counter, produk)
                        }
                        if (LinearViewHolder.produkIdList.contains(id) && counter > 1) {
                            if(LinearViewHolder.tempProduk.contains(produkFilterList[position]) && counter > 1){
                                LinearViewHolder.tempCounter[LinearViewHolder.tempProduk.indexOf(produkFilterList[position])] = counter
                            }

                            LinearViewHolder.produkNameList[LinearViewHolder.produkIdList.indexOf(id)] = produkList[position].namaProduk
                            LinearViewHolder.produkQtyList[LinearViewHolder.produkIdList.indexOf(id)] =
                                counter
                            LinearViewHolder.produkSatuanPerList[LinearViewHolder.produkIdList.indexOf(id)] = satuanPer
                            LinearViewHolder.produkPriceList[LinearViewHolder.produkIdList.indexOf(
                                id
                            )] = produkList[position].hargaJual * counter
                            val produk = Produk(
                                LinearViewHolder.produkIdList,
                                LinearViewHolder.produkNameList,
                                LinearViewHolder.produkQtyList,
                                LinearViewHolder.produkSatuanPerList,
                                LinearViewHolder.produkPriceList
                            )
                            Log.d(TAG, "bind: produkIdList = ${LinearViewHolder.produkIdList}")
                            Log.d(TAG, "bind: produkNameList = ${LinearViewHolder.produkNameList}")
                            Log.d(TAG, "bind: produkQtyList = ${LinearViewHolder.produkQtyList}")
                            Log.d(TAG, "bind: hargaProduk = ${LinearViewHolder.produkPriceList}")
                            binding.counterCardView.strokeColor =
                                if (counter > 0) ContextCompat.getColor(
                                    itemView.context,
                                    R.color.orange
                                ) else ContextCompat.getColor(itemView.context, R.color.abuB)
                            handler.onPlusButtonClick(position, produkList, counter, produk)
                        }
                        binding.counter.setText(counter.toString())
                        binding.rootView.background = if (counter >= 1) ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.item_on_long_click_bg
                        ) else ContextCompat.getDrawable(itemView.context, R.drawable.rectangle_10r)
                        binding.min.isEnabled = counter >= 1
                        binding.min2.backgroundTintList =
                            ContextCompat.getColorStateList(itemView.context, R.color.orange)
                    }
                    binding.min.setOnClickListener {
                        Log.d(
                            TAG,
                            "onBindViewHolder: counter adapter = $counter , counter temp = ${LinearViewHolder.tempCounter}"
                        )
                        binding.counter.removeTextChangedListener(textWatcher)
                        binding.counter.clearFocus()

                        counter -= 1

                        if (counter >= 1 || binding.counter.text.toString() != "") {
                            Log.d(TAG, "bind: $counter")
                            if(counter==0){
                                if(LinearViewHolder.tempProduk.contains(produkFilterList[position])){
                                    LinearViewHolder.tempCounter.removeAt(LinearViewHolder.tempProduk.indexOf(produkFilterList[position]))
                                    LinearViewHolder.tempProduk.removeAt(LinearViewHolder.tempProduk.indexOf(produkFilterList[position]))
                                }
                                if (LinearViewHolder.produkIdList.contains(id)) {
                                    LinearViewHolder.produkNameList.removeAt(LinearViewHolder.produkIdList.indexOf(id))
                                    LinearViewHolder.produkPriceList.removeAt(LinearViewHolder.produkIdList.indexOf(id))
                                    LinearViewHolder.produkQtyList.removeAt(LinearViewHolder.produkIdList.indexOf(id))
                                    LinearViewHolder.produkSatuanPerList.removeAt(LinearViewHolder.produkIdList.indexOf(id))
                                    LinearViewHolder.produkIdList.removeAt(LinearViewHolder.produkIdList.indexOf(id))
                                }
                            }
                            if (LinearViewHolder.tempProduk.indexOf(produkFilterList[position])!=-1){
                                LinearViewHolder.tempCounter[LinearViewHolder.tempProduk.indexOf(produkFilterList[position])]
                                binding.min.isEnabled = false
                            }else{
                                binding.min.isEnabled = true
                            }
                            if (LinearViewHolder.produkIdList.indexOf(id) != -1) {
                                LinearViewHolder.produkNameList[LinearViewHolder.produkIdList.indexOf(id)] = produkList[position].namaProduk
                                LinearViewHolder.produkQtyList[LinearViewHolder.produkIdList.indexOf(
                                    id
                                )] = counter
                                LinearViewHolder.produkSatuanPerList[LinearViewHolder.produkIdList.indexOf(id)] = satuanPer
                                LinearViewHolder.produkPriceList[LinearViewHolder.produkIdList.indexOf(
                                    id
                                )] = produkList[position].hargaJual * counter
                            } else {
                                binding.min.isEnabled = true
                            }
                            val produk = Produk(
                                LinearViewHolder.produkIdList,
                                LinearViewHolder.produkNameList,
                                LinearViewHolder.produkQtyList,
                                LinearViewHolder.produkSatuanPerList,
                                LinearViewHolder.produkPriceList
                            )
                            handler.onMinButtonClick(position, produkList, counter, produk)
                            binding.counter.setText(counter.toString())
                            binding.counter.setTextColor(
                                if (counter == 0) ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.abuB
                                ) else ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.orange
                                )
                            )
                            binding.min2.backgroundTintList =
                                if (counter == 0) ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.abuB
                                ) else ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.orange
                                )
                            binding.rootView.background =
                                if (counter >= 1) ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.item_on_long_click_bg
                                ) else ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.rectangle_10r
                                )
                            binding.counterCardView.strokeColor =
                                if (counter > 0) ContextCompat.getColor(
                                    itemView.context,
                                    R.color.orange
                                ) else ContextCompat.getColor(itemView.context, R.color.abuB)

                        }
                        else {
                            Log.d(TAG, "onBindViewHolder: emg masuk?")
                            counter--
                            if(LinearViewHolder.tempProduk.contains(produkFilterList[position])){
                                LinearViewHolder.tempCounter.removeAt(LinearViewHolder.tempProduk.indexOf(produkFilterList[position]))
                                LinearViewHolder.tempProduk.removeAt(LinearViewHolder.tempProduk.indexOf(produkFilterList[position]))
                            }
                            Log.d(TAG, "onBindViewHolder: ${produkFilterList[position]}")
                            if (LinearViewHolder.produkIdList.contains(id)) {
                                LinearViewHolder.produkNameList.removeAt(LinearViewHolder.produkIdList.indexOf(id))
                                LinearViewHolder.produkPriceList.removeAt(LinearViewHolder.produkIdList.indexOf(id))
                                LinearViewHolder.produkQtyList.removeAt(LinearViewHolder.produkIdList.indexOf(id))
                                LinearViewHolder.produkSatuanPerList.removeAt(LinearViewHolder.produkIdList.indexOf(id))
                                LinearViewHolder.produkIdList.removeAt(LinearViewHolder.produkIdList.indexOf(id))
                            }
                            val produk = Produk(
                                LinearViewHolder.produkIdList,
                                LinearViewHolder.produkNameList,
                                LinearViewHolder.produkQtyList,
                                LinearViewHolder.produkSatuanPerList,
                                LinearViewHolder.produkPriceList
                            )
                            binding.min2.backgroundTintList =
                                ContextCompat.getColorStateList(itemView.context, R.color.abuB)
                            Log.d(TAG, "bind: produkIdList = ${LinearViewHolder.produkIdList}")
                            Log.d(
                                TAG,
                                "bind: produkIdList = ${LinearViewHolder.produkNameList}"
                            )
                            Log.d(
                                TAG,
                                "bind: produkQtyList = ${LinearViewHolder.produkQtyList}"
                            )
                            Log.d(
                                TAG,
                                "bind: hargaProduk = ${LinearViewHolder.produkPriceList}"
                            )
                            binding.counter.setText(counter.toString())
                            binding.counter.setTextColor(
                                if (counter == 0) ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.abuB
                                ) else ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.orange
                                )
                            )
                            binding.rootView.background =
                                if (counter >= 1) ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.item_on_long_click_bg
                                ) else ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.rectangle_10r
                                )
                            binding.min2.backgroundTintList =
                                if (counter == 0) ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.abuB
                                ) else ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.orange
                                )
                            binding.counterCardView.strokeColor =
                                if (counter > 0) ContextCompat.getColor(
                                    itemView.context,
                                    R.color.orange
                                ) else ContextCompat.getColor(itemView.context, R.color.abuB)
                            handler.onMinButtonClick(position, produkList, counter, produk)
                        }
                        binding.min.isEnabled = counter >= 1
                        Log.d(
                            TAG,
                            "onBindViewHolder: counter adapter = $counter , counter temp = ${LinearViewHolder.tempCounter}"
                        )
                    }
                }
            }
            is GridViewHolder -> {
                with(holder) {
                    setIsRecyclable(true)
                    binding.min.isEnabled = false
                    val position = holder.absoluteAdapterPosition
                    val produkList = produkList as ArrayList<ProdukEntity>
                    var counter = 0
                    if(counter >= 1) binding.min.isEnabled = true
                    val textColor = if (binding.counter.text.toString().toInt() > 0 || counter > 0) ContextCompat.getColor(itemView.context, R.color.orange) else ContextCompat.getColor(itemView.context, R.color.abuB)
                    fun unselectItemView(){
                        counter = 0
                        binding.counter.setText(0.toString())
                        binding.min.isEnabled = false
                        binding.min2.isEnabled = false
                        binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context, R.color.abuB)
                        binding.counter.setTextColor(ContextCompat.getColor(itemView.context,R.color.abuB))
                        binding.rootView.background = ContextCompat.getDrawable(itemView.context, R.drawable.rectangle_10r)
                        binding.counterCardView.strokeColor = ContextCompat.getColor(itemView.context, R.color.abuB)
                    }
                    val (id, barcode, namaProduk, deskripsi, modal, hargaJual, satuan, satuanPer, stok, tanggal) = produkFilterList[position]

                    Log.d(
                        TAG,
                        "onBindViewHolder: tempId = ${GridViewHolder.tempProduk} , counter = ${GridViewHolder.tempCounter}"
                    )
                    Log.d(
                        TAG,
                        "onBindViewHolder: isBtnMinEnabled? = ${binding.min.isEnabled}, isBtnMin2Enabled? = ${binding.min2.isEnabled}"
                    )
                    val textWatcher = object : TextWatcher {
                        fun addDataProduk() {
                            if(!GridViewHolder.tempProduk.contains(produkFilterList[position]) && counter > 0){
                                GridViewHolder.tempProduk.add(produkFilterList[position])
                                GridViewHolder.tempCounter.add(GridViewHolder.tempProduk.indexOf(produkFilterList[position]), counter)
                            }
                            GridViewHolder.produkIdList.add(id)
                            GridViewHolder.produkQtyList.add(
                                GridViewHolder.produkIdList.indexOf(
                                    id
                                ), counter
                            )
                            GridViewHolder.produkNameList.add(
                                GridViewHolder.produkIdList.indexOf(
                                    id
                                ), namaProduk
                            )
                            GridViewHolder.produkSatuanPerList.add(
                                GridViewHolder.produkIdList.indexOf(
                                    id
                                ), satuanPer
                            )
                            GridViewHolder.produkPriceList.add(
                                GridViewHolder.produkIdList.indexOf(
                                    id
                                ), hargaJual * counter
                            )
                        }

                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {
                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun afterTextChanged(p0: Editable?) {
                            Log.d(TAG, "afterTextChanged: = $counter")
                            if (!GridViewHolder.produkIdList.contains(id) && binding.counter.text.toString()
                                    .toInt() > 0
                            ) {
                                addDataProduk()
                                val produk = Produk(
                                    GridViewHolder.produkIdList,
                                    GridViewHolder.produkNameList,
                                    GridViewHolder.produkQtyList,
                                    GridViewHolder.produkSatuanPerList,
                                    GridViewHolder.produkPriceList
                                )
                                Log.d(TAG, "bind>0: produkIdList = ${GridViewHolder.produkIdList}")
                                Log.d(
                                    TAG,
                                    "bind: produkNameList = ${GridViewHolder.produkNameList}"
                                )
                                Log.d(
                                    TAG,
                                    "bind: produkQtyList = ${GridViewHolder.produkQtyList}"
                                )
                                Log.d(
                                    TAG,
                                    "bind: hargaProduk = ${GridViewHolder.produkPriceList}"
                                )
                                binding.counter.setTextColor(
                                    ContextCompat.getColor(
                                        itemView.context,
                                        R.color.orange
                                    )
                                )
                                binding.min2.backgroundTintList = ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.orange
                                )
                                binding.counterCardView.strokeColor =
                                    ContextCompat.getColor(itemView.context, R.color.orange)
                                handler.onPlusButtonClick(position, produkList, counter, produk)
                            }
                            if (GridViewHolder.produkIdList.contains(id) && binding.counter.text.toString()
                                    .toInt() > 1
                            ) {

                                if(GridViewHolder.tempProduk.contains(produkFilterList[position]) && counter > 1){
                                    GridViewHolder.tempCounter[GridViewHolder.tempProduk.indexOf(produkFilterList[position])] = counter
                                }
                                GridViewHolder.produkNameList[GridViewHolder.produkIdList.indexOf(
                                    id
                                )] = produkList[position].namaProduk
                                GridViewHolder.produkQtyList[GridViewHolder.produkIdList.indexOf(
                                    id
                                )] = counter
                                GridViewHolder.produkSatuanPerList[GridViewHolder.produkIdList.indexOf(id)] = produkList[position].satuanPer
                                GridViewHolder.produkPriceList[GridViewHolder.produkIdList.indexOf(
                                    id
                                )] = produkList[position].hargaJual * counter
                                val produk = Produk(
                                    GridViewHolder.produkIdList,
                                    GridViewHolder.produkNameList,
                                    GridViewHolder.produkQtyList,
                                    GridViewHolder.produkSatuanPerList,
                                    GridViewHolder.produkPriceList
                                )
                                Log.d(TAG, "bind>1: produkIdList = ${GridViewHolder.produkIdList}")
                                Log.d(
                                    TAG,
                                    "bind: produkNameList = ${GridViewHolder.produkNameList}"
                                )
                                Log.d(
                                    TAG,
                                    "bind: produkQtyList = ${GridViewHolder.produkQtyList}"
                                )
                                Log.d(
                                    TAG,
                                    "bind: hargaProduk = ${GridViewHolder.produkPriceList}"
                                )
                                binding.counter.setTextColor(textColor)
                                binding.min2.backgroundTintList = ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.orange
                                )
                                binding.counterCardView.strokeColor =
                                    if (binding.counter.text.toString()
                                            .toInt() > 0
                                    ) ContextCompat.getColor(
                                        itemView.context,
                                        R.color.orange
                                    ) else ContextCompat.getColor(itemView.context, R.color.abuB)
                                handler.onPlusButtonClick(position, produkList, counter, produk)
                            }

                            binding.rootView.background =
                                if (counter >= 1) ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.item_on_long_click_bg
                                ) else ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.rectangle_10r
                                )
                            binding.min.isEnabled = binding.counter.text.toString().toInt()!=0
                        }
                    }
                    binding.counter.removeTextChangedListener(textWatcher)
                    binding.satuanPer.text = satuanPer

                    Log.d(TAG, "onBindViewHolder: ${produkFilterList[absoluteAdapterPosition]==GridViewHolder.tempFirstPos}")
                    fun observeItem(tempProduk: List<ProdukEntity>, tempCounter: List<Int>) {
                        for(i in tempProduk){
                            if(produkFilterList[0] != GridViewHolder.tempFirstPos){
                                unselectItemView()
                            }
                            if(produkList.contains(i)){
                                for(j in produkList){
                                    if(holder.absoluteAdapterPosition == produkList.indexOf(j)) {
                                        Log.d(TAG, "observeItem: betul = ${holder.absoluteAdapterPosition}")
                                        Log.d(TAG, "observeItem: ${produkFilterList[absoluteAdapterPosition]}")
                                        if(!tempProduk.contains(getItem(0))) {
                                            unselectItemView()
                                        }
                                        else{
                                            if(!GridViewHolder.produkIdList.contains(getItem(position).id_produk)){
                                                unselectItemView()
                                            }else{
                                                counter = GridViewHolder.produkQtyList[GridViewHolder.produkIdList.indexOf(id)]!!
                                                binding.counter.setText(counter.toString())
                                                binding.min.isEnabled = true
                                                binding.min2.isEnabled = true
                                                binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context, R.color.orange)
                                                binding.counter.setTextColor(ContextCompat.getColor(itemView.context,R.color.orange))
                                                binding.rootView.background = ContextCompat.getDrawable(itemView.context, R.drawable.item_on_long_click_bg)
                                                binding.counterCardView.strokeColor = if(tempCounter[tempProduk.indexOf(j)]>0) ContextCompat.getColor(itemView.context, R.color.orange) else ContextCompat.getColor(itemView.context, R.color.abuB)
                                            }

                                        }
                                    }else {
                                        if(!tempProduk.contains(getItem(holder.absoluteAdapterPosition))){
                                            unselectItemView()
                                        } else{
                                            if(!GridViewHolder.produkIdList.contains(getItem(position).id_produk)){
                                                unselectItemView()
                                            }else{
                                                Log.d(TAG, "observeItem: masuk sini2? $position")
                                                counter = GridViewHolder.produkQtyList[GridViewHolder.produkIdList.indexOf(id)]!!
                                                binding.counter.setText(counter.toString())
                                                binding.min.isEnabled = true
                                                binding.min2.isEnabled = true
                                                binding.min2.backgroundTintList = ContextCompat.getColorStateList(itemView.context, R.color.orange)
                                                binding.counter.setTextColor(ContextCompat.getColor(itemView.context,R.color.orange))
                                                binding.rootView.background = ContextCompat.getDrawable(itemView.context, R.drawable.item_on_long_click_bg)
                                                binding.counterCardView.strokeColor = if(tempCounter[tempProduk.indexOf(i)]>0) ContextCompat.getColor(itemView.context, R.color.orange) else ContextCompat.getColor(itemView.context, R.color.abuB)
                                            }

                                        }

                                    }
                                    return
                                }
                            }
                        }
                    }
                    observeItem(GridViewHolder.tempProduk, GridViewHolder.tempCounter)

                    binding.removeBtn.setOnClickListener {
                        binding.counter.removeTextChangedListener(textWatcher)
                        counter = 0
                        binding.counter.setText("0")
                        binding.min.isEnabled = false
                        if (GridViewHolder.produkIdList.contains(id)) {
                            Log.d(TAG, "removeBtn: ${produkFilterList[position]}")
                            Log.d(TAG, "removeBtn: ${GridViewHolder.produkIdList.indexOf(id)}")
                            GridViewHolder.produkNameList.removeAt(GridViewHolder.produkIdList.indexOf(id))
                            GridViewHolder.produkPriceList.removeAt(GridViewHolder.produkIdList.indexOf(id))
                            GridViewHolder.produkQtyList.removeAt(GridViewHolder.produkIdList.indexOf(id))
                            GridViewHolder.produkSatuanPerList.removeAt(GridViewHolder.produkIdList.indexOf(id))
                            GridViewHolder.produkIdList.removeAt(GridViewHolder.produkIdList.indexOf(id))
                        }
                        val produk = Produk(
                            GridViewHolder.produkIdList,
                            GridViewHolder.produkNameList,
                            GridViewHolder.produkQtyList,
                            GridViewHolder.produkSatuanPerList,
                            GridViewHolder.produkPriceList
                        )
                        Log.d(TAG, "removeBtn: $produk")
                        binding.min2.backgroundTintList =
                            ContextCompat.getColorStateList(itemView.context, R.color.abuB)
                        binding.counter.setText(counter.toString())
                        binding.counter.setTextColor(
                            if (counter == 0) ContextCompat.getColorStateList(
                                itemView.context,
                                R.color.abuB
                            ) else ContextCompat.getColorStateList(
                                itemView.context,
                                R.color.orange
                            )
                        )
                        binding.rootView.background =
                            if (counter >= 1) ContextCompat.getDrawable(
                                itemView.context,
                                R.drawable.item_on_long_click_bg
                            ) else ContextCompat.getDrawable(
                                itemView.context,
                                R.drawable.rectangle_10r
                            )
                        binding.min2.backgroundTintList =
                            if (counter == 0) ContextCompat.getColorStateList(
                                itemView.context,
                                R.color.abuB
                            ) else ContextCompat.getColorStateList(
                                itemView.context,
                                R.color.orange
                            )
                        binding.counterCardView.strokeColor =
                            if (counter > 0) ContextCompat.getColor(
                                itemView.context,
                                R.color.orange
                            ) else ContextCompat.getColor(itemView.context, R.color.abuB)
                        handler.onMinButtonClick(position, produkList, counter, produk)
                    }
                    binding.editBtn.setOnClickListener {
                        val inflater = LayoutInflater.from(itemView.context)
                        val dialogBinding = DialogEditQuantityBinding.inflate(inflater, null, false)
                        val builder: android.app.AlertDialog.Builder =
                            android.app.AlertDialog.Builder(itemView.context)
                        builder.setView(dialogBinding.root)
                        val alertDialog: android.app.AlertDialog? = builder.create()
                        dialogBinding.negativeBtn.setOnClickListener {
                            alertDialog?.dismiss()
                        }
                        dialogBinding.positiveBtn.setOnClickListener {
                            Log.d(TAG, "onBindViewHolder: $stok")
                            if (dialogBinding.qty.text?.isNotBlank() == true){
                                if(dialogBinding.qty.text.toString().toInt()!=0){
                                    if (dialogBinding.qty.text.toString().toInt() == 0){
                                        alertDialog?.dismiss()
                                        return@setOnClickListener
                                    }
                                    if (dialogBinding.qty.text.toString().toInt() > 0 && stok == 0) {
                                        Toast.makeText(
                                            itemView.context,
                                            itemView.context.getString(R.string.stok_kosong_arg,namaProduk),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@setOnClickListener
                                    }
                                    if (dialogBinding.qty.text.toString().toInt() > stok) {
                                        Toast.makeText(
                                            itemView.context,
                                            itemView.context.getString(R.string.stok_tidak_cukup),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@setOnClickListener
                                    }
                                    binding.counter.addTextChangedListener(textWatcher)
                                    counter = dialogBinding.qty.text.toString().toInt()
                                    binding.counter.setText(dialogBinding.qty.text.toString())
                                    binding.counter.setTextColor(ContextCompat.getColorStateList(
                                        itemView.context,
                                        R.color.orange
                                    ))
                                } else {
                                    binding.counter.addTextChangedListener(textWatcher)
                                    counter = dialogBinding.qty.text.toString().toInt()
                                    binding.counter.setText(dialogBinding.qty.text.toString())
                                    binding.counter.setTextColor(ContextCompat.getColorStateList(
                                        itemView.context,
                                        R.color.abuB
                                    ))
                                }
                            }

                            alertDialog?.dismiss()
                        }
                        alertDialog?.show()
                    }

                    binding.namaProduk.text = namaProduk
                    binding.deskripsi.text = deskripsi
                    binding.harga.text = itemView.context.getString(R.string.harga_jual_arg_2, hargaJual.toRupiahFormat(), satuan.toString(),satuanPer)
                    binding.stok.text = itemView.context.getString(R.string.stok_arg, stok.toString())

                    fun addDataProduk() {
                        if(!GridViewHolder.tempProduk.contains(produkFilterList[position]) && counter > 0){
                            GridViewHolder.tempProduk.add(produkFilterList[position])
                            GridViewHolder.tempCounter.add(GridViewHolder.tempProduk.indexOf(produkFilterList[position]), counter)
                        }
                        GridViewHolder.produkIdList.add(id)
                        GridViewHolder.produkQtyList.add(
                            GridViewHolder.produkIdList.indexOf(id),
                            counter
                        )
                        GridViewHolder.produkNameList.add(
                            GridViewHolder.produkIdList.indexOf(id),
                            namaProduk
                        )
                        GridViewHolder.produkSatuanPerList.add(
                            GridViewHolder.produkIdList.indexOf(id), satuanPer
                        )
                        GridViewHolder.produkPriceList.add(
                            GridViewHolder.produkIdList.indexOf(
                                id
                            ), hargaJual * counter
                        )
                    }

                    binding.plus.setOnClickListener {

                        Log.d(TAG, "onBindViewHolder: $position")
                        binding.counter.removeTextChangedListener(textWatcher)
                        binding.counter.clearFocus()
                        if (stok == 0) {
                            Toast.makeText(
                                itemView.context,
                                itemView.context.getString(R.string.stok_kosong_arg,namaProduk),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }
                        counter += 1

                        if (counter > stok) {
                            Toast.makeText(
                                itemView.context,
                                itemView.context.getString(R.string.stok_tidak_cukup),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }
                        Log.d(TAG, "counter = ${GridViewHolder.produkIdList.contains(id)}")
                        if (!GridViewHolder.produkIdList.contains(id) && counter > 0|| binding.counter.text.toString() == "") {
                            addDataProduk()

                            val produk = Produk(
                                GridViewHolder.produkIdList,
                                GridViewHolder.produkNameList,
                                GridViewHolder.produkQtyList,
                                GridViewHolder.produkSatuanPerList,
                                GridViewHolder.produkPriceList
                            )
                            Log.d(TAG, "bind: tempProduk = ${GridViewHolder.tempProduk}, tempCounter = ${GridViewHolder.tempCounter}")
                            Log.d(TAG, "bind: produkIdList = ${GridViewHolder.produkIdList}")
                            Log.d(TAG, "bind: produkNameList = ${GridViewHolder.produkNameList}")
                            Log.d(TAG, "bind: produkQtyList = ${GridViewHolder.produkQtyList}")
                            Log.d(TAG, "bind: hargaProduk = ${GridViewHolder.produkPriceList}")
                            binding.min.isEnabled = counter >= 1

                            binding.counter.setTextColor(
                                ContextCompat.getColor(
                                    itemView.context,
                                    R.color.orange
                                )
                            )
                            binding.counterCardView.strokeColor =
                                if (counter > 0) ContextCompat.getColor(
                                    itemView.context,
                                    R.color.orange
                                ) else ContextCompat.getColor(itemView.context, R.color.abuB)
                            handler.onPlusButtonClick(position, produkList, counter, produk)
                        }
                        if (GridViewHolder.produkIdList.contains(id) && counter > 1) {
                            if(GridViewHolder.tempProduk.contains(produkFilterList[position]) && counter > 1){
                                GridViewHolder.tempCounter[GridViewHolder.tempProduk.indexOf(produkFilterList[position])] = counter
                            }

                            GridViewHolder.produkNameList[GridViewHolder.produkIdList.indexOf(id)] = produkList[position].namaProduk
                            GridViewHolder.produkQtyList[GridViewHolder.produkIdList.indexOf(id)] =
                                counter
                            GridViewHolder.produkSatuanPerList[GridViewHolder.produkIdList.indexOf(id)] = satuanPer
                            GridViewHolder.produkPriceList[GridViewHolder.produkIdList.indexOf(
                                id
                            )] = produkList[position].hargaJual * counter
                            val produk = Produk(
                                GridViewHolder.produkIdList,
                                GridViewHolder.produkNameList,
                                GridViewHolder.produkQtyList,
                                GridViewHolder.produkSatuanPerList,
                                GridViewHolder.produkPriceList
                            )
                            Log.d(TAG, "bind: produkIdList = ${GridViewHolder.produkIdList}")
                            Log.d(TAG, "bind: produkNameList = ${GridViewHolder.produkNameList}")
                            Log.d(TAG, "bind: produkQtyList = ${GridViewHolder.produkQtyList}")
                            Log.d(TAG, "bind: hargaProduk = ${GridViewHolder.produkPriceList}")
                            binding.counterCardView.strokeColor =
                                if (counter > 0) ContextCompat.getColor(
                                    itemView.context,
                                    R.color.orange
                                ) else ContextCompat.getColor(itemView.context, R.color.abuB)
                            handler.onPlusButtonClick(position, produkList, counter, produk)
                        }
                        binding.counter.setText(counter.toString())
                        binding.rootView.background = if (counter >= 1) ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.item_on_long_click_bg
                        ) else ContextCompat.getDrawable(itemView.context, R.drawable.rectangle_10r)
                        binding.min.isEnabled = counter >= 1
                        binding.min2.backgroundTintList =
                            ContextCompat.getColorStateList(itemView.context, R.color.orange)
                    }
                    binding.min.setOnClickListener {
                        Log.d(
                            TAG,
                            "onBindViewHolder: counter adapter = $counter , counter temp = ${GridViewHolder.tempCounter}"
                        )
                        binding.counter.removeTextChangedListener(textWatcher)
                        binding.counter.clearFocus()

                        counter -= 1

                        if (counter >= 1 || binding.counter.text.toString() != "") {
                            Log.d(TAG, "bind: $counter")
                            if(counter==0){
                                if(GridViewHolder.tempProduk.contains(produkFilterList[position])){
                                    GridViewHolder.tempCounter.removeAt(GridViewHolder.tempProduk.indexOf(produkFilterList[position]))
                                    GridViewHolder.tempProduk.removeAt(GridViewHolder.tempProduk.indexOf(produkFilterList[position]))
                                }
                                if (GridViewHolder.produkIdList.contains(id)) {
                                    GridViewHolder.produkNameList.removeAt(GridViewHolder.produkIdList.indexOf(id))
                                    GridViewHolder.produkPriceList.removeAt(GridViewHolder.produkIdList.indexOf(id))
                                    GridViewHolder.produkQtyList.removeAt(GridViewHolder.produkIdList.indexOf(id))
                                    GridViewHolder.produkSatuanPerList.removeAt(GridViewHolder.produkIdList.indexOf(id))
                                    GridViewHolder.produkIdList.removeAt(GridViewHolder.produkIdList.indexOf(id))
                                }
                            }
                            if (GridViewHolder.tempProduk.indexOf(produkFilterList[position])!=-1){
                                GridViewHolder.tempCounter[GridViewHolder.tempProduk.indexOf(produkFilterList[position])]
                                binding.min.isEnabled = false
                            }else{
                                binding.min.isEnabled = true
                            }
                            if (GridViewHolder.produkIdList.indexOf(id) != -1) {
                                GridViewHolder.produkNameList[GridViewHolder.produkIdList.indexOf(id)] = produkList[position].namaProduk
                                GridViewHolder.produkQtyList[GridViewHolder.produkIdList.indexOf(
                                    id
                                )] = counter
                                GridViewHolder.produkSatuanPerList[GridViewHolder.produkIdList.indexOf(id)] = satuanPer
                                GridViewHolder.produkPriceList[GridViewHolder.produkIdList.indexOf(
                                    id
                                )] = produkList[position].hargaJual * counter
                            } else {
                                binding.min.isEnabled = true
                            }
                            val produk = Produk(
                                GridViewHolder.produkIdList,
                                GridViewHolder.produkNameList,
                                GridViewHolder.produkQtyList,
                                GridViewHolder.produkSatuanPerList,
                                GridViewHolder.produkPriceList
                            )
                            handler.onMinButtonClick(position, produkList, counter, produk)
                            binding.counter.setText(counter.toString())
                            binding.counter.setTextColor(
                                if (counter == 0) ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.abuB
                                ) else ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.orange
                                )
                            )
                            binding.min2.backgroundTintList =
                                if (counter == 0) ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.abuB
                                ) else ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.orange
                                )
                            binding.rootView.background =
                                if (counter >= 1) ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.item_on_long_click_bg
                                ) else ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.rectangle_10r
                                )
                            binding.counterCardView.strokeColor =
                                if (counter > 0) ContextCompat.getColor(
                                    itemView.context,
                                    R.color.orange
                                ) else ContextCompat.getColor(itemView.context, R.color.abuB)

                        }
                        else {
                            Log.d(TAG, "onBindViewHolder: emg masuk?")
                            counter--
                            if(GridViewHolder.tempProduk.contains(produkFilterList[position])){
                                GridViewHolder.tempCounter.removeAt(GridViewHolder.tempProduk.indexOf(produkFilterList[position]))
                                GridViewHolder.tempProduk.removeAt(GridViewHolder.tempProduk.indexOf(produkFilterList[position]))
                            }
                            Log.d(TAG, "onBindViewHolder: ${produkFilterList[position]}")
                            if (GridViewHolder.produkIdList.contains(id)) {
                                GridViewHolder.produkNameList.removeAt(GridViewHolder.produkIdList.indexOf(id))
                                GridViewHolder.produkPriceList.removeAt(GridViewHolder.produkIdList.indexOf(id))
                                GridViewHolder.produkQtyList.removeAt(GridViewHolder.produkIdList.indexOf(id))
                                GridViewHolder.produkSatuanPerList.removeAt(GridViewHolder.produkIdList.indexOf(id))
                                GridViewHolder.produkIdList.removeAt(GridViewHolder.produkIdList.indexOf(id))
                            }
                            val produk = Produk(
                                GridViewHolder.produkIdList,
                                GridViewHolder.produkNameList,
                                GridViewHolder.produkQtyList,
                                GridViewHolder.produkSatuanPerList,
                                GridViewHolder.produkPriceList
                            )
                            binding.min2.backgroundTintList =
                                ContextCompat.getColorStateList(itemView.context, R.color.abuB)
                            Log.d(TAG, "bind: produkIdList = ${GridViewHolder.produkIdList}")
                            Log.d(
                                TAG,
                                "bind: produkIdList = ${GridViewHolder.produkNameList}"
                            )
                            Log.d(
                                TAG,
                                "bind: produkQtyList = ${GridViewHolder.produkQtyList}"
                            )
                            Log.d(
                                TAG,
                                "bind: hargaProduk = ${GridViewHolder.produkPriceList}"
                            )
                            binding.counter.setText(counter.toString())
                            binding.counter.setTextColor(
                                if (counter == 0) ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.abuB
                                ) else ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.orange
                                )
                            )
                            binding.rootView.background =
                                if (counter >= 1) ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.item_on_long_click_bg
                                ) else ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.rectangle_10r
                                )
                            binding.min2.backgroundTintList =
                                if (counter == 0) ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.abuB
                                ) else ContextCompat.getColorStateList(
                                    itemView.context,
                                    R.color.orange
                                )
                            binding.counterCardView.strokeColor =
                                if (counter > 0) ContextCompat.getColor(
                                    itemView.context,
                                    R.color.orange
                                ) else ContextCompat.getColor(itemView.context, R.color.abuB)
                            handler.onMinButtonClick(position, produkList, counter, produk)
                        }
                        binding.min.isEnabled = counter >= 1
                        Log.d(
                            TAG,
                            "onBindViewHolder: counter adapter = $counter , counter temp = ${GridViewHolder.tempCounter}"
                        )
                    }
                }
            }
        }
    }
    interface ClickHandler {
        fun onPlusButtonClick(
            position: Int,
            produk: ArrayList<ProdukEntity>,
            counter: Int,
            produkData: Produk
        )

        fun onMinButtonClick(
            position: Int,
            produk: ArrayList<ProdukEntity>,
            counter: Int,
            produkData: Produk
        )
    }
}