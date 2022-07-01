package com.example.photo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_diary.*
import kotlinx.android.synthetic.main.activity_main.*

class AddDiaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diary)

        addbtn.setOnClickListener{
            val dataBaseReference = FirebaseDatabase.getInstance().reference
            val data = HashMap<String, String>()
            val addRef = dataBaseReference.child(DiaryPATH)
            data["title"] = "タイトル"
            data["contents"] = "内容"
            data["name"] = "名前"
            addRef.push().setValue(data)
        }

    }


}