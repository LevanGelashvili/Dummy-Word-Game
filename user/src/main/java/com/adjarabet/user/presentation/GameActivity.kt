package com.adjarabet.user.presentation

import android.os.Bundle
import com.adjarabet.user.databinding.ActivityUserBinding
import dagger.android.support.DaggerAppCompatActivity

class GameActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainerView.id, GameFragment.newInstance())
            .commitNow()
    }
}