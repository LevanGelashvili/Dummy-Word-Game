package com.adjarabet.user.presentation

import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adjarabet.user.databinding.FragmentUserBinding
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
                wordsAdapter.addPlayerWord(word)
                viewModel.sendWordToOpponent(word)
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

                }
            }
        })

        viewModel.opponentWordLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Success -> {
                    wordsAdapter.addOpponentWord(it.data)
                }
                is Result.Error -> {
                    // handle error
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        //TODO: stopBotService()
    }

    companion object {
        fun newInstance(): GameFragment {
            return GameFragment()
        }
    }
}