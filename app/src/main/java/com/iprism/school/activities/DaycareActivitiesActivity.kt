package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.iprism.school.databinding.ActivityDaycareActivitiesBinding

class DaycareActivitiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaycareActivitiesBinding
    private var planId = ""
    private var studentId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDaycareActivitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        planId = intent.getStringExtra("planId").toString()
        studentId = intent.getStringExtra("studentId").toString()
        handleBack()
        handleNapLo()
        handleMedicationLo()
        handleNotesLo()
        handleDiaperLo()
        handleActivityLo()
        handleFoodLo()
        handleAttachmentLo()
        handleMoodLo()
    }

    private fun handleAttachmentLo() {
        binding.attachmentLo.setOnClickListener { view ->
            var intent = Intent(this, AttachmentActivity::class.java)
            intent.putExtra("planId", planId)
            intent.putExtra("studentId", studentId)
            intent.putExtra("type", "Attachment")
            intent.putExtra("id", "8")
            startActivity(intent)
        }
    }

    private fun handleActivityLo() {
        binding.activityLo.setOnClickListener { view ->
            var intent = Intent(this, PlayActivity::class.java)
            intent.putExtra("planId", planId)
            intent.putExtra("studentId", studentId)
            intent.putExtra("type", "Activity")
            intent.putExtra("id", "1")
            startActivity(intent)
        }
    }

    private fun handleFoodLo() {
        binding.foodLo.setOnClickListener { view ->
            var intent = Intent(this, FoodActivity::class.java)
            intent.putExtra("planId", planId)
            intent.putExtra("studentId", studentId)
            intent.putExtra("type", "Food")
            intent.putExtra("id", "2")
            startActivity(intent)
        }
    }

    private fun handleMoodLo() {
        binding.moodLo.setOnClickListener { view ->
            var intent = Intent(this, MoodActivity::class.java)
            intent.putExtra("planId", planId)
            intent.putExtra("studentId", studentId)
            intent.putExtra("type", "Mood")
            intent.putExtra("id", "3")
            startActivity(intent)
        }
    }

    private fun handleDiaperLo() {
        binding.diaperLo.setOnClickListener { view ->
            var intent = Intent(this, DiaperActivity::class.java)
            intent.putExtra("planId", planId)
            intent.putExtra("studentId", studentId)
            intent.putExtra("type", "Diaper")
            intent.putExtra("id", "8")
            startActivity(intent)
        }
    }


    private fun handleMedicationLo() {
        binding.medicationLo.setOnClickListener { view ->
            var intent = Intent(this, MedicationAndNotesActivity::class.java)
            intent.putExtra("planId", planId)
            intent.putExtra("studentId", studentId)
            intent.putExtra("type", "Medication")
            intent.putExtra("id", "6")
            startActivity(intent)
        }
    }

    private fun handleNotesLo() {
        binding.notesLo.setOnClickListener { view ->
            var intent = Intent(this, MedicationAndNotesActivity::class.java)
            intent.putExtra("planId", planId)
            intent.putExtra("studentId", studentId)
            intent.putExtra("type", "Notes")
            intent.putExtra("id", "4")
            startActivity(intent)
        }
    }

    private fun handleNapLo() {
        binding.napsLo.setOnClickListener { view ->
            var intent = Intent(this, NapActivity::class.java)
            intent.putExtra("planId", planId)
            intent.putExtra("studentId", studentId)
            intent.putExtra("type", "nap")
            intent.putExtra("id", "7")
            startActivity(intent)
        }
    }


    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

}