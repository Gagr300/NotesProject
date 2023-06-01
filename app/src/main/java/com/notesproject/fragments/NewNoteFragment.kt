package com.notesproject.fragments


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.notesproject.databinding.FragmentNewNoteBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.notesproject.R
import com.notesproject.notes.Note
import java.text.DateFormat
import java.util.Calendar

class NewNoteFragment : Fragment() {

    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!

    var imageURL: String? = null
    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewNoteBinding.inflate(inflater, container, false)

        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Toast.makeText(activity,"Image Selected", Toast.LENGTH_SHORT).show()
                val data = result.data
                uri = data!!.data
                binding.uploadImage.setImageURI(uri)
            } else {
                Toast.makeText(activity,"No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.uploadImage.setOnClickListener {
            Toast.makeText(activity,"binding.uploadImage", Toast.LENGTH_SHORT).show()
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        binding.saveButton.setOnClickListener {
            Toast.makeText(activity,"binding.saveButton", Toast.LENGTH_SHORT).show()
            saveData()
        }


        return binding.root
    }

    private fun saveData(){
        val storageReference = FirebaseStorage.getInstance().reference
            .child("Task Images")
            .child(uri!!.lastPathSegment!!)

        val builder = activity?.let { AlertDialog.Builder(it.applicationContext) }

        if (builder != null) {
            builder.setCancelable(false)
            builder.setView(R.layout.progress_layout)
            val dialog = builder.create()
            dialog.show()
            storageReference.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isComplete);
                val urlImage = uriTask.result
                imageURL = urlImage.toString()
                uploadData()
                dialog.dismiss()
            }.addOnFailureListener {
                dialog.dismiss()
            }
        }

    }

    private fun uploadData(){
        val title = binding.uploadTitle.text.toString()
        val desc = binding.uploadDesc.text.toString()
        val date = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        val dataClass = FirebaseAuth.getInstance().currentUser?.let{Note(it.uid, title, desc, date, imageURL) }

        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        FirebaseDatabase.getInstance().getReference("Notes").child(currentDate)
            .setValue(dataClass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, "Saved", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText( activity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}
