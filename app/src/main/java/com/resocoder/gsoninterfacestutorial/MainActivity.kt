package com.resocoder.gsoninterfacestutorial

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_save_random_fruit.setOnClickListener {
            val random = Random()
            val randNum = random.nextInt(2)

            val fruit =
                    if (randNum == 0)
                        Lemon(acidity = random.nextInt(5))
                    else
                        Watermelon(isSeedless = random.nextBoolean(),
                                color = if (random.nextInt(2) == 0) "red" else "yellow")

            saveFruitToPreferences(fruit)
        }

        btn_load_fruit.setOnClickListener {
            val fruit = getFruitFromPreferences()
            textView.text = fruit.toString()
        }
    }

    private val gsonForFruit: Gson by lazy {
        GsonBuilder()
                .registerTypeAdapter(Fruit::class.java, FruitTypeAdapter())
                .create()
    }

    private fun saveFruitToPreferences(fruit: Fruit) {
        val prefEditor = PreferenceManager.getDefaultSharedPreferences(this).edit()
        val jsonString = gsonForFruit.toJson(fruit, Fruit::class.java)
        prefEditor.putString("fruit", jsonString).apply()
    }

    private fun getFruitFromPreferences(): Fruit {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val jsonString = preferences.getString("fruit", null)
        return gsonForFruit.fromJson(jsonString, Fruit::class.java)
    }
}
