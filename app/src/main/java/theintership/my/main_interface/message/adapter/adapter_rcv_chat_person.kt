package theintership.my.main_interface.message.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import theintership.my.R
import theintership.my.main_interface.message.model.item_in_chat_person


class adapter_rcv_chat_person(val account_ref_owner: String, val mcontext: Context) :
    RecyclerView.Adapter<adapter_rcv_chat_person.ViewHolder>() {

    private var list = mutableListOf<item_in_chat_person>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rcv_chat_person, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list.get(position)
        holder.setIsRecyclable(false)
        if (item.account_ref.toString() == account_ref_owner) {
            holder.layout_to.visibility = View.GONE
            holder.img_from.visibility = View.GONE
            holder.text_from.text = item.text
            holder.text_from.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                setMargins(60 , 0 , 0 , 0)
            }
        } else {
            holder.layout_from.visibility = View.GONE
            Glide.with(mcontext).load(item.link_avatar.toString()).error(R.drawable.error_image)
                .placeholder(R.drawable.icon_loading_image).dontAnimate().into(holder.img_to)
            holder.text_to.text = item.text
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun add_item(item : item_in_chat_person){
        val size = list.size
        list.add(item)
        notifyItemInserted(size - 1)
    }

    fun submit_list(list1 : MutableList<item_in_chat_person>){
        list = list1
        notifyDataSetChanged()
    }

    fun clear() {
        val sz = list.size
        list.clear()
        notifyItemRangeRemoved(0, sz)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout_from: RelativeLayout = itemView.findViewById(R.id.item_rcv_chat_person_layout_from)
        val layout_to: RelativeLayout = itemView.findViewById(R.id.item_rcv_chat_person_layout_to)
        val img_from: CircleImageView = itemView.findViewById(R.id.item_rcv_chat_person_avatar_from)
        val img_to: CircleImageView = itemView.findViewById(R.id.item_rcv_chat_person_avatar_to)
        val text_from: TextView = itemView.findViewById(R.id.item_rcv_chat_person_text_from)
        val text_to: TextView = itemView.findViewById(R.id.item_rcv_chat_person_text_to)
    }
}
