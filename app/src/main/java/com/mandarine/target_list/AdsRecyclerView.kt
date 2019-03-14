package com.mandarine.target_list

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import android.os.Bundle
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.mandarine.target_list.features.MainActivity


class AdsRecyclerView(private val adsList: MutableList<ClassifiedAd>, private val context: Context) :
    RecyclerView.Adapter<AdsRecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return adsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsRecyclerView.ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ad_item_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdsRecyclerView.ViewHolder, position: Int) {
        val classifiedAd = adsList[position]
        holder.title.text = classifiedAd.title
        holder.name.text = classifiedAd.name
        holder.phone.text = classifiedAd.phoneNumber
//        holder.edit.setOnClickListener {
//            editClassifiedAd(classifiedAd.getAdId())
//        }
//        holder.delete.setOnClickListener {
//            deleteClassifiedAd(classifiedAd.getAdId(), position)
//        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView
        var name: TextView
        var phone: TextView
        var edit: Button
        var delete: Button

        init {
            name = view.findViewById(R.id.title_i)
            title = view.findViewById(R.id.name_i)
            phone = view.findViewById(R.id.phone_i)
            edit = view.findViewById(R.id.edit_ad_b)
            delete = view.findViewById(R.id.delete_ad_b)
        }
    }

    private fun editClassifiedAd(adId: String) {
        val fm = (context as MainActivity).supportFragmentManager

        val bundle = Bundle()
        bundle.putString("adId", adId)

        val addFragment = AddClassifiedFragment()
        addFragment.arguments = bundle

        fm.beginTransaction().replace(R.id.mainFrame, addFragment).commit()
    }

    private fun deleteClassifiedAd(adId: String, position: Int) {
        FirebaseDatabase.getInstance().reference
            .child("classified").child(adId).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //remove item from list alos and refresh recyclerview
                    adsList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, adsList.size)

                    Log.d("Delete Ad", "Classified has been deleted")
                    Toast.makeText(
                        context,
                        "Classified has been deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.d("Delete Ad", "Classified couldn't be deleted")
                    Toast.makeText(
                        context,
                        "Classified could not be deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}