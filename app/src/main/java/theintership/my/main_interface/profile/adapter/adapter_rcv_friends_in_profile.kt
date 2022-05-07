package theintership.my.main_interface.profile.adapter

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_rcv_friends_in_profile.view.*
import theintership.my.Main_Interface_Activity
import theintership.my.R
import theintership.my.all_class.MyMethod.Companion.replacefrag_in_main_interface_with_bundle
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.main_interface.profile.frag_profile_other
import theintership.my.main_interface.profile.model.friend_in_profile

class adapter_rcv_friends_in_profile(
    private val interaction: Interaction? = null,
    activity: Main_Interface_Activity,
    account_ref_from: String
) :
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
    val mainInterfaceActivity = activity
    private val account_ref_from = account_ref_from

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
                val item = differ.currentList.get(position)
                holder.bind(item)
                holder.itemView.item_rcv_friends_in_profile_layout1.setOnClickListener {
                    go_to_frag_profile_others(
                        from = account_ref_from,
                        to = item.account_ref1.toString()
                    )
                }
                holder.itemView.item_rcv_friends_in_profile_layout2.setOnClickListener {
                    go_to_frag_profile_others(
                        from = account_ref_from,
                        to = item.account_ref2.toString()
                    )
                }
                holder.itemView.item_rcv_friends_in_profile_layout3.setOnClickListener {
                    go_to_frag_profile_others(
                        from = account_ref_from,
                        to = item.account_ref3.toString()
                    )
                }
            }
        }
    }

    private fun go_to_frag_profile_others(from: String, to: String) {
        if (from == to) {
            val s = "This is you bro."
            s.showToastShort(mainInterfaceActivity)
            return
        }
        val arg = Bundle()
        arg.putString("account ref from", from)
        arg.putString("account ref to", to)
        replacefrag_in_main_interface_with_bundle(
            "frag_profile_others",
            frag_profile_other(),
            mainInterfaceActivity.supportFragmentManager,
            arg
        )
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
            fun load_avatar(account_ref : String , view : ImageView){
                //Don't use link_avatar from item
                //User can change avatar so if we use that avatar will not be up to date
                val database = Firebase.database.reference
                val ref = database.child("User")
                    .child(account_ref)
                    .child("link avatar")
                ref.get().addOnSuccessListener {
                    val link_avatar = it.getValue().toString()
                    Glide.with(context).load(link_avatar).placeholder(R.drawable.icon_loading_image)
                        .dontAnimate().error(R.drawable.error_image).into(view)
                }
            }
            if (!item.name1.isNullOrEmpty()) {
                item.name1.toString().also { itemView.item_rcv_friends_in_profile_name1.text = it }
                load_avatar(item.account_ref1.toString() , itemView.item_rcv_friends_in_profile_img1)
            } else {
                itemView.item_rcv_friends_in_profile_layout1.visibility = View.INVISIBLE
            }
            if (!item.name2.isNullOrEmpty()) {
                item.name2.toString().also { itemView.item_rcv_friends_in_profile_name2.text = it }
                load_avatar(item.account_ref2.toString() , itemView.item_rcv_friends_in_profile_img2)
            } else {
                itemView.item_rcv_friends_in_profile_layout2.visibility = View.INVISIBLE
            }
            if (!item.name3.isNullOrEmpty()) {
                item.name3.toString().also { itemView.item_rcv_friends_in_profile_name3.text = it }
                load_avatar(item.account_ref3.toString() , itemView.item_rcv_friends_in_profile_img3)
            } else {
                itemView.item_rcv_friends_in_profile_layout3.visibility = View.INVISIBLE
            }

        }


    }


    interface Interaction {
        fun onItemSelected(position: Int, item: friend_in_profile)
    }
}
