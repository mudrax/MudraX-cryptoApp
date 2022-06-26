package com.mudrax.mudraxcrypto.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.mudrax.mudraxcrypto.adapter.MarketAdapter
import com.mudrax.mudraxcrypto.apis.ApiInterface
import com.mudrax.mudraxcrypto.apis.ApiUtilities
import com.mudrax.mudraxcrypto.databinding.FragmentMarketBinding
import com.mudrax.mudraxcrypto.models.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class MarketFragment : Fragment() {

    private lateinit var binding: FragmentMarketBinding
    private lateinit var list: List<CryptoCurrency>
    private lateinit var adapter: MarketAdapter
    lateinit var searchText:String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMarketBinding.inflate(layoutInflater)


        list = listOf()
        adapter = MarketAdapter(requireContext() , list , "market")
        binding.currencyRecyclerView.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()
            if(res.body()!=null)
            {
                withContext(Dispatchers.Main){
                    list = res.body()!!.data.cryptoCurrencyList
                    adapter.updateData(list)
                    binding.spinKitView.visibility = View.GONE
                }
            }


        }
        searchCoin()

        return binding.root
    }

    private fun searchCoin(){
        binding.searchEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                searchText = s.toString().lowercase()
                updateRecyclerView()
            }

        })
    }

    private fun updateRecyclerView(){
        val data = ArrayList<CryptoCurrency>()

        for (item in list) {
            val coinName = item.name.lowercase(Locale.getDefault())
            val coinSymbol = item.name.lowercase(Locale.getDefault())

            if(coinName.contains(searchText)||coinSymbol.contains(searchText)) {
                data.add(item)
            }
        }

        adapter.updateData(data)
    }


}