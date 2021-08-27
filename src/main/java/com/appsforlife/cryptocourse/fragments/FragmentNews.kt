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
import com.appsforlife.cryptocourse.activities.MainActivity
import com.appsforlife.cryptocourse.adapters.CoinNewsAdapter
import com.appsforlife.cryptocourse.databinding.FragmentNewsBinding
import com.appsforlife.cryptocourse.viewmodel.NewsViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

class FragmentNews : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var animation: LayoutAnimationController
    private val compositeDisposable = CompositeDisposable()
    private lateinit var mainActivity: MainActivity
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        val adapter = CoinNewsAdapter()
        binding.rvLastNews.adapter = adapter
        animation = AnimationUtils.loadLayoutAnimation(
            context,
            R.anim.slide_from_bottom_layout
        )

        mainActivity = activity as MainActivity

        mainActivity.binding.toolBarMain.title = getString(R.string.crypto_news)

        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        viewModel.newsList.observe(viewLifecycleOwner, {
            adapter.setData(it)
            if (flag == 1) {
                binding.rvLastNews.layoutAnimation = animation
                flag = 2
            }
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        compositeDisposable.dispose()
    }

    companion object {
        private var flag = 1
    }

}