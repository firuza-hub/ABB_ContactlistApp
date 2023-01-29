package com.vholodynskyi.assignment.presentation.contactslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.vholodynskyi.assignment.databinding.FragmentContactsListBinding
import com.vholodynskyi.assignment.domain.model.ContactModel
import com.vholodynskyi.assignment.util.Event
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

open class ContactsListFragment : Fragment() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var sn: Snackbar? = null
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
    private val viewModel by viewModel<ContactsListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Creates a vertical Layout Manager


        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collect { event ->
                when(event){
                    is Event.ShowToaster -> {
                        Toast.makeText(
                            requireContext(),
                            event.message,
                            Toast.LENGTH_SHORT
                        ).show()}
                }
            }

        }
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

            sn?.dismiss()
            viewModel.refreshDbContacts()
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called
                // when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                val deletedContact: ContactModel? =
                    viewModel.contacts.value?.get(viewHolder.bindingAdapterPosition)

                if (deletedContact != null) {
                    // below line is to get the position
                    // of the item at that position.
                    val position = viewHolder.bindingAdapterPosition

                    // this method is called when item is swiped.
                    // below line is to remove item from our array list.
                    viewModel.deleteDbContact(deletedContact.id)

                    // below line is to display our snackbar with action.
                     sn = Snackbar.make(
                        binding!!.root,
                        "Deleted " + deletedContact.name,
                        Snackbar.LENGTH_LONG
                    ).setAction(
                            "Undo",
                    View.OnClickListener {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        //courseList.add(position, deletedContact)
                        viewModel.undoDeleteDbContact(deletedContact.id)
                        // below line is to notify item is
                        // added to our adapter class.
                        contactAdapter.notifyItemInserted(position)
                    })

                    sn?.show()
                }
            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView( binding!!.contactList)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}