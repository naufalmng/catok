package org.d3ifcool.catok.ui.grafik

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.launch
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.DataStorePreferences
import org.d3ifcool.catok.core.data.dataStore
import org.d3ifcool.catok.databinding.FragmentGrafikBinding
import org.d3ifcool.catok.utils.getCurrentMonth
import org.d3ifcool.catok.utils.getEmptyEntry
import org.koin.androidx.viewmodel.ext.android.viewModel

class GrafikFragment : Fragment() {

    private var _binding: FragmentGrafikBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GrafikViewModel by viewModel()
    private val dataStorePreferences: DataStorePreferences by lazy {
        DataStorePreferences(requireActivity().dataStore)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGrafikBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupChart()
        viewModel.getEntries.observe(viewLifecycleOwner) {
            Log.d("GrafikFragment", "onViewCreated: ${it.size}")
            if (it.isNotEmpty()) {
                updateChart(it)
            }
        }
    }

    private fun setupChart() {
        with(binding.lineChart){
            setNoDataText(getString(R.string.belum_ada_data, getCurrentMonth()))
//            xAxis.valueFormatter = object : ValueFormatter(){
//                override fun getFormattedValue(value: Float): String {
//                    lateinit var currentMonth: String
//                    val pos = value.toInt()-1
//                    val isValidPosition = pos>=0
//
//                    return ""
//                }
//            }

            description.text = ""
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            legend.setDrawInside(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false
        }
    }

    private fun updateChart(listEntry: List<Entry>) {
        var currentMonth = ""
        Log.d("GrafikFragment", "setLineChartData: ${listEntry}")
        dataStorePreferences.currentMonthPrefFlow.asLiveData()
            .observe(viewLifecycleOwner){month->
                if(month!=null){
                    currentMonth = month
                }
            }
        val lineDataSet = LineDataSet(listEntry, "Transaksi Bulan ${currentMonth}")
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
            viewModel.getMonthList.observe(viewLifecycleOwner){
                if(it.isNotEmpty()){
                    xAxis.valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val pos = value.toInt() - 1
                            lateinit var currentMonth : String
                            val isValidPosition = pos >= 0
                            for (i in it.indices){
                                currentMonth = it[i]
                            }
                            return ""
                        }
                    }
                    xAxis.setDrawGridLines(false)
                }
            }
            legend.textColor = ContextCompat.getColor(requireContext(),R.color.orange)
            xAxis.textColor = ContextCompat.getColor(requireContext(),R.color.orange)

            axisRight.textColor = ContextCompat.getColor(requireContext(),R.color.orange)
            axisLeft.textColor = ContextCompat.getColor(requireContext(),R.color.orange)
//            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            animateXY(500, 500)
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}