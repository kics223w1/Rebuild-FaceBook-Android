package theintership.my.signin_signup.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import theintership.my.R
import theintership.my.model.language
import theintership.my.signin_signup.adapter_language

class dialog_showlanguage(context: Context) : Dialog(context) , adapter_language.Interaction {
    init {
        setCancelable(false)
    }

    private lateinit var languageAdapterLanguage : adapter_language
    private lateinit var rcv : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_show_language_select)

        val btn_cancel = findViewById<TextView>(R.id.btn_dialog_showLanguage_select_cancel)
        val list = initlist()
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context)

        rcv = findViewById(R.id.rcv_dialog_showlanguage)
        languageAdapterLanguage = adapter_language(this)
        languageAdapterLanguage.submitList(list)

        rcv.layoutManager = layoutManager
        rcv.adapter = languageAdapterLanguage

        btn_cancel.setOnClickListener {
            dismiss()
        }

    }

    private fun initlist() : List<language>{
        var list : MutableList<language> = mutableListOf()
        list.add(language(1 , "Ngôn ngữ của thiết bị"))
        list.add(language(2 , "Vietnamese"))
        list.add(language(3 , "English"))
        list.add(language(4 , "Korean"))
        list.add(language(5 , "ThaiLand"))
        list.add(language(6 , "Russian"))
        list.add(language(7 , "Japanese"))
        list.add(language(8 , "Germany"))
        list.add(language(9 , "Indian"))
        list.add(language(10 , "Chinese"))
        list.add(language(11 , "Taiwan"))
        return list
    }

    override fun onItemSelected(position: Int, item: language) {
        dismiss()
    }


}