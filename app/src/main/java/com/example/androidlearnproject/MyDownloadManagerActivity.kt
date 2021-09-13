package com.example.androidlearnproject

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.webkit.URLUtil
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.androidlearnproject.databinding.ActivityMyDownloadManagerBinding
import java.io.File

class MyDownloadManagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyDownloadManagerBinding
    private val storagePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) downloadFile()
        else Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
    }
    private var downloadId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyDownloadManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //to listen to download complete with the help of broadcast receiver
        registerReceiver(downloadBroadCastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        binding.btnDownload.setOnClickListener {
            storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun downloadFile() {
        val fileName = URLUtil.guessFileName(binding.etFileUrl.text.toString(), null, null) ?: "DummyFileName"
        val file =
            File(getExternalFilesDir(null), fileName)

        val downloadManagerRequest = DownloadManager.Request(Uri.parse(binding.etFileUrl.text.toString()))
            .setTitle(fileName)
            .setDescription("Downloading file please wait...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) //to show notification while and after downloading the file
//            .setDestinationUri(Uri.fromFile(file)) //to store file in application package folder
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName) //to store file in download folder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            downloadManagerRequest.setRequiresCharging(false)
        }

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadId = downloadManager.enqueue(downloadManagerRequest)

    }

    private val downloadBroadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val id = p1?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadId) {
                Toast.makeText(this@MyDownloadManagerActivity, "Download Finished", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(downloadBroadCastReceiver)
    }
}