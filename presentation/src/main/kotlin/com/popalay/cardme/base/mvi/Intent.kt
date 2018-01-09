/*
 * Created by popalay on 30.12.17 20:10
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 30.12.17 20:10
 */

package com.popalay.cardme.base.mvi

import io.reactivex.ObservableTransformer

interface Intent

typealias IntentFilter<I> = ObservableTransformer<I, I>