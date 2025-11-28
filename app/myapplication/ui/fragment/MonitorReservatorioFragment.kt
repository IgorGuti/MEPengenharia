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
import com.example.myapplication.ui.adapter.ReservatorioAdapter
import com.example.myapplication.ui.repository.ApiRepository
import com.example.myapplication.ui.repository.UserViewModel
class MonitorReservatorioFragment : Fragment() {

    private lateinit var recyclerViewReservatorio: RecyclerView
    private lateinit var adapter: ReservatorioAdapter
    private lateinit var userViewModel: UserViewModel
    private lateinit var apiRepository: ApiRepository // Adicione esta linha

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_monitor_reservatorio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewReservatorio = view.findViewById(R.id.lista_reservatorios_recyclerview)
            ?: throw IllegalStateException("RecyclerView não encontrado")

        // Crie a instância de ApiRepository
        apiRepository = ApiRepository(requireContext())

        // Inicialize o ViewModel
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        // Configure o RecyclerView e o Adapter
        recyclerViewReservatorio.layoutManager = LinearLayoutManager(requireContext())
        adapter = ReservatorioAdapter(emptyList(), { reservatorio ->
            // Aqui você pode definir o que acontece quando um item é clicado
        }, apiRepository) // Passe o apiRepository para o Adapter
        recyclerViewReservatorio.adapter = adapter

        // Observe o LiveData do ViewModel
        userViewModel.reservatorios.observe(viewLifecycleOwner) { reservatorioList ->
            // Atualize o Adapter com a nova lista
            adapter = ReservatorioAdapter(reservatorioList, { reservatorio ->
                // Aqui você pode definir o que acontece quando um item é clicado
            }, apiRepository) // Passe o apiRepository para o Adapter
            recyclerViewReservatorio.adapter = adapter
        }
    }
}
