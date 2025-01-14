/*
 * Copyright (C) 2024 ankio(ankio@ankio.net)
 * Licensed under the Apache License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-3.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package net.ankio.auto.utils

import android.content.Context
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ListAdapter
import android.widget.ListPopupWindow
import net.ankio.auto.R


class ListPopupUtils(val context: Context, anchor: View, val list: ArrayList<String>, onClick:(position:Int)->Unit) {
    private val listPopupWindow: ListPopupWindow = ListPopupWindow(context)
    private val adapter: ArrayAdapter<String> = ArrayAdapter(context, R.layout.list_popup_window_item, list)
    init {
        listPopupWindow.setAdapter(adapter)
        listPopupWindow.anchorView = anchor
        listPopupWindow.setOnItemClickListener{ _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            onClick(position)
            listPopupWindow.dismiss()
            hasShowPopupWindow = false
        }
       // listPopupWindow.width = ListPopupWindow.WRAP_CONTENT;
       // listPopupWindow.width = 300
        listPopupWindow.width =  measureContentWidth(adapter)
    }

    private fun measureContentWidth(listAdapter: ListAdapter): Int {
        var mMeasureParent: ViewGroup? = null
        var maxWidth = 0
        var itemView: View? = null
        var itemType = 0

        val adapter: ListAdapter = listAdapter
        val widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val count: Int = adapter.count
        for (i in 0 until count) {
            val positionType: Int = adapter.getItemViewType(i)
            if (positionType != itemType) {
                itemType = positionType
                itemView = null
            }

            if (mMeasureParent == null) {
                mMeasureParent = FrameLayout(context)
            }

            itemView = adapter.getView(i, itemView, mMeasureParent)
            itemView.measure(widthMeasureSpec, heightMeasureSpec)

            val itemWidth = itemView.measuredWidth

            if (itemWidth > maxWidth) {
                maxWidth = itemWidth
            }
        }

        return maxWidth
    }


    private var hasShowPopupWindow = false

    fun toggle() {
        listPopupWindow.apply {
            if (hasShowPopupWindow) {
                dismiss()
            }else {
                show()
            }
            hasShowPopupWindow = !hasShowPopupWindow
        }
    }

}