package theintership.my.signin_signup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import theintership.my.R

class base_adapter_image_gallery(mcontext : Context) : BaseAdapter() {

    var context = mcontext
    private val layoutInflater : LayoutInflater
    var list = mutableListOf<String>()

    init {
        this.layoutInflater = LayoutInflater.from(context)
    }

    fun setData(list : MutableList<String>){
       this.list = list
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 1L
    }

    override fun getView(position: Int, mview: View?, mviewGroup: ViewGroup?): View {
        val view : View
        if (mview == null){
            view = this.layoutInflater.inflate(R.layout.base_adapter_item_image , mviewGroup , false)
            val image = view.findViewById<ImageView>(R.id.image_view_base_adapter)
            Glide.with(context).load(list[position]).error(R.drawable.error_image).into(image)
        }else{
            view = mview
        }

        return view
    }
}