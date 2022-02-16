package theintership.my.signin_signup

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.model.language

class adapter_language(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<language>() {

        override fun areItemsTheSame(oldItem: language, newItem: language): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: language, newItem: language): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rcv_item_language,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<language>) {
        differ.submitList(list)
    }

    class ViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        lateinit var tv_name: TextView
        lateinit var btn_chose: TextView

        fun bind(item: language) = with(itemView) {
            btn_chose = itemView.findViewById(R.id.btn_itemlanguage_chose)
            tv_name = itemView.findViewById(R.id.tv_itemlanguage_name)
            tv_name.text = item.name

            btn_chose.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: language)
    }
}
