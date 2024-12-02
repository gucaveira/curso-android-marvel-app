package com.example.marvelapp.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.marvelapp.presentation.detail.viewmodel.FavoriteUiActionStateLiveData

@Composable
fun FavoriteButton(
    uiState: FavoriteUiActionStateLiveData.UiState,
    onClick: () -> Unit,
) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge.copy(
            topEnd = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp),
        ),
        color = Color.White,
        onClick = onClick,
    ) {
        var icon = Icons.Filled.FavoriteBorder

        if (uiState is FavoriteUiActionStateLiveData.UiState.Icon) {
            icon = if (uiState.isFavorite) {
                Icons.Filled.Favorite
            } else Icons.Filled.FavoriteBorder
        }

        Icon(
            imageVector = icon,
            contentDescription = null,
            Modifier.padding(8.dp)
        )
    }
}

@Preview
@Composable
fun FavoriteTrueButton() {
    FavoriteButton(
        uiState = FavoriteUiActionStateLiveData.UiState.Icon(true),
        onClick = {}
    )

}

@Preview
@Composable
fun FavoriteFalseButtonPreview() {
    FavoriteButton(
        uiState = FavoriteUiActionStateLiveData.UiState.Icon(false),
        onClick = {}
    )
}