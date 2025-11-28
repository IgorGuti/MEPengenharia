package com.example.myapplication.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        val login_inicial = view.findViewById<Button>(R.id.login_inicial)
        val hiperlinkInstagram = view.findViewById<ImageButton>(R.id.buttonInstagram)
        val hiperlinkWhatsapp = view.findViewById<ImageButton>(R.id.buttonWhatsapp)
        val hiperlinkEmail = view.findViewById<ImageButton>(R.id.buttonEmail)

        // Configurar ação de clique para o botão "Login Inicial"
        login_inicial.setOnClickListener {
            // Navegar para o fragmento de login
            findNavController().navigate(R.id.nav_login)
        }

        // Configurar ação de clique para o botão "Instagram"
        hiperlinkInstagram.setOnClickListener {
            // Adicione aqui o código para abrir o Instagram
            // Substitua "sua_pagina_aqui" pela página desejada
            val instagramUri = Uri.parse("https://www.instagram.com/mepengenharia")
            val intent = Intent(Intent.ACTION_VIEW, instagramUri)
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }
        }
        // Configurar ação de clique para o botão "Instagram"
        hiperlinkWhatsapp.setOnClickListener {
            // Adicione aqui o código para abrir o Instagram
            // Substitua "sua_pagina_aqui" pela página desejada
            val whatsappUri = Uri.parse("https://api.whatsapp.com/send/?phone=556132565996&text&type=phone_number&app_absent=0")
            val intent = Intent(Intent.ACTION_VIEW, whatsappUri)
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }
        }
        hiperlinkEmail.setOnClickListener {
            // Adicione aqui o código para abrir o Instagram
            // Substitua "sua_pagina_aqui" pela página desejada
            val gmailUri = Uri.parse("https://mep.eng.br/contato/#email")
            val intent = Intent(Intent.ACTION_VIEW, gmailUri)
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
