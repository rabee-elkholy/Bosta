package com.androdu.bosta.features.users_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androdu.bosta.R
import com.androdu.bosta.databinding.FragmentUsersBinding
import com.androdu.bosta.features.adapter.UsersAdapter
import com.androdu.bosta.utils.Helper.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersFragment : Fragment() {
    private val mViewModel: UsersViewModel by viewModels()
    private var _binding: FragmentUsersBinding? = null
    private val mBinding get() = _binding!!
    private var usersAdapter: UsersAdapter = UsersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)

        init()

        return mBinding.root
    }

    private fun init() {
        showShimmer()
        setTitle()
        setupRecyclerView()
        setupReferesh()
        setupCollector()
    }

    private fun setupReferesh() {
        mBinding.usersSrlSwipe.setOnRefreshListener {
            mViewModel.setEvent(UsersContract.Event.GetUsers)
            mBinding.usersSrlSwipe.isRefreshing = false
            showShimmer()
        }
    }

    private fun setupCollector() {
        lifecycleScope.launch {
            mViewModel.uiState.collect {
                when (it.userState) {
                    is UsersContract.UsersState.GetUsersSuccess -> {
                        Log.d("TAG", "GetAlbumsSuccess: ")
                        hideShimmer()
                        usersAdapter.setData(it.userState.users.toMutableList())
                    }

                    is UsersContract.UsersState.GetUsersFailed -> {
                        Log.d("TAG", "GetAlbumsFailed: ")
                        hideShimmer()
                        showErrorDialog(requireContext(), desc = it.userState.errMsg)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setupRecyclerView() {
        usersAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putSerializable("user", it)
            findNavController().navigate(R.id.albumsFragment, bundle)
        }

        mBinding.usersRvUsers.apply {
            adapter = usersAdapter
            val mLayoutManager = LinearLayoutManager(requireContext())
            layoutManager = mLayoutManager
        }
    }

    private fun setTitle() {
        mBinding.usersTopBar.topBarTvTitle.text = getString(R.string.users)
        mBinding.usersTopBar.topBarIvBack.visibility = GONE
        mBinding.usersTopBar.topBarEtSearch.visibility = GONE
    }

    private fun showShimmer() {
        mBinding.usersShimmerView.visibility = VISIBLE
        mBinding.usersRvUsers.visibility = INVISIBLE
    }

    private fun hideShimmer() {
        mBinding.usersShimmerView.visibility = GONE
        mBinding.usersRvUsers.visibility = VISIBLE
    }

    override fun onStart() {
        super.onStart()
        Log.d("TAG", "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume: ")

    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG", "onPause: ")

    }

    override fun onStop() {
        super.onStop()
        Log.d("TAG", "onStop: ")

    }
}