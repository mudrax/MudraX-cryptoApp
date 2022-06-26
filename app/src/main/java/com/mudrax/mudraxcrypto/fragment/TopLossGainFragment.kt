package com.mudrax.mudraxcrypto.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.mudrax.mudraxcrypto.adapter.MarketAdapter
import com.mudrax.mudraxcrypto.apis.ApiInterface
import com.mudrax.mudraxcrypto.apis.ApiUtilities
import com.mudrax.mudraxcrypto.databinding.FragmentTopLossGainBinding
import com.mudrax.mudraxcrypto.models.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class TopLossGainFragment : Fragment() {

    private lateinit var binding:FragmentTopLossGainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTopLossGainBinding.inflate(layoutInflater)

        getMarketData()




        return binding.root
    }

    private fun getMarketData() {
        val position = requireArguments().getInt("position")
        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if(res.body()!=null)
            {
                withContext(Dispatchers.Main){
                    val cryptoList = res.body()!!.data.cryptoCurrencyList

                    Collections.sort(cryptoList){
                        o1 , o2 -> (o2.quotes[0].percentChange24h.toFloat())
                        .compareTo(o1.quotes[0].percentChange24h.toFloat())
                    }

                    binding.spinKitView.visibility=View.GONE
                    val list = ArrayList<CryptoCurrency>()
                    if(position==0)
                    {
                        list.clear()
                        for(i in 0..9)
                        {
                            list.add(cryptoList[i])
                        }

                        binding.topGainLoseRecyclerView.adapter = MarketAdapter(
                            requireContext(),
                            list,
                            "home"
                        )
                    }
                    else
                    {
                        list.clear()
                        for(i in 0..9)
                        {
                            list.add(cryptoList[cryptoList.size - i - 1])
                        }

                        binding.topGainLoseRecyclerView.adapter = MarketAdapter(
                            requireContext(),
                            list,
                            "home"
                        )
                    }

                }



            }
        }
    }


}