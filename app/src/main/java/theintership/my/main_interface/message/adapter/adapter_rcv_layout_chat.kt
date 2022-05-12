package theintership.my.main_interface.message.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_rcv_layout_chat.view.*
import theintership.my.R
import theintership.my.activity.Main_Interface_Activity
import theintership.my.all_class.MyMethod
import theintership.my.main_interface.message.fragments.frag_chat_person
import theintership.my.main_interface.message.model.item_in_layout_chat




class adapter_rcv_layout_chat(val mcontext: Context , activity : Main_Interface_Activity) :
    RecyclerView.Adapter<adapter_rcv_layout_chat.ViewHolder>() {

    private var list = mutableListOf<item_in_layout_chat>()
    private val mainInterfaceActivity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rcv_layout_chat, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list.get(position)
        Glide.with(mcontext).load(item.link_avatar.toString()).error(R.drawable.error_image)
            .placeholder(R.drawable.icon_loading_image).dontAnimate()
            .into(holder.avatar)
        Glide.with(mcontext).load(item.link_avatar.toString()).error(R.drawable.error_image)
            .placeholder(R.drawable.icon_loading_image).dontAnimate()
            .into(holder.avatar_readed)
        holder.name.text = item.name.toString()
        holder.message.text = item.last_statement.toString()
        holder.layout.setOnClickListener{
            val b = Bundle()
            b.putString("account ref to" , item.account_ref.toString())
            MyMethod.replacefrag_in_main_interface_with_bundle(
                tag = "frag_chat_person",
                frag = frag_chat_person(),
                fm = mainInterfaceActivity.supportFragmentManager,
                arg = b
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun submitList(list1 : MutableList<item_in_layout_chat>){
        list = list1
        notifyDataSetChanged()
    }

    fun clear() {
        val sz = list.size
        list.clear()
        notifyItemRangeRemoved(0, sz)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val avatar: CircleImageView = itemView.findViewById(R.id.item_rcv_layout_chat_avatar)
            val name: TextView = itemView.findViewById(R.id.item_rcv_layout_chat_name)
            val message: TextView = itemView.findViewById(R.id.item_rcv_layout_chat_message)
            val avatar_readed: CircleImageView =
                itemView.findViewById(R.id.item_rcv_layout_chat_avatar_readed)
            val layout : RelativeLayout = itemView.findViewById(R.id.item_rcv_layout_chat_layout_all)
    }
}
