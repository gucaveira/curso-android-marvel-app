package com.example.marvelapp.presentation.detail

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

//Avisar o Dexguard para não ofuscar essa classe
@Keep
@Parcelize
data class DetailViewArg(
    val name: String,
    val imageUrl: String,
) : Parcelable