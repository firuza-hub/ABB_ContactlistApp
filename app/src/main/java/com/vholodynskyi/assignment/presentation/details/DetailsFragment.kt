package com.vholodynskyi.assignment.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vholodynskyi.assignment.databinding.FragmentDetailsBinding
import com.vholodynskyi.assignment.util.Event
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel


open class DetailsFragment : Fragment() {
    private  var binding: FragmentDetailsBinding? = null
    private val args: DetailsFragmentArgs by navArgs()
    private val detailsViewModel by viewModel<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailsViewModel.id = args.id
        detailsViewModel.getData()

        lifecycleScope.launchWhenStarted {
            detailsViewModel.eventFlow.collect { event ->
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

        return FragmentDetailsBinding.inflate(layoutInflater, container, false)
            .also {
                binding = it
                binding!!.btnBack.setOnClickListener{
                    findNavController().navigateUp()
                }


                binding!!.btnDelete.setOnClickListener {
                    detailsViewModel.delete()
                    findNavController().navigateUp()
                }


                lifecycleScope.launchWhenStarted {
                    detailsViewModel.contact.collect { data ->
                        binding!!.tvName.text = data.name
                        binding!!.tvEmail.text = data.email
                        if (data.picture != null) {
                            setImage(binding!!.ivAvatarImage, data.picture)
                        }
                    }
                }
            }
            .root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setImage(view: ImageView, imageUrl: String) {
        Glide.with(view.context)
            .load(imageUrl)
            .apply(RequestOptions().fitCenter().centerCrop())
            .into(view)
    }
}