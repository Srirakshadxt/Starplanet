package com.sriraksha.starplanet.ui.loadmore.strategy

import androidx.compose.foundation.lazy.LazyListLayoutInfo

interface LoadMoreStrategy {
    fun shouldLoadMore(layoutInfo: LazyListLayoutInfo): Boolean
}