package com.neon.cryptoapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.neon.cryptoapp.R
import com.neon.cryptoapp.adapters.TopMarketAdapter
import com.neon.cryptoapp.api.ApiInterface
import com.neon.cryptoapp.api.RetrofitInstance
import com.neon.cryptoapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)

        getTopCurrencyList()

        return binding.root
    }

    private fun getTopCurrencyList() {
        lifecycleScope.launch(Dispatchers.IO) {
            val res = RetrofitInstance.getInstance().create(ApiInterface::class.java).getMarketData()
            withContext(Dispatchers.Main) {
                binding.rvTopCurrency.adapter = TopMarketAdapter(requireContext(), res.body()!!.data.cryptoCurrencyList)
            }

            Log.d("CRYPTO", "getTopCurrency - ${res.body()!!.data.cryptoCurrencyList}")
        }
    }


}