package br.com.hciraolo.notes.notes.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.hciraolo.notes.R
import br.com.hciraolo.notes.databinding.ActivityListNotesBinding
import br.com.hciraolo.notes.extensions.launchActivity
import br.com.hciraolo.notes.notes.presentation.data.ListNote
import br.com.hciraolo.notes.notes.presentation.data.ListNoteState
import br.com.hciraolo.notes.presentation.LoginActivity

class ListNotesActivity : AppCompatActivity(), ListNotesViewHolder.OnItemTouchedListener,
    ListNotesRecyclerViewAdapter.OnItemDeletedListener {

    lateinit var binding: ActivityListNotesBinding
    lateinit var listNotesViewModel: ListNotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_notes)
        listNotesViewModel = ViewModelProvider(this).get(ListNotesViewModel::class.java)
        binding.viewModel = listNotesViewModel

        setSupportActionBar(binding.tlbNotes)

        initRecyclerView()

        listNotesViewModel.getListNoteStateLiveData().observe(this, Observer {
            when (it) {
                ListNoteState.LOADING -> {
                    binding.cnlLoadingLayout.visibility = View.VISIBLE
                }
                ListNoteState.LIST -> {
                    binding.cnlLoadingLayout.visibility = View.GONE
                    (binding.rcvNotesList.adapter as ListNotesRecyclerViewAdapter).updateDataset(it.complement!!.data!!)
                }
                ListNoteState.DELETE -> {
                    binding.cnlLoadingLayout.visibility = View.GONE
                    Toast.makeText(this, "Nota deletada com sucesso!", Toast.LENGTH_SHORT).show()
                }
                ListNoteState.DELETE_ALL -> {
                    Toast.makeText(this, "Todas notas foram deletadas com sucesso!", Toast.LENGTH_SHORT).show()
                    listNotesViewModel.getAllNotes()
                }
                ListNoteState.ERROR -> {
                    binding.cnlLoadingLayout.visibility = View.GONE
                    AlertDialog.Builder(this)
                        .setTitle(R.string.error_failed_title)
                        .setMessage(R.string.load_notes_failed)
                        .setNeutralButton(R.string.action_ok) { _, _ ->
                            finish()
                        }
                        .create()
                        .show()

                }
            }
        })

        listNotesViewModel.getAllNotes()

    }

    private fun initRecyclerView() {
        binding.rcvNotesList.layoutManager = LinearLayoutManager(this)
        binding.rcvNotesList.setHasFixedSize(true)
        binding.rcvNotesList.adapter = ListNotesRecyclerViewAdapter(this, this)
        ItemTouchHelper(NoteItemTouchHelper(this,
            binding.rcvNotesList.adapter as ListNotesRecyclerViewAdapter
        )).attachToRecyclerView(binding.rcvNotesList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list_notes, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.warning_delete_all_title)
                    .setMessage(R.string.warning_delete_all)
                    .setPositiveButton(R.string.action_yes) { _, _ ->
                        listNotesViewModel.deleteAll()
                    }
                    .setNegativeButton(R.string.action_no, null)
                    .create()
                    .show()
            }

            R.id.menu_logout -> {
                launchActivity<LoginActivity> {
                    this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemTouched(item: ListNote) {
        Toast.makeText(this, "ITEM TOCADO!", Toast.LENGTH_SHORT).show()
    }

    override fun onItemDeleted(item: ListNote) {
        listNotesViewModel.deleteNote(item)
    }
}
