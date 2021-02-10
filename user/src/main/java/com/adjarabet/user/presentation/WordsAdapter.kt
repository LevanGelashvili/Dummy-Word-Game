package com.adjarabet.user.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.adjarabet.user.databinding.CellLoadingBinding
import com.adjarabet.user.databinding.CellOpponentBinding
import com.adjarabet.user.databinding.CellPlayerBinding

class WordsAdapter : RecyclerView.Adapter<WordsAdapter.ViewHolder>() {

    private val listItems = mutableListOf<WordListItem>()

    override fun getItemViewType(position: Int): Int {
        return when (listItems[position]) {
            is WordListItem.PlayerListItem -> VIEW_TYPE_PLAYER
            is WordListItem.OpponentListItem -> VIEW_TYPE_OPPONENT
            is WordListItem.LoadingItem -> VIEW_TYPE_LOADER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = when (viewType) {
            VIEW_TYPE_PLAYER -> CellPlayerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            VIEW_TYPE_OPPONENT -> CellOpponentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            else -> CellLoadingBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        }
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val listItem = listItems[position]) {
            is WordListItem.PlayerListItem -> {
                holder.bindPlayerCell(listItem.word)
            }
            is WordListItem.OpponentListItem -> {
                holder.bindOpponentCell(listItem.word)
            }
            WordListItem.LoadingItem -> {
                holder.bindLoaderCell()
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun addPlayerWord(word: String) {
        addNewListItem(WordListItem.PlayerListItem(word))
    }

    fun addOpponentWord(word: String) {
        if (listItems.isNotEmpty() && listItems.last() is WordListItem.LoadingItem) {
            listItems.removeLast()
        }
        addNewListItem(WordListItem.OpponentListItem(word))
    }

    fun addLoader() {
        addNewListItem(WordListItem.LoadingItem)
    }

    private fun addNewListItem(listItem: WordListItem) {
        listItems.add(listItem)
        notifyItemChanged(listItems.size - 1)
    }

    inner class ViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindPlayerCell(word: String) {
            (binding as CellPlayerBinding).apply {
                textView.text = word
            }
        }

        fun bindOpponentCell(word: String) {
            (binding as CellOpponentBinding).apply {
                textView.text = word
            }
        }

        fun bindLoaderCell() {
        }
    }

    sealed class WordListItem {
        data class PlayerListItem(val word: String) : WordListItem()
        data class OpponentListItem(val word: String) : WordListItem()
        object LoadingItem : WordListItem()
    }

    companion object {
        private const val VIEW_TYPE_PLAYER = 1
        private const val VIEW_TYPE_OPPONENT = 2
        private const val VIEW_TYPE_LOADER = 3
    }
}