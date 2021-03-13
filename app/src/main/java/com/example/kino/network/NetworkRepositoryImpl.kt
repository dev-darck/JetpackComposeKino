package com.example.kino.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.example.kino.applicationm.MovieApplication
import com.example.kino.db.DatabaseRepository
import com.example.kino.network.NetworkRepository.*
import com.example.kino.network.model.GenresList
import com.example.kino.network.model.Movie
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class NetworkRepositoryImpl(
    private val context: Context,
    private val api: Api,
    private val databaseRepository: DatabaseRepository
) : NetworkRepository {

    private val START_RESULT: String = "START"
    private val NO_CONNECTION_NETWORK = "NO_CONNECTION"
    private val ERROR = "ERROR"
    private val API_KEY = "620da4379b4594c225da04326f92ffb1"
    private val typeMovie: String = "movie"
    private val typeTv: String = "tv"

    override fun isDownloadGenres(result: ResultSuccess) {
        when (isOnline()) {
            true -> {
                downloadGenresAll(result)
            }
            false -> {
                isNotEmptyDB(result)
            }
        }
    }

    @SuppressLint("CheckResult")
    fun isNotEmptyDB(result: ResultSuccess) {
        databaseRepository.isNotEmptyGenresAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isNotEmpty) {
                    result.setSuccess(START_RESULT)
                } else {
                    result.setSuccess(NO_CONNECTION_NETWORK)
                }
            }, {
                result.setSuccess(ERROR)
                Timber.e(it)
            })
    }

    override fun getListItems() {
        TODO("Not yet implemented")
    }

    override fun getMovie(page: Int, type: String): Single<Movie> {
        return api.getMovie(type, buildParam(page))
    }

    @SuppressLint("CheckResult")
    private fun downloadGenresAll(resultSuccess: ResultSuccess) {
        val movie: Single<GenresList> = api.getGenres(typeMovie, API_KEY, MovieApplication.language)
        val serial: Single<GenresList> = api.getGenres(typeTv, API_KEY, MovieApplication.language)

        Single.zip(movie, serial, { movies, serials -> Pair(movies, serials) })
            .subscribeOn(Schedulers.io())
            .subscribe({
                databaseRepository.insertGenres(it.first.genres, typeMovie)
                databaseRepository.insertGenres(it.second.genres, typeTv)
                resultSuccess.setSuccess(START_RESULT)
            }, {
                resultSuccess.setSuccess(ERROR)
                Timber.e(it)
            })
    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    private fun buildParam(page: Int): MutableMap<String, String> {
        val map: MutableMap<String, String> = mutableMapOf()
        map["api_key"] = API_KEY
        map["language"] = MovieApplication.language
        map["sort_by"] = "popularity.desc"
        map["page"] = page.toString()
        return map
    }
}