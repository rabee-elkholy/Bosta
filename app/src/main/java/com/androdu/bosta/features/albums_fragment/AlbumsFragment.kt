package com.androdu.bosta.features.albums_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androdu.bosta.R
import com.androdu.bosta.data.api.model.User
import com.androdu.bosta.databinding.FragmentAlbumsBinding
import com.androdu.bosta.features.adapter.AlbumsAdapter
import com.androdu.bosta.utils.Helper
import com.androdu.bosta.utils.Helper.serializable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AlbumsFragment : Fragment() {
    private val mViewModel: AlbumsViewModel by viewModels()
    private var _binding: FragmentAlbumsBinding? = null
    private val mBinding get() = _binding!!
    private var albumsAdapter: AlbumsAdapter = AlbumsAdapter()

    private var userId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumsBinding.inflate(inflater, container, false)

        init()

        return mBinding.root
    }

    private fun init() {
        showShimmer()
        setTitle()
        setupCollector()
        setupRecyclerView()
        setupRefresh()
    }

    private fun setupRefresh() {
        mBinding.albumsSrlSwipe.setOnRefreshListener {
            mViewModel.setEvent(AlbumsContract.Event.GetAlbums(userId))
            mBinding.albumsSrlSwipe.isRefreshing = false
            showShimmer()
        }
    }

    private fun setupCollector() {
        lifecycleScope.launch {
            mViewModel.uiState.collect {
                when (it.userState) {
                    is AlbumsContract.AlbumsState.GetAlbumsSuccess -> {
                        Log.d("TAG", "GetAlbumsSuccess: ")
                        hideShimmer()
                        albumsAdapter.setData(it.userState.albums.toMutableList())
                    }

                    is AlbumsContract.AlbumsState.GetAlbumsFailed -> {
                        Log.d("TAG", "GetAlbumsFailed: ")
                        hideShimmer()
                        Helper.showErrorDialog(requireContext(), desc = it.userState.errMsg)
                    }

                    AlbumsContract.AlbumsState.Idle -> {
                        showShimmer()
                        mViewModel.setEvent(AlbumsContract.Event.GetAlbums(userId))
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        albumsAdapter.setOnItemClickListener {
            findNavController().navigate(
                AlbumsFragmentDirections.actionAlbumsFragmentToPhotosFragment(
                    albumId = it.id,
                    albumTitle = it.title
                )
            )
        }

        mBinding.albumsRvAlbums.apply {
            adapter = albumsAdapter
            val mLayoutManager = LinearLayoutManager(requireContext())
            layoutManager = mLayoutManager
        }
    }

    private fun setTitle() {
        mBinding.albumsTopBar.topBarTvTitle.text = getString(R.string.albums)
        mBinding.albumsTopBar.topBarEtSearch.visibility = View.GONE
        val arguments = arguments
        if (arguments != null && arguments.containsKey("user")) {
            val user = arguments.serializable<User>("user")
            mBinding.user = user
            userId = user.let { if (it != null) return@let it.id else return@let 0 }
        }
        mBinding.albumsTopBar.topBarIvBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showShimmer() {
        mBinding.albumsShimmerView.visibility = View.VISIBLE
        mBinding.albumsRvAlbums.visibility = View.INVISIBLE
    }

    private fun hideShimmer() {
        mBinding.albumsShimmerView.visibility = View.GONE
        mBinding.albumsRvAlbums.visibility = View.VISIBLE
    }
}