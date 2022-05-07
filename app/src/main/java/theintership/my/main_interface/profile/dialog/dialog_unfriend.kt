package theintership.my.main_interface.profile.dialog

import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import theintership.my.R

class dialog_unfriend(context: Context ) : BottomSheetDialog(context) {

    lateinit var btn_unfriend: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_bottom_unfriend)

        btn_unfriend = findViewById<LinearLayout>(R.id.btn_unfriend)!!


    }
}