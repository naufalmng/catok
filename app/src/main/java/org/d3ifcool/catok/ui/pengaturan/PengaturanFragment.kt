package org.d3ifcool.catok.ui.pengaturan

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.*
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
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
    private val storageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
//            val filePathAndName = "${Environment.getExternalStorageDirectory()}/BackupCatok/${getString(R.string.nama_toko).replace("\\s".toRegex(), "")}_Backup.csv"
//            val csvFile = File(filePathAndName)
//
//            if(csvFile.exists()){
////                importCsv()
////                exportCsv()
//            }else Toast.makeText(
//                requireContext(),
//                "File belum dibuat, coba lagi..",
//                Toast.LENGTH_SHORT
//            ).show()
        }else{
            Toast.makeText(requireContext(), "Android tidak support !", Toast.LENGTH_SHORT).show()
        }
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
        val imageFile= File("${Environment.getExternalStorageDirectory()}/BackupCatok/Logo Toko.png")
        try {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            bitmap = BitmapFactory.decodeStream(FileInputStream(imageFile),null,options)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        val filePathAndName = "${Environment.getExternalStorageDirectory()}/BackupCatok/${getString(R.string.nama_toko).replace("\\s".toRegex(), "")}_Backup.csv"
        val csvFile = File(filePathAndName)
        if(csvFile.exists() && csvFile.length()>0L){
            try {
                Log.d(TAG, "importCsv: Masuk Ga Sih")
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
                        Log.d(TAG, "importCsv: nextLine = ${temp}")
                        Log.d(TAG, "importCsv: nextLine = ${nextLine[temp]}")
                        Log.d(TAG, "importCsv: nextLine = ${nextLine[temp+1]}")

                    }
                    if(nextLine[temp].isNotEmpty()){
                        temp+=1
                        for (i in nextLine[temp].toInt()..nextLine[temp-1].toInt()){
                            val idProduk = nextLine[temp++]
                            val barcode = nextLine[temp++]
                            val namaProdukEntity = nextLine[temp++]
                            val deskripsi = nextLine[temp++]
                            val modal = nextLine[temp++]
                            val hargaJual = nextLine[temp++]
                            val satuan = nextLine[temp++]
                            val satuanPer = nextLine[temp++]
                            val stok = nextLine[temp++]
                            val tanggalProdukEntity = nextLine[temp++]
                            val produk = ProdukEntity(idProduk.toInt(),barcode.toInt(),namaProdukEntity,deskripsi,modal.toDouble(),hargaJual.toDouble(),satuan.toInt(),satuanPer,stok.toInt(),tanggalProdukEntity.replace(".",","))
                            Log.d(TAG, "importCsv: produkData = $produk")
                            temp++
                            viewModel.tempProduk.value?.add(produk)
                            dataProdukviewModel.insertRecordProduk(produk)
                        }
                        Log.d(TAG, "importCsv: nextLine = ${nextLine[temp]}")
                        temp--
                    }

//                    if(nextLine[0].isNotEmpty()){
//                        temp = 1
//                        for (i in 1..nextLine[0].toInt()){
//                            val idProduk = nextLine[temp++]
//                            val barcode = nextLine[temp++]
//                            val namaProdukEntity = nextLine[temp++]
//                            val deskripsi = nextLine[temp++]
//                            val modal = nextLine[temp++]
//                            val hargaJual = nextLine[temp++]
//                            val satuan = nextLine[temp++]
//                            val stok = nextLine[temp++]
//                            val tanggalProdukEntity = nextLine[temp++]
//                            val produk = ProdukEntity(idProduk.toInt(),barcode.toInt(),namaProdukEntity,deskripsi,modal.toDouble(),hargaJual.toDouble(),satuan.toInt(),stok.toInt(),tanggalProdukEntity.replace(".",","))
//                            dataProdukviewModel.insertRecordProduk(produk)
//                        }
//                    }

                    if(nextLine[temp].isNotEmpty()){
//                        Log.d(TAG, "importCsv: masuk ${nextLine[temp+1].toInt()}")
                        temp+=1
//                        Log.d(TAG, "importCsv: counter: ${nextLine[temp-1].toInt()}")
//                        Log.d(TAG, "importCsv: counterOri ${nextLine[temp].toInt()}")
                        for(i in nextLine[temp].toInt()..nextLine[temp-1].toInt()){
//                            Log.d(TAG, "importCsv: tes masuk")
                            val idTransaksi = nextLine[temp++]
                            val tanggalTransaksi = nextLine[temp++]
                            val transaksi = TransaksiEntity(idTransaksi.toInt(),tanggalTransaksi.replace(".",","))
                            temp++
                            Log.d(TAG, "importCsv: transaksi = ${idTransaksi}\n${tanggalTransaksi}\n")

                            transaksiViewModel.insertTransaksi(transaksi)
                        }
                        temp--
                    }


//                    if(nextLine[temp+1].isNotEmpty()){
////                        Log.d(TAG, "importCsv: masuk ${nextLine[temp+1].toInt()}")
//                        temp+=1
////                        Log.d(TAG, "importCsv: counter: ${nextLine[temp-1].toInt()}")
////                        Log.d(TAG, "importCsv: counterOri ${nextLine[temp].toInt()}")
//                        for(i in nextLine[temp].toInt()..nextLine[temp-1].toInt()){
////                            Log.d(TAG, "importCsv: tes masuk")
//                            val idTransaksi = nextLine[temp++]
//                            val tanggalTransaksi = nextLine[temp++]
//                            val transaksi = TransaksiEntity(idTransaksi.toInt(),tanggalTransaksi.replace(".",","))
//                            temp++
//                            Log.d(TAG, "importCsv: transaksi = ${idTransaksi}\n${tanggalTransaksi}\n")
//
//                            transaksiViewModel.insertTransaksi(transaksi)
//                        }
//                        temp--
//                    }

                    if(nextLine[temp].isNotEmpty()){
//                        Log.d(TAG, "importCsv: masuk ${nextLine[temp].toInt()}")
                        temp +=1
//                        Log.d(TAG, "importCsv: counter: ${nextLine[temp-1].toInt()}")
//                        Log.d(TAG, "importCsv: counterOri ${nextLine[temp].toInt()}")
                        for(i in nextLine[temp].toInt()..nextLine[temp-1].toInt()){
                            val idProdukTransaksiProdukEntity = nextLine[temp++]
                            val idTransaksiTransaksiProdukEntity= nextLine[temp++]
                            val qtyTransaksiProdukEntity= nextLine[temp++]
                            val transaksiProduk = TransaksiProdukEntity(idProdukTransaksiProdukEntity.toInt(),idTransaksiTransaksiProdukEntity.toInt(),qtyTransaksiProdukEntity.toInt())
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
                            val idHistori = nextLine[temp++]
                            Log.d(TAG, "importCsv idhistori: $idHistori")
                            val total = nextLine[temp++]
                            Log.d(TAG, "importCsv total: $total")
                            val diskon = nextLine[temp++]
                            val bayar = nextLine[temp++]
                            val kembalian = nextLine[temp++]
                            val catatan = nextLine[temp++]
                            val produkDibeli = nextLine[temp++]
                            Log.d(TAG, "importCsv produkDibeli: $produkDibeli")
                            val jumlahProdukDibeli= nextLine[temp++]
                            Log.d(TAG, "importCsv jumlahProdukDibeli: $jumlahProdukDibeli")
                            val invoice = nextLine[temp++]
                            Log.d(TAG, "importCsv invoice: $invoice")
                            val pembayaran = nextLine[temp++]
                            Log.d(TAG, "importCsv pembayaran: $pembayaran")
                            val statusBayar = nextLine[temp++]
                            Log.d(TAG, "importCsv statusBayar: $statusBayar")
                            val tanggal = nextLine[temp++]
                            Log.d(TAG, "importCsv tanggal: $tanggal")
                            val historiTransaksi = HistoriTransaksiEntity(idHistori.toLong(),total.toDouble(),diskon.toDouble(),bayar.toDouble(),kembalian.toDouble(),catatan,produkDibeli.replace(".",","),jumlahProdukDibeli.toInt(),invoice,pembayaran,statusBayar,tanggal.replace(".",","))
                            temp++
                            viewModel.tempTransaksi.value?.add(historiTransaksi)
                            transaksiViewModel.insertHistoriTransaksi(historiTransaksi)
                        }
                        temp--
                    }

                    if(nextLine[temp].isNotEmpty()){
                        temp += 1
                        for(i in nextLine[temp].toInt()..nextLine[temp-1].toInt()){
                            val idGrafik = nextLine[temp++]
                            val totalGrafikEntity = nextLine[temp++]
                            val tanggal = nextLine[temp++]
                            val grafik = GrafikEntity(idGrafik.toInt(),totalGrafikEntity.toDouble(),tanggal)
                            temp++
                            transaksiViewModel.insertDataGrafik(grafik)
                        }
                    }

                    Toast.makeText(requireContext(), "Data berhasil direstore...", Toast.LENGTH_SHORT).show()
                    onResume()
                }
            }catch (e: Exception){
                if (e.message!!.contains("length")){
                    Toast.makeText(requireContext(), "Data berhasil direstore...", Toast.LENGTH_SHORT).show()
                    return
                }
                if((e.message!! == "it must not be null")){
                    Toast.makeText(requireContext(), "Data berhasil direstore...", Toast.LENGTH_SHORT).show()
                    return
                }
                if((e.message!!.contains("For input"))){
                    Toast.makeText(requireContext(), "Data berhasil direstore...", Toast.LENGTH_SHORT).show()
                    return
                }
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                onResume()
            }
        }else{

        }
    }

    private fun exportCsv() {
        if(!isDataUpdated){
            Toast.makeText(requireContext(), "Tambah data produk / lakukan transaksi terlebih dahulu..", Toast.LENGTH_SHORT).show()
        } else{
            val folder = File("${Environment.getExternalStorageDirectory()}/BackupCatok")
            val imageFile= File("${Environment.getExternalStorageDirectory()}/BackupCatok/Logo Toko.png")
            val csvFileName = "${getString(R.string.nama_toko).replace("\\s".toRegex(), "")}_Backup.csv"
            val fileNameAndPath = "$folder/$csvFileName"
            val file = File("${Environment.getExternalStorageDirectory()}/BackupCatok/${getString(R.string.nama_toko).replace("\\s".toRegex(), "")}_Backup.csv")
            val contents = folder.list()
//        val br = BufferedReader(FileReader(file))
            if(!folder.exists()){
                Toast.makeText(requireContext(), "Klik 1x lagi untuk backup data..", Toast.LENGTH_SHORT).show()
                folder.mkdir()
            }else{
                if(imageFile.exists()) imageFile.delete()
                folder.delete()
                if(file.exists()) {
                    file.delete()
                }
                folder.mkdir()
            }
            if(imageFile.exists()) imageFile.delete()
//        if(contents.isNotEmpty()) folder.delete()
//        if (contents != null) {
//            if(contents.isNotEmpty()){
//                file.delete()
//                file.createNewFile()
//            }
//        }

            var profilRecord = arrayListOf<ProfilEntity>()
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
//                for(i in historiTransaksiRecord!!){
//                    i.produkDibeli.replace("\n",".").replace(" ","")
//                }
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
                val dirTarget = File("${Environment.getExternalStorageDirectory()}/BackupCatok/")

                loadingDialog.start()
                val fw = FileWriter(fileNameAndPath)

                for (i in profilRecord.indices){
                    val imageFile = File(dirTarget,"Logo Toko.png")
                    if(imageFile.exists()){
                        imageFile.delete()
                    }
                    try {
                        outputStream = FileOutputStream(imageFile)
                    }
                    catch (e: FileNotFoundException) {e.printStackTrace()}
                    (bitmap2?:profilRecord[i].gambar).compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    Log.d(TAG, "Image berhasil disave ")
                    try {
                        outputStream.flush()
                    }catch (e: IOException){e.printStackTrace()}
                    try {
                        outputStream.close()
                    }catch (e: IOException){e.printStackTrace()}
                    fw.append(""+profilRecord[i].id)
                    fw.append(",")
                    fw.append(""+profilRecord[i].namaToko)
                    fw.append(",")
                    fw.append(""+ "null")
                }
                if (produkRecord!=null){
                    for(i in produkRecord!!.indices){
                        fw.append(",")
                        fw.append("${produkRecord!!.size}")
                        fw.append(",")
                        fw.append(""+produkRecord!![i].id_produk)
                        fw.append(",")
                        fw.append(""+"-1")
                        fw.append(",")
                        fw.append(""+produkRecord!![i].namaProduk)
                        fw.append(",")
                        fw.append(""+produkRecord!![i].deskripsi)
                        fw.append(",")
                        fw.append(""+produkRecord!![i].modal)
                        fw.append(",")
                        fw.append(""+produkRecord!![i].hargaJual)
                        fw.append(",")
                        fw.append(""+produkRecord!![i].satuan)
                        fw.append(",")
                        fw.append(""+produkRecord!![i].satuanPer)
                        fw.append(",")
                        fw.append(""+produkRecord!![i].stok)
                        fw.append(",")
                        fw.append(""+produkRecord!![i].tanggal.replace(",","."))
                    }

                }
                if(transaksiRecord!=null){
                    for(i in transaksiRecord!!.indices){
                        fw.append(",")
                        fw.append("${transaksiRecord!!.size}")
                        fw.append(",")
                        fw.append(""+transaksiRecord!![i].id_transaksi)
                        fw.append(",")
                        fw.append(""+transaksiRecord!![i].tanggal.replace(",","."))
                    }
                    for(i in transaksiProdukRecord!!.indices){
                        fw.append(",")
                        fw.append("${transaksiProdukRecord!!.size}")
                        fw.append(",")
                        fw.append(""+transaksiProdukRecord!![i].id_produk)
                        fw.append(",")
                        fw.append(""+transaksiProdukRecord!![i].id_transaksi)
                        fw.append(",")
                        fw.append(""+transaksiProdukRecord!![i].qty)
                    }
                    for(i in historiTransaksiRecord!!.indices){
                        fw.append(",")
                        fw.append("${historiTransaksiRecord!!.size}")
                        fw.append(",")
                        fw.append(""+historiTransaksiRecord!![i].id_histori)
                        fw.append(",")
                        fw.append(""+historiTransaksiRecord!![i].total)
                        fw.append(",")
                        fw.append(""+historiTransaksiRecord!![i].diskon)
                        fw.append(",")
                        fw.append(""+historiTransaksiRecord!![i].bayar)
                        fw.append(",")
                        fw.append(""+historiTransaksiRecord!![i].kembalian)
                        fw.append(",")
                        fw.append(""+historiTransaksiRecord!![i].catatan)
                        fw.append(",")
                        fw.append(""+historiTransaksiRecord!![i].produkDibeli.replace(",","."))
                        fw.append(",")
                        fw.append(""+historiTransaksiRecord!![i].jumlahProdukDibeli)
                        fw.append(",")
                        fw.append(""+historiTransaksiRecord!![i].invoice)
                        fw.append(",")
                        fw.append(""+historiTransaksiRecord!![i].pembayaran)
                        fw.append(",")
                        fw.append(""+historiTransaksiRecord!![i].statusBayar)
                        fw.append(",")
                        fw.append(""+historiTransaksiRecord!![i].tanggal.replace(",","."))

                    }

                    for(i in grafikRecord!!.indices){
                        fw.append(",")
                        fw.append("${grafikRecord!!.size}")
                        fw.append(",")
                        fw.append(""+grafikRecord!![i].id)
                        fw.append(",")
                        fw.append(""+grafikRecord!![i].totalTransaksi)
                        fw.append(",")
                        fw.append(""+grafikRecord!![i].tanggal)
                    }
                }

                fw.flush()
                fw.close()
                loadingDialog.dismiss()
                if (contents != null) {
                    if(contents.isEmpty() || file.length()== 0L || file.length()<20L) Toast.makeText(requireContext(), "Klik 1x lagi untuk backup data..", Toast.LENGTH_SHORT).show()
                    else showSnackbar(binding.backupData,"Backup Telah diexport ke $fileNameAndPath")
                }
            }catch (e: Exception){
                Toast.makeText(requireContext(), "Izin ditolak...", Toast.LENGTH_SHORT).show()
                loadingDialog.dismiss()
            }
        }
    }



    @SuppressLint("InflateParams")
    fun showSnackbar(v:View, text: String){
        val snackbar = Snackbar.make(v,text, Snackbar.LENGTH_INDEFINITE)
        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.orange))
        snackbar.setActionTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        snackbar.setTextMaxLines(5)
        snackbar.setAction("Tutup"){
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
    var isDataUpdated by Delegates.notNull<Boolean>()
    private var bitmap: Bitmap? = null
    private var bitmap2: Bitmap? = null
    private val dataProdukviewModel: DataProdukViewModel by viewModel()
    private val transaksiViewModel: TransaksiViewModel by viewModel()
    private val viewModel: PengaturanViewModel by viewModel()
    private lateinit var loadingDialog: LoadingDialog
    private val pickAndSaveImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK){
            val uriImage = result.data?.data
            try {
                val dirTarget = File("${Environment.getExternalStorageDirectory()}/BackupCatok/")
                if(!dirTarget.exists()){
                    dirTarget.mkdir()
                }
                val inputStream = requireContext().contentResolver.openInputStream(uriImage!!)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                bitmap2 = bitmap
                sharedViewModel.fotoToko.value = bitmap
                binding.circleImage.load(bitmap)
                viewModel.insertProfil(ProfilEntity(id= 1, namaToko = binding.namaToko.text.toString()?:viewModel.getDataProfil.value!!.namaToko, gambar = bitmap?:binding.circleImage.drawable.toBitmap()))
            } catch (e:FileNotFoundException){
                e.printStackTrace()
            }
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
//        sharedViewModel.fotoToko.value = binding.namaToko.text.toString()
        return binding.root
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED)
    }


    private fun setupObservers() {
        sharedViewModel.getNamaToko.observe(viewLifecycleOwner){
            Log.d("PengaturanFragment", "getNamaToko: ${it}")
            if(it!=null){
                binding.namaToko.text = it
                viewModel.insertProfil(ProfilEntity(id= 1, namaToko = it, gambar = binding.circleImage.drawable.toBitmap()?:viewModel.dataProfil.value!!.gambar))
                Log.d(TAG, "insertProfil: ${viewModel.dataProfil.value?.namaToko.toString()}")
                observeDataProfil()

            }
        }
        sharedViewModel.namaToko.value = null
        observeDataProfil()
    }

    private fun observeDataProfil() {
        viewModel.getDataProfil.observe(viewLifecycleOwner){
            Log.d("PengaturanFragment", "getDataProfil: ${it}")
            if(it!=null){
                binding.circleImage.load(it.gambar?:sharedViewModel.getFotoToko.value)
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
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                pickAndSaveImage.launch(intent)
            }
            editNama.setOnClickListener {
                showDialogEditNama()
            }
        }
    }

    private fun requestPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            Toast.makeText(requireContext(), "Android anda tidak support !", Toast.LENGTH_SHORT).show()
//            try {
//                val uri = Uri.fromParts("package",requireActivity().packageName,null)
//                val filePathAndName = "${Environment.getExternalStorageDirectory()}/BackupCatok/"
//                Toast.makeText(requireContext(), "Simpan ! Jangan pindah folder atau mengganti nama", Toast.LENGTH_LONG).show()
//                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
//                    addCategory(Intent.CATEGORY_OPENABLE)
//                    type = "text/csv"
//                    putExtra(Intent.EXTRA_TITLE,filePathAndName)
//                }
//                storageActivityResultLauncher.launch(intent)
//            }catch (e: Exception){
//                val intent = Intent()
//                storageActivityResultLauncher.launch(intent)
//            }
        }else{
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(WRITE_EXTERNAL_STORAGE,
                READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
        }
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
//            try {
//                val intent = Intent()
//                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
//                val uri = Uri.fromParts("package",requireActivity().packageName,null)
//                intent.data = uri
//                storageActivityResultLauncher.launch(intent)
//            }catch (e: Exception){
//                val intent = Intent()
//                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
//                storageActivityResultLauncher.launch(intent)
//            }
//        }else{
//            ActivityCompat.requestPermissions(requireActivity(), arrayOf(WRITE_EXTERNAL_STORAGE,
//                READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
//        }
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