package com.example.kino.screen.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.GlideBuilder
import com.example.kino.GlideManage
import com.example.kino.adapter.holder.BindHolder
import com.example.kino.databinding.DetailViewHolderBinding
import com.example.kino.databinding.MovieViewHolderLayoutBinding
import com.example.kino.network.model.common.Backdrops
import com.example.kino.network.model.common.Images
import com.example.kino.network.model.common.NetworkItem
import com.example.kino.network.model.common.Posters
import com.example.kino.network.model.movie.MovieDetail

class DetailViewHolder private constructor(
    private val binding: DetailViewHolderBinding,
    private val onClick: (Int) -> Unit,
) : BindHolder<NetworkItem>(binding) {

    constructor(parent: ViewGroup, onClick: (Int) -> Unit) : this(
        DetailViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onClick)

    override fun bind(item: NetworkItem, position: Int) {
        when(item) {
            is Backdrops -> GlideManage.with(binding.root).loadImage(item.file_path, binding.root)
            is Posters -> GlideManage.with(binding.root).loadImage(item.file_path, binding.root)
        }
    }

    override fun onClick(v: View?) {
        onClick(adapterPosition)
    }
}