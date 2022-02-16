package theintership.my.signin_signup

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import theintership.my.R
import theintership.my.model.language

class dialog_show_language_select(context: Context) : Dialog(context) , rcv_language_adapter.Interaction {
    init {
        setCancelable(false)
    }

    private lateinit var languageAdapter : rcv_language_adapter
    private lateinit var rcv : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_show_language_select)

        val btn_cancel = findViewById<TextView>(R.id.btn_cancel)
        val list = initlist()
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context)

        rcv = findViewById(R.id.rcv_dialog_showlanguage)
        languageAdapter = rcv_language_adapter(this)
        languageAdapter.submitList(list)

        rcv.layoutManager = layoutManager
        rcv.adapter = languageAdapter

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