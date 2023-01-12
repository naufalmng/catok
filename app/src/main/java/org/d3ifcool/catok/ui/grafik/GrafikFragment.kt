@file:Suppress(
    "CanBeVal", "CanBeVal", "CanBeVal", "CanBeVal", "CanBeVal", "CanBeVal", "CanBeVal",
    "CanBeVal", "CanBeVal", "CanBeVal", "CanBeVal", "CanBeVal", "CanBeVal", "CanBeVal", "CanBeVal",
    "CanBeVal", "CanBeVal", "CanBeVal", "CanBeVal", "CanBeVal", "CanBeVal", "CanBeVal", "CanBeVal"
)

package org.d3ifcool.catok.ui.grafik

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.whiteelephant.monthpicker.MonthPickerDialog
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.DataStorePreferences
import org.d3ifcool.catok.core.data.dataStore
import org.d3ifcool.catok.databinding.FragmentGrafikBinding
import org.d3ifcool.catok.ui.beranda.transaksi.TransaksiViewModel
import org.d3ifcool.catok.ui.main.SharedViewModel
import org.d3ifcool.catok.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class GrafikFragment : Fragment() {
    companion object {
        private const val TAG = "GrafikFragment"
    }

    private var result = 0.0
    private var _binding: FragmentGrafikBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GrafikViewModel by viewModel()
    private val transaksiViewModel: TransaksiViewModel by viewModel()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var grafikAdapter: GrafikAdapter

    private val dataStorePreferences: DataStorePreferences by lazy {
        DataStorePreferences(requireActivity().dataStore)
    }
    private var currentDate = ""
    private var currentYear = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGrafikBinding.inflate(inflater, container, false)
        grafikAdapter = GrafikAdapter()
        viewModel.selectedMonthAndYear.value = getCurrentMonthAndYear()
        filterObserver()
        setupMonthAndYearPicker()
        setupRecyclerView()
        result = 0.0
        sharedViewModel.totalTransaksi.value = 0.0
        transaksiViewModel.getDataHistoriTransaksiByStatusBayar().observe(viewLifecycleOwner) {
            Log.d(TAG, "dataHistoriTransaksi: $it")
            Log.d(
                "GrafikFragment",
                "totalTransaksi: ${sharedViewModel.totalTransaksi.value.toString()}"
            )

            if (!it.isNullOrEmpty()) {
                result = 0.0
                try {
                    it.forEach { data ->
                        if (data.tanggal.substring(
                                0,
                                data.tanggal.length - 5
                            ) == getCurrentDate().substring(0, data.tanggal.length - 5)
                        ) result += data.total
                        Log.d("GrafikFragment", "totalTransaksi: $result")
                    }
                } catch (e: Exception) {
                } finally {
                    try {
                        Log.d(TAG, "result: $result ")
                        for (i in it) {
                            if (i.tanggal == getCurrentDate2()) transaksiViewModel.updateDataGrafik(
                                result,
                                getCurrentDate2()
                            )

                        }
                    } catch (e: Exception) {
                    } finally {
                        transaksiViewModel.updateDataGrafik(result, getCurrentDate2())
                        viewModel.getDataGrafikByDate(getCurrentMonthAndYear())
                        binding.lineChart.notifyDataSetChanged()
                        binding.lineChart.invalidate()
                    }
                }

            }
        }
        dataStorePreferences.currentDatePrefFlow.asLiveData()
            .observe(viewLifecycleOwner) { date ->
                if (date != null) {
                    currentDate = date
                }
            }
        return binding.root
    }

    private fun filterObserver() {
        viewModel.selectedMonthAndYear.observe(viewLifecycleOwner) {
            currentYear = it.substring(it.length - 4)
            binding.tvMonthAndYear.text = it
            if (it != "") {
                viewModel.getDataGrafikByDate(it)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupMonthAndYearPicker() {
        binding.tvMonthAndYear.setOnClickListener {
            val cal = Calendar.getInstance()
            val builder = MonthPickerDialog.Builder(
                requireContext(), { selectedMonth, selectedYear ->
                    viewModel.selectedMonthAndYear.value = getString(
                        R.string.selectedMonthAndYearArg,
                        DateFormatSymbols.getInstance(Locale("id", "ID")).months[selectedMonth],
                        selectedYear.toString()
                    )
                    binding.tvMonthAndYear.text = viewModel.selectedMonthAndYear.value
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH)
            )

            builder.setActivatedMonth(cal.get(Calendar.MONTH))
                .setMinYear(2023)
                .setActivatedYear(cal.get(Calendar.YEAR))
                .setMaxYear(2100)
                .setMinMonth(Calendar.FEBRUARY)
                .setTitle(getString(R.string.date_picker_title))
                .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                .build().show()
        }
    }

    private fun setupRecyclerView() {
        viewModel.getDataGrafik.observe(viewLifecycleOwner) {
            Log.d(TAG, "setupRecyclerView: $it")
            grafikAdapter.setData(it)
        }
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            setHasFixedSize(true)
            adapter = grafikAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        transaksiViewModel.getDataHistoriTransaksiByStatusBayar().observe(viewLifecycleOwner) {
            Log.d(TAG, "dataHistoriTransaksi: $it")
            Log.d(
                "GrafikFragment",
                "totalTransaksi: ${sharedViewModel.totalTransaksi.value.toString()}"
            )

            if (!it.isNullOrEmpty()) {
                result = 0.0
                try {
                    it.forEach { data ->
                        if (data.tanggal.substring(
                                0,
                                data.tanggal.length - 5
                            ) == getCurrentDate().substring(0, data.tanggal.length - 5)
                        ) result += data.total
                        Log.d("GrafikFragment", "totalTransaksi: $result")
                    }
                } catch (e: Exception) {
                } finally {
                    try {
                        Log.d(TAG, "result: $result ")
                        for (i in it) {
                            if (i.tanggal == getCurrentDate2()) transaksiViewModel.updateDataGrafik(
                                result,
                                getCurrentDate2()
                            )

                        }
                    } catch (e: Exception) {
                    } finally {
                        transaksiViewModel.updateDataGrafik(result, getCurrentDate2())
                        viewModel.getDataGrafikByDate(getCurrentMonthAndYear())
                        binding.lineChart.notifyDataSetChanged()
                        binding.lineChart.invalidate()
                    }
                }

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getDataGrafikByDate(getCurrentMonthAndYear())
        setupChart()


        viewModel.getEntries.observe(viewLifecycleOwner) {
            Log.d("GrafikFragment", "onViewCreated: ${it?.size}")
            if (currentDate != "") {
                updateChart(it)
            }
        }
    }


    private fun setupChart() {
        with(binding.lineChart) {
            setNoDataText(getString(R.string.belum_ada_data2))
            xAxis.valueFormatter = object : ValueFormatter() {
                val sdf =
                    SimpleDateFormat(getString(R.string.format_hari_bulan), Locale("id", "ID"))

                override fun getFormattedValue(value: Float): String {
                    val pos = value.toInt() - 1
                    val isValidPosition = pos >= 0 && pos < grafikAdapter.itemCount
                    return if (isValidPosition) sdf.format(grafikAdapter.getDate(pos)) else ""
                }
            }
            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, highlight: Highlight) {
                    val pos = grafikAdapter.itemCount - highlight.x.toInt()
                    binding.recyclerView.scrollToPosition(pos)
                }

                override fun onNothingSelected() {}
            })


            description.text = ""
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            legend.setDrawInside(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false
        }
    }

    private fun updateChart(
        listEntry: List<Entry>?
    ) {
        Log.d("GrafikFragment", "setLineChartData: $listEntry")
        if (currentDate != "") {
            if (listEntry == null) {
                binding.lineChart.clear()
                binding.lineChart.invalidate()
            } else {
                val lineDataSet =
                    LineDataSet(listEntry, getString(R.string.transaksi_tahun_arg, currentYear))
                lineDataSet.color = ContextCompat.getColor(requireContext(), R.color.orange)
                lineDataSet.setDrawFilled(true)
                lineDataSet.fillColor = ContextCompat.getColor(requireContext(), R.color.orange)
                lineDataSet.valueTextColor =
                    ContextCompat.getColor(requireContext(), R.color.orange)
                var data = LineData(lineDataSet)

                with(binding.lineChart) {
                    this.data = data
                    xAxis.setLabelCount(5, true)
                    legend.textColor = ContextCompat.getColor(requireContext(), R.color.orange)
                    xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.orange)
                    axisRight.textColor = ContextCompat.getColor(requireContext(), R.color.orange)
                    axisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.orange)
                    animateXY(500, 500)
                    notifyDataSetChanged()
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