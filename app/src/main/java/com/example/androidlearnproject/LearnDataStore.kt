package com.example.androidlearnproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.androidlearnproject.databinding.ActivityLearnDataStoreBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LearnDataStore : AppCompatActivity() {

    private lateinit var binding: ActivityLearnDataStoreBinding
    private val dataStore: DataStore<Preferences> by preferencesDataStore("settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnDataStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSaveToDatastore.setOnClickListener {
            val key = binding.etKey.text.toString()
            val value = binding.etValue.text.toString()
            val preferencesKey = stringPreferencesKey(key)
            lifecycleScope.launch {
                dataStore.edit {
                    it[preferencesKey] = value
                }
            }
        }

        binding.btnGetFromDatastore.setOnClickListener {
            val key = binding.etKey.text.toString()
            val preferencesKey = stringPreferencesKey(key)
            lifecycleScope.launch {
                val preferences = dataStore.data.first()
                Toast.makeText(this@LearnDataStore, preferences[preferencesKey] ?: "No Value Found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnClearDataStore.setOnClickListener {
            lifecycleScope.launch {
                dataStore.edit { it.clear() }
            }
        }
    }
}