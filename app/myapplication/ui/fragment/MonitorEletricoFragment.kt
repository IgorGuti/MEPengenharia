package com.example.myapplication.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.adapter.MonitorAdapter
import com.example.myapplication.ui.repository.ApiRepository
import com.example.myapplication.ui.repository.UserViewModel

class MonitorEletricoFragment : Fragment() {

    private lateinit var recyclerViewMonitorEletrico: RecyclerView
    private lateinit var adapter: MonitorAdapter
    private lateinit var userViewModel: UserViewModel
    private lateinit var apiRepository: ApiRepository // Adicione esta linha

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_monitor_eletrico, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewMonitorEletrico = view.findViewById(R.id.lista_relogios_recyclerview)
            ?: throw IllegalStateException("RecyclerView não encontrado")

        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        // Inicialize o ApiRepository
        apiRepository = ApiRepository(requireContext()) // ou obtenha a instância de acordo com sua implementação

        recyclerViewMonitorEletrico.layoutManager = LinearLayoutManager(requireContext())

        adapter = MonitorAdapter(emptyList(), { medidor ->
            // Implementar lógica de clique aqui, se necessário
        }, apiRepository)

        recyclerViewMonitorEletrico.adapter = adapter

        userViewModel.medidores.observe(viewLifecycleOwner) { medidores ->
            adapter = MonitorAdapter(medidores, { medidor ->
                // Implementar lógica de clique aqui, se necessário
            }, apiRepository)
            recyclerViewMonitorEletrico.adapter = adapter
        }
    }
}
