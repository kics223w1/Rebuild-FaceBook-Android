package theintership.my.main_interface.message.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import theintership.my.R
import theintership.my.activity.Main_Interface_Activity
import theintership.my.all_class.MyMethod
import theintership.my.all_class.MyMethod.Companion.get_minutes
import theintership.my.all_class.MyMethod.Companion.setup_ref_chat_between_2_person
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.all_class.SharePrefValue
import theintership.my.databinding.FragChatPersonBinding
import theintership.my.main_interface.message.adapter.adapter_rcv_chat_person
import theintership.my.main_interface.message.model.item_in_chat_person
import theintership.my.main_interface.message.viewmodel.ViewModelMessage


class frag_chat_person : Fragment(R.layout.frag_chat_person) {

    private var _binding: FragChatPersonBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainInterfaceActivity: Main_Interface_Activity
    private val database: DatabaseReference = Firebase.database.reference
    private lateinit var adapter: adapter_rcv_chat_person
    private lateinit var viewModelMessage: ViewModelMessage
    private var stop = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragChatPersonBinding.inflate(inflater, container, false)
        mainInterfaceActivity = activity as Main_Interface_Activity
        viewModelMessage = ViewModelProvider(this).get(ViewModelMessage::class.java)
        binding.fragChatPersonWriting.visibility = View.INVISIBLE

        val accout_ref_to = arguments?.get("account ref to").toString()
        val account_ref_owner = SharePrefValue(mainInterfaceActivity).get_account_ref()

        adapter = adapter_rcv_chat_person(
            account_ref_owner = account_ref_owner,
            mcontext = mainInterfaceActivity
        )


        binding.fragChatPersonRcv.layoutManager = LinearLayoutManager(mainInterfaceActivity)
        binding.btnScroll.setOnClickListener {
            val index = adapter.itemCount
            println("debug vao scroll rcv index")
            binding.fragChatPersonRcv.scrollToPosition(index - 1)
        }

        listen_chat(from = account_ref_owner, to = accout_ref_to)
        listen_is_writing(from = account_ref_owner, to = accout_ref_to)

        binding.fragChatPersonEdt.addTextChangedListener(object : TextWatcher {
            val ref_chat =
                setup_ref_chat_between_2_person(from = account_ref_owner, to = accout_ref_to)
            val ref = database.child("Is writing")
                .child(ref_chat)
                .child(account_ref_owner)

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                println("debug vao on text change")
                stop = false
                if (binding.fragChatPersonEdt.text.toString().length > 0){
                    ref.setValue("writing")
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                println("debug vao after text change")
            }

        })


        binding.fragChatPersonBtnSend.setOnClickListener {
            val text = binding.fragChatPersonEdt.text.toString()
            if (text == "") {
                val s = "Empty"
                s.showToastShort(mainInterfaceActivity)
                return@setOnClickListener
            }
            val ref_chat =
                setup_ref_chat_between_2_person(from = account_ref_owner, to = accout_ref_to)
            val ref = database.child("Is writing")
                .child(ref_chat)
                .child(account_ref_owner)
            ref.setValue("done")
            sendMessage(text = text, from = account_ref_owner, to = accout_ref_to)
        }

        return binding.root
    }


    private fun sendMessage(text: String, from: String, to: String) {
        val ref_chat = setup_ref_chat_between_2_person(from = from, to = to)
        val ref = database.child("Chat")
            .child(ref_chat)
            .push()
        val key = ref.key.toString()
        val account_ref_owner = SharePrefValue(mainInterfaceActivity).get_account_ref()
        val link_avatar_owner = SharePrefValue(mainInterfaceActivity).get_link_avatar()
        val item = item_in_chat_person(
            account_ref = account_ref_owner,
            link_avatar = link_avatar_owner,
            text = text,
            key = key
        )
        CoroutineScope(Dispatchers.IO).launch {
            ref.setValue(item).addOnSuccessListener {
                binding.fragChatPersonEdt.text.clear()
            }.addOnFailureListener {
                val s = "Some thing went wrong, pls do again."
                s.showToastShort(mainInterfaceActivity)
            }
        }
    }

    private fun listen_chat(from: String, to: String) {
        val ref_chat = setup_ref_chat_between_2_person(from = from, to = to)
        val ref = database.child("Chat")
            .child(ref_chat)
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = viewModelMessage.setup_list_item_in_person_chat(snapshot)
                val size = adapter.itemCount
                if (size == 0) {
                    println("debug vao submit list message")
                    adapter.submit_list(list)
                } else {
                    println("debug vao add item message")
                    adapter.add_item(list.last())
                }
                binding.fragChatPersonRcv.adapter = adapter
                val index = adapter.itemCount
                binding.fragChatPersonRcv.scrollToPosition(index - 1)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        ref.addValueEventListener(postListener)
    }

    private fun listen_is_writing(from: String, to: String) {
        val ref_chat = setup_ref_chat_between_2_person(from = from, to = to)
        val ref = database.child("Is writing")
            .child(ref_chat)
            .child(to)
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value.toString()
                println("debug vao datachange trong listen writing $stop")
                if (value == "done") {
                    println("debug vao invisible ne value $value")
                    binding.fragChatPersonWriting.visibility = View.INVISIBLE
                    stop = true
                }else if (value == "writing"){
                    if (binding.fragChatPersonWriting.visibility == View.INVISIBLE) {
                        println("debug vao visible ne value $value")
                        binding.fragChatPersonWriting.visibility = View.VISIBLE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        ref.addValueEventListener(postListener)
    }

}