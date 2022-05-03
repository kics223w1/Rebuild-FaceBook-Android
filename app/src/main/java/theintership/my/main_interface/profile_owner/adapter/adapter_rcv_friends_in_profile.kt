package theintership.my.main_interface.profile_owner.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_rcv_friends_in_profile.view.*
import theintership.my.R
import theintership.my.main_interface.profile_owner.model.friend_in_profile

class adapter_rcv_friends_in_profile(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<friend_in_profile>() {

        override fun areItemsTheSame(
            oldItem: friend_in_profile, newItem: friend_in_profile
        ): Boolean {
            return oldItem.name1 == newItem.name1
                    && oldItem.name2 == newItem.name2
                    && oldItem.name3 == newItem.name3
        }

        override fun areContentsTheSame(
            oldItem: friend_in_profile, newItem: friend_in_profile
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return adapter_rcv_friends_in_profile(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_rcv_friends_in_profile, parent, false
            ), interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is adapter_rcv_friends_in_profile -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<friend_in_profile>) {
        differ.submitList(list)
    }

    class adapter_rcv_friends_in_profile
    constructor(
        itemView: View, private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: friend_in_profile) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            if (!item.name1.isNullOrEmpty()) {
                item.name1.toString().also { itemView.item_rcv_friends_in_profile_name1.text = it }
                Glide.with(context).load(item.link_avatar1)
                    .placeholder(R.drawable.icon_loading_image).error(R.drawable.error_image)
                    .into(itemView.item_rcv_friends_in_profile_img1)
            }else{
                itemView.item_rcv_friends_in_profile_img1.visibility = View.INVISIBLE
                itemView.item_rcv_friends_in_profile_name1.visibility = View.INVISIBLE
            }
            if (!item.name2.isNullOrEmpty()) {
                item.name2.toString().also { itemView.item_rcv_friends_in_profile_name2.text = it }
                Glide.with(context).load(item.link_avatar2)
                    .placeholder(R.drawable.icon_loading_image).error(R.drawable.error_image)
                    .into(itemView.item_rcv_friends_in_profile_img2)
            }else{
                itemView.item_rcv_friends_in_profile_img2.visibility = View.INVISIBLE
                itemView.item_rcv_friends_in_profile_name2.visibility = View.INVISIBLE
            }
            if (!item.name3.isNullOrEmpty()) {
                item.name3.toString().also { itemView.item_rcv_friends_in_profile_name3.text = it }
                Glide.with(context).load(item.link_avatar3)
                    .placeholder(R.drawable.icon_loading_image).error(R.drawable.error_image)
                    .into(itemView.item_rcv_friends_in_profile_img3)
            }else{
                itemView.item_rcv_friends_in_profile_img3.visibility = View.INVISIBLE
                itemView.item_rcv_friends_in_profile_name3.visibility = View.INVISIBLE
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: friend_in_profile)
    }
}
