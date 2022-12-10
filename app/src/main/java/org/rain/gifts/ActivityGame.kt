package org.rain.gifts


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.rain.gifts.databinding.ActivityGameBinding


class ActivityGame : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private var ind = 0
    private var a = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)






        }
    }
