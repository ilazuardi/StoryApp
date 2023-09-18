package com.irfan.storyapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.irfan.storyapp.adapter.LoadingStateAdapter
import com.irfan.storyapp.ui.main.MainActivity
import com.irfan.storyapp.adapter.StoryListAdapter
import com.irfan.storyapp.data.model.local.Story
import com.irfan.storyapp.databinding.FragmentHomeBinding
import java.lang.NullPointerException

@ExperimentalPagingApi
class HomeFragment : Fragment() {

    private val TAG: String = HomeFragment::class.java.simpleName

    private var _binding: FragmentHomeBinding? = null

    private var token: String = ""
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var storyRv: RecyclerView
    private lateinit var storyListAdapter: StoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        token = (activity as MainActivity).getSaveToken()
        Log.d(TAG, "onCreateView: Token = $token")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnRefreshData()
        configStoryList()
        getAllStories()
        (activity as MainActivity).supportActionBar?.apply {
            title = "Home"
            show()
        }
    }

    private fun getAllStories() {
        homeViewModel.getAllStories(token).observe(viewLifecycleOwner) { stories ->
            updateData(stories)
        }
    }

    private fun updateData(stories: PagingData<Story>) {
        val recyclerViewState = storyRv.layoutManager?.onSaveInstanceState()

        storyListAdapter.submitData(lifecycle, stories)
        storyRv.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    private fun setOnRefreshData() {
        binding.srlRefresh.setOnRefreshListener {
            getAllStories()
        }
    }

    private fun configStoryList() {
        storyListAdapter = StoryListAdapter()
        storyListAdapter.addLoadStateListener { state ->
            val paginationDataState = (state.source.refresh is LoadState.NotLoading && state.append.endOfPaginationReached && storyListAdapter.itemCount < 1) || state.source.refresh is LoadState.Error
            binding.apply {
                if (paginationDataState) {
                    tvStoriesEmpty.visibility = View.VISIBLE
                    ivStoriesEmpty.visibility = View.VISIBLE
                    storyListRv.visibility = View.GONE
                } else {
                    tvStoriesEmpty.visibility = View.GONE
                    ivStoriesEmpty.visibility = View.GONE
                    storyListRv.visibility = View.VISIBLE
                }
                srlRefresh.isRefreshing = state.source.refresh is LoadState.Loading
            }
        }
        try {
            storyRv = binding.storyListRv
            storyRv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = storyListAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        storyListAdapter.retry()
                    }
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}