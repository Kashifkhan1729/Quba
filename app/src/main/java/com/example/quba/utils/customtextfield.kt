package com.example.quba.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun kashif() {
    val db = Firebase.firestore
    val database = Firebase.database
    val myRef = database.getReference("message k khan")

    val city = hashMapOf(
        "name" to "Gorakhpur",
        "state" to "up",
        "country" to "india",
    )

    db.collection("Cities").document("qamar").set(city)
        .addOnSuccessListener {    myRef.setValue("Kashif")}
        .addOnFailureListener { myRef.setValue("no value") }

}