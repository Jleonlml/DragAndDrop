package com.example.widgetsapp.viewmodel

import androidx.lifecycle.ViewModel

data class ItemDataModel(
    val itemText: String? = null
)

data class AccountDataModel(
    val accountName: String? = null
)

data class GroupDataModel(
    val groupName: String? = null,
    val accounts: List<AccountDataModel>,
    val index: Int
)

data class StackViewDataModel(
    val groups: List<GroupDataModel>?= null
)

class Fragment1ViewModel : ViewModel() {

}