package com.consolediscounts.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.consolediscounts.R

class DrawerFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.nav_preferences, rootKey)
    }
}