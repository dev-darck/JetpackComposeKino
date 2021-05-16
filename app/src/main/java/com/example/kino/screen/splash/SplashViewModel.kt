package com.example.kino.screen.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kino.SingleLiveEvent
import com.example.kino.network.NetworkEnum
import com.example.kino.network.NetworkRepository
import com.example.kino.network.NetworkRepository.ResultSuccess

class SplashViewModel(
    private val mNetworkRepository: NetworkRepository
) : ViewModel() {

    private val resultLiveData: SingleLiveEvent<NetworkEnum> = SingleLiveEvent()
    val attachObservable: LiveData<NetworkEnum> = resultLiveData

    init {
        startNetwork()
    }

    private fun startNetwork() {
        mNetworkRepository.isDownloadGenres(object : ResultSuccess {
            override fun success(result: NetworkEnum) {
                resultLiveData.postValue(result)
            }
        })
    }

    fun restart() {
        startNetwork()
    }
}