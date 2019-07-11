package com.ps.discounts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.psdiscounts.multiplatform.createApplicationScreenMessage

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("[AAA]", createApplicationScreenMessage())
    }
}
