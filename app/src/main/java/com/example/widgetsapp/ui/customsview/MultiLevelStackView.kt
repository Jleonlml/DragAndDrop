package com.example.widgetsapp.ui.customsview

import android.content.Context
import android.graphics.Rect
import android.transition.ChangeBounds
import android.transition.ChangeTransform
import android.transition.Transition
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.util.AttributeSet
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.annotation.IntDef
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.transition.addListener
import com.example.widgetsapp.R
import com.example.widgetsapp.ui.listbinders.HeaderBinder
import com.example.widgetsapp.ui.model.widgetsListModel
import com.google.android.material.card.MaterialCardView
import java.util.Collections
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt


class MultiLevelStackView: ConstraintLayout {

    private lateinit var  rootLayout: ConstraintLayout
    private var startConstraints: ConstraintSet? = null
    private var endConstraints: ConstraintSet? = null
    private var elementSpacing = 0
    private var animationDuration = 0L
    private var transitionListener: Transition.TransitionListener? = null
    var widgetList = mutableListOf<widgetsListModel>()

    @Orientation
    private var mOrientation = VERTICAL

    constructor(context: Context): super(context) {
        initView()
    }

    constructor(context: Context, attr: AttributeSet?): super(context, attr) {
        initView()
        getStuffFromXml(attr, context)
    }

    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int): super(context, attr, defStyleAttr)
    {
        initView()
        getStuffFromXml(attr, context)
    }


    private fun getStuffFromXml(attrs: AttributeSet?, context: Context) {
        val data = context.obtainStyledAttributes(attrs, R.styleable.MultiLevelStackView)
        elementSpacing = data.getDimensionPixelSize(R.styleable.MultiLevelStackView_elementSpacing, 0)
        data.recycle()
    }

    private fun initView() {
        val dragListener = View.OnDragListener { dragView, event ->
            when(event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    false
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    val targetView =  findChildAtPoint(event.x.roundToInt(), event.y.roundToInt())

                    val originView =  if (event.localState is MaterialCardView) {
                        (event.localState as View).parent
                    } else {
                        (event.localState as View)
                    }
                    val isTargetGroup =
                        widgetList.find { it.item == targetView }?.subViews?.isNotEmpty() ?: false
                    val isOriginGroup =
                        widgetList.find { it.item == originView }?.subViews?.isNotEmpty() ?: false
                    val indexes = targetView?.let { findIndexes((originView as View), it) }
                    Log.d("Juan", originView.toString())
                    val isOriginSubItem = indexes?.first?.second != null
                    val isTargetSubItem = indexes?.second?.second != null
                    val isTargetHeader = targetView?.getTag(R.id.stackView) is HeaderBinder
                    if (targetView != originView)
                    {
                        if (isOriginGroup) {
                            if (indexes != null) {
                                handleMainLevelSwap(indexes)
                            }
                        }
                        else {
                            if (targetView != null && !isTargetGroup) {
                                if ((originView as View).parent == targetView.parent) {
                                    if (indexes != null) {
                                        if(isOriginSubItem && !isTargetHeader) {
                                            handleSubLevelSwap(indexes)
                                        }
                                        else {
                                            handleMainLevelSwap(indexes)
                                        }
                                    }
                                }
                                else
                                {
                                    if (isOriginSubItem) {
                                        if (indexes != null) {
                                            widgetList[indexes.first.first!!].subViews.remove(originView)
                                            if (!isTargetSubItem) {
                                                indexes.second.first?.let { widgetList.add(it, widgetsListModel(originView, mutableListOf())) }
                                            }
                                            else
                                            {
                                                (originView.parent as ViewGroup).removeView(originView)
                                                indexes.second.first?.let { widgetList[it].subViews.add(originView) }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        if (indexes != null && !isTargetHeader) {
                                            indexes.first.first?.let { originGroupIndex -> widgetList.removeAt(originGroupIndex)
                                                if (isTargetSubItem && !isOriginSubItem) {
                                                    indexes.second.first?.let { destinationGroupIndex->
                                                        indexes.second.second?.let { subItemIndex ->
                                                            val indexToAddView = if (originGroupIndex  < destinationGroupIndex)
                                                            {
                                                                destinationGroupIndex - 1
                                                            } else {
                                                                destinationGroupIndex
                                                            }
                                                            widgetList[indexToAddView].subViews.add(
                                                                subItemIndex, originView)
                                                        } }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        redraw(true)
                    }
                    true
                }

                else -> {
                    false
                }
            }
        }

        this.setOnDragListener(dragListener)
    }

    private fun handleSubLevelSwap(indexes: Pair<Pair<Int?, Int?>,Pair<Int?, Int?>>) {
        indexes.second.second?.let {
            indexes.first.second?.let { it1 ->
                Collections.swap(
                    widgetList[indexes.first.first!!].subViews,
                    it1,
                    it
                )
            }
        }
    }

    private fun handleMainLevelSwap(indexes: Pair<Pair<Int?, Int?>,Pair<Int?, Int?>>) {
        indexes.second.first?.let {
            indexes.first.first?.let { it1 ->
                Collections.swap(widgetList,
                    it1,
                    it
                )
            }
        }
    }

    private fun findIndexes(originView: View, targetView: View): Pair<Pair<Int?, Int?>, Pair<Int?, Int?>> {
        var originGroupIndex: Int? = null
        var originSubItemIndex: Int? = null
        var targetGroupIndex: Int? = null
        var targetSubItemIndex: Int? = null
        widgetList.forEachIndexed { groupIndex, group ->
            if(group.item == originView)
            {
                originGroupIndex = groupIndex
            }
            if(group.item == targetView)
            {
                targetGroupIndex = groupIndex
            }
            group.subViews.forEachIndexed { accountIndex, account ->
                if (account == originView) {
                    originSubItemIndex = accountIndex
                    originGroupIndex = groupIndex
                }
                if (account == targetView) {
                    targetSubItemIndex = accountIndex
                    targetGroupIndex = groupIndex
                }
            }
        }
        return Pair(Pair(originGroupIndex, originSubItemIndex), Pair(targetGroupIndex, targetSubItemIndex))
    }

    private fun findChildAtPoint(pointX: Int, pointY: Int): View? {
        widgetList.map { widgetsListModel ->
            widgetsListModel.subViews.forEach {
                val childView = it
                val hitRect = Rect().apply { childView.getHitRect(this) }
                if (hitRect.contains(pointX.minus(widgetsListModel.item.left), pointY.minus(widgetsListModel.item.top))) {
                    return childView
                }
            }
            widgetsListModel.item
        }.let {
            return search(it, pointX, pointY)
        }
    }

    private tailrec fun search(viewList: List<View>, pointX: Int, pointY: Int): View? {
        if (viewList.isEmpty()) {
            return null
        }
        val middleIndex = viewList.size / 2
        val childView = viewList[middleIndex]
        val hitRect = Rect().apply { childView.getHitRect(this) }
        if (hitRect.contains(pointX, pointY)) {
            return childView
        }
        if (viewList.size == 1) {
            return null
        }
        if (isVertical()) {
            when {
                pointY < hitRect.top -> {
                    return search(viewList.subList(0, max(0, middleIndex)), pointX, pointY)
                }

                pointY > hitRect.bottom -> {
                    return search(
                        viewList.subList(min(middleIndex + 1, viewList.size), viewList.size),
                        pointX,
                        pointY
                    )
                }

                else -> {
                    return null
                }
            }
        }
        else {
            when {
                pointY < hitRect.left -> {
                    return search(viewList.subList(0, max(0, middleIndex)), pointX, pointY)
                }

                pointY > hitRect.right -> {
                    return search(
                        viewList.subList(min(middleIndex + 1, viewList.size), viewList.size),
                        pointX,
                        pointY
                    )
                }

                else -> {
                    return null
                }
            }
        }
    }

    private fun redraw(removeViews: Boolean = true) {
        if (removeViews)
        {
            removeAllViews()
        }
        val viewsList = widgetList.map { view ->
            if (view.subViews.isNotEmpty()) {
                (view.item as ConstraintLayout).removeAllViews()
                view.subViews.forEach {
                    (view.item as ConstraintLayout).addView(it)
                    rebuildAndApplyConstraints(view.subViews, view.item)
                }
            }
            if (view.item.parent != null) {
                (view.item.parent as ViewGroup).removeAllViews()
            }
            addView(view.item)
            view.item
        }
        rebuildAndApplyConstraints(viewsList, this)
    }

    private fun rebuildAndApplyConstraints(widgets: List<View>, layout: ConstraintLayout,  animate: Boolean = false)
    {
        rebuildTransition(widgets, layout)
        applyConstraints(widgets, layout, endConstraints, animate)
    }

    private fun rebuildTransition(widgets: List<View>, layout: ConstraintLayout) {
        if (widgets.isEmpty())
            return

        startConstraints = ConstraintSet()
        startConstraints?.clone(layout)
        endConstraints = ConstraintSet()
        endConstraints?.clone(layout)

        widgets.forEachIndexed { index, view ->
            val margin = 50
            val viewId = view.id
            when(mOrientation) {
                VERTICAL -> {
                    buildVerticalStartConstraints(startConstraints, viewId, index, margin)
                    buildVerticalEndConstraints(endConstraints, viewId, index, widgets)
                }

                HORIZONTAL -> {
                    buildHorizontalStartConstraints(startConstraints, viewId, index, margin)
                    buildHorizontalEndConstraints(endConstraints, viewId, index)
                }
            }
        }
    }

    private fun buildVerticalStartConstraints(
        constraintSet: ConstraintSet?,
        viewId: Int,
        index: Int,
        margin: Int
    ) {
        constraintSet?.apply {
            connect(viewId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)
            connect(viewId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(viewId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            constrainWidth(viewId, LayoutParams.MATCH_CONSTRAINT)
            constrainHeight(viewId, LayoutParams.WRAP_CONTENT)
        }
    }

    private fun buildHorizontalStartConstraints(
        constraintSet: ConstraintSet?,
        viewId: Int,
        index: Int,
        margin: Int
    ) {
        constraintSet?.apply {
            connect(viewId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)
            connect(viewId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(viewId, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            constrainWidth(viewId, LayoutParams.MATCH_CONSTRAINT)
            constrainHeight(viewId, LayoutParams.WRAP_CONTENT)
        }
    }

    private fun buildVerticalEndConstraints(
        constraintSet: ConstraintSet?,
        viewId: Int,
        index: Int,
        widgets: List<View>
    ) {
        constraintSet?.apply {
            if (index == 0) {
                connect(viewId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
            } else {
                connect(viewId, ConstraintSet.TOP, widgets[index - 1].id, ConstraintSet.BOTTOM, elementSpacing)
            }

            connect(viewId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(viewId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            constrainWidth(viewId, LayoutParams.MATCH_CONSTRAINT)
            constrainHeight(viewId, LayoutParams.WRAP_CONTENT)
        }
    }

    private fun buildHorizontalEndConstraints(
        constraintSet: ConstraintSet?,
        viewId: Int,
        index: Int
    ) {
        constraintSet?.apply {
            if (index == 0) {
                connect(viewId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
            } else {
                connect(viewId, ConstraintSet.START, widgetList[index - 1].item.id, ConstraintSet.END, elementSpacing)
            }
            connect(viewId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            constrainWidth(viewId, LayoutParams.MATCH_CONSTRAINT)
            constrainHeight(viewId, LayoutParams.WRAP_CONTENT)
        }
    }

    private fun applyConstraints(widgets: List<View>, layout: ConstraintLayout, constraintSet: ConstraintSet?, animate: Boolean = false) {
        constraintSet ?: return
        if (widgets.isEmpty()) {
            return
        }
        if (animate.not())
        {
            constraintSet.applyTo(layout)
            return
        }
        TransitionManager.beginDelayedTransition(layout, TransitionSet().apply {
            duration = animationDuration
            ordering = TransitionSet.ORDERING_TOGETHER
            interpolator = LinearInterpolator()
            addTransition(ChangeBounds())
            addTransition(ChangeTransform())
            transitionListener?.also {
                addListener { it }
            }
        })
        constraintSet.applyTo(layout)
    }

    fun setWidgets(views: List<widgetsListModel>) {
        this.widgetList = views.toMutableList()
        redraw()
    }

    fun isVertical(): Boolean = mOrientation == VERTICAL

    companion object {
        @IntDef(VERTICAL, HORIZONTAL)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Orientation

        const val VERTICAL = 0
        const val HORIZONTAL = 1
    }

    enum class Direction {
        LEFT, TOP, RIGHT, BOTTOM
    }
}