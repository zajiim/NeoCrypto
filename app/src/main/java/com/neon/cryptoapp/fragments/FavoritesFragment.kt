package com.neon.cryptoapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.neon.cryptoapp.R
import com.neon.cryptoapp.adapters.MarketAdapter
import com.neon.cryptoapp.api.ApiInterface
import com.neon.cryptoapp.api.RetrofitInstance
import com.neon.cryptoapp.databinding.FragmentFavoritesBinding
import com.neon.cryptoapp.models.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoriteList: ArrayList<String>
    private lateinit var favoriteListItem: ArrayList<CryptoCurrency>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)

        readData()
        lifecycleScope.launch(Dispatchers.IO) {
            val res = RetrofitInstance.getInstance().create(ApiInterface::class.java).getMarketData()

            if(res.body() != null) {
                withContext(Dispatchers.Main) {
                    favoriteListItem = ArrayList()
                    favoriteListItem.clear()

                    for(favoritesData in favoriteList) {
                        for(item in res.body()!!.data.cryptoCurrencyList) {
                            if(favoritesData == item.symbol) {
                                favoriteListItem.add(item)
                            }
                        }
                    }

                    binding.spinKitView.visibility = View.GONE
                    binding.watchlistRecyclerView.adapter = MarketAdapter(requireContext(), favoriteListItem, "favoritefragment")
                }
            }
        }
        return binding.root
    }

    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("favoriteList", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("favoriteList", ArrayList<String>().toString())
        val type = object: TypeToken<ArrayList<String>>(){}.type
        favoriteList = gson.fromJson(json, type)

    }


}