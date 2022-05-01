package theintership.my.main_interface.notifications.adapter

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_rcv_notification.view.*
import theintership.my.R
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.main_interface.notifications.model.Notifications

class adapter_rcv_earlier(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Notifications>() {

        override fun areItemsTheSame(oldItem: Notifications, newItem: Notifications): Boolean {
            return oldItem.day_and_time == newItem.day_and_time
        }

        override fun areContentsTheSame(oldItem: Notifications, newItem: Notifications): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return adapter_rcv_earlier(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_rcv_notification,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is adapter_rcv_earlier -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun getCurrentList() : MutableList<Notifications>{
        return differ.currentList.toMutableList()
    }



    fun submitList(list: List<Notifications>) {
        differ.submitList(list)
    }

    class adapter_rcv_earlier
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Notifications) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.rcv_item_notifications_content.setText(item.black_out_content())
            itemView.rcv_item_notifications_day_and_time.text = item.day_and_time.toString()
            if (item.is_readed) {
                itemView.rcv_item_notifications_layout.background =
                    resources.getDrawable(R.color.background_notification_readed, null)
            }

            val background_icon_color = setup_background_icon(item.icon.toString())
            itemView.rcv_item_notifications_layout_icon.setCardBackgroundColor(
                ContextCompat.getColor(
                    context.applicationContext,
                    background_icon_color
                )
            )

            Glide.with(context).load(item.link_avatar_person)
                .placeholder(R.drawable.icon_loading_image).error(R.drawable.error_image)
                .dontAnimate().into(itemView.rcv_item_notifications_avatar)

            Glide.with(context).load(setup_icon(item.icon.toString())).error(R.drawable.error_image)
                .dontAnimate().into(itemView.rcv_item_notifications_icon)

            itemView.rcv_item_notifications_menu.setOnClickListener {
                println("debug vao menu")
            }
        }

        fun setup_icon(icon: String): Int {
            when (icon) {
                "like" -> return R.drawable.icon_like
                "love" -> return R.drawable.icon_love
                "haha" -> return R.drawable.icon_haha
                "sad" -> return R.drawable.icon_sad
                "love love" -> return R.drawable.icon_love_love
                "reply" -> return R.drawable.icon_reply
            }
            return R.drawable.icon_like
        }

        fun setup_background_icon(icon: String): Int {
            when (icon) {
                "like" -> return R.color.background_icon_like
                "love" -> return R.color.background_icon_love
                "reply" -> return R.color.background_icon_reply
            }
            return R.color.light_blue
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Notifications)
    }
}
