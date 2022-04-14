package theintership.my.signin_signup.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.icu.number.NumberFormatter.with
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import theintership.my.R
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.signin_signup.model.image
import java.io.File


interface IClickImage {
    fun onClickImage(path: String)
}

class adapter_image(
    val iClickImage: IClickImage,
    val context: Context,
) :
    RecyclerView.Adapter<adapter_image.ViewHolder>() {

    private var list_image = mutableListOf<image>()
    private var index_click = -1
    private var image_click = ""
    private var list_holder = mutableListOf<adapter_image.ViewHolder>()
    private var list_holder_load_image_fail =
        mutableMapOf<adapter_image.ViewHolder, MutableList<String>>()

    fun submitList(list: MutableList<image>) {
        list_image = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapter_image.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rcv_image_for_selecting_avatar, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: adapter_image.ViewHolder, position: Int) {
        val image = list_image.get(position)
        list_holder.add(holder)
        list_holder_load_image_fail[holder] = mutableListOf()

        val path1 = image.path1
        val path2 = image.path2
        val path3 = image.path3

        if (path1 != "") {
            Glide.with(context.applicationContext).load(path1)
                .placeholder(R.drawable.icon_loading_image)
                .apply(RequestOptions())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        list_holder_load_image_fail[holder]?.add("image1")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                }).error(R.drawable.error_image).into(holder.image1)
        } else {
            holder.layout_item_image1.visibility = View.INVISIBLE
            holder.image1.visibility = View.GONE
        }

        if (path2 != "") {
            Glide.with(context.applicationContext).load(path2)
                .placeholder(R.drawable.icon_loading_image)
                .apply(RequestOptions())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        list_holder_load_image_fail[holder]?.add("image2")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                }).error(R.drawable.error_image).into(holder.image2)
        } else {
            holder.layout_item_image2.visibility = View.INVISIBLE
            holder.image2.visibility = View.GONE
        }

        if (path3 != "") {
            Glide.with(context.applicationContext).load(path3)
                .placeholder(R.drawable.icon_loading_image)
                .apply(RequestOptions())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        list_holder_load_image_fail[holder]?.add("image3")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                }).error(R.drawable.error_image).into(holder.image3)
        } else {
            holder.layout_item_image3.visibility = View.INVISIBLE
            holder.image3.visibility = View.GONE
        }

        //If user click image has loaded fail , app will crash
        holder.image1.setOnClickListener {
            if (list_holder_load_image_fail[holder]?.contains("image1") == true) {
                val s = "This image is fail loaded."
                s.showToastShort(context.applicationContext)
                return@setOnClickListener
            }
            iClickImage.onClickImage(path1)
            check_layout_clicking_image(holder.adapterPosition, "image1")
        }
        holder.image2.setOnClickListener {
            if (list_holder_load_image_fail[holder]?.contains("image2") == true) {
                val s = "This image is fail loaded."
                s.showToastShort(context.applicationContext)
                return@setOnClickListener
            }
            iClickImage.onClickImage(path2)
            check_layout_clicking_image(holder.adapterPosition, "image2")
        }
        holder.image3.setOnClickListener {
            if (list_holder_load_image_fail[holder]?.contains("image3") == true) {
                val s = "This image is fail loaded."
                s.showToastShort(context.applicationContext)
                return@setOnClickListener
            }
            iClickImage.onClickImage(path3)
            check_layout_clicking_image(holder.adapterPosition, "image3")
        }
    }

    override fun getItemCount(): Int {
        return list_image.size
    }

    private fun remove_layout_click(image_click: String, index: Int) {
        when (image_click) {
            "image1" -> {
                list_holder[index].checkbox_image1.visibility = View.INVISIBLE
                list_holder[index].image1.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    setMargins(0, 0, 0, 0)
                }
            }
            "image2" -> {
                list_holder[index].checkbox_image2.visibility = View.INVISIBLE
                list_holder[index].image2.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    setMargins(0, 0, 0, 0)
                }
            }
            "image3" -> {
                list_holder[index].checkbox_image3.visibility = View.INVISIBLE
                list_holder[index].image3.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    setMargins(0, 0, 0, 0)
                }
            }
        }
    }


    private fun add_layout_click(image_click: String, index: Int) {
        when (image_click) {
            "image1" -> {
                list_holder[index].checkbox_image1.visibility = View.VISIBLE
                list_holder[index].image1.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    setMargins(10, 10, 10, 10)
                }
            }
            "image2" -> {
                list_holder[index].checkbox_image2.visibility = View.VISIBLE
                list_holder[index].image2.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    setMargins(10, 10, 10, 10)
                }
            }
            "image3" -> {
                list_holder[index].checkbox_image3.visibility = View.VISIBLE
                list_holder[index].image3.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    setMargins(10, 10, 10, 10)
                }
            }
        }
    }


    private fun check_layout_clicking_image(position: Int, image1: String) {
        if (index_click == -1) {
            //User click image first time
            index_click = position
            image_click = image1
            add_layout_click(image_click = image1, index = position)
            return
        }
        if (position == index_click && image_click == image1) {
            //User click the image that has been clicked
            remove_layout_click(image_click, index_click)
            index_click = -1
            image_click = ""
            return
        }
        //One image has been clicked and user click new image
        remove_layout_click(image_click, index_click)
        image_click = image1
        index_click = position
        add_layout_click(image_click = image_click, index = index_click)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var layout_item_image1: FrameLayout
        var layout_item_image2: FrameLayout
        var layout_item_image3: FrameLayout
        var image1: ImageView
        var image2: ImageView
        var image3: ImageView
        var checkbox_image1: CheckBox
        var checkbox_image2: CheckBox
        var checkbox_image3: CheckBox

        init {
            layout_item_image1 = itemView.findViewById(R.id.layout_item_image1)
            layout_item_image2 = itemView.findViewById(R.id.layout_item_image2)
            layout_item_image3 = itemView.findViewById(R.id.layout_item_image3)
            image1 = itemView.findViewById(R.id.item_image1)
            image2 = itemView.findViewById(R.id.item_image2)
            image3 = itemView.findViewById(R.id.item_image3)
            checkbox_image1 = itemView.findViewById(R.id.checkbox_image1)
            checkbox_image2 = itemView.findViewById(R.id.checkbox_image2)
            checkbox_image3 = itemView.findViewById(R.id.checkbox_image3)

        }
    }


}