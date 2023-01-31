package com.vholodynskyi.assignment.presentation.contactslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vholodynskyi.assignment.util.Event
import org.koin.androidx.viewmodel.ext.android.viewModel

open class ContactsListFragment : Fragment() {

    private var sn: Snackbar? = null

    private fun onContactClicked(id: String) {
        findNavController()
            .navigate(ContactsListFragmentDirections.actionContactListToDetails(id))
    }

    private lateinit var composeView: ComposeView
    private val contactListViewModel by viewModel<ContactsListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        lifecycleScope.launchWhenStarted {
            contactListViewModel.eventFlow.collect { event ->
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

        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeView.setContent {

            val state by contactListViewModel.contacts.collectAsState()
            val isLoading by contactListViewModel.isLoading.collectAsState()
            ContactListScreen(
                contacts = state,
                isLoading = isLoading,
                navigateToContact = { onContactClicked(it) },
                refreshDBContacts = {contactListViewModel.refreshDbContacts()}
            )
        }
    }


}