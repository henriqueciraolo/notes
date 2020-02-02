package br.com.hciraolo.notes.notes.presentation.data

import br.com.hciraolo.notes.notes.repository.data.Priority


data class ListNote(
    val id: Int,
    val title: String,
    val shortDescription: String,
    val priority: Priority
)