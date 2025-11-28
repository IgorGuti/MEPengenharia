package com.example.myapplication.ui.repository

data class Reservatorio(
    val identificador: Int,
    val nome: String,
    val resposta: ReservatorioResponse? = null // Adiciona um campo para armazenar a resposta
)
data class Medidor(
    val identificador: Int,
    val nome: String,
    val respostaEletrico: ResponseMedidorEletrico? = null
)
data class Hidrometro(val identificador: Int, val nome: String)
data class Login(
    val username: String,
    val password: String
)
data class LoginResponse(
    val resultado: String,
    val url: String,
    val qntReservatorio: Int,
    val reservatoriosNomes: String,
    val qntMedidor: Int,
    val medidoresNomes: String,
    val qntHidromentro: Int,
    val hidrometrosNomes: String
)
data class ReservatorioResponse(
    val volume: String,
    val distancia: String,
    val nivel: String,
    val horario: String
)
data class ResponseMedidorEletrico (
    val correnteTotal: String,
    val correnteFaseC: String,
    val correnteFaseB: String,
    val correnteFaseA: String,
    val potenciaAtivaTotal: String,
    val potenciaAtivaFaseC: String,
    val potenciaAtivaFaseB: String,
    val potenciaAtivaFaseA: String,
    val fatorPotenciaTotal: String,
    val fatorPotenciaFaseC: String,
    val fatorPotenciaFaseB: String,
    val fatorPotenciaFaseA: String,
    val consumoTotal: String,
    val consumoMensalC: String,
    val consumoMensalB: String,
    val consumoMensalA: String,
    val voltzFaseC: String,
    val voltzFaseB: String,
    val voltzFaseA: String
)