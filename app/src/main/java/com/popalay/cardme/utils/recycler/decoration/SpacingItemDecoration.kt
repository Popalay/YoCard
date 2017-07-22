package com.popalay.cardme.utils.recycler.decoration

import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View

class SpacingItemDecoration private constructor(
        private val showFirstDivider: Boolean,
        private val showLastDivider: Boolean,
        private val betweenItems: Boolean,
        private val onSides: Boolean,
        private val dividerSize: Int
) : RecyclerView.ItemDecoration() {

    companion object {
        fun create(init: Builder.() -> Unit) = Builder(init).build()
    }

    private constructor(builder: Builder) : this(builder.showFirstDivider, builder.showLastDivider,
            builder.showBetween, builder.showOnSides, builder.dividerSize)

    private var orientation = -1

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        getOrientation(parent)

        if (orientation == OrientationHelper.HORIZONTAL) {
            applyToHorizontalList(outRect, position, state.itemCount)
        } else {
            applyToVerticalList(outRect, position, state.itemCount)
        }
    }

    private fun applyToVerticalList(outRect: Rect, position: Int, count: Int) {
        if (betweenItems) {
            outRect.top = dividerSize / 2
            outRect.bottom = dividerSize / 2

            if (position == 0) {
                outRect.top = if (showFirstDivider) dividerSize else 0
            }
            if (position == count - 1) {
                outRect.bottom = if (showLastDivider) dividerSize else 0
            }
        }

        if (onSides) {
            outRect.left = dividerSize
            outRect.right = dividerSize
        }
    }

    private fun applyToHorizontalList(outRect: Rect, position: Int, count: Int) {
        if (betweenItems) {
            outRect.left = dividerSize / 2
            outRect.right = dividerSize / 2

            if (position == 0) {
                outRect.left = if (showFirstDivider) dividerSize else 0
            }
            if (position == count - 1) {
                outRect.right = if (showLastDivider) dividerSize else 0
            }
        }

        if (onSides) {
            outRect.top = dividerSize
            outRect.bottom = dividerSize
        }
    }

    private fun getOrientation(parent: RecyclerView) {
        if (orientation != -1) return
        if (parent.layoutManager is LinearLayoutManager) {
            val layoutManager = parent.layoutManager as LinearLayoutManager
            orientation = layoutManager.orientation
        } else {
            throw IllegalStateException("SpacingItemDecoration can only be used with a LinearLayoutManager.")
        }
    }

    class Builder private constructor() {

        constructor(init: Builder.() -> Unit) : this() {
            init()
        }

        var showFirstDivider: Boolean = false
        var showLastDivider: Boolean = false
        var showBetween: Boolean = false
        var showOnSides: Boolean = false
        var dividerSize: Int = 0

        fun dividerSize(init: Builder.() -> Int) = apply { dividerSize = init() }

        fun showFirst(init: Builder.() -> Boolean) = apply { showFirstDivider = init() }

        fun showLast(init: Builder.() -> Boolean) = apply { showLastDivider = init() }

        fun showBetween(init: Builder.() -> Boolean) = apply { showBetween = init() }

        fun showOnSides(init: Builder.() -> Boolean) = apply { showOnSides = init() }

        fun build() = SpacingItemDecoration(this)
    }
}