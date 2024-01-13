package com.example.app

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.repository.StarRepository
import com.example.app.repository.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val starRepository = StarRepository()

    private val mutablePeopleData = MutableLiveData<UIState<List<Character>>>()
    val immutablePeopleData: LiveData<UIState<List<Character>>> = mutablePeopleData

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = starRepository.getStarResponse()
                val characters = request.body()?.results
                mutablePeopleData.postValue(UIState(data = characters, isLoading = false, error = null))

            } catch (e: Exception) {
                mutablePeopleData.postValue(UIState(data = null, isLoading = false, error = e.message))
                Log.e("MainViewModel", "Operacja nie powiodla sie", e)
            }
        }
    }
}