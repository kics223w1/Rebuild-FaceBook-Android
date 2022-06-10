package theintership.my.main_interface.message.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.emoji.widget.EmojiTextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.dialog_bottom_options_item_chat_person.*
import theintership.my.R
import theintership.my.activity.Main_Interface_Activity
import theintership.my.all_class.MyMethod
import theintership.my.all_class.MyMethod.Companion.copyToClipboard
import theintership.my.all_class.MyMethod.Companion.setup_ref_chat_between_2_person
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.main_interface.friends.model.Friends
import theintership.my.main_interface.message.dialog.dialog_bottom_options_item_chat_person
import theintership.my.main_interface.message.model.item_in_chat_person
import theintership.my.main_interface.message.viewmodel.ViewModelMessage


class adapter_rcv_chat_person(
    val account_ref_owner: String,
    val account_ref_to: String,
    val mcontext: Context,
    val activity : Main_Interface_Activity
) :
    RecyclerView.Adapter<adapter_rcv_chat_person.ViewHolder>() {

    private var list = mutableListOf<item_in_chat_person>()
    private lateinit var last_holder: ViewHolder
    private val database: DatabaseReference = Firebase.database.reference
    private val viewModelMessage = ViewModelProvider(activity).get(ViewModelMessage::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rcv_chat_person, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list.get(position)
        last_holder = holder
        holder.setIsRecyclable(false)
        if (item.account_ref.toString() == account_ref_owner) {
            holder.layout_from.visibility = View.GONE
//            holder.img_to.visibility = View.GONE
            Glide.with(mcontext).load(item.link_avatar.toString()).error(R.drawable.error_image)
                .placeholder(R.drawable.icon_loading_image).dontAnimate().into(holder.img_to)
            if (item.status){
                holder.text_to.text = item.text
            }else{
                holder.text_to.text = "Remove message"
            }
//            holder.text_to.updateLayoutParams<ViewGroup.MarginLayoutParams> {
//                setMargins(60, 0, 0, 0)
//            }
        } else {
            holder.layout_to.visibility = View.GONE
            Glide.with(mcontext).load(item.link_avatar.toString()).error(R.drawable.error_image)
                .placeholder(R.drawable.icon_loading_image).dontAnimate().into(holder.img_from)
            if (item.status){
                holder.text_from.text = item.text
            }else{
                holder.text_from.text = "Remove message"
            }
        }
        holder.layout_to.setOnClickListener {
            val s = "$position"
            s.showToastShort(mcontext)
        }
        holder.layout_to.setOnLongClickListener {
            val dialog = dialog_bottom_options_item_chat_person(context = mcontext)
            dialog.show()
            dialog.btn_reply.setOnClickListener {
                dialog.dismiss()
            }
            dialog.btn_copy.setOnClickListener {
                dialog.dismiss()
                val s = item.text.toString()
                copy(s)
            }
            dialog.btn_edit.setOnClickListener {
                dialog.layout_options.visibility = View.GONE
                dialog.layout_edit_chat.visibility = View.VISIBLE
                dialog.btn_back_to_options.setOnClickListener {
                    dialog.layout_edit_chat.visibility = View.GONE
                    dialog.layout_options.visibility = View.VISIBLE
                }
                dialog.btn_ok_edit_chat.setOnClickListener {
                    val new = dialog.edt_edit_chat.text.toString()
                    dialog.layout_edit_chat.visibility = View.GONE
                    dialog.layout_options.visibility = View.VISIBLE
                    dialog.dismiss()
                    holder.text_to.text = new
                    edit_item_chat(item.text.toString() , new , item ,mcontext , holder)
                    remove_last()
                }
            }
            dialog.btn_remove.setOnClickListener {
                dialog.dismiss()
                holder.text_to.text = "Remove message"
                list.get(position).text = "Remove message"
                list.get(position).status = false
                remove_item_chat(item ,mcontext , holder)
                remove_last()
            }
            true
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    private fun copy(s: String) {
        var alert = "Copy success"
        if (s.isEmpty()) {
            alert = "Some thing went wrong"
        } else {
            mcontext.copyToClipboard(s)
        }
        alert.showToastShort(mcontext)
    }

    fun getList() : MutableList<item_in_chat_person>{
        return list
    }

    fun add_item(item: item_in_chat_person) {
        val size = list.size
        list.add(item)
        notifyItemInserted(size - 1)
    }

    fun translate_text(index : Int, str : String){
        list.get(index).text = str
        notifyDataSetChanged()
    }

    fun submit_list(list1: MutableList<item_in_chat_person>) {
        list = list1
        notifyDataSetChanged()
    }
    fun remove_last(){
        var id = itemCount
        val s = "$id"
        s.showToastShort(mcontext)
        list.removeAt(id - 1)
    }

    private fun edit_item_chat(
        old : String,
        new : String,
        mItem: item_in_chat_person,
        context: Context,
        holder : adapter_rcv_chat_person.ViewHolder
    ) {
        val ref_chat =
            setup_ref_chat_between_2_person(from = account_ref_owner, to = account_ref_to)
        val item = mItem
        item.text = new
        val ref = database.child("Chat")
            .child(ref_chat)
            .child(item.key.toString())
        ref.setValue(item).addOnSuccessListener {
            val s = "Edit message success"
            s.showToastShort(context)
        }.addOnFailureListener {
            holder.text_to.text = old
            val s = "Edit message fail , please do again"
            s.showToastShort(context)
        }
    }

    private fun remove_item_chat(
        mItem: item_in_chat_person,
        context: Context,
        holder : adapter_rcv_chat_person.ViewHolder
    ) {
        val ref_chat =
            setup_ref_chat_between_2_person(from = account_ref_owner, to = account_ref_to)
        val item = mItem
        item.status = false
        val ref = database.child("Chat")
            .child(ref_chat)
            .child(item.key.toString())
        ref.setValue(item).addOnSuccessListener {
            val s = "Remove message success"
            s.showToastShort(context)
        }.addOnFailureListener {
            holder.text_to.text = item.text.toString()
            val s = "Remove message fail , please do it again"
            s.showToastShort(context)
        }
    }

    fun clear() {
        val sz = list.size
        list.clear()
        notifyItemRangeRemoved(0, sz)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout_from: RelativeLayout =
            itemView.findViewById(R.id.item_rcv_chat_person_layout_from)
        val layout_to: RelativeLayout = itemView.findViewById(R.id.item_rcv_chat_person_layout_to)
        val img_from: CircleImageView = itemView.findViewById(R.id.item_rcv_chat_person_avatar_from)
        val img_to: CircleImageView = itemView.findViewById(R.id.item_rcv_chat_person_avatar_to)
        val text_from: TextView = itemView.findViewById(R.id.item_rcv_chat_person_text_from)
        val text_to: TextView = itemView.findViewById(R.id.item_rcv_chat_person_text_to)
        val is_readed: TextView = itemView.findViewById(R.id.item_rcv_chat_person_layout_is_read)
    }
}
