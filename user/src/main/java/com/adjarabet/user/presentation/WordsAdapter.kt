package com.adjarabet.user.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.adjarabet.user.databinding.CellOpponentBinding
import com.adjarabet.user.databinding.CellPlayerBinding

class WordsAdapter : RecyclerView.Adapter<WordsAdapter.ViewHolder>() {

    private val listItems = mutableListOf<WordListItem>()
    var onItemAdded: (() -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return when (listItems[position]) {
            is WordListItem.PlayerListItem -> VIEW_TYPE_PLAYER
            is WordListItem.OpponentListItem -> VIEW_TYPE_OPPONENT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = when (viewType) {
            VIEW_TYPE_PLAYER -> CellPlayerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            else -> CellOpponentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        }
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val listItem = listItems[position]) {
            is WordListItem.PlayerListItem -> {
                holder.bindPlayerCell(listItem.word, position + 1)
            }
            is WordListItem.OpponentListItem -> {
                holder.bindOpponentCell(listItem.word, position + 1)
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun clearWords() {
        listItems.clear()
        notifyDataSetChanged()
    }

    fun addWordsForPlayer(word: String) {
        addNewListItem(WordListItem.PlayerListItem(word))
    }

    fun addWordsForOpponent(word: String) {
        addNewListItem(WordListItem.OpponentListItem(word))
        onItemAdded?.invoke()
    }

    private fun addNewListItem(listItem: WordListItem) {
        listItems.add(listItem)
        notifyItemChanged(listItems.size - 1)
    }

    inner class ViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindPlayerCell(word: String, position: Int) {
            (binding as CellPlayerBinding).apply {
                textViewContent.text = word
                textViewPosition.text = "$position."
            }
        }

        fun bindOpponentCell(word: String, position: Int) {
            (binding as CellOpponentBinding).apply {
                textViewContent.text = word
                textViewPosition.text = ".$position"
            }
        }
    }

    sealed class WordListItem {
        data class PlayerListItem(val word: String) : WordListItem()
        data class OpponentListItem(val word: String) : WordListItem()
    }

    companion object {
        private const val VIEW_TYPE_PLAYER = 1
        private const val VIEW_TYPE_OPPONENT = 2
    }
}