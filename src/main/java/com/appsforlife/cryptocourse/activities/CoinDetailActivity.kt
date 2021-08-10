package com.appsforlife.cryptocourse.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.appsforlife.cryptocourse.databinding.ActivityCoinDetailBinding
import com.appsforlife.cryptocourse.viewmodel.CoinViewModel

class CoinDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: CoinViewModel
    private lateinit var binding: ActivityCoinDetailBinding
    private lateinit var fromSymbol: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(EXTRA_FROM_SYMBOL)) {
            fromSymbol = intent.getStringExtra(EXTRA_FROM_SYMBOL).toString()
        } else {
            finish()
            return
        }

//        setSupportActionBar(binding.toolBarDetail)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        binding.toolBarDetail.setNavigationOnClickListener { onBackPressed() }

        viewModel = ViewModelProvider(this).get(CoinViewModel::class.java)
        viewModel.getDetailInfo(fromSymbol).observe(this, {

        })
    }

    companion object {
        private const val EXTRA_FROM_SYMBOL = "fSym"

        fun newIntent(context: Context, fromSymbol: String): Intent {
            val intent = Intent(context, CoinDetailActivity::class.java)
            intent.putExtra(EXTRA_FROM_SYMBOL, fromSymbol)
            return intent
        }
    }
}