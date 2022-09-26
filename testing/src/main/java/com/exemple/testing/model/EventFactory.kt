package com.exemple.testing.model

import com.example.core.domain.model.Event

class EventFactory {
    fun create(comic: FakeEvent) = when (comic) {
        FakeEvent.FakeEvent1 -> Event(
            1, "http://fakeurl.jpg"
        )
    }

    sealed class FakeEvent {
        object FakeEvent1 : FakeEvent()
    }
}