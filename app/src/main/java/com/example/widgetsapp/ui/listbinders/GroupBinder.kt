package com.example.widgetsapp.ui.listbinders

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.widgetsapp.R
import com.example.widgetsapp.databinding.AccountBinding
import com.example.widgetsapp.databinding.GroupBinding
import com.example.widgetsapp.ui.adapter.GenericRecyclerViewAdapter
import com.example.widgetsapp.ui.adapter.ListItem
import com.example.widgetsapp.ui.listbinders.bindersdata.StackViewBinderData
import com.example.widgetsapp.viewmodel.GroupDataModel

class GroupBinder(private val view: View, private val binderData: StackViewBinderData): GenericRecyclerViewAdapter.Binder<GroupDataModel>(view) {

    private var mBinding: GroupBinding? = null

    override fun bind(item: GroupDataModel, position: Int) {
        mBinding = DataBindingUtil.bind(view) ?: throw IllegalArgumentException("Invalid View")
        with(mBinding) {
            this?.groupRootLayout?.setOnLongClickListener { clickedView ->
                clickedView.startDragAndDrop(
                    ClipData(
                        "group_tile",
                        emptyArray(),
                        ClipData.Item("group_tile")
                    ),
                    View.DragShadowBuilder(clickedView),
                    clickedView,
                    View.DRAG_FLAG_OPAQUE
                )
            }
        }
    }

    companion object {
        fun getGroupItemBinder(
            item: GroupDataModel,
            container: ViewGroup,
            binderData: StackViewBinderData
        ): GroupBinder {
            val layoutInflater = LayoutInflater.from(container.context)
            return DataBindingUtil.inflate<GroupBinding>(
                layoutInflater,
                R.layout.group,
                container,
                false
            ).let { binding ->
                val itemView = binding.root
                val itemBinder = GroupBinder(itemView, binderData)
                itemView.post {
                    itemBinder.bind(
                        item,
                        binderData.recyclerViewPosition?: 0
                    )
                }
                itemBinder
            }
        }
    }

    fun getBinding(): GroupBinding? {
        return mBinding
    }
}