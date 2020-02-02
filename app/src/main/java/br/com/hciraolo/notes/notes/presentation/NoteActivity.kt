package br.com.hciraolo.notes.notes.presentation

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.hciraolo.notes.R
import br.com.hciraolo.notes.databinding.ActivityNoteBinding
import br.com.hciraolo.notes.extensions.launchActivity
import br.com.hciraolo.notes.notes.presentation.data.Note
import br.com.hciraolo.notes.notes.presentation.data.NoteFormState
import br.com.hciraolo.notes.notes.presentation.data.NoteState
import br.com.hciraolo.notes.notes.repository.data.Priority
import br.com.hciraolo.notes.presentation.LoginActivity

class NoteActivity : AppCompatActivity() {

    lateinit var binding: ActivityNoteBinding
    lateinit var noteViewModel: NoteViewModel

    var id: Int = 0

    var priority: Priority = Priority.MEDIUM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_note)
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        binding.viewModel = noteViewModel

        setSupportActionBar(binding.tlbNotes)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp)

        binding.tietNotePriority.setText(R.string.menu_priority_medium)

        id = intent.getIntExtra("id", 0)

        if (id != 0) {
            noteViewModel.getNoteById(id)
        }

        binding.tietNotePriority.setOnClickListener {
            val popupMenu = PopupMenu(this, binding.tietNotePriority)
            popupMenu.menuInflater.inflate(R.menu.menu_priority, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.menu_priority_higher -> {
                        priority = Priority.HIGHEST
                        binding.tietNotePriority.setText(R.string.menu_priority_highest)
                    }
                    R.id.menu_priority_high -> {
                        priority = Priority.HIGH
                        binding.tietNotePriority.setText(R.string.menu_priority_high)
                    }
                    R.id.menu_priority_medium -> {
                        priority = Priority.MEDIUM
                        binding.tietNotePriority.setText(R.string.menu_priority_medium)
                    }
                    R.id.menu_priority_low -> {
                        priority = Priority.LOW
                        binding.tietNotePriority.setText(R.string.menu_priority_low)
                    }
                    R.id.menu_priority_lowest -> {
                        priority = Priority.LOWEST
                        binding.tietNotePriority.setText(R.string.menu_priority_lowest)
                    }
                }
                true
            }
            popupMenu.show()
        }

        noteViewModel.getListNoteStateLiveData().observe(this, Observer {
            when (it) {
                NoteState.LOADING -> {
                    binding.cnlLoadingLayout.visibility = View.VISIBLE
                }
                NoteState.GETID -> {
                    binding.cnlLoadingLayout.visibility = View.GONE
                    fillScreen(it.complement!!.note)
                }
                NoteState.CREATED -> {
                    binding.cnlLoadingLayout.visibility = View.GONE
                    AlertDialog.Builder(this)
                        .setTitle(R.string.success_title)
                        .setMessage(R.string.success_add_desc)
                        .setNeutralButton(R.string.action_ok) { _, _ ->
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                        .create()
                        .show()
                }
                NoteState.EDITED -> {
                    binding.cnlLoadingLayout.visibility = View.GONE
                    AlertDialog.Builder(this)
                        .setTitle(R.string.success_title)
                        .setMessage(R.string.success_edit_desc)
                        .setNeutralButton(R.string.action_ok) { _, _ ->
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                        .create()
                        .show()
                }
                NoteState.ERROR -> {
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

        noteViewModel.noteFormState.observe(this, Observer {
            binding.tilNoteTitle.error = ""
            binding.tilNoteShortDescription.error = ""
            binding.tilNoteDescription.error = ""

            when (it) {
                NoteFormState.TITLE_BLANK -> {
                    binding.tilNoteTitle.error = getString(R.string.invalid_title)
                }
                NoteFormState.SHORT_DESC_BLANK -> {
                    binding.tilNoteTitle.error = getString(R.string.invalid_short_desc)
                }
                NoteFormState.DESC_BLANK -> {
                    binding.tilNoteTitle.error = getString(R.string.invalid_description)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                setResult(Activity.RESULT_CANCELED)
            }

            R.id.menu_save -> {
                if (id == 0) {
                    noteViewModel.addNote(binding.tietNoteTitle.text.toString(), binding.tietNoteShortDescription.text.toString(), binding.tietNoteDescription.text.toString(), priority)
                } else {
                    noteViewModel.editNote(id, binding.tietNoteTitle.text.toString(), binding.tietNoteShortDescription.text.toString(), binding.tietNoteDescription.text.toString(), priority)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fillScreen(note: Note?) {
        binding.tietNoteTitle.setText(note!!.title)
        binding.tietNoteShortDescription.setText(note.shortDescription)
        binding.tietNoteDescription.setText(note.description)

        when(note.priority) {
            Priority.HIGHEST -> {
                priority = Priority.HIGHEST
                binding.tietNotePriority.setText(R.string.menu_priority_highest)
            }
            Priority.HIGH -> {
                priority = Priority.HIGH
                binding.tietNotePriority.setText(R.string.menu_priority_high)
            }
            Priority.MEDIUM -> {
                priority = Priority.MEDIUM
                binding.tietNotePriority.setText(R.string.menu_priority_medium)
            }
            Priority.LOW -> {
                priority = Priority.LOW
                binding.tietNotePriority.setText(R.string.menu_priority_low)
            }
            Priority.LOWEST -> {
                priority = Priority.LOWEST
                binding.tietNotePriority.setText(R.string.menu_priority_lowest)
            }
        }
    }
}
