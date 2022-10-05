package com.example.marvelapp.presentation.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marvelapp.databinding.ItemCharacterBinding
import com.example.marvelapp.framework.imageLoader.ImageLoader
import com.example.marvelapp.presentation.common.GenericViewHolder

class FavoritesViewHolder(
    itemBinding: ItemCharacterBinding,
    private val imageLoader: ImageLoader
) : GenericViewHolder<FavoriteItem>(itemBinding) {

    private val textName = itemBinding.textName
    private val imageCharacter = itemBinding.imageCharacter

    override fun bind(data: FavoriteItem) {
        textName.text = data.name
        imageLoader.load(imageCharacter, data.imageUrl)
    }

    companion object {
        fun create(parent: ViewGroup, imageLoader: ImageLoader): FavoritesViewHolder {
            val itemBinding = ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return FavoritesViewHolder(itemBinding, imageLoader)
        }
    }
}