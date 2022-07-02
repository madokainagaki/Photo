package com.example.photo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_diary.*
import kotlinx.android.synthetic.main.activity_main.*
import java.security.Key
import java.util.concurrent.ThreadLocalRandom.current

class AddDiaryActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diary)

        val countInt : Int = countDiary.toInt() +1

        addbtn.setOnClickListener{
            var title  = editTitle.getText().toString()
            if (title.length == 0){
             title = "無名の旅($countInt)"
            }
            var contents = editContents.getText().toString()
            if (contents.length == 0){
                contents = "記録がありません"
            }
            var place = editPlace.getText().toString()
            if (place.length == 0){
                place = "記録がありません"
            }

            val dataBaseReference = FirebaseDatabase.getInstance().reference
            val data = HashMap<String, String>()
            val addRef = dataBaseReference.child(DiaryPATH)
            data["title"] = title.toString()
            data["contents"] = contents.toString()
            data["name"] = place.toString()
            data["uid"] = FirebaseAuth.getInstance().currentUser?.uid ?: ""

            addRef.push().setValue(data)

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

    }


}