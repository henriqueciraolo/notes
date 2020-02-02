package br.com.hciraolo.notes.notes.presentation.data

import br.com.hciraolo.notes.notes.repository.data.Priority

data class ListNote (val id: Int, val title: String, val shortDescription: String, val priority: Priority)
data class Note (val id: Int, val title: String, val shortDescription: String, val description: String, val priority: Priority)