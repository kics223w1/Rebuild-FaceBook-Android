package theintership.my.signin_signup.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import theintership.my.MyMethod.Companion.showToastLong
import theintership.my.R
import theintership.my.databinding.FragSetAvatarBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.adapter_image
import java.util.*


class frag_set_avatar : Fragment(R.layout.frag_set_avatar) {

    private var _binding: FragSetAvatarBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSetAvatarBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity
        val rcv = binding.gridViewFragSetAvatar

        val list = getAllImage()
        println("debug vao frag avatar va list : $list")
        val adapter = adapter_image(signup1activity)
        adapter.setData(list)
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(signup1activity)
        rcv.layoutManager = layoutManager
        rcv.adapter = adapter


        return binding.root
    }

    @SuppressLint("Recycle")
    private fun getAllImage() : MutableList<String>{
        var uri : Uri
        var cursor : Cursor?
        var colum_index_data : Int?
        var list = mutableListOf<String>()
        var path : String
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        var protection = Array<String?>(2){
            MediaStore.MediaColumns.DATA; MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        }
        cursor = signup1activity.contentResolver.query(uri , protection , null, null ,
            "DESC")
        colum_index_data = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        while (cursor!!.moveToNext()){
            path = cursor.getString(colum_index_data!!)
            list.add(path)
        }
        return list
    }



}
