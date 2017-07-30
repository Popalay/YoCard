package com.popalay.cardme.business

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.popalay.cardme.business.cards.CardInteractor
import com.popalay.cardme.business.exception.AppException
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.data.models.Holder
import com.popalay.cardme.data.repositories.card.CardRepository
import com.popalay.cardme.data.repositories.debt.DebtRepository
import com.popalay.cardme.data.repositories.holder.HolderRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import kotlin.test.assertEquals

class CardInteractorTest {

    private lateinit var cardRepository: CardRepository
    private lateinit var holderRepository: HolderRepository
    private lateinit var debtRepository: DebtRepository
    private lateinit var cardInteractor: CardInteractor

    @Before fun beforeEachTest() {
        cardRepository = mock<CardRepository>()
        holderRepository = mock<HolderRepository>()
        debtRepository = mock<DebtRepository>()

        cardInteractor = CardInteractor(cardRepository, holderRepository, debtRepository)
    }

    @Test fun get_Success() {
        val cardNumber = "6767 8989 5454 5555"
        val card = Card(number = cardNumber)

        whenever(cardRepository.get(cardNumber)).thenReturn(Flowable.just(card))

        val testObserver = cardInteractor.get(cardNumber).test()

        testObserver.awaitTerminalEvent()

        verify(cardRepository).get(cardNumber)

        testObserver
                .assertNoErrors()
                .assertValue { it == card }
                .assertComplete()
    }

    @Test fun setLastScanned_Success() {
        val cardNumber = "6767 8989 5454 5555"
        val card = Card(number = cardNumber)

        whenever(cardRepository.setLastScanned(card)).thenReturn(Completable.complete())

        val testObserver = cardInteractor.setLastScanned(card).test()

        testObserver.awaitTerminalEvent()

        verify(cardRepository).setLastScanned(card)

        testObserver
                .assertNoErrors()
                .assertComplete()
    }

    @Test fun getLastScanned_Success() {
        val cardNumber = "6767 8989 5454 5555"
        val card = Card(number = cardNumber)

        whenever(cardRepository.getLastScanned()).thenReturn(Single.just(card))

        val testObserver = cardInteractor.getLastScanned().test()

        testObserver.awaitTerminalEvent()

        verify(cardRepository).getLastScanned()

        testObserver
                .assertNoErrors()
                .assertValue { it == card }
                .assertComplete()
    }

    @Test fun validateCard_Success() {
        val card = Card(number = "8876437654376548", redactedNumber = "**** **** **** 6548")

        whenever(cardRepository.cardIsNew(card)).thenReturn(Single.just(true))

        val testObserver = cardInteractor.checkCardExist(card).test()

        testObserver.awaitTerminalEvent()

        verify(cardRepository).cardIsNew(card)

        testObserver
                .assertNoErrors()
                .assertComplete()
    }

    @Test fun validateCard_Failed() {
        val card = Card(number = "8876437654376548", redactedNumber = "**** **** **** 6548")

        whenever(cardRepository.cardIsNew(card)).thenReturn(Single.just(false))

        val testObserver = cardInteractor.checkCardExist(card).test()

        testObserver.awaitTerminalEvent()

        verify(cardRepository).cardIsNew(card)

        testObserver
                .assertError(AppException::class.java)
                .assertTerminated()
    }

    @Test fun hasAllData_True() {
        val holder = Holder(name = "Denis")
        val card = Card(holder = holder)

        val testObserver = cardInteractor.hasAllData(card, it).test()

        testObserver.awaitTerminalEvent()

        testObserver
                .assertNoErrors()
                .assertValue { it }
                .assertComplete()
    }

    @Test fun hasAllData_False() {
        val holder = Holder(name = "")
        val card = Card(holder = holder)

        val testObserver = cardInteractor.hasAllData(card, it).test()

        testObserver.awaitTerminalEvent()

        testObserver
                .assertNoErrors()
                .assertValue { !it }
                .assertComplete()
    }

    @Test fun saveCard_Success() {
        val card = Card()

        whenever(cardRepository.save(card)).thenReturn(Single.just(card))
        whenever(holderRepository.updateCounts(card.holder)).thenReturn(Completable.complete())

        val testObserver = cardInteractor.save(card).test()

        testObserver.awaitTerminalEvent()

        verify(cardRepository).save(card)
        verify(holderRepository).updateCounts(card.holder)

        testObserver
                .assertNoErrors()
                .assertComplete()
    }

    @Test fun getCards_Success() {
        val cards = (1..5).map { Card() }.toMutableList()
        val trashedCard = Card(isTrash = true)
        cards.add(trashedCard)

        whenever(cardRepository.getAll()).thenReturn(Flowable.fromCallable {
            cards.remove(trashedCard)
            cards
        })

        val testObserver = cardInteractor.getCards().test()

        testObserver.awaitTerminalEvent()

        verify(cardRepository).getAll()

        testObserver
                .assertNoErrors()
                .assertValue { it.count() == 5 }
                .assertComplete()
    }

    @Test fun getCardsByHolder_Success() {
        val cards = (1..5).map { Card() }.toMutableList()
        val trashedCard = Card(isTrash = true)
        cards.add(trashedCard)
        val holderId = "0"

        whenever(cardRepository.getAllByHolder(holderId)).thenReturn(Flowable.fromCallable {
            cards.remove(trashedCard)
            cards
        })

        val testObserver = cardInteractor.getCardsByHolder(holderId).test()

        testObserver.awaitTerminalEvent()

        verify(cardRepository).getAllByHolder(holderId)

        testObserver
                .assertNoErrors()
                .assertValue { it.count() == 5 }
                .assertComplete()
    }

    @Test fun updateCards_Success() {
        val cards = (1..5).map { Card(position = it * 3) }.toMutableList()
        val cardsRePositioned = (1..5).map { Card(position = it - 1) }.toMutableList()

        whenever(cardRepository.update(cards)).thenReturn(Completable.complete())

        val testObserver = cardInteractor.update(cards).test()

        testObserver.awaitTerminalEvent()

        argumentCaptor<List<Card>>().apply {
            verify(cardRepository).update(capture())
            assertEquals(cardsRePositioned, firstValue)
        }

        testObserver
                .assertNoErrors()
                .assertComplete()
    }

    @Test fun markAsTrash_Success() {
        val card = Card()

        whenever(cardRepository.markAsTrash(card)).thenReturn(Completable.complete())
        whenever(holderRepository.updateCounts(card.holder)).thenReturn(Completable.complete())

        val testObserver = cardInteractor.markAsTrash(card).test()

        testObserver.awaitTerminalEvent()

        verify(cardRepository).markAsTrash(card)
        verify(holderRepository).updateCounts(card.holder)

        testObserver
                .assertNoErrors()
                .assertComplete()
    }

    @Test fun restart_Success() {
        val card = Card(isTrash = true)

        whenever(cardRepository.restore(card)).thenReturn(Completable.complete())
        whenever(holderRepository.updateCounts(card.holder)).thenReturn(Completable.complete())

        val testObserver = cardInteractor.restore(card).test()

        testObserver.awaitTerminalEvent()

        verify(cardRepository).restore(card)
        verify(holderRepository).updateCounts(card.holder)

        testObserver
                .assertNoErrors()
                .assertComplete()
    }

    @Test fun removeTrashed_Success() {
        whenever(cardRepository.removeTrashed()).thenReturn(Completable.complete())
        whenever(holderRepository.removeTrashed()).thenReturn(Completable.complete())
        whenever(debtRepository.removeTrashed()).thenReturn(Completable.complete())

        val testObserver = cardInteractor.emptyTrash().test()

        testObserver.awaitTerminalEvent()

        verify(cardRepository).removeTrashed()
        verify(holderRepository).removeTrashed()
        verify(debtRepository).removeTrashed()

        testObserver
                .assertNoErrors()
                .assertComplete()
    }
}