package theintership.my.main_interface.friends.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_rcv_friends_request.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import theintership.my.R
import theintership.my.all_class.MyMethod.Companion.count_days
import theintership.my.all_class.MyMethod.Companion.count_hour
import theintership.my.all_class.MyMethod.Companion.set_today
import theintership.my.all_class.MyMethod.Companion.showToastLong
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.main_interface.friends.model.Friends
import kotlin.coroutines.coroutineContext

class adapter_rcv_friends_request(private val interaction: Interaction? = null, context: Context , account_ref: String) :
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
    private val context = context
    private val account_ref = account_ref


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return adapter_rcv_friends_request(
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
            is adapter_rcv_friends_request -> {
                val fr = differ.currentList.get(position)
                holder.bind(fr)
                holder.itemView.rcv_item_friends_request_btn_confirm.setOnClickListener {
                    confirm_fr(account_ref, fr)
                }
                holder.itemView.rcv_item_friends_request_btn_delete.setOnClickListener {
                    delete_fr(account_ref, fr)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Friends>) {
        differ.submitList(list)
    }

    fun remove_ele(fr: Friends) {
        val list = differ.currentList.toMutableList()
        list.remove(fr)
        submitList(list)
    }

    class adapter_rcv_friends_request
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Friends) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelectedFrRequest(adapterPosition, item)
            }
            Glide.with(context.applicationContext).load(item.link_avatar)
                .placeholder(R.drawable.icon_loading_image).error(R.drawable.error_image)
                .into(itemView.rcv_item_friends_request_image)
            itemView.rcv_item_friends_request_name.text = item.name
            itemView.rcv_item_friends_request_time.text =
                set_time(item.day.toString(), item.hour.toString())

        }

        private fun set_time(day: String, hour: String): String {
            val today = set_today()
            val days = count_days(day, today)
            if (days > 0) {
                val s = days.toString()
                return s + "d"
            }
            val hours = count_hour(hour).toString()
            return hours + "h"
        }


    }

    private fun confirm_fr(account_ref: String, fr: Friends) {
        val database: DatabaseReference = Firebase.database.reference
        val ref = database.child("User")
            .child(account_ref)
            .child("friends")
            .child("request")
            .child(fr.account_ref.toString())
        val ref2 = database.child("User")
            .child(account_ref)
            .child("friends")
            .child("real")
            .child(fr.account_ref.toString())
        CoroutineScope(Dispatchers.IO).launch {
            ref.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    remove_ele(fr)
                } else {
                    val s = "Some thing went wrong"
                    s.showToastShort(context = context)
                }
            }
            ref2.setValue(fr)
        }

    }


    private fun delete_fr(account_ref: String, fr: Friends) {
        val database: DatabaseReference = Firebase.database.reference
        val ref = database.child("User")
            .child(account_ref)
            .child("friends")
            .child("request")
            .child(fr.account_ref.toString())
        CoroutineScope(Dispatchers.IO).launch {
            ref.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    remove_ele(fr)
                } else {
                    val s = "Some thing went wrong"
                    s.showToastShort(context = context)
                }
            }
        }
    }

    interface Interaction {
        fun onItemSelectedFrRequest(position: Int, item: Friends)
    }


}
