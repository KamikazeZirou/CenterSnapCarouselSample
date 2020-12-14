package com.example.centersnapcarouselsample

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val snapHelper = GravitySnapHelper(Gravity.CENTER, true)

        val carousel = findViewById<RecyclerView>(R.id.carousel)
        carousel.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = MyAdapter((0..30).map { it.toString() }.toTypedArray())

            // カルーセルの両端が中央に表示されるようにRecyclerViewの両端にPaddingを入れる。
            // ItemDecoratorを使って両端の項目にPaddingを入れる方式もあるが、項目数が変化する場合には使えない。
            // 末端項目を削除すると、ItemDecoratorが消える中間状態があり、そのせいで表示位置がおかしくなる。
            val itemWidthDp = 96
            val itemWidthPixels = (itemWidthDp * resources.displayMetrics.density  + 0.5).toInt()
            val paddingH = (resources.displayMetrics.widthPixels - itemWidthPixels) / 2
            setPadding(paddingH, 0, paddingH, 0)
            clipToPadding = false
            setHasFixedSize(true)

            // GravitySnapHelperは、カールセルのSnap位置の変化を検知するリスナーが付いているので素のLinearSnapHelperよりも便利。
            snapHelper.attachToRecyclerView(this)
        }

        // カールセルのSnap位置の変化の検知
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = "0" // 最初はリスナー呼ばれないので、初期Snap位置に応じた表示を自分で行う必要がある。
        snapHelper.setSnapListener {
            textView.text = it.toString()
        }
    }
}

class MyAdapter(private val myDataset: Array<String>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.carousel_item, parent, false) as TextView
        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = myDataset[position]
    }

    override fun getItemCount() = myDataset.size
}