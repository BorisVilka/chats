package org.rain.gifts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.rain.gifts.databinding.ListItemBinding
import java.time.format.DateTimeFormatter

class MyAdapter(var list: MutableList<Pair>) : RecyclerView.Adapter<MyAdapter.Companion.MyHolder>() {

    val form = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm")

    companion object {

        class MyHolder(var binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
       return MyHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.binding.textView5.text = list[position].txt
        holder.binding.textView6.text = list[position].date.format(form)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}