package com.appsforlife.cryptocourse.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import com.appsforlife.cryptocourse.R
import com.appsforlife.cryptocourse.activities.MainActivity
import com.appsforlife.cryptocourse.adapters.CoinNewsAdapter
import com.appsforlife.cryptocourse.api.ApiFactory
import com.appsforlife.cryptocourse.databinding.FragmentNewsBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class FragmentNews : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val adapter = CoinNewsAdapter()
    private lateinit var animation: LayoutAnimationController
    private val compositeDisposable = CompositeDisposable()
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        binding.rvLastNews.adapter = adapter
        animation = AnimationUtils.loadLayoutAnimation(
            context,
            R.anim.slide_from_bottom_layout
        )

        mainActivity = activity as MainActivity

        mainActivity.binding.toolBarMain.title = getString(R.string.crypto_news)

        loadLastNews()

        return binding.root
    }

    private fun loadLastNews() {
        val disposable = ApiFactory.apiService.getLastNews()
            .map { it.news }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe({
                adapter.setData(it)
                if (flag == 1) {
                    binding.rvLastNews.layoutAnimation = animation
                    flag = 2
                }
                Log.d("LOAD_NEWS", it.toString())
            }, {
                Log.d("LOAD_NEWS", "Failure ${it.message}")
            })
        compositeDisposable.addAll(disposable)
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