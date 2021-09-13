package com.example.androidlearnproject

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidlearnproject.databinding.ActivityChartBinding
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight

class ChartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lineChart.let {
            it.axisRight.isEnabled = false
            it.description.isEnabled = false
            it.setTouchEnabled(true)
            it.isDragEnabled = true
            it.setScaleEnabled(false)
            it.setPinchZoom(false)
        }
        /* The library has an Entry data type that represents a single data point in the graph. You
        need to convert from whatever format you hold your data, into a List<Entry>, with each
        entry defining an X and Y (as Float). */
        val entries = listOf(
            Entry(0f, 5f),
            Entry(2f, 8f),
            Entry(3f, 1f)
        )
        val dataSet = LineDataSet(entries, "My Chart Example").apply {
            //set line values
            lineWidth = 3f
            color = Color.YELLOW
            //set text values
            setDrawValues(true)
            valueTextColor = Color.GREEN
            valueTextSize = 10f
            //set highlight values
            isHighlightEnabled = true
            highLightColor = Color.RED
            setDrawHighlightIndicators(true)
            //change style of chart to cubic
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            fillColor = Color.BLUE
        }
        /* ****(Optional) Marker*****
         A marker is a small "popup" view that appears when a user clicks on a data point*/
        binding.lineChart.marker = MyMarker(this)
        binding.lineChart.data = LineData(dataSet)
//      set the dataset in the chart and call invalidate(). Every time you change the chart data
        binding.lineChart.invalidate()
    }

    class MyMarker(context: Context) : MarkerView(context, R.layout.chart_marker) {
        private val markerX: TextView = findViewById(R.id.tv_marker_x)
        private val markerY: TextView = findViewById(R.id.tv_marker_y)
        override fun refreshContent(entry: Entry, highlight: Highlight) {
            super.refreshContent(entry, highlight)
            markerX.text = entry.x.toString()
            markerY.text = entry.y.toString()
        }
    }

}