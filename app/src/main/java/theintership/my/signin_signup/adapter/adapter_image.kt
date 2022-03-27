package theintership.my.signin_signup.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import theintership.my.R
import theintership.my.model.image


interface IClickImage{
    fun onClickImage(path : String)
}

class adapter_image(
    val iClickImage: IClickImage,
    val context: Context,
) :
    RecyclerView.Adapter<adapter_image.ViewHolder>() {

    private var list_image = mutableListOf<image>()

    fun submitList(list: MutableList<image>) {
        list_image = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapter_image.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rcv_image_for_selecting_avatar, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: adapter_image.ViewHolder, position: Int) {
        val image = list_image.get(position)
        val path1 = image.path1
        val path2 = image.path2
        val path3 = image.path3
        if (path1 != ""){
            Glide.with(context).load(path1).error(R.drawable.error_image).into(holder.image1)
        }
        if (path2 != ""){
            Glide.with(context).load(path2).error(R.drawable.error_image).into(holder.image2)
        }
        if (path3 != ""){
            Glide.with(context).load(path2).error(R.drawable.error_image).into(holder.image3)
        }
        holder.image1.setOnClickListener {
            iClickImage.onClickImage(path1)
            holder.image1.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                setMargins(10 , 10 , 10 , 10)
            }
            holder.checkbox_image1.visibility = View.VISIBLE
        }
        holder.image2.setOnClickListener {
            iClickImage.onClickImage(path2)
            holder.image2.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                setMargins(10 , 10 , 10 , 10)
            }
            holder.checkbox_image2.visibility = View.VISIBLE

        }
        holder.image3.setOnClickListener {
            iClickImage.onClickImage(path3)
            holder.image3.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                setMargins(10 , 10 , 10 , 10)
            }
            holder.image3.visibility = View.INVISIBLE
            holder.image3.visibility = View.VISIBLE
            //holder.checkbox_image3.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return list_image.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image1 : ImageView
        var image2 : ImageView
        var image3 : ImageView
        var checkbox_image1 : CheckBox
        var checkbox_image2 : CheckBox
        var checkbox_image3 : CheckBox

        init {
            image1 = itemView.findViewById(R.id.item_image1)
            image2 = itemView.findViewById(R.id.item_image2)
            image3 = itemView.findViewById(R.id.item_image3)
            checkbox_image1 = itemView.findViewById(R.id.checkbox_image1)
            checkbox_image2 = itemView.findViewById(R.id.checkbox_image2)
            checkbox_image3 = itemView.findViewById(R.id.checkbox_image3)

        }
    }


}