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

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {

    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mAdapter: DiaryListAdapter
    private lateinit var mDiaryArrayList: ArrayList<Diary>

    private var mTestRef: DatabaseReference? = null

    private val mEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

            val map = dataSnapshot.value as Map<String, String>
            val title = map["title"] ?: ""
            val contents = map["contents"] ?: ""
            val name = map["name"] ?: ""
            val diaryUid = map["diaryUid"] ?: ""

            val diary = Diary(title, contents, name, dataSnapshot.key ?: "")
            Log.d("test" , diaryUid)

            mDiaryArrayList.add(diary)
            mAdapter.notifyDataSetChanged()
        }

        override fun onCancelled(error: DatabaseError) {
        }


        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
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

        toLoginPage.setOnClickListener{

            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)

            val data = HashMap<String, String>()
            val testRef = dataBaseReference.child(DiaryPATH)
            data["title"] = "タイトル"
            data["contents"] = "内容"
            data["name"] = "名前"
            data["diaryUid"] = "uid"
            testRef.push().setValue(data)
        }

        diaryListView.setOnItemClickListener{parent, view, position, id ->
            val diaryUid = mDiaryArrayList[position].diaryUid
            Log.d("test", diaryUid)
        }

        mDiaryArrayList.clear()
        mAdapter.setDiaryArrayList(mDiaryArrayList)
        diaryListView.adapter = mAdapter

        mDatabaseReference = FirebaseDatabase.getInstance().reference

        mTestRef = mDatabaseReference.child(DiaryPATH)
        mTestRef!!.addChildEventListener(mEventListener)
    }
}

