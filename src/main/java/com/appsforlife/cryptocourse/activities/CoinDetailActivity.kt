package com.appsforlife.cryptocourse.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.appsforlife.cryptocourse.api.ApiFactory
import com.appsforlife.cryptocourse.databinding.ActivityCoinDetailBinding
import com.appsforlife.cryptocourse.databinding.CoinFullPriceBinding
import com.appsforlife.cryptocourse.databinding.CoinMainDetailBinding
import com.appsforlife.cryptocourse.databinding.LinkViewLayoutBinding
import com.appsforlife.cryptocourse.pojo.CoinPriceInfo
import com.appsforlife.cryptocourse.pojo.CoinPriceInfoDisplayData
import com.appsforlife.cryptocourse.utils.getJSOUPContent
import com.appsforlife.cryptocourse.viewmodel.CoinViewModel
import com.bumptech.glide.Glide
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class CoinDetailActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var viewModel: CoinViewModel
    private lateinit var binding: ActivityCoinDetailBinding
    private lateinit var mainDetailBinding: CoinMainDetailBinding
    private lateinit var coinFullPriceBinding: CoinFullPriceBinding
    private lateinit var linkViewBinding: LinkViewLayoutBinding
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCoinDetailBinding.inflate(layoutInflater)
        mainDetailBinding = binding.incMain
        linkViewBinding = binding.incLinkView
        coinFullPriceBinding = binding.incDetail
        setContentView(binding.root)

        if (intent.hasExtra(EXTRA_ID)) {
            id = intent.getStringExtra(EXTRA_ID).toString()
        } else {
            finish()
            return
        }

        setSupportActionBar(binding.toolBarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolBarDetail.setNavigationOnClickListener { onBackPressed() }

        viewModel = ViewModelProvider(this).get(CoinViewModel::class.java)

        viewModel.getDetailInfo(id).observe(this, {
            with(mainDetailBinding) {
                binding.toolBarDetail.title = it.fullName
                tvName.text = it.name
                tvAlgorithmDesc.text = it.algorithm
                tvLaunchDesc.text = it.assetLaunchDate
                tvProofDesc.text = it.proofType
                tvDocumentDesc.text = it.documentType
            }
            Glide.with(this).load(it.getFullImageURL()).into(mainDetailBinding.ivLogo)
            it.name?.let { name -> getFullListPrice(name) }
            linkViewBinding.tvLinkUrl.text = it.getFullCoinUrl()
            it.url?.let { _ -> setPreviewLink(it.getFullCoinUrl()) }
        })
    }

    private fun getFullListPrice(fSym: String) {
        val disposable = ApiFactory.apiService.getFullPriceList(fSyms = fSym)
            .map { getPriceListFromRawData(it) }
            .delaySubscription(1, TimeUnit.SECONDS)
            .repeat()
            .retry()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it != null) {
                    with(coinFullPriceBinding) {
                        tvSymbolsDesc.text = String.format(
                            getString(com.appsforlife.cryptocourse.R.string.symbols),
                            it.fromSymbol,
                            it.toSymbol
                        )
                        tvPriceDesc.text = it.price
                        tvOpenDesc.text = it.openDay
                        tvHighDayDesc.text = it.highDay
                        tvLowDayDesc.text = it.lowDay
                        tvLowHourDesc.text = it.lowHour
                        tvHighHourDesc.text = it.highHour
                        tvOpenHourDesc.text = it.openHour
                        tvHigh24hourDesc.text = it.high24Hour
                        tvLow24hourDesc.text = it.low24Hour
                        tvOpen24hourDesc.text = it.open24Hour
                        tvChangeDayDesc.text = it.changeDay
                        tvChangeHourDesc.text = it.changeHour
                        tvChange24hourDesc.text = it.change24Hour
                        tvVolumeDayDesc.text = it.volumeDay
                        tvVolumeHourDesc.text = it.volumeHour
                        tvVolume24hourDesc.text = it.volume24Hour

                        fmLoading.visibility = View.GONE

                    }
                }
                Log.d("LOAD_DETAIL", "Success $it")
            }, {
                coinFullPriceBinding.clFullPrice.visibility = View.GONE
                Log.d("LOAD_DETAIL", "Failure ${it.message}")

            })
        compositeDisposable.add(disposable)
    }

    private fun setPreviewLink(url: String) {
        val disposable = getJSOUPContent(url)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.retry()
            ?.subscribe({ result ->
                val metaTags = result?.getElementsByTag("meta")
                if (metaTags != null) {
                    for (element in metaTags)
                        when {
                            element.attr("property").equals("og:title") -> {
                                linkViewBinding.tvLinkTitle.text = element.attr("content")
                            }
                            element.attr("property").equals("og:description") -> {
                                linkViewBinding.tvLinkDescription.text = element.attr("content")
                            }
                        }
                    linkViewBinding.clLinkDetail.visibility = View.VISIBLE
                    setLoading()
                }
            },
                { _ ->
                    linkViewBinding.clLinkDetail.visibility = View.GONE
                    setLoading()
                })
        compositeDisposable.add(disposable)
    }

    private fun setLoading() {
        if (linkViewBinding.clLinkDetail.visibility == View.VISIBLE) {
            linkViewBinding.lottieLoading.visibility = View.GONE
        } else {
            linkViewBinding.lottieLoading.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        setLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun getPriceListFromRawData(
        coinPriceInfoDisplayData: CoinPriceInfoDisplayData
    ): CoinPriceInfo? {
        var result: CoinPriceInfo? = null
        val jsonObject = coinPriceInfoDisplayData.coinPriceInfoJsonObject ?: return null
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinPriceInfo::class.java
                )
                result = priceInfo
            }
        }
        return result
    }

    companion object {
        private const val EXTRA_ID = "id"

        fun newIntent(activity: Activity, view: View, id: String) {
            val intent =
                Intent(activity, CoinDetailActivity::class.java)
            val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                view,
                view.transitionName
            )
            intent.putExtra(EXTRA_ID, id)
            activity.startActivity(intent, options.toBundle())
        }
    }

}