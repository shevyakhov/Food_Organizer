package com.chelz.foodorganizer.screens.profile.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.chelz.foodorganizer.databinding.FragmentProfileBinding
import com.chelz.foodorganizer.screens.foodList.data.dao.StatisticsEntity
import com.chelz.foodorganizer.screens.profile.presentation.ProfileViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

	companion object {

		fun newInstance() = ProfileFragment()
	}

	private var _binding: FragmentProfileBinding? = null
	private val binding get() = _binding!!

	private val viewModel by viewModel<ProfileViewModel>()
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentProfileBinding.inflate(inflater, container, false)
		return binding.root
	}

	@SuppressLint("DiscouragedApi")
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupPieChart()
		binding.datePicker.setOnClickListener {
			val pd = MonthYearPickerDialog()
			pd.setListener { _, selectedYear, selectedMonth, _ ->
				val selectedMonthFormatted = String.format("%02d", selectedMonth)
				val selectedDate = "$selectedMonthFormatted/$selectedYear"
				viewModel.dateFlow.value = selectedDate
			}
			pd.show(childFragmentManager, "MonthYearPickerDialog")
		}

		binding.settingsButton.setOnClickListener { viewModel.navigateToSettings() }
		viewModel.dateFlow.onEach {
			binding.datePicker.text = it
			viewModel.getStatistics()
		}.launchIn(lifecycleScope)

		viewModel.statisticsFlow.onEach {
			if (it != null) {
				loadPieChartData(it)
			} else
				binding.chart.clear()
		}.launchIn(lifecycleScope)
	}

	private fun setupPieChart() {
		binding.chart.isDrawHoleEnabled = true
		binding.chart.setUsePercentValues(true)
		binding.chart.setEntryLabelTextSize(12f)
		binding.chart.setEntryLabelColor(Color.BLACK)
		binding.chart.centerText = "Расход продуктов"
		binding.chart.setCenterTextSize(24f)
		binding.chart.description.isEnabled = false
		val l = binding.chart.legend
		l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
		l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
		l.orientation = Legend.LegendOrientation.VERTICAL
		l.setDrawInside(false)
		l.isEnabled = true
	}

	private fun loadPieChartData(entity: StatisticsEntity) {
		val entries: ArrayList<PieEntry> = ArrayList()
		entries.add(PieEntry(entity.trashedNumber, "Утилизировано"))
		entries.add(PieEntry(entity.consumedNumber, "Употреблено"))

		val colors: ArrayList<Int> = ArrayList()
		for (color in ColorTemplate.MATERIAL_COLORS) {
			colors.add(color)
		}

		val dataSet = PieDataSet(entries, "Расход продуктов")
		dataSet.colors = colors
		val data = PieData(dataSet)
		data.setDrawValues(true)
		data.setValueFormatter(PercentFormatter(binding.chart))
		data.setValueTextSize(12f)
		data.setValueTextColor(Color.BLACK)
		binding.chart.data = data
		binding.chart.invalidate()
		binding.chart.animateY(1000, Easing.EaseInOutQuad)
	}
}
