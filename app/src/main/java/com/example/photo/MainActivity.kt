package com.example.photo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.lifecycle.Transformations.map
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.Map as Map

var countDiary: String = "0"
var uid = ""

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mAdapter: DiaryListAdapter

    private lateinit var mDiaryArrayList: ArrayList<Diary>
    private var mTestRef: DatabaseReference? = null
    private var mCountRef: DatabaseReference? = null
    var count = 0

    private lateinit var auth: FirebaseAuth

    private val mEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

            val map = dataSnapshot.value as Map<String, String>
            val title = map["title"] ?: ""
            val contents = map["contents"] ?: ""
            val name = map["name"] ?: ""
            val userId = map["userId"] ?: ""

            val diary = Diary(title, contents, name, userId,dataSnapshot.key ?: "")

            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
// (1)で設定した uid で取得する(orderByKey().equalTo() でとれます)
            mDatabaseReference.child(UsersPATH).orderByKey().equalTo(uid).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.value as Map<*, *>?
                    val user = data?.values?.firstOrNull() as Map<*, *>?
                    // (2) 名前を取得
                    diary.name = user!!["name"].toString() ?: ""
                    // (3) 表示更新
                    mAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

            mDiaryArrayList.add(diary)
            mAdapter.notifyDataSetChanged()

            count = count + 1
            mDatabaseReference.child(CountPATH).child("count").setValue(count.toString())
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

    private val mCountListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }


        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            countDiary = snapshot.getValue().toString()
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
        }

        toDiaryAddPage.setOnClickListener{
            val intent = Intent(applicationContext, AddDiaryActivity::class.java)
            startActivity(intent)

        }

        diaryListView.setOnItemClickListener{parent, view, position, id ->
            if (FirebaseAuth.getInstance().currentUser == null){
                return@setOnItemClickListener
            }
            val diaryUid = mDiaryArrayList[position].diaryUid
            val diaryRef = dataBaseReference.child(DiaryPATH).child(diaryUid)
            diaryRef.removeValue().addOnSuccessListener{Log.d("test","addonSuccessListener")}
                .addOnFailureListener{Log.d("test",it.toString())}
            mDiaryArrayList.removeAt(position)
            mAdapter.notifyDataSetChanged()
        }

        mDiaryArrayList.clear()
        mAdapter.setDiaryArrayList(mDiaryArrayList)
        diaryListView.adapter = mAdapter

        mDatabaseReference = FirebaseDatabase.getInstance().reference

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if(user != null) {
            uid = user.uid
        }

        mCountRef = mDatabaseReference.child(CountPATH)
        mCountRef!!.removeValue()
        mCountRef!!.addChildEventListener(mCountListener)

        mTestRef = mDatabaseReference.child(DiaryPATH)
        mTestRef!!.addChildEventListener(mEventListener)
    }
}

