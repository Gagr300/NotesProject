package com.notesproject.fragments

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.notesproject.AdapterNote
import com.notesproject.R
import com.notesproject.databinding.FragmentNotesBinding
import com.notesproject.notes.Note
import java.util.*


class NotesFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var eventListener: ValueEventListener
    private lateinit var noteList: ArrayList<Note>
    private lateinit var adapter: AdapterNote

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotesBinding.inflate(inflater, container, false)

        val gridLayoutManager = GridLayoutManager(activity, 1)
        binding.rvNotes.layoutManager = gridLayoutManager
        binding.search.clearFocus()

        val builder = activity?.let { AlertDialog.Builder(it.applicationContext) }
        if (builder != null) {
            builder.setCancelable(false)
            builder.setView(R.layout.progress_layout)

            val dialog = builder.create()
            dialog.show()

            noteList = ArrayList()
            adapter = activity?.let { AdapterNote(it, noteList) }!!
            binding.rvNotes.adapter = adapter
            databaseReference = FirebaseDatabase.getInstance().getReference("Todo List")
            dialog.show()

            eventListener = databaseReference!!.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    noteList.clear()
                    for (itemSnapshot in snapshot.children) {
                        val dataClass = itemSnapshot.getValue(Note::class.java)
                        if ((dataClass != null) && (dataClass.dataUser
                                    == FirebaseAuth.getInstance().currentUser?.uid))
                            noteList.add(dataClass)
                    }

                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }

                override fun onCancelled(error: DatabaseError) {
                    dialog.dismiss()
                }
            })
        }



        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(activity,"Not found", Toast.LENGTH_SHORT).show()
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                searchList(newText)
                return true
            }
        })


        return binding.root
    }


    fun searchList(text: String) {
        val searchList = java.util.ArrayList<Note>()
        for (dataClass in noteList) {
            if (dataClass.dataTitle?.lowercase()
                    ?.contains(text.lowercase(Locale.getDefault())) == true
            ) {
                searchList.add(dataClass)
            }
        }
        adapter.searchDataList(searchList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}