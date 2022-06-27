package com.example.photo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.list_diary.view.*

class DiaryListAdapter(context: Context) : BaseAdapter() {
    private var mLayoutInflater: LayoutInflater
    var mDiaryList = ArrayList<Diary>()

    init {
        mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return mDiaryList.size
    }

    override fun getItem(position: Int): Any {
        return mDiaryList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var convertView = view

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_diary, parent, false)
        }

        val titleText = convertView!!.text1 as TextView
        titleText.text = mDiaryList[position].title

        val contentText = convertView.text2 as TextView
        contentText.text = mDiaryList[position].contents

        val nameText = convertView.text3 as TextView
        nameText.text = mDiaryList[position].name

        return convertView
    }

    fun setDiaryArrayList(diaryArrayList: ArrayList<Diary>) {
        mDiaryList = diaryArrayList
    }
}