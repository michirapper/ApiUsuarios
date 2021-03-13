package com.example.ejemplosebas.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ejemplosebas.databinding.FragmentDetallesBinding
import com.example.ejemplosebas.model.UserViewModel


class DetallesFragment : Fragment() {
    private lateinit var binding: FragmentDetallesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentDetallesBinding.inflate(inflater, container, false)

        var viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        var user = viewModel.getUserSeleccionada()

        binding.editTextId.setText(user.id.toString())
        binding.editTextNombre.setText(user.name.toString())
        binding.editTextFechaNacimiento.setText(user.birthdate.toString())


        binding.btnBorrar.setOnClickListener {

        }
        binding.btnGuardar.setOnClickListener {

        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetallesFragment().apply {}
    }
}