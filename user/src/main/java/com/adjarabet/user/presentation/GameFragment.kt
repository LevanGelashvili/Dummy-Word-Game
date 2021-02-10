package com.adjarabet.user.presentation

import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
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
                viewModel.sendWordToOpponent(word)
                wordsAdapter.addPlayerWord(word)
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
                is Result.Loading -> {
                    wordsAdapter.addLoader()
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