package org.rain.gifts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.rain.gifts.databinding.ActivityEndBinding

class EndActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEndBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textView4.text = "Дано правильных ответов ${intent.getStringExtra("count")}/10"
        binding.button3.setOnClickListener {
            startActivity(Intent(applicationContext, ActivityGame::class.java))
            finish()
        }
    }
}