package theintership.my.signin_signup.dialog

import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import theintership.my.R

class dialog_bottom_sex(context: Context) : BottomSheetDialog(context) {

    lateinit var layout_her: LinearLayout
    lateinit var layout_him: LinearLayout
    lateinit var layout_them: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_bottom_sex)

        val btn_close = findViewById<ImageView>(R.id.btn_dialog_bottom_sex_close)
        layout_her = findViewById(R.id.layout_dialog_bottom_sex_her)!!
        layout_him = findViewById(R.id.layout_dialog_bottom_sex_him)!!
        layout_them = findViewById(R.id.layout_dialog_bottom_sex_them)!!

        btn_close?.setOnClickListener {
            dismiss()
        }



    }
}