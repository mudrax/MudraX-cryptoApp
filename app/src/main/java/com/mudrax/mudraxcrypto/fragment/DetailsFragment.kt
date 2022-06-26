package com.mudrax.mudraxcrypto.fragment

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mudrax.mudraxcrypto.R
import com.mudrax.mudraxcrypto.databinding.FragmentDetailsBinding
import com.mudrax.mudraxcrypto.models.CryptoCurrency


class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding

    private val item: DetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment

        val data:CryptoCurrency = item.data!!

        setUpDetails(data)

        loadChart(data)

        setButtonOnClick(data)

        addToWatchList(data)



        return binding.root
    }

    private fun setButtonOnClick(data: CryptoCurrency) {
        val oneMonth = binding.button
        val oneWeek = binding.button1
        val oneDay = binding.button2
        val fourHour = binding.button3
        val oneHour = binding.button4
        val fifteenMinutes = binding.button5

        val clickListener = View.OnClickListener {
            when(it.id){
                fifteenMinutes.id-> loadChartData(it , "15" , data , oneDay , oneMonth , oneWeek , fourHour , oneHour)
                oneHour.id-> loadChartData(it , "1H" , data , oneDay , oneMonth , oneWeek , fourHour , fifteenMinutes)
                fourHour.id-> loadChartData(it , "4H" , data , oneDay , oneMonth , oneWeek , fifteenMinutes , oneHour)
                oneDay.id-> loadChartData(it , "D" , data , fifteenMinutes , oneMonth , oneWeek , fourHour , oneHour)
                oneWeek.id-> loadChartData(it , "W" , data , oneDay , oneMonth , fifteenMinutes , fourHour , oneHour)
                oneMonth.id-> loadChartData(it , "M" , data , oneDay , fifteenMinutes , oneWeek , fourHour , oneHour)
            }
        }

        fifteenMinutes.setOnClickListener(clickListener)
        oneHour.setOnClickListener(clickListener)
        fourHour.setOnClickListener(clickListener)
        oneDay.setOnClickListener(clickListener)
        oneWeek.setOnClickListener(clickListener)
        oneMonth.setOnClickListener(clickListener)

    }

    private fun loadChartData( it: View?, s: String, data: CryptoCurrency, oneDay: AppCompatButton, oneMonth: AppCompatButton, oneWeek: AppCompatButton,
                               fourHour: AppCompatButton, oneHour: AppCompatButton)
    {

        disableButton(oneDay , oneMonth, oneWeek , fourHour , oneHour)
        it!!.setBackgroundResource(R.drawable.active_button)
        binding.detaillChartWebView.settings.javaScriptEnabled=true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE , null)
        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol="   + data.symbol
                .toString() + "USD&interval="+s+"&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&" +
                    "toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=" +
                    "Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=" +
                    "[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&ut"
        )
    }

    private fun disableButton(oneDay: AppCompatButton, oneMonth: AppCompatButton, oneWeek: AppCompatButton, fourHour: AppCompatButton, oneHour: AppCompatButton) {
        oneDay.background=null
        oneMonth.background=null
        oneWeek.background=null
        fourHour.background=null
        oneHour.background=null
    }

    private fun loadChart(data:CryptoCurrency) {
        binding.detaillChartWebView.settings.javaScriptEnabled=true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE , null)
        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol="   + data.symbol
            .toString() + "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&" +
                "toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=" +
                "Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=" +
                "[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&ut"
        )

    }

    private fun setUpDetails(data:CryptoCurrency) {

        binding.detailSymbolTextView.text =data.symbol

        Glide.with(requireContext()).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + data.id + ".png"
        ).thumbnail(Glide.with(requireContext()).load(R.drawable.spinner))
            .into(binding.detailImageView)

        //price
        binding.detailPriceTextView.text = "$ ${String.format("%.03f" , data.quotes[0].price)}"

        //change
        binding.detailChangeTextView.text = "${String.format("%.02f" , data.quotes[0].percentChange24h)}%"

        //currency rate set
        if(data.quotes[0].percentChange24h>0)
        {
            context?.resources?.let {
                binding.detailChangeTextView.setTextColor(
                    it.getColor(
                        R.color.green
                    ))
            }
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
        }
        else
        {

            context?.resources?.let {
                binding.detailChangeTextView.setTextColor(
                    it.getColor(
                        R.color.crimson
                    ))
            }
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)

        }


    }
    var watchList: ArrayList<String>? = null
    var watchListChecked = false

    private fun addToWatchList(data:CryptoCurrency){
        readData()

        watchListChecked = if(watchList!!.contains(data.symbol))
        {
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star)

            true
        }
        else
        {
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
            false
        }

        binding.addWatchlistButton.setOnClickListener {
            watchListChecked =
                if(!watchListChecked)
                {
                    if(!watchList!!.contains(data.symbol))
                    {
                        watchList!!.add(data.symbol)
                    }
                    storeData()
                    binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
                    true
                    //

                }
            else
                {
                    binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
                    watchList!!.remove(data.symbol)
                    storeData()
                    false
                }
        }
    }

    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("watchlist" , Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("watchlist" , ArrayList<String>().toString())
        val type = object: TypeToken<ArrayList<String>>(){}.type
        watchList = gson.fromJson(json , type)
    }
    private fun storeData(){
        val sharedPreferences = requireContext().getSharedPreferences("watchlist" , Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(watchList)
        editor.putString("watchlist" , json)
        editor.apply()
    }


}