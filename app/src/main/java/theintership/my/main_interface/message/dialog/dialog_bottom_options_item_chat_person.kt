package theintership.my.main_interface.message.dialog

import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import theintership.my.R

class dialog_bottom_options_item_chat_person(context : Context) : BottomSheetDialog(context){

    lateinit var btn_reply: RelativeLayout
    lateinit var btn_copy : RelativeLayout
    lateinit var btn_edit : RelativeLayout
    lateinit var btn_remove : RelativeLayout
    lateinit var layout_options : LinearLayout
    lateinit var layout_edit_chat : LinearLayout
    lateinit var btn_back_to_options : ImageView
    lateinit var btn_ok_edit_chat : ImageView
    lateinit var edt_edit_chat : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_bottom_options_item_chat_person)

        btn_reply = findViewById(R.id.btn_reply)!!
        btn_copy= findViewById(R.id.btn_copy)!!
        btn_edit = findViewById(R.id.btn_forward)!!
        btn_remove = findViewById(R.id.btn_remove)!!
        layout_options = findViewById(R.id.layout_options)!!
        layout_edit_chat = findViewById(R.id.layout_edt_chat)!!
        btn_back_to_options = findViewById(R.id.btn_back_options)!!
        btn_ok_edit_chat = findViewById(R.id.btn_ok_edit)!!
        edt_edit_chat = findViewById(R.id.edt_edit_chat)!!
    }

}