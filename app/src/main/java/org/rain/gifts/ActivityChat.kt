package org.rain.gifts

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.rain.gifts.databinding.ActivityChatBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ActivityChat : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            // get input stream
            val ims = assets.open("bg.jpg")
            // load image as Drawable
            val d = Drawable.createFromStream(ims, null)
            // set image to ImageView
            binding.imageView.setImageDrawable(d)
            ims.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
            return
        }
        var s = getSharedPreferences("prefs",Context.MODE_PRIVATE).getStringSet("data",HashSet<String>())
        var l = mutableListOf<Pair>()
        val form = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm")
        for(i in s!!) {
            val tmp = i.split("~")
            l.add(Pair(
                tmp[0],
                LocalDateTime.from(form.parse(tmp[1]))
            ))
        }
        l.sortBy { it.date }
        val adapter = MyAdapter(l)
        binding.list.adapter = adapter
        binding.imageButton.setOnClickListener {
            if(binding.edit.text.toString().isNotEmpty()) {
                val p = Pair(binding.edit.text.toString(), LocalDateTime.now())
                l.add(p)
                adapter.notifyItemInserted(l.size-1)
                val s1 = l.map { it.txt+"~"+it.date.format(form) }.toSet()
                getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putStringSet("data",s1).apply()
                binding.edit.setText("")
            }
        }
    }
}
data class Pair(
    val txt: String = "",
    val date: LocalDateTime = LocalDateTime.now()
)