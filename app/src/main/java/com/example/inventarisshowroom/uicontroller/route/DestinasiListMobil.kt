package com.example.inventarisshowroom.uicontroller.route

object DestinasiListMobil : DestinasiNavigasi {
    override val route = "list_mobil"
    override val titleRes = "Daftar Mobil"
    const val MERK_ID = "merkId"
    const val MERK_NAME = "merkName"
    val routeWithArgs = "$route/{$MERK_ID}/{$MERK_NAME}"

    fun createRoute(merkId: Int, merkName: String) = "$route/$merkId/$merkName"
}