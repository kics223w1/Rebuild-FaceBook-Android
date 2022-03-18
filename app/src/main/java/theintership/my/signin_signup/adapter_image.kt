
package theintership.my.signin_signup

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import theintership.my.R
import theintership.my.model.Language

class adapter_image(
    context : Context
) :
    RecyclerView.Adapter<adapter_image.ViewHolder>() {

    private var list_path_of_image = mutableListOf<String>()
    private val context = context

    fun setData(list: MutableList<String>) {
        list_path_of_image = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapter_image.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rcv_item_image, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder:adapter_image.ViewHolder, position: Int) {
        val path : String = list_path_of_image[position]
        Glide.with(context).load(path).error(R.drawable.error_image).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return list_path_of_image.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        init {
            imageView = itemView.findViewById(R.id.image_view_frag_set_avatar)
        }
    }


}