/*
 * Copyright (C) 2023 ankio(ankio@ankio.net)
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

package net.ankio.auto.ui.dialog

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import net.ankio.auto.R
import net.ankio.auto.utils.SpUtils


abstract class BaseSheetDialog(context: Context) :
    BottomSheetDialog(context, R.style.BottomSheetDialog) {
    private val job = Job()
    val coroutineScope = CoroutineScope(Dispatchers.Main + job)
    lateinit var cardView: MaterialCardView
    abstract fun onCreateView(inflater: LayoutInflater): View

    fun show(float: Boolean = false) {
        val inflater = LayoutInflater.from(context)
        val root = this.onCreateView(inflater)
        this.setContentView(root)
        this.setCancelable(false);
        if (float) {
            this.window?.setType((WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY));
        }
        val bottomSheet: View = root.parent as View


        val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)

        // 禁用向下滑动关闭行为
        bottomSheetBehavior.isDraggable = false
        // 设置BottomSheetDialog展开到全屏高度
        bottomSheetBehavior.skipCollapsed = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED;

        if(::cardView.isInitialized){
            val layoutParams = if (cardView.layoutParams != null) {
                cardView.layoutParams as ViewGroup.MarginLayoutParams
            } else {
                // 如果 RadiusCardView 还没有布局参数，则创建新的参数
                ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
            //是否使用圆角风格
            val margin = dpToPx(context,20f)
            if(SpUtils.getBoolean("cardRound",false)){
                layoutParams.setMargins(margin,margin,margin,margin)
                cardView.layoutParams = layoutParams
                //使用圆角风格
            }else{
                cardView.setPadding(0,0,0,margin)
                layoutParams.setMargins(0, 0, 0, - margin)
                cardView.layoutParams = layoutParams
            }
        }

        this.show()
    }
    fun dpToPx(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }


    override fun dismiss() {
        super.dismiss()
        job.cancel()
    }

}