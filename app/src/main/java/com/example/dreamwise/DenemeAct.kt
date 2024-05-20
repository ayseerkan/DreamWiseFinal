package com.example.dreamwise

import com.example.dreamwise.fragments.NightmareFragment
import com.example.dreamwise.fragments.SweetdreamFragment
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.example.dreamwise.databinding.ActivityDenemeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class DenemeAct : AppCompatActivity() {
    private lateinit var binding: ActivityDenemeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDenemeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = auth.currentUser

        if (user == null) {
            navigateToLogin()
        } else {
            binding.textViewDeneme.text = user.email
        }

        binding.logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            navigateToLogin()
        }

        setupViewPagerAndTabs()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupViewPagerAndTabs() {
        val fragments = listOf(SweetdreamFragment(), NightmareFragment())
        val titles = listOf("Sweet Dreams", "Nightmares")
        val adapter = DreamsViewPagerAdapter(this, fragments)
        binding.viewPager.adapter = adapter

        // Attaching the ViewPager2 to the TabLayout
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}

class ViewPagerAdapter(
    activity: AppCompatActivity,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}
