package com.example

data class BillitemDataModel(val name: String, val mrp: Double, val weight: Double, val qty: String,val total_mat: Double)
{
    // No-argument constructor
    constructor() : this("0", 0.0, 0.0, "0", 0.0)
}