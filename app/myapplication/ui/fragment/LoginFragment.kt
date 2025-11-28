package com.example.myapplication.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.ui.repository.ApiRepository
import com.example.myapplication.ui.repository.UserViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var apiRepository: ApiRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        apiRepository = ApiRepository(requireContext())

        // Configurar ação de clique para o botão "Login"
        binding.logando.setOnClickListener {
            val username = binding.loginUsuario.text.toString()
            val password = binding.loginSenha.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            apiRepository.login(username, password,
                onSuccess = { loginResponse, reservatorios, medidores, hidrometros ->
                    // Atualiza o ViewModel com a resposta do login e as listas
                    userViewModel.setLoginResponse(loginResponse)
                    userViewModel.setReservatorios(reservatorios)
                    userViewModel.setMedidores(medidores)
                    userViewModel.setHidrometros(hidrometros)
                    // Navegar para o próximo fragmento após o login bem-sucedido
                    findNavController().navigate(R.id.nav_monitorEletrico)
                },
                onFailure = { throwable ->
                    // Mostrar mensagem de erro para o usuário
                    Toast.makeText(requireContext(), "Falha na requisição: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
