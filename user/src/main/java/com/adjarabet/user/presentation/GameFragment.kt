package com.adjarabet.user.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adjarabet.user.R
import com.adjarabet.user.domain.usecase.WordUseResult
import com.adjarabet.user.databinding.FragmentUserBinding
import com.adjarabet.user.utils.Result
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding

    private val viewModel = GameViewModel()
    private val wordsAdapter by lazy {
        WordsAdapter().apply {
            scrollOnItemAdded = {
                binding.nestedScrollView.smoothScrollBy(0, binding.root.height)
            }
        }
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
                editText.setText("")
                button.isEnabled = false
                handlePlayerInput(word)
            }
            recyclerView.adapter = wordsAdapter
        }
    }

    private fun initObservers() {
        viewModel.gameInitializedLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Success -> {
                    binding.button.isEnabled = true
                }
                else -> {
                    showDialog(getString(R.string.dialog_error_title), getString(R.string.dialog_error_connect))
                }
            }
        })
        viewModel.opponentWordLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Success -> {
                    handleOpponentsWord(it.data)
                }
                is Result.Error -> {
                    showDialog(getString(R.string.dialog_error_title), getString(R.string.dialog_error_send))
                }
            }
        })
    }

    private fun handlePlayerInput(input: String) {

        val playerLostDialogTitle = getString(R.string.dialog_title_loss)

        when (val result = viewModel.validatePlayerInput(input)) {
            is WordUseResult.Ok -> {
                wordsAdapter.addWordsForPlayer(input)
                viewModel.sendWordToOpponent(result.correctWord)
            }
            is WordUseResult.Repeated -> {
                showDialog(playerLostDialogTitle, getString(R.string.dialog_repeated_word, result.repeatedWord))
            }
            is WordUseResult.Invalid -> {
                showDialog(playerLostDialogTitle, getString(R.string.dialog_invalid_word, result.invalidWord))
            }
            is WordUseResult.Conflicting -> {
                showDialog(playerLostDialogTitle, getString(R.string.dialog_conflicting_word, result.playerWord, result.opponentsWord))
            }
            is WordUseResult.GaveUp -> {
                showDialog(playerLostDialogTitle, getString(R.string.dialog_player_gave_up))
            }
        }
    }

    private fun handleOpponentsWord(word: String) {

        val botLostDialogTitle = getString(R.string.dialog_title_win)

        when (viewModel.validateOpponentWord(word)) {
            is WordUseResult.Ok -> {
                val formattedWords = viewModel.formatWordsForAdapter()
                wordsAdapter.addWordsForOpponent(formattedWords)
                binding.button.isEnabled = true
            }
            is WordUseResult.Repeated -> {
                showDialog(botLostDialogTitle, getString(R.string.dialog_repeated_word, word))
            }
            is WordUseResult.Invalid -> {
                showDialog(botLostDialogTitle, getString(R.string.dialog_invalid_word, word))
            }
            is WordUseResult.GaveUp -> {
                showDialog(botLostDialogTitle, getString(R.string.dialog_bot_gave_up))
            }
            is WordUseResult.Conflicting -> {
                // opponent can not return a conflicting value
            }
        }
    }

    private fun showDialog(title: String, message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setNeutralButton(
                getString(R.string.dialog_button_text)
            ) { _, _ ->
                gameOver()
            }.show()
    }

    private fun gameOver() {
        viewModel.shutdownOpponent()
        requireActivity().finish()
    }

    companion object {
        fun newInstance(): GameFragment {
            return GameFragment()
        }
    }
}