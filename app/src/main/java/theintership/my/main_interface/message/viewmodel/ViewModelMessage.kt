package theintership.my.main_interface.message.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.all_class.MyMethod
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.main_interface.message.model.item_in_chat_person
import theintership.my.main_interface.message.model.item_in_layout_chat

class ViewModelMessage : ViewModel() {

    private val database: DatabaseReference = Firebase.database.reference

    fun setup_list_item_in_layout_chat(snapshot: DataSnapshot): MutableList<item_in_layout_chat> {
        val list = snapshot.children.toMutableList()
        val ans = mutableListOf<item_in_layout_chat>()
        for (i in 0 until list.size) {
            val it = list[i]
            val name = it.child("name").getValue().toString()
            val account_ref = it.child("account_ref").getValue().toString()
            var link_avatar = it.child("link_avatar").getValue().toString()
            var item = item_in_layout_chat(
                name = name,
                account_ref = account_ref,
                link_avatar = link_avatar,
                last_statement = "You: Oke luon 4:36 PM",
                is_readed = "true"
            )
            ans.add(item)
        }
        return ans
    }

    fun setup_list_item_in_person_chat(snapshot: DataSnapshot): MutableList<item_in_chat_person> {
        val list = snapshot.children.toMutableList()
        val ans = mutableListOf<item_in_chat_person>()
        for (i in 0 until list.size) {
            val it = list[i]
            val text = it.child("text").getValue()?.toString()
            val account_ref = it.child("account_ref").getValue()?.toString()
            val link_avatar = it.child("link_avatar").getValue()?.toString()
            val key = it.child("key").getValue()?.toString()
            val mStatus = it.child("status").getValue()?.toString()
            println("debug mstatus: $mStatus")
            var status = true
            if (!mStatus.isNullOrEmpty()) {
                if (mStatus == "false") {
                    status = false
                }
            }
            val item = item_in_chat_person(
                text = text,
                account_ref = account_ref,
                link_avatar = link_avatar,
                key = key,
                status = status
            )
            ans.add(item)
        }
        return ans
    }



}