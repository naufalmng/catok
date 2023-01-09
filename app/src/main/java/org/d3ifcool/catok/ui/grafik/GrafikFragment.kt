package org.d3ifcool.catok.ui.grafik

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.DataStorePreferences
import org.d3ifcool.catok.core.data.dataStore
import org.d3ifcool.catok.databinding.FragmentGrafikBinding
import org.d3ifcool.catok.ui.beranda.transaksi.TransaksiViewModel
import org.d3ifcool.catok.ui.main.SharedViewModel
import org.d3ifcool.catok.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception

class GrafikFragment : Fragment() {
    companion object{
        private const val TAG = "GrafikFragment"
    }
    var result = 0.0
    private var _binding: FragmentGrafikBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GrafikViewModel by viewModel()
    private val transaksiViewModel: TransaksiViewModel by viewModel()
    var listTanggal = mutableListOf<String>()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val dataStorePreferences: DataStorePreferences by lazy {
        DataStorePreferences(requireActivity().dataStore)
    }
    private var currentDate = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGrafikBinding.inflate(inflater, container, false)
        result = 0.0
        sharedViewModel.totalTransaksi.value = 0.0
        transaksiViewModel.getDataHistoriTransaksi.observe(viewLifecycleOwner){
            Log.d(TAG, "dataHistoriTransaksi: $it")
            Log.d("GrafikFragment", "totalTransaksi: ${ sharedViewModel.totalTransaksi.value.toString()}")

            if(!it.isNullOrEmpty()){
                result = 0.0
                it.forEach { data ->
                    result += data.total
                    Log.d("GrafikFragment", "totalTransaksi: ${result}")
                }
                sharedViewModel.totalTransaksi.value = sharedViewModel.totalTransaksi.value!!+result
            }
        }
        binding.spinnerFilter.text = currentDate
//        result = 0.0
//        viewModel.totalTransaksi.value = 0.0
        transaksiViewModel.getFilterGrafik.observe(viewLifecycleOwner){
            listTanggal.clear()
            for (i in it.indices){
                listTanggal.add(it[i].date)
            }
            try {
                binding.spinnerFilter.setItems(listTanggal)
            }catch (e: Exception){
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
            }
            finally {
                if(listTanggal.contains(currentDate)){
                    binding.spinnerFilter.selectItemByIndex(listTanggal.indexOf(currentDate))
                }
                else{
                    try {
                        for (i in getListDateOfMonth().indices){
                            transaksiViewModel.insertFilterGrafik(getListDateOfMonth()[i])
                        }
                    }catch (e:Exception){}
                    finally {
                        binding.spinnerFilter.selectItemByIndex(listTanggal.indexOf(currentDate))
                    }
                }
            }
//
//            try {
//                Collections.sort(listTanggal,object : Comparator<String>{
//                    override fun compare(p0: String?, p1: String?): Int {
//                        return extractInt(p0!!) - extractInt(p1!!)
//                    }
//                    fun extractInt(s: String): Int {
//                        val num = s.replace("\\D".toRegex(), "")
//                        // return 0 if no digits found
//                        return if (num.isEmpty()) 0 else num.toInt()
//                    }
//                })
//            }finally {
//                try {
//                    binding.spinnerFilter.setItems(listTanggal)
//                }finally {
//                    try {
//                        binding.spinnerFilter.selectItemByIndex(listTanggal.indexOf(currentDate))
//                    }catch (e: Exception){
//                        Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
        }
        dataStorePreferences.currentDatePrefFlow.asLiveData()
            .observe(viewLifecycleOwner){date->
                if (date != null) {
                    currentDate = date
                }
            }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.spinnerFilter.selectItemByIndex(listTanggal.indexOf(currentDate))
        result = 0.0
        sharedViewModel.totalTransaksi.value = 0.0
        transaksiViewModel.getDataHistoriTransaksi.observe(viewLifecycleOwner){
            Log.d(TAG, "dataHistoriTransaksi: $it")
            Log.d("GrafikFragment", "totalTransaksi: ${ sharedViewModel.totalTransaksi.value.toString()}")

            if(!it.isNullOrEmpty()){
                result = 0.0
                it.forEach { data ->
                    result += data.total
                    Log.d("GrafikFragment", "totalTransaksi: ${result}")
                }
                sharedViewModel.totalTransaksi.value = sharedViewModel.totalTransaksi.value!!+result
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spinnerFilter.setOnSpinnerItemSelectedListener<String>{ oldIndex, oldItem, newIndex, text ->
            viewModel.getDataGrafikByDate(text)
        }
        setupChart()

//        transaksiViewModel.getFilterGrafik.observe(viewLifecycleOwner){
//            Log.d(TAG, "filterGrafik: $it")
//        }


//        viewModel.totalTransaksi.observe(viewLifecycleOwner){
//            if(it!=0.0){
//                Log.d(TAG, "totalTransaksi2: ${it}")
//                if(viewModel.getDataGrafikByDate(currentDate).isNullOrEmpty()) transaksiViewModel.insertDataGrafik(GrafikEntity(totalTransaksi = it!!, tanggal = currentDate))
////                else transaksiViewModel.updateDataGrafik(viewModel.totalTransaksi.value!!,currentDate)
//            }
//
//        }

        viewModel.getDataGrafikByDate(currentDate)



        viewModel.getEntries.observe(viewLifecycleOwner) {
            Log.d("GrafikFragment", "onViewCreated: ${it?.size}")
            if(currentDate!=""){
                updateChart(it)
            }
        }
    }

    private fun observeDataHistoriTransaksi() {
        result = 0.0
        sharedViewModel.totalTransaksi.value = 0.0
        transaksiViewModel.getDataHistoriTransaksi.observe(viewLifecycleOwner){
            Log.d(TAG, "dataHistoriTransaksi: $it")
            Log.d("GrafikFragment", "totalTransaksi: ${ sharedViewModel.totalTransaksi.value.toString()}")

            if(!it.isNullOrEmpty()){
                result = 0.0
                it.forEach { data ->
                    result += data.total
                    Log.d("GrafikFragment", "totalTransaksi: ${result}")
                }
             sharedViewModel.totalTransaksi.value = sharedViewModel.totalTransaksi.value!!+result
            }
        }
    }

    private fun setupChart() {
        with(binding.lineChart){
            setNoDataText(getString(R.string.belum_ada_data2))
            xAxis.valueFormatter = object : ValueFormatter(){
                override fun getFormattedValue(value: Float): String {
                    return ""
                }
            }

            description.text = ""
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            legend.setDrawInside(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false
        }
    }

    private fun updateChart(listEntry: List<Entry>?
    ) {
        Log.d("GrafikFragment", "setLineChartData: ${listEntry}")
        if(currentDate!=""){
            if(listEntry == null) {
                binding.lineChart.clear()
                binding.lineChart.invalidate()
            }else{
                val lineDataSet = LineDataSet(listEntry, "Transaksi ${currentDate} = ${result.toRupiahFormat()}")
                lineDataSet.color = ContextCompat.getColor(requireContext(), R.color.orange)
//        lineDataSet.circleRadius = 0f
                lineDataSet.setDrawFilled(true)
//        lineDataSet.setDrawCircles(true)
                lineDataSet.fillColor = ContextCompat.getColor(requireContext(), R.color.orange)
                lineDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.orange)
//        lineDataSet.fillAlpha = 30
                var data = LineData(lineDataSet)
                //        Log.d("Grafik Fragment", "setLineChartData: ${data}")

                with(binding.lineChart) {
                    this.data = data
                    setScaleEnabled(false)
//                viewModel.getListTanggal.observe(viewLifecycleOwner){
//                    if(it.isNotEmpty()){
//                        xAxis.valueFormatter = object : ValueFormatter() {
//                            override fun getFormattedValue(value: Float): String {
//                                return ""
//                            }
//                        }
//                        xAxis.setDrawGridLines(false)
//                    }
//                }
                    legend.textColor = ContextCompat.getColor(requireContext(),R.color.orange)
                    xAxis.textColor = ContextCompat.getColor(requireContext(),R.color.orange)
                    axisRight.textColor = ContextCompat.getColor(requireContext(),R.color.orange)
                    axisLeft.textColor = ContextCompat.getColor(requireContext(),R.color.orange)
//            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                    animateXY(500, 500)
                    invalidate()
                }
            }


        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}