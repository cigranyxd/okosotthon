package com.example.okosotthon

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Termosztat : AppCompatActivity() {

    // Vezérlő és termosztát
    private val smartHomeController = SmartHomeController()
    private val thermostat = Thermostat("Termosztát", 22.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_termosztat)

        // Eszköz hozzáadása a vezérlőhöz
        smartHomeController.addDevice(thermostat)

        // Hőmérséklet kijelző TextView
        val temperatureDisplay = findViewById<TextView>(R.id.temperatureDisplay)

        // Csúszka (SeekBar) a hőmérséklet beállításához
        val temperatureSeekBar = findViewById<SeekBar>(R.id.temperatureSeekBar)
        temperatureSeekBar.progress = thermostat.getTemperature().toInt()

        // Hőmérséklet csúszka változásának kezelése
        temperatureSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                thermostat.setTemperature(progress.toDouble())
                temperatureDisplay.text = "Hőmérséklet: $progress°C"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Kapcsoló gombok a termosztát ki/bekapcsolásához
        val turnOnButton = findViewById<Button>(R.id.turnOnButton)
        val turnOffButton = findViewById<Button>(R.id.turnOffButton)

        turnOnButton.setOnClickListener {
            smartHomeController.controlDevice("Termosztát", "on")
            Toast.makeText(this, "Termosztát bekapcsolva", Toast.LENGTH_SHORT).show()
        }

        turnOffButton.setOnClickListener {
            smartHomeController.controlDevice("Termosztát", "off")
            Toast.makeText(this, "Termosztát kikapcsolva", Toast.LENGTH_SHORT).show()
        }
    }

    // Eszközök absztrakt osztálya
    abstract class Device(val name: String) {
        abstract fun turnOn()
        abstract fun turnOff()
    }

    // Termosztát eszköz
    class Thermostat(name: String, private var temperature: Double) : Device(name) {

        override fun turnOn() {
            println("$name: A termosztát bekapcsolva, hőmérséklet: $temperature°C")
        }

        override fun turnOff() {
            println("$name: A termosztát kikapcsolva")
        }

        fun setTemperature(newTemp: Double) {
            temperature = newTemp
            println("$name: A hőmérséklet beállítva $newTemp°C-ra")
        }

        fun getTemperature(): Double {
            return temperature
        }
    }

    // Okosotthon vezérlő
    class SmartHomeController {
        private val devices = mutableListOf<Device>()

        fun addDevice(device: Device) {
            devices.add(device)
            println("${device.name} hozzáadva a rendszerhez.")
        }

        fun controlDevice(name: String, action: String) {
            val device = devices.find { it.name == name }
            if (device != null) {
                when (action) {
                    "on" -> device.turnOn()
                    "off" -> device.turnOff()
                    else -> println("Ismeretlen parancs: $action")
                }
            } else {
                println("Eszköz nem található: $name")
            }
        }
    }
}
