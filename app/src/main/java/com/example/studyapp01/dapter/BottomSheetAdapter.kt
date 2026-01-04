package com.example.studyapp01.dapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.studyapp01.R
import com.example.studyapp01.databinding.ItemBottomsheetLayoutBinding
import com.example.studyapp01.model.Competition

class BottomSheetAdapter : RecyclerView.Adapter<BottomSheetAdapter.ViewHolder>() {
    var datas = mutableListOf<Competition>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: ItemBottomsheetLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(position: Int) {
            val model = datas[position]
            binding.itemName.text = model.name
            binding.itemLayout.setBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.main_bg_color))
            binding.itemName.setTextColor(ContextCompat.getColor(binding.root.context,R.color.text_color))
            if (model.id != 100) {
                binding.itemLayout.setBackgroundResource(if (model.selected) R.color.teal_100 else R.color.white)
                if (model.headPosition > -1)
                    binding.itemLayout.setBackgroundResource(R.color.gray_300)
                binding.itemLayout.isEnabled = model.headPosition == -1
                binding.itemCheck.isVisible = model.selected
                binding.itemLayout.setOnClickListener { onItemClick(binding.root, position) }
            }else {
                binding.itemLayout.setBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.main_bg_color))
                binding.itemName.setTextColor(ContextCompat.getColor(binding.root.context,R.color.text_color))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBottomsheetLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = datas.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(position)
    }

    private lateinit var onItemClick: (View, Int) -> Unit
    fun setOnItemClickListener(listener: (View, Int) -> Unit) {
        onItemClick = listener
    }
}
