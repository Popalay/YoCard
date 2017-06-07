package com.popalay.cardme.business.debts;

import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.data.repositories.debt.DebtRepository;
import com.popalay.cardme.data.repositories.holder.HolderRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Completable;
import rx.Observable;
import rx.schedulers.Schedulers;

@Singleton
public class DebtsInteractor {

    private final DebtRepository debtRepository;
    private final HolderRepository holderRepository;

    @Inject public DebtsInteractor(DebtRepository debtRepository, HolderRepository holderRepository) {
        this.debtRepository = debtRepository;
        this.holderRepository = holderRepository;
    }

    public Completable save(Debt debt) {
        return debtRepository.save(debt)
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Debt>> getDebts() {
        return debtRepository.getAll()
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Debt>> getDebtsByHolder(long holderId) {
        return debtRepository.getAllByHolder(holderId)
                .subscribeOn(Schedulers.io());
    }

    public Completable remove(Debt debt) {
        return debtRepository.remove(debt)
                .andThen(holderRepository.updateCounts(debt.getHolder()))
                .subscribeOn(Schedulers.io());
    }
}
