package com.appsforlife.cryptocourse.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.appsforlife.cryptocourse.R
import com.appsforlife.cryptocourse.activities.CoinDetailActivity
import com.appsforlife.cryptocourse.activities.MainActivity
import com.appsforlife.cryptocourse.adapters.CoinInfoAdapter
import com.appsforlife.cryptocourse.databinding.FragmentTopListBinding
import com.appsforlife.cryptocourse.listeners.CoinClickListener
import com.appsforlife.cryptocourse.pojo.CoinPriceInfo
import com.appsforlife.cryptocourse.viewmodel.CoinViewModel


class FragmentTopList : Fragment(), CoinClickListener {

    private var _binding: FragmentTopListBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainActivity: MainActivity

    private lateinit var viewModel: CoinViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopListBinding.inflate(inflater, container, false)

        mainActivity = activity as MainActivity

        val adapter = CoinInfoAdapter(this)
        binding.rvTopList.adapter = adapter
        val animation = AnimationUtils.loadLayoutAnimation(
            context,
            R.anim.slide_from_bottom_layout
        )

        viewModel = ViewModelProvider(this).get(CoinViewModel::class.java)
        viewModel.priceList.observe(viewLifecycleOwner, {
            adapter.coinInfoList = it
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

    override fun onCoinClick(coinPriceInfo: CoinPriceInfo) {
        val intent = CoinDetailActivity.newIntent(
            this.requireContext(),
            coinPriceInfo.fromSymbol
        )
        startActivity(intent)
    }

    companion object {
        private var flag = 1
    }

}