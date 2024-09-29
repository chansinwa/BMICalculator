package com.example.lab1_bmi

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var heightUnitSpinner: Spinner
    private lateinit var weightUnitSpinner: Spinner
    private lateinit var selectedHeightUnit: String
    private lateinit var selectedWeightUnit: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val calculateButton = findViewById<Button>(R.id.calculateButton)
        val weight = findViewById<EditText>(R.id.weight)
        val height = findViewById<EditText>(R.id.height)
        val clearButton = findViewById<Button>(R.id.clearButton)
        val result = findViewById<TextView>(R.id.result)

        heightUnitSpinner = findViewById(R.id.heightUnitSpinner)
        weightUnitSpinner = findViewById(R.id.weightUnitSpinner)
        val heightItems = listOf("ft", "inch", "cm")
        val weightItems = listOf("lb", "kg")

        val heightAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, heightItems)
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        heightUnitSpinner.adapter = heightAdapter

        heightUnitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedHeightUnit = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        val weightAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, weightItems)
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        weightUnitSpinner.adapter = weightAdapter

        weightUnitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedWeightUnit = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }


        calculateButton.setOnClickListener{
            val weightString = weight.text.toString()
            val heightString = height.text.toString()
            var weightDouble = weightString.toDouble()
            var heightDouble = heightString.toDouble()

            heightDouble = when(selectedHeightUnit){
                "cm" -> heightDouble / 100 //convert cm to m
                "ft" -> heightDouble * 0.3048
                "inch" -> heightDouble * 0.0254
                else -> heightDouble //Default case
            }

            weightDouble = when(selectedWeightUnit){
                "lb" -> weightDouble * 0.4536 //convert lb to kg
                else -> weightDouble
            }


            val bmi = weightDouble / (heightDouble * heightDouble)
            val category = getBMICategory(bmi)
            result.text = "Your BMI: %.2f (%s)".format(bmi, category)
        }
        clearButton.setOnClickListener{
            weight.setText("")
            height.setText("")
            result.text = ""
        }
    }
    private fun getBMICategory(bmi: Double):String {
        return when{
            bmi < 18.5 -> "Underweight"
            bmi < 24.9 -> "Normal weight"
            bmi < 29.9 -> "Overweight"
            else -> "Obese"
        }
    }
}