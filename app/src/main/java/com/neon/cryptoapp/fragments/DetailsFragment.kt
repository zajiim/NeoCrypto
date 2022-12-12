package com.neon.cryptoapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.neon.cryptoapp.R
import com.neon.cryptoapp.databinding.FragmentDetailsBinding
import com.neon.cryptoapp.models.CryptoCurrency


class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val item: DetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDetailsBinding.inflate(layoutInflater)

        var data: CryptoCurrency = item.data!!
        setupDetails(data)

        loadChart(data)

        setButtonOnClick(data)

        addToFavorites(data)


        return binding.root
    }

    var favoriteList: ArrayList<String> ?= null
    var favoriteListIsChecked = false

    private fun addToFavorites(data: CryptoCurrency) {
        readData()
        favoriteListIsChecked = if(favoriteList!!.contains(data.symbol)) {
            binding.addWatchlistButton.setImageResource(R.drawable.star_icon)
            true
        } else {
            binding.addWatchlistButton.setImageResource(R.drawable.star_outline_icon)
            false
        }

        binding.addWatchlistButton.setOnClickListener {
            favoriteListIsChecked = if(!favoriteListIsChecked) {
                if(!favoriteList!!.contains(data.symbol)) {
                    favoriteList!!.add(data.symbol)
                }
                storeData()
                binding.addWatchlistButton.setImageResource(R.drawable.star_icon)
                true
            } else {
                binding.addWatchlistButton.setImageResource(R.drawable.star_outline_icon)
                favoriteList!!.remove(data.symbol)
                storeData()
                false
            }
        }

    }

    private fun storeData() {
        val sharedPreferences = requireContext().getSharedPreferences("favoriteList", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(favoriteList)
        editor.putString("favoriteList", json)
        editor.apply()
    }

    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("favoriteList", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("favoriteList", ArrayList<String>().toString())
        val type = object: TypeToken<ArrayList<String>>(){}.type
        favoriteList = gson.fromJson(json, type)

    }

    private fun setButtonOnClick(data: CryptoCurrency) {
        val oneMonth = binding.button
        val oneWeek = binding.button1
        val oneDay = binding.button2
        val fourHour = binding.button3
        val oneHour = binding.button4
        val fifteenMin = binding.button5

        val clickListener = View.OnClickListener {
            when(it.id){
                fifteenMin.id -> loadChartData(it, "15", item, oneDay, oneMonth, oneWeek, fourHour, oneHour)
                oneHour.id -> loadChartData(it, "1h", item, oneDay, oneMonth, oneWeek, fourHour, fifteenMin)
                fourHour.id -> loadChartData(it, "4h", item, oneDay, oneMonth, oneWeek, fifteenMin, oneHour)
                oneDay.id -> loadChartData(it, "D", item, fifteenMin, oneMonth, oneWeek, fourHour, oneHour)
                oneWeek.id -> loadChartData(it, "W", item, oneDay, oneMonth, fifteenMin, fourHour, oneHour)
                oneMonth.id -> loadChartData(it, "M", item, oneDay, fifteenMin, oneWeek, fourHour, oneHour)
            }
        }

        fifteenMin.setOnClickListener(clickListener)
        oneHour.setOnClickListener(clickListener)
        fourHour.setOnClickListener(clickListener)
        oneDay.setOnClickListener(clickListener)
        oneWeek.setOnClickListener(clickListener)
        oneMonth.setOnClickListener(clickListener)


    }

    private fun loadChartData(
        it: View?,
        s: String,
        item: DetailsFragmentArgs,
        oneDay: AppCompatButton,
        oneMonth: AppCompatButton,
        oneWeek: AppCompatButton,
        fourHour: AppCompatButton,
        oneHour: AppCompatButton
    ) {
        disableButton(oneHour, fourHour, oneDay, oneWeek, oneMonth)
        it!!.setBackgroundResource(R.drawable.active_button)

        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        binding.detaillChartWebView.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol+%20item.symbol%20.toString()%20+%20%22USD&interval="+s+"&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT%22")

    }

    private fun disableButton(oneHour: AppCompatButton, fourHour: AppCompatButton, oneDay: AppCompatButton, oneWeek: AppCompatButton, oneMonth: AppCompatButton) {
        oneDay.background = null
        oneMonth.background = null
        oneWeek.background = null
        fourHour.background = null
        oneHour.background = null

    }

    private fun loadChart(data: CryptoCurrency) {
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        binding.detaillChartWebView.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol+%20item.symbol%20.toString()%20+%20%22USD&interval=%22%20+%20s%20+%20%22&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT%22")

    }

    private fun setupDetails(data: CryptoCurrency) {
        binding.detailSymbolTextView.text = data.symbol

        Glide.with(requireContext()).load("https://s2.coinmarketcap.com/static/img/coins/64x64/${data.id}.png")
            .thumbnail(Glide.with(requireContext()).load(R.drawable.spinner))
            .into(binding.detailImageView)

        binding.detailPriceTextView.text = "${String.format("$ %.04f", data.quotes[0].price)}"


        if(data.quotes!![0].percentChange24h > 0) {
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.olive_green))
            binding.detailChangeImageView.setImageResource(R.drawable.up_arrow_icon)
            binding.detailChangeTextView.text = "+${String.format("%.04f", data.quotes[0].percentChange24h)} %"
        } else {

            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.red))
            binding.detailChangeImageView.setImageResource(R.drawable.down_arrow_icon)
            binding.detailChangeTextView.text = "${String.format("%.04f", data.quotes[0].percentChange24h)} %"
        }
    }


}