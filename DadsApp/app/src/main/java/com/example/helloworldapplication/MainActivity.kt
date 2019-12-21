package com.example.helloworldapplication

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val MY_KEYPAD_INTENT_REQUEST_CODE = 241

    companion object {
        var cards: ArrayList<Card>? = ArrayList<Card>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Log.d(resources.getString(R.string.logtag), "onCreate Main Activity")
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        authenticateUser()
        // TODO("figure out a way to ask for authentication when application goes into back and after a timeout.")


        // opening the card page to show card information
        fab.setOnClickListener {
            Toast.makeText(this, "Button Clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AddNewCardActivity::class.java))
        }

//        TODO("Adjust size of text in name text view and card number text view based on the length of string")
    }

    private fun authenticateUser() {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val intentToSignIn = keyguardManager.createConfirmDeviceCredentialIntent(
            "Log into your Phone",
            "Use PIN/Password/Pattern/Fingerprint to sign in"
        )
        startActivityForResult(intentToSignIn, MY_KEYPAD_INTENT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Runnable {
            if (resultCode == RESULT_OK && requestCode == MY_KEYPAD_INTENT_REQUEST_CODE) {
                // TODO("Add a message to close app after use for first time installation")

                runOnUiThread {
                    Toast.makeText(this, "Logged in Successfully", Toast.LENGTH_SHORT).show()
                }
            } else if (resultCode == Activity.RESULT_CANCELED && requestCode == MY_KEYPAD_INTENT_REQUEST_CODE) {
                finish()
            }
        }.run()
    }

    override fun onStart() {
        super.onStart()
        Log.d(resources.getString(R.string.logtag), "onStart Main Activity")
        loadData(this)
        initRecyclerView()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            Toast.makeText(this, "Permission Granted!!", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(
                this,
                "Permission Not Granted!! ${grantResults.size}",
                Toast.LENGTH_SHORT
            ).show()

    }

    private fun initRecyclerView() {
        val adapter = MyRecyclerViewAdapter(this, cards!!)
        val rv: RecyclerView = findViewById(R.id.recyclerView)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
    }

    private fun biometricsAuthenticate() { // For API > 29

        val biometricPrompt = BiometricPrompt.Builder(this)
            .setTitle("Authentication Required")
            .setDescription("Use fingerprint to authenticate")
            .setDeviceCredentialAllowed(true)
            .build()


        biometricPrompt.authenticate(
            CancellationSignal(),
            mainExecutor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    Log.d(
                        resources.getString(R.string.logtag),
                        "Authentication Success, via biometric prompt!!"
                    )
                    runOnUiThread(Runnable {
                        Toast.makeText(
                            this@MainActivity,
                            "Authentication Success",
                            Toast.LENGTH_LONG
                        ).show()
                    })
                }

            })
    }

    private fun initCards() {
        cards!!.add(
            Card(
                "SBI Card",
                "NISHANT KARTIKEYA J",
                "4202541120209999",
                GregorianCalendar(2013, 11, 1),
                GregorianCalendar(2023, 11, 1),
                366
            )
        )
        cards!!.add(
            Card(
                "Axis Card",
                "NISHANT KARTIKEYA J",
                "4202541120209999",
                GregorianCalendar(2013, 11, 1),
                GregorianCalendar(2023, 11, 1),
                366
            )
        )
    }
}
