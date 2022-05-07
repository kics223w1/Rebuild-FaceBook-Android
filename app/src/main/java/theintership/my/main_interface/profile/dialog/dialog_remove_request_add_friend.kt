package theintership.my.main_interface.profile.dialog

import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_bottom_remove_request_add_friend.*
import theintership.my.R

class dialog_remove_request_add_friend(context: Context ) : BottomSheetDialog(context) {

    lateinit var btn_remove_request: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_bottom_remove_request_add_friend)

        btn_remove_request = findViewById<LinearLayout>(R.id.btn_remove_request)!!


    }
}