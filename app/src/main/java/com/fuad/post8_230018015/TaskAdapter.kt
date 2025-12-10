package com.fuad.post8_230018015

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.fuad.post8_230018015.databinding.ItemTaskBinding

class TaskAdapter(
    private val list: List<Task>,
    private val ref: DatabaseReference
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.cbSelesai.isChecked = task.selesai
            binding.tvTask.text = task.task
            binding.tvDeskripsi.text = task.deskripsi
            binding.tvTanggal.text = task.tanggal

            binding.cbSelesai.setOnCheckedChangeListener { _, isChecked ->
                ref.child(task.id!!).child("selesai").setValue(isChecked)
            }

            binding.root.alpha = if (task.selesai) 0.5f else 1f

            binding.btnHapus.setOnClickListener {
                ref.child(task.id!!).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(
                            binding.root.context,
                            "Tugas berhasil dihapus",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}