package com.androdu.bosta.features.photos_fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.androdu.bosta.databinding.FragmentPhotosBinding
import com.androdu.bosta.features.adapter.PhotosAdapter
import com.androdu.bosta.utils.Helper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotosFragment : Fragment() {
    private val mViewModel: PhotosViewModel by viewModels()
    private var _binding: FragmentPhotosBinding? = null
    private val mBinding get() = _binding!!
    private var photosAdapter: PhotosAdapter = PhotosAdapter()

    private var albumId = 0
    private var albumTitle = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)

        arguments?.let {
            albumId = it.getInt("albumId")
            albumTitle = it.getString("albumTitle").toString()
        }

        init()

        return mBinding.root
    }

    private fun init() {
        setTitle()
        setupCollector()
        setupRecyclerView()
        setupRefresh()
    }

    private fun setupRefresh() {
        mBinding.photosSrlSwipe.setOnRefreshListener {
            mViewModel.setEvent(PhotosContract.Event.GetPhotos(albumId))
            mBinding.photosTopBar.topBarEtSearch.text.clear()
            mBinding.photosSrlSwipe.isRefreshing = false
            showShimmer()
        }
    }

    private fun setupCollector() {
        lifecycleScope.launch {
            mViewModel.uiState.collect {
                when (it.photosState) {
                    is PhotosContract.PhotosState.GetPhotosSuccess -> {
                        Log.d("TAG", "GetAlbumsSuccess: ")
                        hideShimmer()
                        photosAdapter.setData(it.photosState.photos.toMutableList())
                    }

                    is PhotosContract.PhotosState.GetPhotosFailed -> {
                        Log.d("TAG", "GetAlbumsFailed: ")
                        hideShimmer()
                        Helper.showErrorDialog(requireContext(), desc = it.photosState.errMsg)
                    }

                    PhotosContract.PhotosState.Idle -> {
                        showShimmer()
                        mViewModel.setEvent(PhotosContract.Event.GetPhotos(albumId))
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        photosAdapter.setOnItemClickListener {
            findNavController().navigate(PhotosFragmentDirections.actionPhotosFragmentToZoomingFragment(
                photoTitle = it.title,
                photoUrl = it.url
            ))
        }

        mBinding.photosRvPhotos.apply {
            adapter = photosAdapter
            val mLayoutManager = GridLayoutManager(requireContext(), 3)
            layoutManager = mLayoutManager
        }
    }

    private fun setTitle() {
        mBinding.photosTopBar.topBarTvTitle.text = albumTitle
        mBinding.photosTopBar.topBarIvBack.setOnClickListener {
            findNavController().navigateUp()
        }
        mBinding.photosTopBar.topBarEtSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(txt: Editable?) {
                photosAdapter.filter.filter(txt)

                Log.d("TAG", "afterTextChanged: ${txt.toString().trim()}")
            }
        })
    }

    private fun showShimmer() {
        mBinding.photosShimmerView.visibility = View.VISIBLE
        mBinding.photosRvPhotos.visibility = View.INVISIBLE
    }

    private fun hideShimmer() {
        mBinding.photosShimmerView.visibility = View.GONE
        mBinding.photosRvPhotos.visibility = View.VISIBLE
    }
}