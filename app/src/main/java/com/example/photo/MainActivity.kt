package com.example.photo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Transformations.map
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.Map as Map

class MainActivity : AppCompatActivity() {

    private lateinit var mDiaryAdapter: DiaryListAdapter
    private lateinit var mDiaryArrayList: ArrayList<Diary>
    private lateinit var mDiaryRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDiaryAdapter = DiaryListAdapter(this)
        mDiaryArrayList = ArrayList<Diary>()

        val title = "熱海旅行"
        val content = "夏のグループ旅行"
        val name = "iagaki"
        val diary = Diary(title, content, name)

        mDiaryArrayList.add(diary)

        mDiaryAdapter.mDiaryList = mDiaryArrayList
        diaryListView.adapter = mDiaryAdapter
        mDiaryAdapter.notifyDataSetChanged()

        val dataBaseReference = FirebaseDatabase.getInstance().reference
        val data2 = "●●の旅"

        val testRef = dataBaseReference.child(DiaryPATH).child(data2)
        val data = HashMap<String, String>()
        data["title"] = "タイトルだよーん"
        data["content"] = "内容だよーん"
        data["name"] = "名前だよーん"
        testRef.push().setValue(data)

        val data3 = HashMap<String, String>()
        val testRef3 = dataBaseReference.child(DiaryPATH).child(data2).child("-N5NLnkjj-5_dFEFL13v")
        testRef3.removeValue()

        toLoginPage.setOnClickListener{
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        }
    }
}
