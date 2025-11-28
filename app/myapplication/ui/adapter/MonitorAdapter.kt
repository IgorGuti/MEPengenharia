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
import com.example.myapplication.ui.repository.Medidor
import com.example.myapplication.ui.repository.ResponseMedidorEletrico

class MonitorAdapter(
    private var medidores: List<Medidor>,
    private val onClickListener: (Medidor) -> Unit,
    private val apiRepository: ApiRepository
) : RecyclerView.Adapter<MonitorAdapter.MedidorViewHolder>() {

    // ---------- Estados ----------
    private val estadosExpansao = mutableSetOf<Int>()           // guarda IDs expandido
    private val ultimosAbertos = mutableListOf<Int>()           // guarda histórico
    private val handler = Handler(Looper.getMainLooper())

    // ---------- Loop de Log ----------
    private val logRunnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, 15000)

            Log.d("MonitorAdapter", "Loop executando...")

            for (id in ultimosAbertos) {
                Log.d("ID Medidor", "Acompanhando ID: $id")
            }
        }
    }

    init {
        handler.post(logRunnable)
    }

    // ---------- ViewHolder ----------
    inner class MedidorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val buttonMedidor: Button = itemView.findViewById(R.id.idExtenderApartamento)
        private val layoutExpand: LinearLayout = itemView.findViewById(R.id.ExtenderEletricoLinearLayout)
        private val textViewMaisDetalhes: TextView = itemView.findViewById(R.id.maisOuMenosDetalhes)
        private val layoutMaisDetalhes: LinearLayout = itemView.findViewById(R.id.ExtenderMaisDetalhesLinearLayout)

        private val idMedidor: TextView = itemView.findViewById(R.id.idMedidor)

        private val voltzFaseA: TextView = itemView.findViewById(R.id.voltzFaseA)
        private val voltzFaseB: TextView = itemView.findViewById(R.id.voltzFaseB)
        private val voltzFaseC: TextView = itemView.findViewById(R.id.voltzFaseC)
        private val consumoMensalA: TextView = itemView.findViewById(R.id.consumoMensalFaseA)
        private val consumoMensalB: TextView = itemView.findViewById(R.id.consumoMensalFaseB)
        private val consumoMensalC: TextView = itemView.findViewById(R.id.consumoMensalFaseC)
        private val consumoTotal: TextView = itemView.findViewById(R.id.consumoMensalTotal)
        private val fatorPotenciaFaseA: TextView = itemView.findViewById(R.id.fatorPotenciaFaseA)
        private val fatorPotenciaFaseB: TextView = itemView.findViewById(R.id.fatorPotenciaFaseB)
        private val fatorPotenciaFaseC: TextView = itemView.findViewById(R.id.fatorPotenciaFaseC)
        private val fatorPotenciaTotal: TextView = itemView.findViewById(R.id.fatorPotenciaTotal)
        private val potenciaAtivaFaseA: TextView = itemView.findViewById(R.id.potenciaAtivaFaseA)
        private val potenciaAtivaFaseB: TextView = itemView.findViewById(R.id.potenciaAtivaFaseB)
        private val potenciaAtivaFaseC: TextView = itemView.findViewById(R.id.potenciaAtivaFaseC)
        private val potenciaAtivaTotal: TextView = itemView.findViewById(R.id.potenciaAtivaTotal)
        private val correnteFaseA: TextView = itemView.findViewById(R.id.correnteFaseA)
        private val correnteFaceB: TextView = itemView.findViewById(R.id.correnteFaseB)
        private val correnteFaceC: TextView = itemView.findViewById(R.id.correnteFaseC)
        private val correnteTotal: TextView = itemView.findViewById(R.id.correnteTotal)

        fun bind(medidor: Medidor) {
            val id = medidor.identificador

            buttonMedidor.text = medidor.nome
            idMedidor.text = "ID: $id"

            // Tela principal
            layoutExpand.visibility = if (estadosExpansao.contains(id)) View.VISIBLE else View.GONE

            // Reset do sublayout
            layoutMaisDetalhes.visibility = View.GONE

            // Eventos de clique
            buttonMedidor.setOnClickListener {
                toggleExpansao(id)
            }

            textViewMaisDetalhes.setOnClickListener {
                layoutMaisDetalhes.visibility =
                    if (layoutMaisDetalhes.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }

            // Abrir details de item
            itemView.setOnClickListener { onClickListener(medidor) }

            // Atualiza medições se houver
            medidor.respostaEletrico?.let {
                updateTextViews(it)
            }
        }

        private fun updateTextViews(medidor: ResponseMedidorEletrico) {
            voltzFaseA.text = "Voltz Fase A: ${medidor.voltzFaseA}"
            voltzFaseB.text = "Voltz Fase B: ${medidor.voltzFaseB}"
            voltzFaseC.text = "Voltz Fase C: ${medidor.voltzFaseC}"

            consumoMensalA.text = "Consumo A: ${medidor.consumoMensalA}"
            consumoMensalB.text = "Consumo B: ${medidor.consumoMensalB}"
            consumoMensalC.text = "Consumo C: ${medidor.consumoMensalC}"
            consumoTotal.text = "Total: ${medidor.consumoTotal}"

            fatorPotenciaFaseA.text = "FP A: ${medidor.fatorPotenciaFaseA}"
            fatorPotenciaFaseB.text = "FP B: ${medidor.fatorPotenciaFaseB}"
            fatorPotenciaFaseC.text = "FP C: ${medidor.fatorPotenciaFaseC}"
            fatorPotenciaTotal.text = "FP Total: ${medidor.fatorPotenciaTotal}"

            potenciaAtivaFaseA.text = "Potência A: ${medidor.potenciaAtivaFaseA}"
            potenciaAtivaFaseB.text = "Potência B: ${medidor.potenciaAtivaFaseB}"
            potenciaAtivaFaseC.text = "Potência C: ${medidor.potenciaAtivaFaseC}"
            potenciaAtivaTotal.text = "Potência Total: ${medidor.potenciaAtivaTotal}"

            correnteFaseA.text = "Corrente A: ${medidor.correnteFaseA}"
            correnteFaceB.text = "Corrente B: ${medidor.correnteFaseB}"
            correnteFaceC.text = "Corrente C: ${medidor.correnteFaseC}"
            correnteTotal.text = "Total: ${medidor.correnteTotal}"
        }
    }

    // ---------- Adapter ----------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedidorViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item_monitor_eletrico, parent, false)
        return MedidorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MedidorViewHolder, position: Int) {
        holder.bind(medidores[position])
    }

    override fun getItemCount(): Int = medidores.size

    // ---------- Expansão segura usando ID ----------
    private fun toggleExpansao(id: Int) {
        if (estadosExpansao.contains(id)) {
            estadosExpansao.remove(id)
        } else {
            estadosExpansao.add(id)
            registrarHistorico(id)
        }

        notifyDataSetChanged()  // muda poucas views, mas garante consistência
    }

    private fun registrarHistorico(id: Int) {
        if (ultimosAbertos.size >= 5) {
            ultimosAbertos.removeAt(0)
        }
        if (!ultimosAbertos.contains(id)) {
            ultimosAbertos.add(id)
        }

        Log.d("MonitorAdapter", "Ultimos abertos: $ultimosAbertos")
    }

    // ---------- Evita vazamento ----------
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        handler.removeCallbacks(logRunnable)
    }
}
