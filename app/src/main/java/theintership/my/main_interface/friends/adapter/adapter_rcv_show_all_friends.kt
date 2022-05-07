package theintership.my.main_interface.friends.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_rcv_show_all_friends.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import theintership.my.R
import theintership.my.all_class.MyMethod
import theintership.my.all_class.SharePrefValue
import theintership.my.main_interface.friends.model.Friends
import theintership.my.main_interface.friends.model.friend_in_show_all
import theintership.my.main_interface.profile.dialog.dialog_remove_request_add_friend
import theintership.my.main_interface.profile.dialog.dialog_unfriend
import theintership.my.main_interface.profile.viewModel.ViewModelFragProfile

class adapter_rcv_show_all_friends(private val interaction: Interaction? = null, context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<friend_in_show_all>() {

        override fun areItemsTheSame(
            oldItem: friend_in_show_all,
            newItem: friend_in_show_all
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: friend_in_show_all,
            newItem: friend_in_show_all
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    val context = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return adapter_rcv_show_all_friends(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_rcv_show_all_friends,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is adapter_rcv_show_all_friends -> {
                holder.bind(differ.currentList.get(position))
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<friend_in_show_all>) {
        differ.submitList(list)
    }

    class adapter_rcv_show_all_friends
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {


        fun bind(item: friend_in_show_all) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            val database = Firebase.database.reference
            fun load_avatar(account_ref: String, view: ImageView) {
                //Don't use link_avatar from item
                //User can change avatar so if we use that avatar will not be up to date
                val ref = database.child("User")
                    .child(account_ref)
                    .child("link avatar")
                ref.get().addOnSuccessListener {
                    val link_avatar = it.getValue().toString()
                    Glide.with(context).load(link_avatar).placeholder(R.drawable.icon_loading_image)
                        .dontAnimate().error(R.drawable.error_image).into(view)
                }
            }

            fun add_friend(from: String, to: String, user_name: String, link_avatar: String) {
                val ref = database
                    .child("User")
                    .child(to)
                    .child("friends")
                    .child("request")
                    .child(from)
                val fr =
                    Friends(user_name, from, link_avatar, MyMethod.set_today(), MyMethod.get_hour())
                ref.setValue(fr)
            }

            fun remove_request_add_friend(context: Context, from: String, to: String) {
                val ref = database.child("User")
                    .child(from)
                    .child("friends")
                    .child("request")
                    .child(to)
                CoroutineScope(Dispatchers.IO).launch {
                    ref.removeValue()
                }
            }

            fun is_friend(account_ref_from: String, account_ref_to: String) {
                val ref = database.child("User")
                    .child(account_ref_from)
                    .child("friends")
                    .child("real")
                    .child(account_ref_to)
                ref.get().addOnSuccessListener {
                    if (it.exists()) {
                        itemView.item_rcv_show_all_friends_btn_add_friend.text = "Friend"
                    }
                }
            }

            fun is_pending_friend(account_ref_from: String, account_ref_to: String) {
                val ref = database.child("User")
                    .child(account_ref_from)
                    .child("friends")
                    .child("request")
                    .child(account_ref_to)
                ref.get().addOnSuccessListener {
                    if (it.exists()) {
                        itemView.item_rcv_show_all_friends_btn_add_friend.text = "Requesting"
                    }
                }
            }

            fun unfriend(context: Context, from: String, to: String) {
                val ref = database.child("User")
                    .child(from)
                    .child("friends")
                    .child("real")
                    .child(to)
                val ref2 = database.child("User")
                    .child(to)
                    .child("friends")
                    .child("real")
                    .child(from)
                CoroutineScope(Dispatchers.IO).launch {
                    ref2.removeValue()
                    ref.removeValue()
                }
            }

            fun show_dialog_remove_request_add_friend(context: Context, from: String, to: String) {
                val dialog = dialog_remove_request_add_friend(context)
                dialog.show()
                dialog.btn_remove_request.setOnClickListener {
                    remove_request_add_friend(
                        context,
                        from = from,
                        to = to
                    )
                    itemView.item_rcv_show_all_friends_btn_add_friend.text = "Add friend"
                    dialog.dismiss()
                }
            }

            fun show_dialog_unfriend(context: Context, from: String, to: String) {
                val dialog = dialog_unfriend(context)
                dialog.show()
                dialog.btn_unfriend.setOnClickListener {
                    unfriend(context , from = from , to = to)
                    itemView.item_rcv_show_all_friends_btn_add_friend.text = "Add friend"
                    dialog.dismiss()
                }
            }


            val account_ref_owner = SharePrefValue(context).get_account_ref()
            val account_ref_fr = item.account_ref.toString()

            load_avatar(item.account_ref.toString(), itemView.item_rcv_show_all_friends_image)
            itemView.item_rcv_show_all_friends_name.text = item.name.toString()

            is_pending_friend(account_ref_from = account_ref_owner, account_ref_to = account_ref_fr)
            is_friend(account_ref_from = account_ref_owner, account_ref_to = account_ref_fr)
            itemView.item_rcv_show_all_friends_btn_add_friend.setOnClickListener {
                val check = itemView.item_rcv_show_all_friends_btn_add_friend.text.toString()
                if (check == "Friend") {
                    show_dialog_unfriend(context, from = account_ref_owner, to = account_ref_fr)
                    return@setOnClickListener
                }
                if (check == "Requesting") {
                    show_dialog_remove_request_add_friend(
                        context,
                        from = account_ref_owner,
                        to = account_ref_fr
                    )
                    return@setOnClickListener
                }
                itemView.item_rcv_show_all_friends_btn_add_friend.text = "Requesting"
                val name_owner = SharePrefValue(context).get_user_name()
                val link_avatar_owner = SharePrefValue(context).get_link_avatar()
                add_friend(
                    from = account_ref_owner,
                    to = account_ref_fr,
                    user_name = name_owner,
                    link_avatar = link_avatar_owner
                )

            }


        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: friend_in_show_all)
    }
}
