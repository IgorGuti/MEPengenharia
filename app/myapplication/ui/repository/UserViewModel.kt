package com.example.myapplication.ui.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> get() = _loginResponse

    private val _reservatorios = MutableLiveData<List<Reservatorio>>()
    val reservatorios: LiveData<List<Reservatorio>> get() = _reservatorios

    private val _medidores = MutableLiveData<List<Medidor>>()
    val medidores: LiveData<List<Medidor>> get() = _medidores

    private val _hidrometros = MutableLiveData<List<Hidrometro>>()
    val hidrometros: LiveData<List<Hidrometro>> get() = _hidrometros

    fun setLoginResponse(response: LoginResponse) {
        _loginResponse.value = response
    }

    fun setReservatorios(reservatorios: List<Reservatorio>) {
        _reservatorios.value = reservatorios
    }

    fun setMedidores(medidores: List<Medidor>) {
        _medidores.value = medidores
    }

    fun setHidrometros(hidrometros: List<Hidrometro>) {
        _hidrometros.value = hidrometros
    }
}
