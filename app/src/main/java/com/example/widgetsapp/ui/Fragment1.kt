package com.example.widgetsapp.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.widgetsapp.R
import com.example.widgetsapp.databinding.Fragment1Binding
import com.example.widgetsapp.ui.adapter.ListItem
import com.example.widgetsapp.ui.adapter.RecyclerViewAdapter
import com.example.widgetsapp.viewmodel.AccountDataModel
import com.example.widgetsapp.viewmodel.Fragment1ViewModel
import com.example.widgetsapp.viewmodel.GroupDataModel
import com.example.widgetsapp.viewmodel.StackViewDataModel

class Fragment1 : Fragment() {

    private lateinit var viewModel: Fragment1ViewModel
    private lateinit var mBinding: Fragment1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment1, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = RecyclerViewAdapter(this@Fragment1).apply {
                updateItems(
                    listOf(
                        ListItem.StackViewItem(
                            StackViewDataModel(
                                listOf(
                                    GroupDataModel(
                                        "group1",
                                        listOf(
                                            AccountDataModel("account1"),
                                            AccountDataModel("account2")
                                        ),
                                        0,
                                    ),
                                    GroupDataModel(
                                        accounts =
                                        listOf(
                                            AccountDataModel("account3")
                                        ),
                                        index = 1
                                    ),
                                    GroupDataModel(
                                        "group2",
                                        listOf(
                                            AccountDataModel("account4"),
                                            AccountDataModel("account5")
                                        ),
                                        2,
                                    ),
                                    GroupDataModel(
                                        accounts =
                                        listOf(
                                            AccountDataModel("account6")
                                        ),
                                        index = 3
                                    )
                                )
                            )
                        )
                    )
                )
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[Fragment1ViewModel::class.java]
    }
}