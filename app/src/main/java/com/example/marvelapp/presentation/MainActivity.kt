package com.example.marvelapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.marvelapp.R
import com.example.marvelapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarController: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostContainer) as NavHostFragment

        navController = navHostFragment.navController
        appBarController = AppBarConfiguration(
            setOf(
                R.id.charactersFragment,
                R.id.favoritesFragment,
                R.id.aboutFragment
            )
        )

        binding.bottomNaviMain.setupWithNavController(navController)

        binding.toolbarApp.setupWithNavController(navController, appBarController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isTopLevelDestination =
                appBarController.topLevelDestinations.contains(destination.id)
            if (isTopLevelDestination.not()) {
                binding.toolbarApp.setNavigationIcon(R.drawable.ic_back)
            }
        }
    }
}