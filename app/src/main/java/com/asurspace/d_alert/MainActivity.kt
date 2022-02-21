package com.asurspace.d_alert

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.asurspace.d_alert.app_contract.ICustomToolbarTitleProvider
import com.asurspace.d_alert.app_contract.IProvideCustomAction
import com.asurspace.d_alert.app_contract.Navigator
import com.asurspace.d_alert.app_contract.model.CustomAction
import com.asurspace.d_alert.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationIconTint(Color.WHITE)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_container, MenuDialogFragment())
                .commit()
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
    }

    private fun updateUI() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_container)
        if (currentFragment is ICustomToolbarTitleProvider) {
            binding.toolbar.title = getString(currentFragment.getStringRes())
        } else {
            binding.toolbar.title = getString(R.string.toolbar_app_name)
        }

        if (currentFragment is IProvideCustomAction) {
            createCustomToolbarAction(currentFragment.getCustomAction())
        } else {
            binding.toolbar.menu.clear()
        }

        if (supportFragmentManager.backStackEntryCount > 0) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        }

    }

    private fun openFragment(fragment: Fragment, clearBS: Boolean = false) {
        if (clearBS) {
            clearBackstack()
        }
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(fragment.javaClass.name)
            .replace(R.id.main_container, fragment, fragment.javaClass.name)
            .commit()
    }

    private fun createCustomToolbarAction(action: CustomAction) {
        binding.toolbar.menu.clear()

        val iconDrawable =
            DrawableCompat.wrap(ContextCompat.getDrawable(this, action.drawableRes)!!)
        iconDrawable.setTint(Color.WHITE)

        val menuItem = binding.toolbar.menu.add(action.descriptionRes)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.icon = iconDrawable
        menuItem.setOnMenuItemClickListener {
            action.runnable.run()
            return@setOnMenuItemClickListener true
        }
    }

    private fun clearBackstack() =
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}