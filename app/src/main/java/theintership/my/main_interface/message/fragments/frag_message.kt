package theintership.my.main_interface.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.activity.Main_Interface_Activity
import theintership.my.R
import theintership.my.all_class.MyMethod
import theintership.my.all_class.SharePrefValue
import theintership.my.databinding.FragMessageBinding
import theintership.my.main_interface.message.adapter.adapter_rcv_layout_chat
import theintership.my.main_interface.message.viewmodel.ViewModelMessage


class frag_message : Fragment(R.layout.frag_message){

    private var _binding: FragMessageBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainInterFaceActivity: Main_Interface_Activity
    private lateinit var adapter: adapter_rcv_layout_chat
    private lateinit var viewModel: ViewModelMessage
    private val database: DatabaseReference = Firebase.database.reference
    private var account_ref : String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragMessageBinding.inflate(inflater, container, false)
        mainInterFaceActivity = activity as Main_Interface_Activity
        viewModel = ViewModelProvider(mainInterFaceActivity).get(ViewModelMessage::class.java)
        adapter = adapter_rcv_layout_chat(mainInterFaceActivity , mainInterFaceActivity)
        account_ref = SharePrefValue(mainInterFaceActivity).get_account_ref()

        binding.fragMessageRcv.layoutManager = LinearLayoutManager(mainInterFaceActivity)
        set_rcv_layout_chat(account_ref)

        return binding.root
    }

    private fun set_rcv_layout_chat(account_ref: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = viewModel.setup_list_item_in_layout_chat(snapshot)
                adapter.submitList(list)
                binding.fragMessageRcv.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        val ref = database.child("User")
            .child(account_ref)
            .child("friends")
            .child("real")
        ref.addValueEventListener(postListener)
    }



}