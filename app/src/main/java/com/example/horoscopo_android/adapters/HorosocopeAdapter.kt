package com.example.horoscopo_android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Transition
import com.example.horoscopo_android.R
import com.example.horoscopo_android.data.Horoscope
import com.example.horoscopo_android.utils.SessionManager

class HorosocopeAdapter(
    var items: List<Horoscope>,
    val onClickListener: (Int) -> Unit
) : RecyclerView.Adapter<HorosocopeViewHolder>() {

    // Cuál es la vista para los elementos
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HorosocopeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_horoscopo, parent, false)
        return HorosocopeViewHolder(view)
    }

    // Cuales son los datos para el elemento
    override fun onBindViewHolder(
        holder: HorosocopeViewHolder,
        position: Int
    ) {
        val item = items[position]
        holder.render(item)
        holder.itemView.setOnClickListener {
            onClickListener(position)
        }
    }

    // Cuantos elemntos se quieren listar
    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(items: List<Horoscope>){
        this.items = items
        notifyDataSetChanged()
    }

}

class HorosocopeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val nameTextView: TextView = view.findViewById(R.id.nameTextView)
    val dateTextView: TextView = view.findViewById(R.id.dateTextView)
    val iconImageView: ImageView = view.findViewById(R.id.iconImageView)
    val favoriteImageView: ImageView = view.findViewById(R.id.favoriteImageView)

    fun render(horoscope: Horoscope) {
        nameTextView.setText(horoscope.name)
        dateTextView.setText(horoscope.dates)
        iconImageView.setImageResource(horoscope.icon)

        val session = SessionManager(itemView.context)
        if(session.isFavorite(horoscope.id, horoscope.id)){
            favoriteImageView.visibility = View.VISIBLE
//            favoriteImageView.isVisible = true
        }
        else {
            favoriteImageView.visibility = View.GONE
//            favoriteImageView.isVisible = false
        }
    }

}