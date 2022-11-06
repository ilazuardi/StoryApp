package com.irfan.storyapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.irfan.storyapp.MainActivity
import com.irfan.storyapp.adapter.StoryListAdapter
import com.irfan.storyapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val TAG: String = HomeFragment::class.java.simpleName

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var storyListAdapter: StoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storyListAdapter = StoryListAdapter()
        initViewModel()
        configStoryList(storyListAdapter)
        (activity as MainActivity).supportActionBar?.apply {
            title = "Home"
            show()
        }
    }

    private fun initViewModel() {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        homeViewModel.apply {

            getAllStories((activity as MainActivity).userPref.getUser().token)
            isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
            stories.observe(viewLifecycleOwner) {
                if (it != null) {
                    storyListAdapter.setListStories(it)
                }
            }
            message.observe(viewLifecycleOwner) {
                Log.d(TAG, "Message = $it")
            }
        }
    }

    private fun configStoryList(storyAdapter: StoryListAdapter) {
        binding.storyListRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = storyAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingHomePb.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

    
}