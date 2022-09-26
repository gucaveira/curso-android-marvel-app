package com.example.marvelapp.presentation.detail

import androidx.annotation.StringRes


data class DetailChildViewEnd(
    val id: Int,
    val imageUrl: String
)

data class DetailParentViewEnd(
    @StringRes
    val categoryStringResId: Int,
    val detailChildList: List<DetailChildViewEnd> = listOf()
)
