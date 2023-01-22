package com.vholodynskyi.assignment.ui.contactslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vholodynskyi.assignment.databinding.FragmentContactsListBinding
import com.vholodynskyi.assignment.di.GlobalFactory

open class ContactsListFragment : Fragment() {

    private lateinit var swipeRefreshLayout:SwipeRefreshLayout
    private val contactAdapter: ContactAdapter by lazy {
        ContactAdapter(
            requireActivity(),
            this::onContactClicked
        )
    }

    private fun onContactClicked(id: String) {
        findNavController()
            .navigate(ContactsListFragmentDirections.actionContactListToDetails(id))
    }

    private var binding: FragmentContactsListBinding? = null
    private val viewModel by viewModels<ContactsListViewModel> { GlobalFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Creates a vertical Layout Manager
        return FragmentContactsListBinding.inflate(layoutInflater, container, false)
            .apply {
                contactList.layoutManager = LinearLayoutManager(context)
                contactList.adapter = contactAdapter
                viewModel.contacts.observe(viewLifecycleOwner) {
                    contactAdapter.items = it
                }
            }
            .also {
                binding = it
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = binding?.root!!
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            viewModel.refreshDbContacts()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}