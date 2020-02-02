package br.com.hciraolo.notes.notes.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import br.com.hciraolo.notes.R
import br.com.hciraolo.notes.databinding.ItemNoteBinding
import br.com.hciraolo.notes.notes.presentation.data.ListNote

class ListNotesRecyclerViewAdapter(val onItemTouchedListener: ListNotesViewHolder.OnItemTouchedListener, val onItemDeletedListener: OnItemDeletedListener) : RecyclerView.Adapter<ListNotesViewHolder>() {

    val dataset: MutableList<ListNote> = ArrayList()
    lateinit var binding: ItemNoteBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNotesViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_note, parent, false)
        binding.viewHolder =
            ListNotesViewHolder(
                binding.root,
                binding
            )
        return binding.viewHolder as ListNotesViewHolder
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ListNotesViewHolder, position: Int) {
        holder.onBind(dataset[position], onItemTouchedListener)
    }

    fun updateDataset(list: List<ListNote>) {
        dataset.clear()
        dataset.addAll(list)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        dataset.remove(dataset[position])
        notifyItemRemoved(position)
        onItemDeletedListener.onItemDeleted(dataset[position])
    }

    interface OnItemDeletedListener {
        fun onItemDeleted(item: ListNote)
    }
}

class ListNotesViewHolder(view: View, val binding: ItemNoteBinding) : RecyclerView.ViewHolder(view) {
    fun onBind(listNote: ListNote, onItemTouchedListener: OnItemTouchedListener) {
        binding.txvTitle.text = listNote.title
        binding.txvShortDescription.text = listNote.shortDescription
        binding.txvPriority.text = listNote.priority.code.toString()

        binding.root.setOnClickListener {
            onItemTouchedListener.onItemTouched(listNote)
        }
    }

    interface OnItemTouchedListener {
        fun onItemTouched(item: ListNote)
    }
}

class NoteItemTouchHelper(val context: Context, val mAdapter: ListNotesRecyclerViewAdapter) : ItemTouchHelper.SimpleCallback(0, (ItemTouchHelper.LEFT)) {
    val icon = ContextCompat.getDrawable(context, R.drawable.ic_delete_white_24dp)
    val background = ColorDrawable(Color.RED)
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        mAdapter.deleteItem(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20

        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight

        if (dX > 0) {
            val iconLeft = itemView.left + iconMargin + icon.intrinsicWidth
            val iconRight = itemView.left + iconMargin

            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom)
        } else if (dX < 0) {
            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin

            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            background.setBounds(itemView.right + dX.toInt() - backgroundCornerOffset, itemView.top, itemView.right, itemView.bottom)
        } else {
            background.setBounds(0,0,0,0)
        }

        background.draw(c)
        icon.draw(c)
    }
}