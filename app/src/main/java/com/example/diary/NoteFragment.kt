package com.example.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.diary.databinding.FragmentNoteBinding
import com.example.diary.models.NoteRequest
import com.example.diary.models.NoteResponse
import com.example.diary.utils.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding : FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private var note : NoteResponse? = null
    private val noteViewModel by viewModels<NoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        bindHandlers()
        bindObservers()
    }

    private fun bindObservers() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Error -> {
                    findNavController().popBackStack()
                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {

                }
            }
        })
    }

    private fun bindHandlers() {
        binding.btnDelete.setOnClickListener{
            note?.let {
                noteViewModel.deleteNote(it!!._id)
            }
        }
        binding.apply {
            binding.btnSubmit.setOnClickListener{
                val title = binding.txtTitle.text.toString()
                val desc = binding.txtDescription.text.toString()
                val noteRequest = NoteRequest(title, desc)
                if(note == null){
                    noteViewModel.createNote(noteRequest)
                }else{
                    noteViewModel.updateNote(note!!._id, noteRequest)
                }
            }
        }

    }

    private fun setInitialData() {
        val json_note = arguments?.getString("note")
        if(json_note != null){
            note = Gson().fromJson<NoteResponse>(json_note, NoteResponse::class.java)
            note?.let {
                binding.txtTitle.setText(it.title)
                binding.txtDescription.setText(it.description)
            }
        }else{
            binding.addEditText.text = "Add Note"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}