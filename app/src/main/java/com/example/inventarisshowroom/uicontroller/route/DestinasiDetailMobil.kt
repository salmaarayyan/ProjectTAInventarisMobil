package com.example.inventarisshowroom.uicontroller.route

object DestinasiDetailMobil : DestinasiNavigasi {
    override val route = "detail_mobil"
    override val titleRes = "Detail Mobil"
    const val MOBIL_ID = "mobilId"
    val routeWithArgs = "$route/{$MOBIL_ID}"

    fun createRoute(mobilId: Int) = "$route/$mobilId"
}