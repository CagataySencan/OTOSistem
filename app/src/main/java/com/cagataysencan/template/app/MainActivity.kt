package com.cagataysencan.template.app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cagataysencan.template.core.base.BaseActivity
import com.cagataysencan.template.databinding.ActivityMainBinding

/**
 * Single-activity host for the Navigation Component graph.
 * Extend only if you need app-level activity behavior beyond the nav host.
 */
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    /** Inflates the nav host layout and applies system bar insets for edge-to-edge display. */
    override fun onInit(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
