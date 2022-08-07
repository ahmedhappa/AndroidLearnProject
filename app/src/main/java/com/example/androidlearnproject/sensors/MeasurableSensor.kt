package com.example.androidlearnproject.sensors

abstract class MeasurableSensor(
    protected val sensorType: Int
) {

    abstract val doesSensorExist: Boolean

    protected var onSensorValuesChanges: ((List<Float>) -> Unit)? = null

    abstract fun startListening()
    abstract fun stopListening()

    fun setOnSensorValuesChangeListener(listener: (List<Float>) -> Unit) {
        onSensorValuesChanges = listener
    }
}