@file:Suppress("KotlinDeprecation")

package org.d3ifcool.catok.ui.pengaturan

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.opencsv.CSVReader
import kotlinx.coroutines.launch
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.DataStorePreferences
import org.d3ifcool.catok.core.data.dataStore
import org.d3ifcool.catok.core.data.source.local.entities.*
import org.d3ifcool.catok.databinding.FragmentPengaturanBinding
import org.d3ifcool.catok.ui.beranda.produk.DataProdukViewModel
import org.d3ifcool.catok.ui.beranda.transaksi.TransaksiViewModel
import org.d3ifcool.catok.ui.main.MainActivity
import org.d3ifcool.catok.ui.main.SharedViewModel
import org.d3ifcool.catok.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.*
import kotlin.properties.Delegates


class PengaturanFragment : Fragment() {
    companion object{
        private const val STORAGE_PERMISSION_CODE = 100
        private val TAG = PengaturanFragment().javaClass.name
    }
    private fun checkPermission(): Boolean{
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            Environment.isExternalStorageManager()
        }
        else {
            val write = ContextCompat.checkSelfPermission(requireActivity(),WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(requireActivity(),READ_EXTERNAL_STORAGE)
            return write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun importCsv() {
        lifecycleScope.launch {
            dataStorePreferences.saveDataUpdateSetting(requireContext(),false)
        }
        val imageFile= File("${Environment.getExternalStorageDirectory()}${getString(R.string.photoPath)}")
        try {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            bitmap = BitmapFactory.decodeStream(FileInputStream(imageFile),null,options)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        val filePathAndName = "${Environment.getExternalStorageDirectory()}${getString(R.string.restore_folder)}${getString(R.string.nama_toko).replace("\\s".toRegex(), "")}_Backup.csv"
        val csvFile = File(filePathAndName)
        if(csvFile.exists() && csvFile.length()>0L){
            try {
                val csvReader = CSVReader(FileReader(csvFile.absolutePath))
                var nextLine : Array<String>
                while (csvReader.readNext().also { nextLine = it } != null){
                    var temp = 1
                    if(nextLine[0].isNotEmpty()){
                        temp = 0
                        for (i in 1..nextLine[0].toInt()){
                            val idProfil = nextLine[temp++]
                            val namaToko = nextLine[temp++]
                            val gambar = nextLine[temp++]
                            Log.d(TAG, "importCsv: ${idProfil},${namaToko},${gambar}")
                            viewModel.insertProfil(ProfilEntity(idProfil.toInt(),namaToko, bitmap?:binding.circleImage.drawable.toBitmap()))
                        }
                        Log.d(TAG, "importCsv: nextLine = $temp")
                        Log.d(TAG, "importCsv: nextLine = ${nextLine[temp]}")
                        Log.d(TAG, "importCsv: nextLine = ${nextLine[temp+1]}")

                    }
                    if(nextLine[temp].isNotEmpty()){
                        temp+=1
                        for (i in nextLine[temp].toInt()..nextLine[temp-1].toInt()){
                            val idProduk = nextLine[temp++]
                            val barcode = nextLine[temp++]
                            val namaProduk = nextLine[temp++]
                            val deskripsi = nextLine[temp++]
                            val modal = nextLine[temp++]
                            val hargaJual = nextLine[temp++]
                            val satuan = nextLine[temp++]
                            val satuanPer = nextLine[temp++]
                            val stok = nextLine[temp++]
                            val tanggalProduk = nextLine[temp++]
                            val produk = ProdukEntity(idProduk.toInt(),barcode.toInt(),namaProduk,deskripsi,modal.toDouble(),hargaJual.toDouble(),satuan.toInt(),satuanPer,stok.toInt(),tanggalProduk.replace(".",getString(R.string.comma)))
                            Log.d(TAG, "importCsv: produkData = $produk")
                            temp++
                            viewModel.tempProduk.value?.add(produk)
                            dataProdukviewModel.insertRecordProduk(produk)
                        }
                        temp--
                        Log.d(TAG, "importCsv: nextLine = ${nextLine[temp]}")
                    }

                    if(nextLine[temp].isNotEmpty()){
                        temp+=1
                        for(i in nextLine[temp].toInt()..nextLine[temp-1].toInt()){
                            Log.d(TAG, "importCsv: transaksi tes masuk")
                            temp++
                            val idTransaksi = nextLine[temp++]
                            val tanggalTransaksi = nextLine[temp++]
                            val transaksi = TransaksiEntity(idTransaksi,tanggalTransaksi.replace(".",getString(R.string.comma)))
                            Log.d(TAG, "importCsv: transaksi = $transaksi")
                            temp++
                            transaksiViewModel.insertTransaksi(transaksi)
                        }
                        temp--
                    }


                    if(nextLine[temp].isNotEmpty()){
                        temp +=1
                        for(i in nextLine[temp].toInt()..nextLine[temp-1].toInt()){
                            temp++
                            val idProdukTransaksiProdukEntity = nextLine[temp++]
                            val idTransaksiTransaksiProdukEntity= nextLine[temp++]
                            val qtyTransaksiProdukEntity= nextLine[temp++]
                            val transaksiProduk = TransaksiProdukEntity(idProdukTransaksiProdukEntity.toInt(),idTransaksiTransaksiProdukEntity,qtyTransaksiProdukEntity.toInt())
                            Log.d(TAG, "importCsv: transaksiProduk = \n${idProdukTransaksiProdukEntity}\n${idTransaksiTransaksiProdukEntity}\n${qtyTransaksiProdukEntity}\n")
                            temp++
                            transaksiViewModel.insertTransaksiProdukRecord(transaksiProduk)
                        }

                        temp--
                    }

                    if(nextLine[temp].isNotEmpty()){
                        temp +=1
                        Log.d(TAG, "importCsv: counter: ${nextLine[temp].toInt()}")
                        Log.d(TAG, "importCsv: counter: ${nextLine[temp-1].toInt()}")

                        for (i in nextLine[temp].toInt()..nextLine[temp-1].toInt()){
                            temp++
                            val idHistori = nextLine[temp++]
                            Log.d(TAG, "importCsv idhistori: $idHistori")
                            val total = nextLine[temp++]
                            Log.d(TAG, "importCsv total: $total")
                            val diskon = nextLine[temp++]
                            val bayar = nextLine[temp++]
                            val kembalian = nextLine[temp++]
                            val catatan = nextLine[temp++]
                            val produkDibeli = nextLine[temp++]
                            val jumlahProdukDibeli= nextLine[temp++]
                            Log.d(TAG, "importCsv jumlahProdukDibeli: $jumlahProdukDibeli")
                            val pembayaran = nextLine[temp++]
                            Log.d(TAG, "importCsv pembayaran: $pembayaran")
                            val statusBayar = nextLine[temp++]
                            Log.d(TAG, "importCsv statusBayar: $statusBayar")
                            val tanggal = nextLine[temp++]
                            Log.d(TAG, "importCsv tanggal: $tanggal")
                            val historiTransaksi = HistoriTransaksiEntity(idHistori,total.toDouble(),diskon.toDouble(),bayar.toDouble(),kembalian.toDouble(),catatan,produkDibeli.replace("*","\n").replace(".",":"),jumlahProdukDibeli.toInt(),pembayaran,statusBayar,tanggal.replace(".",getString(R.string.comma)))
                            temp++
                            viewModel.tempTransaksi.value?.add(historiTransaksi)
                            transaksiViewModel.insertHistoriTransaksi(historiTransaksi)
                        }
                        temp--
                    }

                    if(nextLine[temp].isNotEmpty()){
                        temp += 1
                        for(i in nextLine[temp].toInt()..nextLine[temp-1].toInt()){
                            temp++
                            val idGrafik = nextLine[temp++]
                            val totalGrafikEntity = nextLine[temp++]
                            val tanggal = nextLine[temp++]
                            val tanggal2 = nextLine[temp++]
                            val bulanDanTahun = nextLine[temp++]
                            val grafik = GrafikEntity(idGrafik,totalGrafikEntity.toDouble(),tanggal = tanggal.toLong(), bulanDanTahun = bulanDanTahun, tanggal2 = tanggal2.replace(".",getString(R.string.comma)))
                            temp++
                            transaksiViewModel.insertDataGrafik(grafik)
                        }
                        temp--
                    }

                    Toast.makeText(requireContext(), getString(R.string.data_berhasil_direstore), Toast.LENGTH_SHORT).show()
                    onResume()
                }
            }catch (e: Exception){
                if (e.message!!.contains(getString(R.string.restoreMsgAneh1))){
                    Toast.makeText(requireContext(), getString(R.string.data_berhasil_direstore), Toast.LENGTH_SHORT).show()
                    return
                }
                if((e.message!! == getString(R.string.restoreMsgAneh2))){
                    Toast.makeText(requireContext(), getString(R.string.data_berhasil_direstore), Toast.LENGTH_SHORT).show()
                    return
                }
                if((e.message!!.contains(getString(R.string.restoreMsgAneh3)))){
                    Toast.makeText(requireContext(), getString(R.string.data_berhasil_direstore), Toast.LENGTH_SHORT).show()
                    return
                }
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                onResume()
            }
        }
    }

    private fun exportCsv() {
        if(!isDataUpdated){
            Toast.makeText(requireContext(), getString(R.string.backup_warning), Toast.LENGTH_SHORT).show()
        } else{
            val folder = File("${Environment.getExternalStorageDirectory()}${getString(R.string.backup_folder)}")
            val csvFileName = "${getString(R.string.nama_toko).replace("\\s".toRegex(), "")}_Backup.csv"
            val fileNameAndPath = "$folder/$csvFileName"
            val file = File("${Environment.getExternalStorageDirectory()}${getString(R.string.restore_folder)}${getString(R.string.nama_toko).replace("\\s".toRegex(), "")}_Backup.csv")
            val contents = folder.list()
            if(!folder.exists()){
                Toast.makeText(requireContext(), getString(R.string.press_twice_to_backup), Toast.LENGTH_SHORT).show()
                folder.delete()
                folder.mkdir()
            }else{
                folder.delete()
                if(file.exists()) {
                    file.delete()
                }else{
                    Toast.makeText(requireContext(), getString(R.string.press_twice_to_backup), Toast.LENGTH_SHORT).show()
                }
                folder.mkdir()
            }

            val profilRecord = arrayListOf<ProfilEntity>()
            var produkRecord : ArrayList<ProdukEntity>? = null
            var transaksiRecord : ArrayList<TransaksiEntity>? = null
            var transaksiProdukRecord : ArrayList<TransaksiProdukEntity>? = null
            var historiTransaksiRecord : ArrayList<HistoriTransaksiEntity>? = null
            var grafikRecord : ArrayList<GrafikEntity>? = null
            profilRecord.clear()
            produkRecord?.clear()
            transaksiRecord?.clear()
            transaksiProdukRecord?.clear()
            historiTransaksiRecord?.clear()
            grafikRecord?.clear()
            viewModel.getDataProfil.observe(viewLifecycleOwner){
                if(it!=null){
                    profilRecord.add(it)
                }
            }
            transaksiViewModel.getDataProduk.observe(viewLifecycleOwner){
                if(it!=null){
                    produkRecord = it
                }
            }
            transaksiViewModel.getDataTransaksi.observe(viewLifecycleOwner){
                if(it!=null){
                    transaksiRecord = it
                }
            }
            transaksiViewModel.getDataTransaksiProduk.observe(viewLifecycleOwner){
                if(it!=null){
                    transaksiProdukRecord = it
                    Log.d(TAG, "exportCsv: $transaksiProdukRecord")
                }
            }
            transaksiViewModel.getDataHistoriTransaksi.observe(viewLifecycleOwner){
                if(it!=null){
                    historiTransaksiRecord = it
                    Log.d(TAG, "exportCsv: $historiTransaksiRecord")
                }
            }
            transaksiViewModel.getDataGrafik.observe(viewLifecycleOwner){
                if(it!=null){
                    grafikRecord = it
                }
            }
            try {
                lateinit var outputStream : OutputStream
                val dirTarget = File("${Environment.getExternalStorageDirectory()}${getString(R.string.restore_folder)}")

                loadingDialog.start()
                val fw = FileWriter(fileNameAndPath)
                fun writeCsvFile(){
                    for (i in profilRecord.indices){
                        val imageFile = File(dirTarget,getString(R.string.nama_file_foto))
                        try {
                            outputStream = FileOutputStream(imageFile)
                        }
                        catch (e: FileNotFoundException) {e.printStackTrace()}
                        finally {
                            (sharedViewModel.getFotoToko.value?:profilRecord[i].gambar).compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                        }

                        Log.d(TAG, "Image berhasil disave ")
                        try {
                            outputStream.flush()
                        }catch (e: IOException){e.printStackTrace()}
                        try {
                            outputStream.close()
                        }catch (e: IOException){e.printStackTrace()}
                        fw.append(""+profilRecord[i].id)
                        fw.append(getString(R.string.comma))
                        fw.append(""+profilRecord[i].namaToko)
                        fw.append(getString(R.string.comma))
                        fw.append(""+ "null")
                    }
                    if (produkRecord!=null){
                        for(i in produkRecord!!.indices){
                            fw.append(getString(R.string.comma))
                            fw.append("${produkRecord!!.size}")
                            fw.append(getString(R.string.comma))
                            fw.append(""+produkRecord!![i].id_produk)
                            fw.append(getString(R.string.comma))
                            fw.append(""+getString(R.string.minusOne))
                            fw.append(getString(R.string.comma))
                            fw.append(""+produkRecord!![i].namaProduk)
                            fw.append(getString(R.string.comma))
                            fw.append(""+produkRecord!![i].deskripsi)
                            fw.append(getString(R.string.comma))
                            fw.append(""+produkRecord!![i].modal)
                            fw.append(getString(R.string.comma))
                            fw.append(""+produkRecord!![i].hargaJual)
                            fw.append(getString(R.string.comma))
                            fw.append(""+produkRecord!![i].satuan)
                            fw.append(getString(R.string.comma))
                            fw.append(""+produkRecord!![i].satuanPer)
                            fw.append(getString(R.string.comma))
                            fw.append(""+produkRecord!![i].stok)
                            fw.append(getString(R.string.comma))
                            fw.append(""+produkRecord!![i].tanggal.replace(getString(R.string.comma),"."))
                        }

                    }
                    if(transaksiRecord!=null){
                        for(i in transaksiRecord!!.indices){
                            fw.append(getString(R.string.comma))
                            fw.append("${transaksiRecord!!.size}")
                            fw.append(getString(R.string.comma))
                            fw.append(""+i.plus(1))
                            fw.append(getString(R.string.comma))
                            fw.append(""+transaksiRecord!![i].id_transaksi)
                            fw.append(getString(R.string.comma))
                            fw.append(""+transaksiRecord!![i].tanggal.replace(getString(R.string.comma),"."))
                        }
                        for(i in transaksiProdukRecord!!.indices){
                            fw.append(getString(R.string.comma))
                            fw.append("${transaksiProdukRecord!!.size}")
                            fw.append(getString(R.string.comma))
                            fw.append(""+i.plus(1))
                            fw.append(getString(R.string.comma))
                            fw.append(""+transaksiProdukRecord!![i].id_produk)
                            fw.append(getString(R.string.comma))
                            fw.append(""+transaksiProdukRecord!![i].id_transaksi)
                            fw.append(getString(R.string.comma))
                            fw.append(""+transaksiProdukRecord!![i].qty)
                        }
                        for(i in historiTransaksiRecord!!.indices){
                            fw.append(getString(R.string.comma))
                            fw.append("${historiTransaksiRecord!!.size}")
                            fw.append(getString(R.string.comma))
                            fw.append(""+i.plus(1))
                            fw.append(getString(R.string.comma))
                            fw.append(""+historiTransaksiRecord!![i].id_histori)
                            fw.append(getString(R.string.comma))
                            fw.append(""+historiTransaksiRecord!![i].total)
                            fw.append(getString(R.string.comma))
                            fw.append(""+historiTransaksiRecord!![i].diskon)
                            fw.append(getString(R.string.comma))
                            fw.append(""+historiTransaksiRecord!![i].bayar)
                            fw.append(getString(R.string.comma))
                            fw.append(""+historiTransaksiRecord!![i].kembalian)
                            fw.append(getString(R.string.comma))
                            fw.append(""+historiTransaksiRecord!![i].catatan)
                            fw.append(getString(R.string.comma))
                            fw.append(""+historiTransaksiRecord!![i].produkDibeli.replace("\n","*").replace(":","."))
                            fw.append(getString(R.string.comma))
                            fw.append(""+historiTransaksiRecord!![i].jumlahProdukDibeli)
                            fw.append(getString(R.string.comma))
                            fw.append(""+historiTransaksiRecord!![i].pembayaran)
                            fw.append(getString(R.string.comma))
                            fw.append(""+historiTransaksiRecord!![i].statusBayar)
                            fw.append(getString(R.string.comma))
                            fw.append(""+historiTransaksiRecord!![i].tanggal.replace(getString(R.string.comma),"."))
                        }

                        for(i in grafikRecord!!.indices){
                            fw.append(getString(R.string.comma))
                            fw.append("${grafikRecord!!.size}")
                            fw.append(getString(R.string.comma))
                            fw.append(""+i.plus(1))
                            fw.append(getString(R.string.comma))
                            fw.append(""+grafikRecord!![i].id)
                            fw.append(getString(R.string.comma))
                            fw.append(""+grafikRecord!![i].totalTransaksi)
                            fw.append(getString(R.string.comma))
                            fw.append(""+grafikRecord!![i].tanggal)
                            fw.append(getString(R.string.comma))
                            fw.append(""+grafikRecord!![i].tanggal2.replace(getString(R.string.comma),"."))
                            fw.append(getString(R.string.comma))
                            fw.append(""+grafikRecord!![i].bulanDanTahun)
                        }
                    }
                }
                writeCsvFile()
                fw.flush()
                fw.close()

                loadingDialog.dismiss()

            }catch (e: Exception){
                Toast.makeText(requireContext(), getString(R.string.izin_ditolak), Toast.LENGTH_SHORT).show()
                loadingDialog.dismiss()
            }
            finally {
                if (contents != null) {
                    if(contents.isEmpty() || file.length() == 0L || file.length()<=27L || !file.exists()) Toast.makeText(requireContext(), "Klik 1x lagi untuk backup data..", Toast.LENGTH_SHORT).show()
                    else {
                        showSnackbar(binding.backupData,getString(R.string.export_arg,fileNameAndPath))
                        binding.backupData.isClickable = false
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.backupData.isClickable = true
                            requireActivity().startActivity(Intent(requireContext(), MainActivity::class.java))
                        },1500)
                    }
                }
            }
        }
    }



    @SuppressLint("InflateParams")
    fun showSnackbar(v:View, text: String){
        val snackbar = Snackbar.make(v,text, Snackbar.LENGTH_INDEFINITE)
        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.orange))
        snackbar.setActionTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        snackbar.setTextMaxLines(5)
        snackbar.setAction(getString(R.string.tutup)){
            snackbar.dismiss()
        }
        snackbar.show()
    }
    private var _binding: FragmentPengaturanBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }
    private val dataStorePreferences: DataStorePreferences by lazy {
        DataStorePreferences(requireActivity().dataStore)
    }
    private var isDataUpdated by Delegates.notNull<Boolean>()
    private var bitmap: Bitmap? = null
    private var bitmap2: Bitmap? = null
    private val dataProdukviewModel: DataProdukViewModel by viewModel()
    private val transaksiViewModel: TransaksiViewModel by viewModel()
    private val viewModel: PengaturanViewModel by viewModel()
    private lateinit var loadingDialog: LoadingDialog
    private val pickAndSaveImage = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
        try {
            val dirTarget = File("${Environment.getExternalStorageDirectory()}${getString(R.string.restore_folder)}")

            if (!dirTarget.exists()) {
                dirTarget.mkdir()
            }
            val inputStream = requireContext().contentResolver.openInputStream(result!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            bitmap2 = bitmap
            sharedViewModel.fotoToko.value = bitmap
            binding.circleImage.load(bitmap)
//
            viewModel.updateProfil(
                nama = binding.namaToko.text.toString() ?: viewModel.getDataProfil.value!!.namaToko,
                gambar = sharedViewModel.fotoToko.value ?: bitmap
                ?: binding.circleImage.drawable.toBitmap()
            )
        }catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPengaturanBinding.inflate(inflater, container, false)
        loadingDialog = LoadingDialog(requireActivity())
        dataStorePreferences.isDataUpdated.asLiveData()
            .observe(viewLifecycleOwner){
                isDataUpdated = it
            }
        Log.d("PengaturanFragment", "onCreateView: ${sharedViewModel.fotoToko.value}")
        checkStoragePermission()
        binding.restoreData.enableOnClickAnimation()
        binding.backupData.enableOnClickAnimation()
        return binding.root
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED)
    }


    private fun setupObservers() {
        sharedViewModel.getNamaToko.observe(viewLifecycleOwner){
            Log.d("PengaturanFragment", "getNamaToko: $it")
            if(it!=null){
                binding.namaToko.text = it
                viewModel.updateProfil(nama = it, gambar = sharedViewModel.getFotoToko.value?:binding.circleImage.drawable.toBitmap())
                Log.d(TAG, "insertProfil: ${viewModel.dataProfil.value?.namaToko.toString()}")
                observeDataProfil()
            }
        }

        observeDataProfil()
    }

    private fun observeDataProfil() {
        viewModel.getDataProfil.observe(viewLifecycleOwner){
            Log.d("PengaturanFragment", "getDataProfil: $it")
            if(it!=null){
                binding.circleImage.load(sharedViewModel.getFotoToko.value?:it.gambar)
                binding.namaToko.text = sharedViewModel.getNamaToko.value?:it.namaToko
            }
            else{
                binding.circleImage.load(bitmap?:ContextCompat.getDrawable(requireContext(),R.drawable.ic_catok)!!.toBitmap())
                binding.namaToko.text = sharedViewModel.getNamaToko.value?:getString(R.string.nama_toko)
            }
        }
    }

    private fun setupListeners() {
        with(binding){
            backupData.setOnClickListener{
                viewModel.isBtnBackupDataClicked = true
                if(checkPermission()){
                    exportCsv()
                    onResume()
                }
                else {
                    val snackbar = Snackbar.make(backupData,getString(R.string.izin_penyimpanan), Snackbar.LENGTH_LONG)
                    snackbar.setAction(getString(R.string.izinkan)){
                        snackbar.dismiss()
                        requestPermission()
                    }
                    snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.orange))
                    snackbar.setActionTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                    snackbar.show()

                    if(snackbar.isShown){
                        Handler(Looper.getMainLooper()).postDelayed({
                            snackbar.dismiss()
                        },3000)
                    }
                }
            }
            restoreData.setOnClickListener{
                viewModel.isBtnBackupDataClicked = false
                if(checkPermission()){
                    importCsv()
                    onResume()
                }
                else {
                    val snackbar = Snackbar.make(backupData,getString(R.string.izin_penyimpanan), Snackbar.LENGTH_LONG)
                    snackbar.setAction(getString(R.string.izinkan)){
                        snackbar.dismiss()
                        requestPermission()
                    }
                    snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.orange))
                    snackbar.setActionTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                    snackbar.show()

                    if(snackbar.isShown){
                        Handler(Looper.getMainLooper()).postDelayed({
                            snackbar.dismiss()
                        },3000)
                    }
                }
            }
            editPhoto.setOnClickListener {
                if(checkPermission()){
                    pickAndSaveImage.launch(getString(R.string.input_type))
                    onResume()
                }else{
                    requestPermission()
                }
            }
            editNama.setOnClickListener {
                showDialogEditNama()
            }
        }
    }

    private fun requestPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            Toast.makeText(requireContext(), getString(R.string.android_tidak_support), Toast.LENGTH_SHORT).show()
        }else{
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(WRITE_EXTERNAL_STORAGE,
                READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
        }
    }

    private fun showDialogEditNama() {
        findNavController().navigate(R.id.action_pengaturanFragment_to_editNamaDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupListeners()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}