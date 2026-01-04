package com.example.studyapp01.ui.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.studyapp01.databinding.FragMineBinding

class MineFragment : Fragment() {
    private lateinit var binding: FragMineBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragMineBinding.inflate(inflater,container,false)
        return binding.root
    }
}