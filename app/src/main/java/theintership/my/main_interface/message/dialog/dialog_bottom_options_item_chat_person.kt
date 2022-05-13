package theintership.my.main_interface.message.dialog

import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.RelativeLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import theintership.my.R

class dialog_bottom_options_item_chat_person(context : Context) : BottomSheetDialog(context){

    lateinit var btn_reply: RelativeLayout
    lateinit var btn_copy : RelativeLayout
    lateinit var btn_forward : RelativeLayout
    lateinit var btn_remove : RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_bottom_options_item_chat_person)

        btn_reply = findViewById(R.id.btn_reply)!!
        btn_copy= findViewById(R.id.btn_copy)!!
        btn_forward = findViewById(R.id.btn_forward)!!
        btn_remove = findViewById(R.id.btn_remove)!!


    }

}