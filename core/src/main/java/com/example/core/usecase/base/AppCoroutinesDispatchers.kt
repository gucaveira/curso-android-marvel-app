package com.example.core.usecase.base

import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

data class AppCoroutinesDispatchers @Inject constructor(
    val io: CoroutineDispatcher,
    val computation: CoroutineDispatcher,
    val main: CoroutineDispatcher,
)