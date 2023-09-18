package com.irfan.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.irfan.storyapp.R
import com.irfan.storyapp.databinding.ActivityMainBinding
import com.irfan.storyapp.ui.add.newpost.NewPostActivity
import com.irfan.storyapp.ui.authentication.login.LoginFragment
import com.irfan.storyapp.ui.home.HomeFragment
import com.irfan.storyapp.ui.location.LocationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var saveToken: String = ""
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        checkSession()
    }

    fun moveToFragment(fragment: Fragment) {
        this.supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_story_fragments_fl, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun checkSession() {
        lifecycleScope.launchWhenCreated {
            launch {
                mainViewModel.getUserToken().collect() { token ->
                    if (token.isNullOrEmpty()) moveToFragment(LoginFragment())
                    else {
                        saveToken = token
                        moveToFragment(HomeFragment())
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_menu -> {
                mainViewModel.saveUserToken("")
                moveToFragment(LoginFragment())
                true
            }
            R.id.add_story_menu -> {
                val intent = Intent(this, NewPostActivity::class.java)
                startActivity(intent)
                FragmentManager.POP_BACK_STACK_INCLUSIVE
                true
            }
            R.id.location_menu -> {
                val intent = Intent(this, LocationActivity::class.java)
                startActivity(intent)
                FragmentManager.POP_BACK_STACK_INCLUSIVE
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    fun getSaveToken(): String {
        return saveToken
    }
}