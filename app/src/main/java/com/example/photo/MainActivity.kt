package com.example.photo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mDiaryAdapter: DiaryListAdapter
    private lateinit var mDiaryArrayList: ArrayList<Diary>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDiaryAdapter = DiaryListAdapter(this)
        mDiaryArrayList = ArrayList<Diary>()

        val title = "タイトル"
        val content = "内容"
        val name = "名前"
        val diary = Diary(title, content, name)

        mDiaryArrayList.add(diary)

        mDiaryAdapter.mDiaryList = mDiaryArrayList
        diaryListView.adapter = mDiaryAdapter
        mDiaryAdapter.notifyDataSetChanged()
    }
}