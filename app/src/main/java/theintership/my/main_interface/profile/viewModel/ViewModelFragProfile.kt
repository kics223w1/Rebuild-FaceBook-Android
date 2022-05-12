package theintership.my.main_interface.profile.viewModel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import theintership.my.all_class.GetUri_Image_Firebase
import theintership.my.all_class.MyMethod
import theintership.my.all_class.upload_image_by_putBytes_to_firebase
import theintership.my.main_interface.friends.model.Friends
import theintership.my.main_interface.friends.model.friend_in_show_all
import theintership.my.main_interface.profile.model.friend_in_profile
import kotlin.math.min

class ViewModelFragProfile : ViewModel() {


    private val database: DatabaseReference = Firebase.database.reference

    fun setup_list_friend_in_profile(snapshot: DataSnapshot): MutableList<friend_in_profile> {
        val list = snapshot.children.toMutableList()
        val ans = mutableListOf<friend_in_profile>()
        for (i in 0 until min(list.size, 6) step 3) {
            val account_ref1 = list[i].child("account_ref").getValue().toString()
            val name1 = list[i].child("name").getValue().toString()
            val link_avatar1 = list[i].child("link_avatar").getValue().toString()

            var account_ref2 = ""
            var name2 = ""
            var link_avatar2 = ""


            var account_ref3 = ""
            var name3 = ""
            var link_avatar3 = ""

            if (i + 1 < list.size) {
                account_ref2 = list[i + 1].child("account_ref").getValue().toString()
                name2 = list[i + 1].child("name").getValue().toString()
                link_avatar2 = list[i + 1].child("link_avatar").getValue().toString()
            }
            if (i + 2 < list.size) {
                account_ref3 = list[i + 2].child("account_ref").getValue().toString()
                name3 = list[i + 2].child("name").getValue().toString()
                link_avatar3 = list[i + 2].child("link_avatar").getValue().toString()
            }
            val fr =
                friend_in_profile(
                    account_ref1 = account_ref1,
                    name1 = name1,
                    link_avatar1 = link_avatar1,
                    account_ref2 = account_ref2,
                    name2 = name2,
                    link_avatar2 = link_avatar2,
                    account_ref3 = account_ref3,
                    name3 = name3,
                    link_avatar3 = link_avatar3
                )
            ans.add(fr)
        }
        return ans
    }

    fun setup_link_avatar_and_storage(account_ref: String, image: Bitmap) {
        val ref = database.child("User")
            .child(account_ref)
            .child("link avatar")
        val ref_storage = "avatar_user/$account_ref"
        CoroutineScope(Dispatchers.IO).launch {
            upload_image_by_putBytes_to_firebase().upload(image, ref_storage).addOnSuccessListener {
                GetUri_Image_Firebase().getUri(ref_storage).addOnSuccessListener {
                    ref.setValue(it.toString())
                }
            }
        }
        //If it fail , user will set it again in next time
    }

    fun setup_link_coverImage_and_storage(account_ref: String, image: Bitmap) {
        val ref = database.child("User")
            .child(account_ref)
            .child("link cover image")
        val ref_storage = "cover_image/$account_ref"
        CoroutineScope(Dispatchers.IO).launch {
            upload_image_by_putBytes_to_firebase().upload(image, ref_storage).addOnSuccessListener {
                GetUri_Image_Firebase().getUri(ref_storage).addOnSuccessListener {
                    ref.setValue(it.toString())
                }
            }
        }
        //If it fail , user will set it again in next time
    }

    fun add_friend(from: String, to: String, user_name: String, link_avatar: String) {
        val ref = database
            .child("User")
            .child(to)
            .child("friends")
            .child("request")
            .child(from)
        val fr = Friends(user_name, from, link_avatar, MyMethod.set_today(), MyMethod.get_hour())
        ref.setValue(fr)
    }

    fun remove_request_add_friend(context: Context, from: String, to: String) {
        val ref = database.child("User")
            .child(from)
            .child("friends")
            .child("request")
            .child(to)
        CoroutineScope(Dispatchers.IO).launch {
            ref.removeValue()
        }
    }


    fun unfriend(context: Context, from: String, to: String) {
        val ref = database.child("User")
            .child(from)
            .child("friends")
            .child("real")
            .child(to)
        val ref2 = database.child("User")
            .child(to)
            .child("friends")
            .child("real")
            .child(from)
        CoroutineScope(Dispatchers.IO).launch {
            ref2.removeValue()
            ref.removeValue()
        }
    }

    fun setup_friend_in_show_all(snapshot: DataSnapshot): MutableList<friend_in_show_all> {
        val list = snapshot.children.toMutableList()
        val friendInShowAlls = mutableListOf<friend_in_show_all>()
        for (i in 0 until list.size){
            val it = list[i]
            if (it.exists()){
                val name = it.child("name").getValue().toString()
                val link_avatar = it.child("link_avatar").getValue().toString()
                val account_ref = it.child("account_ref").getValue().toString()
                val fr = friend_in_show_all(
                    name = name,
                    link_avatar = link_avatar,
                    account_ref = account_ref
                )
                friendInShowAlls.add(fr)
            }
        }
        return friendInShowAlls
    }


}