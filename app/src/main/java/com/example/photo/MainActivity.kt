package com.example.photo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.lifecycle.Transformations.map
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.Map as Map

class MainActivity : AppCompatActivity() {

    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mAdapter: DiaryListAdapter
    private lateinit var mDiaryArrayList: ArrayList<Diary>

    private var mTestRef: DatabaseReference? = null

    private val mEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

            val map = dataSnapshot.value as Map<String, String>
            val title = map["title"] ?: ""
            val body = map["body"] ?: ""
            val name = map["name"] ?: ""

            val diary = Diary(title, body, name)
            mDiaryArrayList.add(diary)
            mAdapter.notifyDataSetChanged()
            Log.d("test", mDiaryArrayList.toString())
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }


        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            TODO("Not yet implemented")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdapter = DiaryListAdapter(this)
        mDiaryArrayList = ArrayList<Diary>()

        mAdapter.mDiaryList = mDiaryArrayList
        diaryListView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
//
        val dataBaseReference = FirebaseDatabase.getInstance().reference
        val data2 = "●●の旅"



//削除するときはこれ
//  .removeValue()

        toLoginPage.setOnClickListener{

//            val intent = Intent(applicationContext, LoginActivity::class.java)
//            startActivity(intent)

            val data = HashMap<String, String>()
            val testRef = dataBaseReference.child(DiaryPATH).child("1")
            data["title"] = "タイトルだよーん"
            data["content"] = "内容だよーん"
            data["name"] = "名前だよーん"
            testRef.push().setValue(data)
        }

        diaryListView.setOnItemClickListener{parent, view, position, id ->
            Log.d("test", "$position 番目が押されましたよ")
        }

        mDiaryArrayList.clear()
        mAdapter.setDiaryArrayList(mDiaryArrayList)
        diaryListView.adapter = mAdapter

        mDatabaseReference = FirebaseDatabase.getInstance().reference

        mTestRef = mDatabaseReference.child(DiaryPATH).child("1")
        mTestRef!!.addChildEventListener(mEventListener)
    }
}
