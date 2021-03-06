package com.adjarabet.user.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.adjarabet.user.R
import com.adjarabet.user.databinding.FragmentUserBinding
import com.adjarabet.user.domain.usecase.WordValidation
import com.adjarabet.user.utils.Result
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class GameFragment : DaggerFragment() {

    private lateinit var binding: FragmentUserBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)
    }

    private val wordsAdapter by lazy {
        WordAdapter().apply {
            onItemAdded = { scrollToBottom() }
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
        initGameInitializedObserver()
        initOpponentWordObserver()
        initOpponentStateClearedObserver()
    }

    private fun initGameInitializedObserver() {
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
    }

    private fun initOpponentWordObserver() {
        viewModel.opponentWordLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Success -> {
                    handleOpponentsWord(it.data)
                    binding.button.isEnabled = true
                }
                is Result.Error -> {
                    showDialog(getString(R.string.dialog_error_title), getString(R.string.dialog_error_send))
                }
            }
        })
    }

    private fun initOpponentStateClearedObserver() {
        viewModel.clearOpponentStateLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Success -> {
                    wordsAdapter.clearWords()
                }
                is Result.Error -> {
                    showDialog(getString(R.string.dialog_error_title), getString(R.string.dialog_bot_clear_error))
                }
            }
        })
    }

    private fun handlePlayerInput(input: String) {

        val playerLostDialogTitle = getString(R.string.dialog_title_loss)

        when (val result = viewModel.validatePlayerInput(input)) {
            is WordValidation.Ok -> {
                wordsAdapter.addWordsForPlayer(input)
                viewModel.sendWordToOpponent(result.correctWord)
            }
            is WordValidation.Repeated -> {
                showDialog(playerLostDialogTitle, getString(R.string.dialog_repeated_word, result.repeatedWord))
            }
            is WordValidation.Invalid -> {
                showDialog(playerLostDialogTitle, getString(R.string.dialog_invalid_word, result.invalidWord))
            }
            is WordValidation.Conflicting -> {
                showDialog(playerLostDialogTitle, getString(
                        R.string.dialog_conflicting_word,
                        result.playerWord,
                        result.opponentsWord
                    ))
            }
            is WordValidation.GaveUp -> {
                showDialog(playerLostDialogTitle, getString(R.string.dialog_player_gave_up))
            }
        }
    }

    private fun handleOpponentsWord(word: String) {

        val botLostDialogTitle = getString(R.string.dialog_title_win)

        when (viewModel.validateOpponentWord(word)) {
            is WordValidation.Ok -> {
                val formattedWords = viewModel.formatWordsForAdapter()
                wordsAdapter.addWordsForOpponent(formattedWords)
            }
            is WordValidation.Repeated -> {
                showDialog(botLostDialogTitle, getString(R.string.dialog_repeated_word, word))
            }
            is WordValidation.Invalid -> {
                showDialog(botLostDialogTitle, getString(R.string.dialog_invalid_word, word))
            }
            is WordValidation.GaveUp -> {
                showDialog(botLostDialogTitle, getString(R.string.dialog_bot_gave_up))
            }
            is WordValidation.Conflicting -> {
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
                onGameRetried()
            }.show()
    }

    private fun onGameRetried() {
        viewModel.clearPlayerState()
        viewModel.clearOpponentState()
        binding.button.isEnabled = true
    }

    private fun scrollToBottom() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.nestedScrollView.smoothScrollBy(0, binding.root.height)
        }, SCROLL_TIMEOUT)
    }

    companion object {

        const val SCROLL_TIMEOUT = 200L

        fun newInstance(): GameFragment {
            return GameFragment()
        }
    }
}