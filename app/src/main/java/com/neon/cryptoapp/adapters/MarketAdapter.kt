package com.neon.cryptoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neon.cryptoapp.R
import com.neon.cryptoapp.databinding.CurrencyItemLayoutBinding
import com.neon.cryptoapp.fragments.HomeFragmentDirections
import com.neon.cryptoapp.models.CryptoCurrency


class MarketAdapter(var context: Context, var list: List<CryptoCurrency>): RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    inner class MarketViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var binding = CurrencyItemLayoutBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        return  MarketViewHolder(LayoutInflater.from(context).inflate(R.layout.currency_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val item = list[position]
        holder.binding.currencyNameTextView.text = item.name
        holder.binding.currencySymbolTextView.text = item.symbol

        Glide.with(context).load("https://s2.coinmarketcap.com/static/img/coins/64x64/${item.id}.png")
            .thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.currencyImageView)

        holder.binding.currencyPriceTextView.text = "${String.format("$ %.02f", item.quotes[0].price)}"

        if(item.quotes!![0].percentChange24h > 0) {
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.olive_green))
            holder.binding.currencyChangeTextView.text = "+${String.format("%.04f", item.quotes[0].percentChange24h)} %"
        } else {

            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.currencyChangeTextView.text = "${String.format("%.04f", item.quotes[0].percentChange24h)} %"
        }

        holder.itemView.setOnClickListener {
            findNavController(it).navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item)
            )
        }


        Glide.with(context).load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${item.id}.png")
            .thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.currencyChartImageView)
    }

    override fun getItemCount(): Int = list.size
}