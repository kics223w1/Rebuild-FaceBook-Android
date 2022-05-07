package theintership.my.main_interface.friends.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_rcv_friends_request.view.*
import theintership.my.R
import theintership.my.all_class.MyMethod.Companion.count_days
import theintership.my.all_class.MyMethod.Companion.count_hour
import theintership.my.all_class.MyMethod.Companion.set_today
import theintership.my.main_interface.friends.model.Friends

class adapter_rcv_friends_may_know(private val interaction: Interaction2? = null ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Friends>() {

        override fun areItemsTheSame(oldItem: Friends, newItem: Friends): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Friends, newItem: Friends): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return adapter_rcv_friends_may_know(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_rcv_friends_request,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is adapter_rcv_friends_may_know -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Friends>) {
        differ.submitList(list)
    }

    class adapter_rcv_friends_may_know
    constructor(
        itemView: View,
        private val interaction: Interaction2?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Friends) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelectedFrMayKnow(adapterPosition, item)
            }
            Glide.with(context.applicationContext).load(item.link_avatar)
                .placeholder(R.drawable.icon_loading_image).error(R.drawable.error_image)
                .into(itemView.rcv_item_friends_request_image)
            itemView.rcv_item_friends_request_name.text = item.name
            itemView.rcv_item_friends_request_time.text = "1812d"
        }

        private fun set_time(day : String , hour : String): String{
            val today = set_today()
            val days = count_days(day, today)
            if (days > 0){
                val s = days.toString()
                return s +"d"
            }
            val hours = count_hour(hour).toString()
            return hours + "h"
        }

    }

    interface Interaction2 {
        fun onItemSelectedFrMayKnow(position: Int, item: Friends)
    }


}
