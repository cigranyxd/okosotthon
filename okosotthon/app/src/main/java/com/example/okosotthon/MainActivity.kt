package com.example.okosotthon

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

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
        }

        // Biztonsági rendszer eszköz
        class SecuritySystem(name: String) : Device(name) {
            private var isArmed: Boolean = false

            override fun turnOn() {
                isArmed = true
                println("$name: A biztonsági rendszer élesítve")
            }

            override fun turnOff() {
                isArmed = false
                println("$name: A biztonsági rendszer hatástalanítva")
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

        fun main() {
            val smartHomeController = SmartHomeController()

            val livingRoomLight = Light("Nappali Lámpa")
            val kitchenLight = Light("Konyhai Lámpa")
            val thermostat = Thermostat("Termosztát", 22.0)
            val securitySystem = SecuritySystem("Biztonsági Rendszer")

            smartHomeController.addDevice(livingRoomLight)
            smartHomeController.addDevice(kitchenLight)
            smartHomeController.addDevice(thermostat)
            smartHomeController.addDevice(securitySystem)

            // Minden eszköz bekapcsolása
            smartHomeController.turnOnAllDevices()

            // Egyedi eszköz vezérlése
            smartHomeController.controlDevice("Termosztát", "off")
            smartHomeController.controlDevice("Nappali Lámpa", "off")
            smartHomeController.controlDevice("Konyhai Lámpa", "on")
        }


        //Váltás a lámpák kezelőfelületére
        val LampakClick = findViewById<Button>(R.id.LampakBtn)
        LampakClick.setOnClickListener {
            val intent = Intent(this, LampakActivity::class.java)
            startActivity(intent)
        }

        val TermosztatClick = findViewById<Button>(R.id.TermosztatBtn)
        TermosztatClick.setOnClickListener{
            val intent = Intent(this, Termosztat::class.java)
            startActivity(intent)
        }


    }
}