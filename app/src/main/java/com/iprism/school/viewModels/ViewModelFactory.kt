package com.iprism.school.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory<T>(private val creator: () -> T) : ViewModelProvider.Factory where T : ViewModel {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return creator() as T
    }

}