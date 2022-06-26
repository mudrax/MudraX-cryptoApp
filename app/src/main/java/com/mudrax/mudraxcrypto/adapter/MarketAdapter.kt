package com.mudrax.mudraxcrypto.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mudrax.mudraxcrypto.R
import com.mudrax.mudraxcrypto.databinding.CurrencyItemLayoutBinding
import com.mudrax.mudraxcrypto.fragment.MarketFragmentDirections
import com.mudrax.mudraxcrypto.fragment.WatchlistFragment
import com.mudrax.mudraxcrypto.fragment.WatchlistFragmentDirections
import com.mudrax.mudraxcrypto.fragment.homeFragmentDirections
import com.mudrax.mudraxcrypto.models.CryptoCurrency

class MarketAdapter(var context: Context, private var list: List<CryptoCurrency>, var type: String):RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    inner class MarketViewHolder(private val currencyItemLayoutBinding: CurrencyItemLayoutBinding):RecyclerView.ViewHolder(currencyItemLayoutBinding.root){

        @SuppressLint("SetTextI18n")
        fun bindItem(item: CryptoCurrency){
            //name
            currencyItemLayoutBinding.currencyNameTextView.text = item.name

            //symbol
            currencyItemLayoutBinding.currencySymbolTextView.text = item.symbol

            //price
            currencyItemLayoutBinding.currencyPriceTextView.text = //item.quotes[0].price.toString() // convert to 2 decimals
                                        "$ ${String.format("%.03f" , item.quotes[0].price)}"

            //change 24h
            currencyItemLayoutBinding.currencyChangeTextView.text = //item.quotes[0].percentChange24h.toString() //convert to 3 decimals
                                        "${String.format("%.02f" , item.quotes[0].percentChange24h)}%"
            //image of bitcoin
            Glide.with(context).load(
                "https://s2.coinmarketcap.com/static/img/coins/64x64/" + item.id + ".png"
            ).thumbnail(Glide.with(context).load(R.drawable.spinner))
                .into(currencyItemLayoutBinding.currencyImageView)

            //graph of bitcoin
            Glide.with(context).load(
                "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/" + item.id + ".png"
            ).thumbnail(Glide.with(context).load(R.drawable.spinner))
                .into(currencyItemLayoutBinding.currencyChartImageView)


            //currency rate set
            if(item.quotes[0].percentChange24h>0)
            {
                currencyItemLayoutBinding.currencyChangeTextView.setTextColor(context.resources.getColor(
                    R.color.chartreuse
                ))
            }
            else
            {
                currencyItemLayoutBinding.currencyChangeTextView.typeface = Typeface.DEFAULT_BOLD
                currencyItemLayoutBinding.currencyChangeTextView.setTextColor(context.resources.getColor(
                    R.color.top_crypto_fall_red
                ))

            }

        }

    }

    fun updateData(dataItem: List<CryptoCurrency>)
    {
        list = dataItem
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        return MarketViewHolder(CurrencyItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val item = list[position]

        holder.bindItem(item)

        holder.itemView.setOnClickListener {

            if(type=="home") {
                findNavController(it).navigate(
                    homeFragmentDirections.actionHomeFragmentToDetailsFragment(item)
                )
            }
            else if(type == "market")
            {
                findNavController(it).navigate(
                    MarketFragmentDirections.actionMarketFragmentToDetailsFragment(item)
                )
            }
            else
            {
                findNavController(it).navigate(
                    WatchlistFragmentDirections.actionWatchlistFragmentToDetailsFragment(item)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}