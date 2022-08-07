package com.example.androidlearnproject.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

abstract class AndroidSensor(
    private val context: Context,
    private val sensorFeature: String,
    sensorType: Int
) : MeasurableSensor(sensorType), SensorEventListener {

    override val doesSensorExist: Boolean
        get() {
            val isSensorExist = context.packageManager.hasSystemFeature(sensorFeature)
            if (isSensorExist.not())
                println("This Sensor Is Not Supported")
            return isSensorExist
        }
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    override fun startListening() {
        if (doesSensorExist.not())
            return
        if (::sensorManager.isInitialized.not() && sensor == null) {
            sensorManager = context.getSystemService(SensorManager::class.java) as SensorManager
            sensor = sensorManager.getDefaultSensor(sensorType)
        }
        sensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (doesSensorExist.not())
            return
        if (event?.sensor?.type == sensorType) {
            onSensorValuesChanges?.invoke(event.values.toList())
        }
    }

    override fun stopListening() {
        if (doesSensorExist.not() || ::sensorManager.isInitialized.not())
            return
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}