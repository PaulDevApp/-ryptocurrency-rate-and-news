package com.appsforlife.cryptocourse.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.appsforlife.cryptocourse.R
import com.appsforlife.cryptocourse.activities.CoinDetailActivity
import com.appsforlife.cryptocourse.activities.MainActivity
import com.appsforlife.cryptocourse.adapters.CoinInfoAdapter
import com.appsforlife.cryptocourse.databinding.FragmentTopListBinding
import com.appsforlife.cryptocourse.listeners.CoinClickListener
import com.appsforlife.cryptocourse.viewmodel.CoinViewModel


class FragmentTopList : Fragment(), CoinClickListener {

    private var _binding: FragmentTopListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private lateinit var viewModel: CoinViewModel
    private lateinit var animation: LayoutAnimationController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopListBinding.inflate(inflater, container, false)

        mainActivity = activity as MainActivity

        val adapter = CoinInfoAdapter(this)
        binding.rvTopList.adapter = adapter
        animation = AnimationUtils.loadLayoutAnimation(
            context,
            R.anim.slide_from_bottom_layout
        )

        mainActivity.binding.toolBarMain.title = getString(R.string.crypto_courses)

        viewModel = ViewModelProvider(this).get(CoinViewModel::class.java)
        viewModel.priceList.observe(viewLifecycleOwner, {
            adapter.setData(it)
            if (flag == 1) {
                binding.rvTopList.layoutAnimation = animation
                flag = 2
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private var flag = 1
    }

    override fun onCoinClick(id: String, view: View) {
        CoinDetailActivity.newIntent(mainActivity, view, id)
    }

}