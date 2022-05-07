package theintership.my.main_interface.profile.dialog

import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import theintership.my.R

class dialog_set_avatar_and_coverImage(context: Context ) : BottomSheetDialog(context) {

    lateinit var btn_take_picture: LinearLayout
    lateinit var btn_chose_from_gallery: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_bottom_set_avatar_and_coverimage)

        btn_take_picture = findViewById<LinearLayout>(R.id.btn_take_picture)!!
        btn_chose_from_gallery= findViewById<LinearLayout>(R.id.btn_chose_from_gallery)!!


    }
}