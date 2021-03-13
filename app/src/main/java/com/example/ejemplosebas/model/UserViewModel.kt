package com.example.ejemplosebas.model

import androidx.lifecycle.ViewModel
import com.example.ejemplosebas.pojo.User
import kotlinx.datetime.toLocalDateTime

class UserViewModel: ViewModel() {
    private var usuarioSelecionada: User

    init{
        usuarioSelecionada = User(0, "", "0001-01-01T00:00:00".toLocalDateTime())
    }

    fun getUserSeleccionada():User{
        return usuarioSelecionada
    }

    fun setUserSeleccionada(city: User){
        usuarioSelecionada = city
    }
}