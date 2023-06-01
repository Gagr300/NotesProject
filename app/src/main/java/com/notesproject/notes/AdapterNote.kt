package com.notesproject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.notesproject.notes.Note

class AdapterNote(
    private val context: Context,
    private var noteList: List<Note>)
    : RecyclerView.Adapter<AdapterNote.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : NoteViewHolder {

        val itemVew = LayoutInflater.from(parent.context).
        inflate(R.layout.item_fragment, parent, false)

        return NoteViewHolder(itemVew)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val currentItem = noteList[position]

        Glide.with(context).load(currentItem.dataImage).into(holder.recImage)

        holder.recTitle.text = currentItem.dataTitle
        holder.recDesc.text = currentItem.dataDesc
        holder.recDate.text = currentItem.dataDate

        holder.recCard.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("Image", noteList[holder.adapterPosition].dataImage)
            intent.putExtra("Description", noteList[holder.adapterPosition].dataDesc)
            intent.putExtra("Title", noteList[holder.adapterPosition].dataTitle)
            intent.putExtra("Date", noteList[holder.adapterPosition].dataDate)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun searchDataList(searchList: List<Note>) {
        noteList = searchList
        notifyDataSetChanged()
    }

    class NoteViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val recImage: ImageView = itemView.findViewById(R.id.recImage)
        val recTitle: TextView = itemView.findViewById(R.id.recTitle)
        val recDesc: TextView = itemView.findViewById(R.id.recDesc)
        val recDate: TextView = itemView.findViewById(R.id.recDate)
        val recCard: CardView = itemView.findViewById(R.id.recCard)

    }
}

