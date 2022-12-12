package com.neon.cryptoapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.neon.cryptoapp.adapters.MarketAdapter
import com.neon.cryptoapp.api.ApiInterface
import com.neon.cryptoapp.api.RetrofitInstance
import com.neon.cryptoapp.databinding.FragmentStatsBinding
import com.neon.cryptoapp.models.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding
    private lateinit var list: List<CryptoCurrency>
    private lateinit var adapter: MarketAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentStatsBinding.inflate(layoutInflater)

        list = listOf()
        adapter = MarketAdapter(requireContext(), list, "stats")
        binding.currencyRecyclerView.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO) {
            val res = RetrofitInstance.getInstance().create(ApiInterface::class.java).getMarketData()
            if(res.body() != null) {
                withContext(Dispatchers.Main) {
                    list = res.body()!!.data.cryptoCurrencyList

                    adapter.updateData(list)
                    binding.spinKitView.visibility = View.GONE
                }
            }
        }

        searchCoin()


        return binding.root
    }

    lateinit var searchText: String
    private fun searchCoin() {

        binding.searchEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                searchText = p0.toString().lowercase(Locale.getDefault())

                updateRecyclerView()
            }
        })

    }

    private fun updateRecyclerView() {
        val data = ArrayList<CryptoCurrency>()
        for(item in list) {
            val coinName = item.name.lowercase(Locale.getDefault())
            val coinSymbol = item.symbol.lowercase(Locale.getDefault())

            if(coinName.contains(searchText) || coinSymbol.contains(searchText)) {
                data.add(item)
            }
        }

        adapter.updateData(data)
    }


}