package com.consolediscounts.fragments

import android.os.Bundle
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.consolediscounts.R
import com.consolediscounts.domain.interfaces.IStore
import com.consolediscounts.kodein
import org.kodein.di.erased.instance

class DrawerFragment : PreferenceFragmentCompat() {

    private val psn: IStore by kodein.instance("psn")
    private val eShop: IStore by kodein.instance("eshop")
    private val microsoft: IStore by kodein.instance("microsoft")
    private val goodsRu: IStore by kodein.instance("goods.ru")

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.nav_preferences, rootKey)
        listOf(psn, eShop, microsoft, goodsRu).forEach { store ->
            val category = PreferenceCategory(context)
            category.title = store.name
            category.key = store.name
            preferenceScreen.addPreference(category)

            store.supportedPlatforms.forEach {
                val platformSwitch = SwitchPreference(context)
                platformSwitch.title = it.platformName
                platformSwitch.key = "pref_${store.name}_${it.platformName}"
                platformSwitch.isIconSpaceReserved = false
                platformSwitch.setDefaultValue(listOf(psn.name, eShop.name, microsoft.name).contains(store.name))
                category.addPreference(platformSwitch)
            }
        }
    }
}