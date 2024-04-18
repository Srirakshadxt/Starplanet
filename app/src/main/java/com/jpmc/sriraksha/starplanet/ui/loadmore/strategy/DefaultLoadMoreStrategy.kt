package com.jpmc.sriraksha.starplanet.ui.loadmore.strategy

import androidx.compose.foundation.lazy.LazyListLayoutInfo

/**
 * Determines whether more items should be loaded based on the lazy list state.
 */
class DefaultLoadMoreStrategy(private val boundary: Int = 5) : LoadMoreStrategy {
    override fun shouldLoadMore(layoutInfo: LazyListLayoutInfo): Boolean {
        val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
        val totalItemCount = layoutInfo.totalItemsCount
        return lastVisibleItem != null && lastVisibleItem.index >= totalItemCount - boundary
    }
}