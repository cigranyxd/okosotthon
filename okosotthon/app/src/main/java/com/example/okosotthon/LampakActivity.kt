package com.example.okosotthon

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LampakActivity : AppCompatActivity() {

    // Vezérlő és lámpák
    private val smartHomeController = SmartHomeController()
    private val livingRoomLight = Light("Nappali Lámpa")
    private val kitchenLight = Light("Konyhai Lámpa")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lampak)

        // Lámpák hozzáadása a vezérlőhöz
        smartHomeController.addDevice(livingRoomLight)
        smartHomeController.addDevice(kitchenLight)

        // Kapcsolók inicializálása és hozzáadása a UI-hoz
        val livingRoomSwitch = findViewById<Switch>(R.id.livingRoomSwitch)
        val kitchenSwitch = findViewById<Switch>(R.id.kitchenSwitch)

        // Nappali kapcsoló kezelése
        livingRoomSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                smartHomeController.controlDevice("Nappali Lámpa", "on")
                Toast.makeText(this, "Nappali lámpa bekapcsolva", Toast.LENGTH_SHORT).show()
            } else {
                smartHomeController.controlDevice("Nappali Lámpa", "off")
                Toast.makeText(this, "Nappali lámpa kikapcsolva", Toast.LENGTH_SHORT).show()
            }
        }

        // Konyhai kapcsoló kezelése
        kitchenSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                smartHomeController.controlDevice("Konyhai Lámpa", "on")
                Toast.makeText(this, "Konyhai lámpa bekapcsolva", Toast.LENGTH_SHORT).show()
            } else {
                smartHomeController.controlDevice("Konyhai Lámpa", "off")
                Toast.makeText(this, "Konyhai lámpa kikapcsolva", Toast.LENGTH_SHORT).show()
            }
        }

        // Minden lámpa fel/lekapcsolása gombbal
        val turnOnAllBtn = findViewById<Button>(R.id.turnOnAllBtn)
        val turnOffAllBtn = findViewById<Button>(R.id.turnOffAllBtn)

        turnOnAllBtn.setOnClickListener {
            smartHomeController.turnOnAllDevices()
            livingRoomSwitch.isChecked = true
            kitchenSwitch.isChecked = true
            Toast.makeText(this, "Minden lámpa bekapcsolva", Toast.LENGTH_SHORT).show()
        }

        turnOffAllBtn.setOnClickListener {
            smartHomeController.turnOffAllDevices()
            livingRoomSwitch.isChecked = false
            kitchenSwitch.isChecked = false
            Toast.makeText(this, "Minden lámpa kikapcsolva", Toast.LENGTH_SHORT).show()
        }
    }

    // Eszközök absztrakt osztálya
    abstract class Device(val name: String) {
        abstract fun turnOn()
        abstract fun turnOff()
    }

    // Lámpa eszköz
    class Light(name: String) : Device(name) {
        private var isOn: Boolean = false

        override fun turnOn() {
            isOn = true
            println("$name: A lámpa bekapcsolva")
        }

        override fun turnOff() {
            isOn = false
            println("$name: A lámpa kikapcsolva")
        }
    }

    // Okosotthon vezérlő
    class SmartHomeController {
        private val devices = mutableListOf<Device>()

        fun addDevice(device: Device) {
            devices.add(device)
            println("${device.name} hozzáadva a rendszerhez.")
        }

        fun turnOnAllDevices() {
            devices.forEach { it.turnOn() }
        }

        fun turnOffAllDevices() {
            devices.forEach { it.turnOff() }
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
