//package com.mandarine.target_list
//
//import android.content.Context
//import android.widget.Toast
//import com.google.firebase.database.DatabaseError
//import java.nio.file.Files.size
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.ValueEventListener
//import android.widget.TextView
//import android.support.v7.widget.DividerItemDecoration
//import com.firebase.ui.auth.AuthUI.getApplicationContext
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//import com.google.firebase.database.FirebaseDatabase
//import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.util.Log
//import android.view.ViewGroup
//import android.view.LayoutInflater
//import android.view.View
//import android.widget.Button
//import com.google.firebase.database.DatabaseReference
//
//class ViewClassifiedFragment : Fragment() {
//    private var databaseReference: DatabaseReference? = null
//    private var adsRecyclerView: RecyclerView? = null
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//
//        val view = inflater.inflate(
//            R.layout.ad_view_layout,
//            container, false
//        )
//
//        databaseReference = FirebaseDatabase.getInstance().reference
//
//        val button = view.findViewById(R.id.view_adds_b) as Button
//        button.setOnClickListener(object : View.OnClickListener() {
//            override fun onClick(v: View) {
//                getClassifiedAds()
//            }
//        })
//
//        adsRecyclerView = view.findViewById(R.id.ads_lst)
//
//        val recyclerLayoutManager = LinearLayoutManager(getActivity().getApplicationContext())
//        adsRecyclerView!!.layoutManager = recyclerLayoutManager
//
//        val dividerItemDecoration = DividerItemDecoration(
//            adsRecyclerView!!.context,
//            recyclerLayoutManager.orientation
//        )
//        adsRecyclerView!!.addItemDecoration(dividerItemDecoration)
//
//        return view
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//    }
//
//    fun getClassifiedAds() {
//        val category = (getActivity()
//            .findViewById(R.id.category_v) as TextView).text.toString()
//        getClassifiedsFromDb(category)
//    }
//
//    private fun getClassifiedsFromDb(category: String) {
//        databaseReference!!.child("classified").orderByChild("category")
//            .equalTo(category).addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    val adsList = ArrayList<ClassifiedAd>()
//                    for (adSnapshot in dataSnapshot.children) {
//                        adsList.add(adSnapshot.getValue(ClassifiedAd::class.java))
//                    }
//                    Log.d("some", "no of ads for search is " + adsList.size)
//                    val recyclerViewAdapter = AdsRecyclerView(adsList, getActivity())
//                    adsRecyclerView!!.adapter = recyclerViewAdapter
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    Log.d(
//                        "some", "Error trying to get classified ads for " + category +
//                                " " + databaseError
//                    )
//                    Toast.makeText(
//                        getActivity(),
//                        "Error trying to get classified ads for $category",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            })
//    }
//
//    companion object {
//        private val "some" = "ViewAdsFragment"
//    }
//}