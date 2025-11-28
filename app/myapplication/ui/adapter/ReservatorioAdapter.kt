package com.example.myapplication.ui.adapter

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.repository.ApiRepository
import com.example.myapplication.ui.repository.Reservatorio
import com.example.myapplication.ui.repository.ReservatorioResponse

class ReservatorioAdapter(
    private var reservatorioVariavel: List<Reservatorio>,
    private val onClickListener: (Reservatorio) -> Unit,
    private val apiRepository: ApiRepository
) : RecyclerView.Adapter<ReservatorioAdapter.ReservatorioViewHolder>() {

    private val expandedPositions = mutableSetOf<Int>()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservatorioViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item_monitor_reservatorio, parent, false)
        return ReservatorioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReservatorioViewHolder, position: Int) {
        val reservatorio = reservatorioVariavel[position]
        holder.bind(reservatorio)

        val isExpanded = expandedPositions.contains(position)
        holder.expandedLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE

        if (isExpanded) {
            // Fetch and update data if the item is expanded
            apiRepository.getReservatorio("teste_testado", reservatorio.identificador.toString(),
                onSuccess = { response ->
                    // Use post to ensure UI updates on the main thread
                    holder.itemView.post {
                        // Verifica se a posição ainda é a mesma e o ViewHolder não foi reciclado
                        if (position == holder.adapterPosition) {
                            holder.updateResposta(response)
                        } else {
                            Log.d("ReservatorioAdapter", "O ViewHolder foi reciclado antes da atualização")
                        }
                    }
                },
                onFailure = { error ->
                    Log.e("ApiRepository", "Erro: ${error.message}")
                }
            )
        }
    }
    override fun getItemCount(): Int = reservatorioVariavel.size

    private fun logExpansion(position: Int, isExpanded: Boolean, nomeReservatorio: String, idReservatorio: String) {
        val state = if (isExpanded) "expanded" else "collapsed"
        Log.d("ReservatorioAdapter", "Item at position $position is $state. Nome: $nomeReservatorio, ID: $idReservatorio")

        apiRepository.getReservatorio("teste_testado", idReservatorio,
            onSuccess = { response ->
                Log.d("Resposta Servidor", "$response")

                // Log para depuração
                Log.d("ReservatorioAdapter", "Expanded positions: $expandedPositions")

                // Atualize o item da lista e notifique o RecyclerView
                if (expandedPositions.contains(position)) {
                    val updatedReservatorio = reservatorioVariavel[position].copy(resposta = response)
                    reservatorioVariavel = reservatorioVariavel.toMutableList().apply { this[position] = updatedReservatorio }
                    notifyItemChanged(position)
                }
            },
            onFailure = { error ->
                Log.e("ApiRepository", "Erro: ${error.message}")
            }
        )
    }
    private fun logAllExpansions() {
        for (position in reservatorioVariavel.indices) {
            val isExpanded = expandedPositions.contains(position)
            val reservatorio = reservatorioVariavel[position]
            logExpansion(position, isExpanded, reservatorio.nome, reservatorio.identificador.toString())
        }
    }

    inner class ReservatorioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val buttonReservatorio: Button = itemView.findViewById(R.id.buttonExtenderReservatorio)
        val expandedLayout: LinearLayout = itemView.findViewById(R.id.expandirReservatorioLinearLayout)
        val idReservatorio: TextView = itemView.findViewById(R.id.idReservatorio)
        private val volume: TextView = itemView.findViewById(R.id.volumeReservatorio)
        private val distancia: TextView = itemView.findViewById(R.id.distanciaReservatorio)
        private val nivel: TextView = itemView.findViewById(R.id.nivelReservatorio)
        private val horario: TextView = itemView.findViewById(R.id.horarioReservatorio)

        init {
            buttonReservatorio.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val isExpanded = expandedPositions.contains(position)
                    if (isExpanded) {
                        expandedPositions.remove(position)
                    } else {
                        expandedPositions.add(position)
                    }
                    notifyItemChanged(position)
                    val reservatorio = reservatorioVariavel[position]
                    logExpansion(position, !isExpanded, reservatorio.nome, reservatorio.identificador.toString())
                }
            }
        }
        fun bind(reservatorio: Reservatorio) {
            buttonReservatorio.text = reservatorio.nome
            idReservatorio.text = "ID: ${reservatorio.identificador}"
            itemView.setOnClickListener { onClickListener(reservatorio) }

            // Atualize com a resposta se disponível
            reservatorio.resposta?.let { updateResposta(it) }
        }

        fun updateResposta(resposta: ReservatorioResponse) {
            Log.d("atualizr tela", "Atualizando resposta: ${resposta.nivel}")
            volume.text = "Volume: ${resposta.volume}"
            distancia.text = "Distância: ${resposta.distancia}"
            horario.text = "Data: ${resposta.horario}"
            nivel.text = "Nível: ${resposta.nivel}"
        }
    }
}
