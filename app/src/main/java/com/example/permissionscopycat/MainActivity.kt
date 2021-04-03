package com.example.permissionscopycat

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.permissionscopycat.util.checkSelfPermissionCompat
import com.example.permissionscopycat.util.requestPermissionsCompat
import com.example.permissionscopycat.util.shouldShowRequestPermissionRationaleCompat
import com.example.permissionscopycat.util.showSnackbar
import com.google.android.material.snackbar.Snackbar

const val PERMISSION_REQUEST_MIC = 0

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var layout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout = findViewById(R.id.main_layout)

        val requestButton = findViewById<Button>(R.id.button1)
        requestButton.setOnClickListener { showMicrophonePreview() }
    }

    private fun showMicrophonePreview() {
        // Check if the Camera permission has been granted
        if (checkSelfPermissionCompat(Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            layout.showSnackbar(R.string.microphone_permission_available, Snackbar.LENGTH_SHORT)
            //startCamera()
        } else {
            // Permission is missing and must be requested.
            requestMicrophonePermission()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_MIC) {
            // Request for microphone permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                toastMsg("Microphone Permission Successful")
            } else {
                // Permission request was denied.
                toastMsg("Microphone Permission Denied")
            }
        }
    }

    /**
     * Requests the [android.Manifest.permission.RECORD_AUDIO] permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private fun requestMicrophonePermission() {
        // Permission has not been granted and must be requested.
        if (shouldShowRequestPermissionRationaleCompat(Manifest.permission.RECORD_AUDIO)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            layout.showSnackbar(R.string.microphone_access_required,
                    Snackbar.LENGTH_INDEFINITE, R.string.ok_toast) {
                requestPermissionsCompat(arrayOf(Manifest.permission.CAMERA),
                        PERMISSION_REQUEST_MIC)
            }

        } else {
            layout.showSnackbar(R.string.microphone_permission_not_available, Snackbar.LENGTH_SHORT)

            // Request the permission. The result will be received in onRequestPermissionResult().
            requestPermissionsCompat(arrayOf(Manifest.permission.RECORD_AUDIO), PERMISSION_REQUEST_MIC)
        }
    }

    private fun toastMsg(msg: String) {
        val toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        toast.show()
    }

}