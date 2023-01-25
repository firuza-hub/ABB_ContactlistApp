package com.vholodynskyi.assignment.presentation.contactslist

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vholodynskyi.assignment.databinding.ItemContactListBinding
import com.vholodynskyi.assignment.domain.model.ContactModel

class ContactAdapter (
    private val context: Activity,
    private val onItemClicked: ItemClick
) : RecyclerView.Adapter<ViewHolder>() {

    var items: List<ContactModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(
            ItemContactListBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            tvName.text = item.name
            tvEmail.text = item.email
            if(!item.picture.isNullOrEmpty())
                setImage(ivPicture, item.picture)

            root.setOnClickListener {
                onItemClicked(item.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
fun setImage(view:ImageView,imageUrl:String ) {
    Glide.with(view.context)
        .load(imageUrl)
        .apply(RequestOptions().fitCenter().circleCrop())
        .into(view)
}
class ViewHolder (val binding: ItemContactListBinding) : RecyclerView.ViewHolder(binding.root)

typealias ItemClick = (String) -> Unit