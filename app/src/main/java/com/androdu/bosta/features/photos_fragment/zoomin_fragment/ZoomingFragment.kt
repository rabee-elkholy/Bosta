package com.androdu.bosta.features.photos_fragment.zoomin_fragment

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.androdu.bosta.databinding.FragmentZoomingBinding
import kotlinx.coroutines.launch


class ZoomingFragment : Fragment() {
    private var _binding: FragmentZoomingBinding? = null
    private val mBinding get() = _binding!!

    private var photoTitle = ""
    private var photoUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentZoomingBinding.inflate(inflater, container, false)

        arguments?.let {
            photoTitle = it.getString("photoTitle").toString()
            photoUrl = it.getString("photoUrl").toString()
        }

        init()

        return mBinding.root
    }

    private fun init() {
        setupTitle()
        setupClick()
    }

    private fun setupClick() {
        mBinding.zoomingBtnShare.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val loader = ImageLoader(requireContext())
                    val request = ImageRequest.Builder(requireContext())
                        .data(photoUrl)
                        .allowHardware(false) // Disable hardware bitmaps.
                        .build()

                    val result = (loader.execute(request) as SuccessResult).drawable
                    val bitmap = (result as BitmapDrawable).bitmap
                    val bitmapPath: String =
                        Images.Media.insertImage(
                            requireActivity().contentResolver,
                            bitmap,
                            "title",
                            null
                        )
                    val bitmapUri = Uri.parse(bitmapPath)

                    val intent = Intent(Intent.ACTION_SEND)
                    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.type = "image/png"
                    startActivity(intent)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setupTitle() {
        mBinding.photoUrl = photoUrl
        mBinding.zoomingTopBar.topBarTvTitle.text = photoTitle
        mBinding.zoomingTopBar.topBarEtSearch.visibility = GONE
        mBinding.zoomingTopBar.topBarIvBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}