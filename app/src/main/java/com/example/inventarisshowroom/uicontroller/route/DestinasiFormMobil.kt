package com.example.inventarisshowroom.uicontroller.route

object DestinasiFormMobil : DestinasiNavigasi {
    override val route = "form_mobil"
    override val titleRes = "Form Mobil"

    // Parameter untuk mode tambah
    const val MERK_ID = "merkId"
    const val MERK_NAME = "merkName"

    // Parameter untuk mode edit
    const val MOBIL_ID = "mobilId"

    // Route untuk tambah mobil
    const val routeAdd = "form_mobil/add/{merkId}/{merkName}"
    fun createRouteAdd(merkId: Int, merkName: String) = "form_mobil/add/$merkId/$merkName"

    // Route untuk edit mobil
    const val routeEdit = "form_mobil/edit/{mobilId}"
    fun createRouteEdit(mobilId: Int) = "form_mobil/edit/$mobilId"
}