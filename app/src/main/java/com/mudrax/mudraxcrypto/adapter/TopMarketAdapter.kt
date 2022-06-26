package com.mudrax.mudraxcrypto.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mudrax.mudraxcrypto.R
import com.mudrax.mudraxcrypto.databinding.TopCurrencyLayoutBinding
import com.mudrax.mudraxcrypto.fragment.homeFragmentDirections
import com.mudrax.mudraxcrypto.models.CryptoCurrency

class TopMarketAdapter(var context: Context , val list:List<CryptoCurrency>):RecyclerView.Adapter<TopMarketAdapter.TopMarketViewHolder>() {

    inner class TopMarketViewHolder(val topCurrencyLayoutBinding: TopCurrencyLayoutBinding):RecyclerView.ViewHolder(topCurrencyLayoutBinding.root){

        fun bindItem(item: CryptoCurrency)
        {
            //name change
            topCurrencyLayoutBinding.topCurrencyNameTextView.text = item.name

            //image set
            Glide.with(context).load(
                "https://s2.coinmarketcap.com/static/img/coins/64x64/" + item.id + ".png"
            ).thumbnail(Glide.with(context).load(R.drawable.spinner))
                .into(topCurrencyLayoutBinding.topCurrencyImageView)

            //currency rate set
            if(item.quotes[0].percentChange24h>0)
            {
                topCurrencyLayoutBinding.topCurrencyChangeTextView.text = "+"+item.quotes[0].percentChange24h.toString()+"%"
                topCurrencyLayoutBinding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(
                    R.color.chartreuse
                ))
            }
            else
            {
                topCurrencyLayoutBinding.topCurrencyChangeTextView.typeface = Typeface.DEFAULT_BOLD
                topCurrencyLayoutBinding.topCurrencyChangeTextView.text = item.quotes[0].percentChange24h.toString()+"%"
                topCurrencyLayoutBinding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(
                    R.color.top_crypto_fall_red
                ))

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopMarketViewHolder {
        return TopMarketViewHolder(TopCurrencyLayoutBinding.inflate(LayoutInflater.from(parent.context),parent , false))
    }

    override fun onBindViewHolder(holder: TopMarketViewHolder, position: Int) {
        val item = list[position]
        holder.bindItem(item)

        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(
                homeFragmentDirections.actionHomeFragmentToDetailsFragment(item)
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

