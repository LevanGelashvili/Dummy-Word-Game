package com.adjarabet.user.presentation

import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.adjarabet.user.R
import com.adjarabet.user.databinding.FragmentUserBinding
import com.adjarabet.user.domain.entities.Word
import com.adjarabet.user.utils.Result

class GameFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding

    private val viewModel = GameViewModel()
    private val wordsAdapter by lazy {
        WordsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)

        initViews()
        initObservers()

        return binding.root
    }

    private fun initViews() {
        binding.apply {

            button.setOnClickListener {
                val word = binding.editText.text.toString()

                if (viewModel.isValidWord(word)) {
                    viewModel.sendWordToOpponent(word)
                    wordsAdapter.addPlayerWord(Word(word))

                } else {
                    //display alert
                }
            }

            recyclerView.apply {
                adapter = wordsAdapter
                addItemDecoration(
                    DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
                )
            }
        }
    }

    private fun initObservers() {
        viewModel.gameSuccessfullyInitedLiveData.observe(viewLifecycleOwner, { gameResult ->
            if (gameResult) {
                Toast.makeText(requireContext(), "YAY", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "sadj", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.opponentWordLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Success -> {
                    wordsAdapter.addOpponentWord(Word(it.data))
                }
                is Result.Error -> {
                    // handle error
                }
                is Result.Loading -> {
                    wordsAdapter.addLoader()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        //stopBotService()
    }

    companion object {
        fun newInstance(): GameFragment {
            return GameFragment()
        }
    }
}