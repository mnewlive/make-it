package com.mandarine.target_list

import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.Transaction
import com.google.firebase.database.MutableData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.mandarine.target_list.features.MainActivity

class AddClassifiedFragment : Fragment() {

    private var dbRef: DatabaseReference? = null
    private var nextClassifiedID: Int = 0
    private var isEdit: Boolean = false
    private var adId: String? = null
    private var button: Button? = null
    private var headTxt: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.ad_add_layout, container, false)

        button = view.findViewById(R.id.post_add) as Button
        headTxt = view.findViewById(R.id.add_head_tv)

        dbRef = FirebaseDatabase.getInstance().reference

        button?.setOnClickListener { if (!isEdit) addEvent() else updateEvent() }

        //add or update depending on existence of adId in arguments
        if (arguments != null) adId = arguments?.getString("adId")
        if (adId != null) populateUpdateAd()
        return view
    }

    private fun addEvent() {
        Log.d("some", "addEvent")
        val classifiedAd = createClassifiedAdObj()
        addClassifiedToDB(classifiedAd)
    }

    private fun updateEvent() {
        val classifiedAd = createClassifiedAdObj()
        updateClassifiedToDB(classifiedAd)
    }

    private fun addClassifiedToDB(classifiedAd: ClassifiedAd) {
        Log.d("some", "addClassifiedToDB")
        dbRef?.child("fields")?.child(classifiedAd.adId)?.setValue(classifiedAd)


        Log.d("some", "idDatabaseRef : $dbRef")
        dbRef?.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                //create id node if it doesn't exist
                //this code runs only once
                if (mutableData.getValue(Int::class.javaPrimitiveType!!) == null) {
                    dbRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            //set initial value
                            if (dataSnapshot.value == null) {
                                dbRef?.setValue(1)
                                Log.d("some", "Initial id is set")
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.d("some", "onCancelled")
                        }
                    })
                    Log.d("some", "Classified id null so " + " transaction aborted, ")
                    return Transaction.abort()
                }

                nextClassifiedID = mutableData.getValue(Int::class.javaPrimitiveType!!)!!
                mutableData.value = nextClassifiedID + 1
                return Transaction.success(mutableData)
            }

            override fun onComplete(databaseError: DatabaseError?, state: Boolean, dataSnapshot: DataSnapshot?) {
                if (state) {
                    Log.d("some", "Classified id retrieved ")
                    addClassified(classifiedAd, "" + nextClassifiedID)
                } else {
                    Log.d("some", "Classified id retrieval unsuccessful " + databaseError!!)
                }

            }
        })
    }

//    TODO:check
//    private fun addClassifiedToDB(classifiedAd: ClassifiedAd) {
//        val idDatabaseRef = FirebaseDatabase.getInstance()
//            .getReference("ClassifiedIDs").child("id")
//
//        idDatabaseRef.runTransaction(object : Transaction.Handler {
//            override fun doTransaction(mutableData: MutableData): Transaction.Result {
//                //create id node if it doesn't exist
//                //this code runs only once
//                if (mutableData.getValue(Int::class.javaPrimitiveType!!) == null) {
//                    idDatabaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                        override fun onDataChange(dataSnapshot: DataSnapshot) {
//                            //set initial value
//                            if (dataSnapshot.value == null) {
//                                idDatabaseRef.setValue(1)
//                                Log.d(TAG, "Initial id is set")
//                            }
//                        }
//
//                        override fun onCancelled(databaseError: DatabaseError) {
//
//                        }
//                    })
//
//                    Log.d(TAG, "Classified id null so " + " transaction aborted, ")
//                    return Transaction.abort()
//                }
//
//                nextClassifiedID = mutableData.getValue(Int::class.javaPrimitiveType!!)
//                mutableData.value = nextClassifiedID + 1
//                return Transaction.success(mutableData)
//            }
//
//            override fun onComplete(
//                databaseError: DatabaseError?, state: Boolean,
//                dataSnapshot: DataSnapshot?
//            ) {
//                if (state) {
//                    Log.d(TAG, "Classified id retrieved ")
//                    addClassified(classifiedAd, "" + nextClassifiedID)
//                } else {
//                    Log.d(TAG, "Classified id retrieval unsuccessful " + databaseError!!)
//                    Toast.makeText(
//                        activity,
//                        "There is a problem, please submit ad post again",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//            }
//        })
//    }

    private fun addClassified(classifiedAd: ClassifiedAd, cAdId: String) {
//        classifiedAd.setAdId(cAdId);
        classifiedAd.adId = cAdId
//        dbRef?.child("fields")?.child(classifiedAd.adId)?.setValue(classifiedAd)

        dbRef?.child("fields")?.child(cAdId)?.setValue(classifiedAd)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (isEdit) addClassifieds() else restUi()
                    Log.d("some", "Classified has been added to db")
                    Toast.makeText(activity, "Classified has been posted", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("some", "Classified couldn't be added to db")
                    Toast.makeText(activity, "Classified could not be added", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun populateUpdateAd() {
        headTxt?.text = "Edit Ad"
        button?.text = "Edit Ad"
        isEdit = true

        adId?.let {
            dbRef?.child("classified")?.child(it)?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val cAd = dataSnapshot.getValue<ClassifiedAd>(ClassifiedAd::class.java)
                    displayAdForUpdate(cAd!!)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("some", "Error trying to get classified ad for update $databaseError")
                    Toast.makeText(activity, "Please try classified edit action again", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun displayAdForUpdate(cAd: ClassifiedAd) {
        (activity?.findViewById(R.id.title_a) as EditText).setText(cAd.title)
        (activity?.findViewById(R.id.category_a) as EditText).setText(cAd.category)
        (activity?.findViewById(R.id.desc_a) as EditText).setText(cAd.description)
        (activity?.findViewById(R.id.name_a) as EditText).setText(cAd.name)
        (activity?.findViewById(R.id.phone_a) as EditText).setText(cAd.phoneNumber)
        (activity?.findViewById(R.id.city_a) as EditText).setText(cAd.city)
    }

    private fun updateClassifiedToDB(classifiedAd: ClassifiedAd) {
        addClassified(classifiedAd, adId ?: "0")
    }

    private fun createClassifiedAdObj(): ClassifiedAd {
        val ad = ClassifiedAd()
        ad.title = (activity?.findViewById(R.id.title_a) as? EditText)?.text?.toString() ?: "dasd"
        ad.category = (activity?.findViewById(R.id.category_a) as? EditText)?.text?.toString() ?: "dasda"
        ad.description = (activity?.findViewById(R.id.desc_a) as? EditText)?.text?.toString() ?: "soka"
        ad.name = (activity?.findViewById(R.id.name_a) as? EditText)?.text?.toString() ?: "dasdd"
        ad.phoneNumber = (activity?.findViewById(R.id.phone_a) as? EditText)?.text?.toString() ?: "dasada"
        ad.city = (activity?.findViewById(R.id.city_a) as? EditText)?.text?.toString() ?: "dasdad"
        Log.d("some", "createClassifiedAdObj : $ad")
        return ad
    }

    private fun restUi() {
        (activity?.findViewById(R.id.title_a) as? EditText)?.setText("")
        (activity?.findViewById(R.id.category_a) as? EditText)?.setText("")
        (activity?.findViewById(R.id.desc_a) as? EditText)?.setText("")
        (activity?.findViewById(R.id.name_a) as? EditText)?.setText("")
        (activity?.findViewById(R.id.phone_a) as? EditText)?.setText("")
        (activity?.findViewById(R.id.city_a) as? EditText)?.setText("")
    }

    private fun addClassifieds() {
        Log.d("some", "addClassifieds")
        startActivity(Intent().setClass(activity, MainActivity::class.java))
    }

    companion object {
        private const val TAG = "AddAdFragment"
    }
}