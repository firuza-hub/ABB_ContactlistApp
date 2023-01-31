package com.vholodynskyi.assignment.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vholodynskyi.assignment.util.Event
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


open class DetailsFragment : Fragment() {
    private val args: DetailsFragmentArgs by navArgs()
    private val detailsViewModel by viewModel<DetailsViewModel>()
    private lateinit var composeView: ComposeView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailsViewModel.id = args.id
        detailsViewModel.getData()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailsViewModel.eventFlow.collect { event ->
                    when (event) {
                        is Event.ShowToaster -> {
                            Toast.makeText(
                                requireContext(),
                                event.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeView.setContent {

            val state by detailsViewModel.contact.collectAsState()

            DetailsScreen(
                state = state,
                onDeleteClick =
                {
                    detailsViewModel.delete()
                    findNavController().navigateUp()
                },
                onBackClick =
                {
                    findNavController().navigateUp()
                }
            )
        }
    }

}