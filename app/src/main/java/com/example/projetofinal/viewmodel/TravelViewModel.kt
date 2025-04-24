package com.example.projetofinal.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetofinal.data.Travel
import com.example.projetofinal.data.TravelRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TravelViewModel(private val repository: TravelRepository) : ViewModel() {
    private val apiKey = "AIzaSyBjhbhy9-4mB8sgzJ2hzuRk2ClDjRlWLS4"

    fun insertTravel(travel: Travel) {
        viewModelScope.launch {
            repository.insertTravel(travel)
        }
    }

    suspend fun getTravelsByUser(userId: Int): List<Travel> {
        return repository.getTravelsByUser(userId)
    }

    fun deleteTravel(travelId: Int) {
        viewModelScope.launch {
            repository.deleteTravel(travelId)
        }
    }

    fun updateTravel(travel: Travel) {
        viewModelScope.launch {
            repository.updateTravel(travel)
        }
    }

    suspend fun getTravelById(travelId: Int): Travel? {
        return repository.getTravelById(travelId)
    }

    suspend fun gerarRoteiroGemini(travel: Travel): String = withContext(Dispatchers.IO) {
        try {
            val generativeModel = GenerativeModel(
                modelName = "models/gemini-1.5-pro-latest",
                apiKey = apiKey
            )

            val promptText = """
                Crie um roteiro de viagem para o destino: ${travel.destination}.
                Tipo de viagem: ${travel.travelType}.
                Data: de ${travel.startDate} até ${travel.endDate}.
                Orçamento disponível: R$${travel.budget}.
                Dê sugestões realistas, em português, considerando o perfil da viagem.
            """.trimIndent()

            val response = generativeModel.generateContent(content { text(promptText) })

            return@withContext response.text ?: "Não foi possível gerar o roteiro no momento."
        } catch (e: Exception) {
            Log.e("TravelViewModel", "Erro ao gerar roteiro", e) // Log the full error
            return@withContext "Erro ao gerar roteiro: ${e.message ?: "erro desconhecido"}"
        }
    }
}