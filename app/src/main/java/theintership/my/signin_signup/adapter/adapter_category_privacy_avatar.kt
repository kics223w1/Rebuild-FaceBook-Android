package theintership.my.signin_signup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import theintership.my.R
import theintership.my.model.category_privacy_avatar
import theintership.my.model.image

class adapter_category_privacy_avatar(
    context: Context,
    resource: Int,
    list: MutableList<category_privacy_avatar>
) :
    ArrayAdapter<category_privacy_avatar>(context, resource, list) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView2 = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_category_privacy_avatar, parent, false)
        val tv = convertView2.findViewById<TextView>(R.id.tv_select_category_privacy_avatar)
        val item = this.getItem(position)
        if (item != null){
            val name = item.name.toString()
            tv.setText(name)
        }


        return convertView2
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView2 = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_privacy_avatar, parent, false)
        val tv = convertView2.findViewById<TextView>(R.id.tv_item_category_privacy_avatar)
        val imageView = convertView2.findViewById<ImageView>(R.id.image_view_item_category_privacy_avatar)
        val item = this.getItem(position)
        if (item != null){
            val name = item.name.toString()
            val image = item.image
            tv.setText(name)
            Glide.with(convertView2).load(image).error(R.drawable.error_image).into(imageView)
        }
        return convertView2
    }

}