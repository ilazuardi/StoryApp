package com.irfan.storyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.irfan.storyapp.data.preferences.UserPreferences
import com.irfan.storyapp.databinding.ActivityMainBinding
import com.irfan.storyapp.ui.add.newpost.NewPostActivity
import com.irfan.storyapp.ui.authentication.login.LoginFragment
import com.irfan.storyapp.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var userPref: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPref = UserPreferences(this)
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
        if (userPref.getUser().isLogin) {
            moveToFragment(HomeFragment())
        } else {
            moveToFragment(LoginFragment())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.logout_menu -> {
                userPref.logout()
                moveToFragment(LoginFragment())
                true
            }
            R.id.add_story_menu -> {
                val intent = Intent(this, NewPostActivity::class.java)
                startActivity(intent)
                FragmentManager.POP_BACK_STACK_INCLUSIVE
                true
            }
            else -> {return super.onOptionsItemSelected(item)}
        }
    }
}