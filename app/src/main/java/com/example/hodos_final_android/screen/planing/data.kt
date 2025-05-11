package com.example.hodos_final_android.screen.planing

import android.util.Log
import com.example.hodos_final_android.model.Trip
import com.google.android.gms.maps.model.LatLng


data class Route(
    val tripRoutes: TripRoute ,
    val waypoints: List<Waypoint>? = null,
    val routes: List<RouteDetail> = emptyList(),
    val trips: List<Trip>? = emptyList()
)

data class TripRoute(
    val distance: Double,
    val duration: Double,
    val geometry: String,
    val legs: List<Leg>? = null,
    val weight: Double,
    val weight_name: String
)

data class Leg(
    val distance: Double,
    val duration: Double,
    val steps: List<Step>? = null,
    val summary: String,
    val weight: Double
)

data class Step(
    val distance: Distance? = null,
    val duration: Duration? = null,
    val geometry: String? = null,
    val html_instructions: String? = null,
    val maneuver: String? = null,
    val mode: String? = null,
    val name: String? = null
)

data class Distance(
    val text: String,
    val value: Int
)

data class Duration(
    val text: String,
    val value: Int
)

data class Waypoint(
    val distance: Double,
    val location: List<Double>? = null,
    val place_id: String,
    val trips_index: Int,
    val waypoint_index: Int
)

// For backward compatibility with the original code
data class RouteDetail(
    val legs: List<Leg> = emptyList(),
    val overview_polyline: OverviewPolyline = OverviewPolyline("")
)

data class OverviewPolyline(
    val points: String
)

val exampleTrip = Route(
    tripRoutes = TripRoute(
        distance = 33477.8,
        duration = 10195.9,
        geometry = "wfl_Coz~dS{CUEb@zF`@nAJzLz@tBPzM`AbAFbJp@w@jH]hDsArK_@bECXEFCLOrAOtAGh@QhBOrA?H@FU~AEXKNJp@Ff@TlBB\\\\\\\\@HHpA?FDTDb@DVNtATbBHp@Hp@h@|ERbCdAnM\\\\\\\\jEh@bHt@bKZ~DTnCBj@Qv@[`Bo@`EUtAId@Qz@Mz@Mn@Gd@YdBCL_@lBUtA]nBcAxFWzAQdAQ~@WrA]`B_@lAs@zA]j@s@bA_AvAOTe@t@e@r@INUZY^GHSVyApBg@p@mA`B_ApAu@`AiA|AmDzEeCjDUVs@`AQZIZI^G^C^@b@BhABzABd@FzABLFLLzBH`@HRLRBJBD|C~CLLHXD^@NBdACR@PDPFJFFLDN?LENIJOLGLCL?hATN@FAPDjBh@pAb@RFZJVHj@ZFD`Ah@zBvA`Al@|AfAs@lAjAr@CLBMkAs@r@mA}AgAaAm@{BwAaAi@Qk@Mq@F]j@cC`@kBXsATcAF]l@cC^sAN[LUBEd@k@b@e@dCgC^a@x@}@r@x@ORv@~@hAjAt@z@|@`AjA`A@@b@ZZRRL^XXP`DvBdCzA`@w@P_@l@Zh@Vd@Vn@No@Oe@Wi@Wm@[JSBGR_@FKTe@HQLWFKJSBILUVe@XeABI|@}BDOLa@L_@Tm@TLxAn@p@Tx@RnARp@Dr@E`@I\\\\\\\\MXOJI\\\\\\\\YrAaB~D}Ej@m@j@k@j@g@n@c@n@]`B{@lB{@TKbEgBnEgBjAg@l@[lAs@hCeBfA}@v@q@fAoA`@e@TWjEsEz@eA^k@fEwGfIyMv@mAVa@LS`F{JpAiCJJ`@\\\\\\\\d@`@HHJHXVTRFD^\\\\\\\\NLTTTPNNNLHF??NJdAt@B@DBARCDc@`@g@l@_@d@IJHK^e@f@m@b@a@BE@SECCAeAu@OK??IGOMOOUQUUOM_@]GEUSYWKImErIqBxDKGq@g@MISOYSW`@y@pAmAnBaBlCuCvEgExGQX_@d@g@l@w@z@}A`BML{@~@a@b@o@t@[^uBbB[T_@VOJkAv@aAh@Ya@X`@k@ZiAf@cA`@kCdALZnEgBjAg@l@[lAs@hCeBfA}@v@q@fAoAFFXX\\\\\\\\`@LLb@d@FHJJ|BfCfBrBZ^DDv@~@~AfBn@t@zBfCj@n@PP`@a@Y]UUOQa@c@{DoEf@Yg@X[_@oAuAY]gBuBaAiAaAgAIAQDIHARJJ|BfCfBrBZ^DDv@~@~AfBn@t@zBfCj@n@PPJL^`@j@r@h@j@b@d@f@l@f@n@d@n@b@l@f@t@b@n@\\\\\\\\h@f@r@h@x@`@j@d@r@RXnC`ER\\\\\\\\`AxAF\\\\\\\\FJrAnBHPzA~BT`@Xp@x@~Bf@jA|@jBzAvBh@v@RZnDzFBFBFaAb@OJ^n@dA`BPFBH{BfByCzBeBoCaBiCKOcBmCy@qA}AaCbE}Cb@]\\\\\\\\ObBw@vC{BpAwAZ[h@e@BCNMTQ~AkAXULKx@o@bAu@DCj@c@fDgC`CgBVQxBcBb@[tADbA`BLR^n@Zd@`A|Al@~@vBbDdBjCfAfBiAz@eA?iCV_A]k@JSRG^SRRSF_@RSj@K~@\\\\\\\\hCWdA?hA{@VOsA_CiEsHKSU_@_BkCkB{CCEa@o@k@}@k@b@eAv@oCrBgJ`HiAz@e@\\\\\\\\}ErD]XUPuAf@WRiCnByAbBWTQJuElD_BlAA?k@d@mGxEMOOKMA{@wAKO}@{AoAkBoAiBuCuEcFxDoEfD_Ar@wEbDsEjDe@^sA~@}AhAyAdAw@j@eBnAq@gAc@w@S[Yc@xAeAyAdAe@s@i@y@wBcEvBbE`DaCnA_A|AmAfBqAd@]}BgEu@wAiAyBEIPqAd@UlBaAt@_@fCsAdAk@vAs@lBaAfAi@f@W`B{@`Ae@HIFODUBQ\\\\\\\\iCB]@MDo@De@Be@JuA@KH}@Fy@@SBU@Kd@yEn@_CD[@M@EC[AYG_AWkEl@W~AfBn@t@zBfCj@n@PP`@a@Y]UUOQa@c@{DoE[_@oAuAY]q@YeH_I{AcBgCyC{A{A[Yo@c@}@q@_@Q}@c@wBcA_CkAq@[y@_@{@c@_@S{As@yAu@}BiAw@a@kB}@WMWM}EaC{Aw@yAw@EC{Ao@_Ac@YIoDq@WGmCg@oASSE{@SSEeBUy@MyAUc@IICaAQ_@IaAUiBa@fAmGN_ALiADUZ_AZ{@Aq@WqD[kEg@yGWsDYsDGk@MwAEk@Gs@IeAM_BIy@KuAKiAEa@Em@M_BGq@q@eGe@yDKw@Iq@Ea@AG?EAS?W@K@SHq@NmAFc@DWNaA`@uBD[`@qBDUDW@CDQ?CH_@F[@GLm@Je@Lo@??FUDSF]Nq@XyAVoAp@qDVqALo@?ABQt@wDd@aCHe@TcALq@KKSGcBQuC[mIs@{AKyMaAuBO{L}@qAK}AK",
        legs = listOf(
            Leg(6795.3, 1259.8, emptyList(), "", 1259.8),
            Leg(1792.0, 669.6, emptyList(), "", 669.6),
            Leg(3498.2, 732.7, emptyList(), "", 732.7),
            Leg(1885.2, 385.6, emptyList(), "", 385.6),
            Leg(1641.7, 297.0, emptyList(), "", 297.0),
            Leg(2794.0, 2354.1, emptyList(), "", 2354.1),
            Leg(2208.0, 1922.5, emptyList(), "", 1922.5),
            Leg(2359.7, 517.0, emptyList(), "", 517.0),
            Leg(1501.2, 331.9, emptyList(), "", 331.9),
            Leg(256.1, 103.7, emptyList(), "", 103.7),
            Leg(1664.7, 416.3, emptyList(), "", 416.3),
            Leg(7081.7, 1205.7, emptyList(), "", 1205.7)
        ),
        weight = 10195.9,
        weight_name = "routability"
    ),
    waypoints = listOf(
        Waypoint(3.0, listOf(21.021606, 52.227147), "ulica Warecka", 3, 3),
        Waypoint(0.0, listOf(21.012249, 52.230791), "Nowy Świat – Uniwersytet", 0, 0),
        Waypoint(4.0, listOf(21.021273, 52.225260), "Nowy Świat", 4, 4),
        Waypoint(2.0, listOf(21.026105, 52.222017), "Nowy Świat", 2, 2),
        Waypoint(1.0, listOf(21.024695, 52.225594), "Centrum Nauki Kopernik", 1, 1),
        Waypoint(5.0, listOf(21.021109, 52.215348), "ulica Dobra", 5, 5),
        Waypoint(6.0, listOf(21.008682, 52.212359), "Tamka 49", 6, 6),
        Waypoint(7.0, listOf(21.002957, 52.211586), "Tamka 32", 7, 7),
        Waypoint(8.0, listOf(21.000006, 52.211444), "Tamka 22", 8, 8),
        Waypoint(9.0, listOf(20.999148, 52.211384), "Tamka 18", 9, 9),
        Waypoint(10.0, listOf(20.994629, 52.213559), "ulica Solec", 10, 10),
        Waypoint(11.0, listOf(20.990419, 52.211739), "ulica Wilanowska", 11, 11)
    )
)

val exampleTripData : String = "{\"code\":\"Ok\",\"trips\":[{\"distance\":33477.8,\"duration\":10195.9,\"geometry\":\"wfl_Coz~dS{CUEb@zF`@nAJzLz@tBPzM`AbAFbJp@w@jH]hDsArK_@bECXEFCLOrAOtAGh@QhBOrA?H@FU~AEXKNJp@Ff@TlBB\\\\@HHpA?FDTDb@DVNtATbBHp@Hp@h@|ERbCdAnM\\\\jEh@bHt@bKZ~DTnCBj@Qv@[`Bo@`EUtAId@Qz@Mz@Mn@Gd@YdBCL_@lBUtA]nBcAxFWzAQdAQ~@WrA]`B_@lAs@zA]j@s@bA_AvAOTe@t@e@r@INUZY^GHSVyApBg@p@mA`B_ApAu@`AiA|AmDzEeCjDUVs@`AQZIZI^G^C^@b@BhABzABd@FzABLFLLzBH`@HRLRBJBD|C~CLLHXD^@NBdACR@PDPFJFFLDN?LENIJOLGLCL?hATN@FAPDjBh@pAb@RFZJVHj@ZFD`Ah@zBvA`Al@|AfAs@lAjAr@CLBMkAs@r@mA}AgAaAm@{BwAaAi@Qk@Mq@F]j@cC`@kBXsATcAF]l@cC^sAN[LUBEd@k@b@e@dCgC^a@x@}@r@x@ORv@~@hAjAt@z@|@`AjA`A@@b@ZZRRL^XXP`DvBdCzA`@w@P_@l@Zh@Vd@Vn@No@Oe@Wi@Wm@[JSBGR_@FKTe@HQLWFKJSBILUVe@XeABI|@}BDOLa@L_@Tm@TLxAn@p@Tx@RnARp@Dr@E`@I\\\\MXOJI\\\\YrAaB~D}Ej@m@j@k@j@g@n@c@n@]`B{@lB{@TKbEgBnEgBjAg@l@[lAs@hCeBfA}@v@q@fAoA`@e@TWjEsEz@eA^k@fEwGfIyMv@mAVa@LS`F{JpAiCJJ`@\\\\d@`@HHJHXVTRFD^\\\\NLTTTPNNNLHF??NJdAt@B@DBARCDc@`@g@l@_@d@IJHK^e@f@m@b@a@BE@SECCAeAu@OK??IGOMOOUQUUOM_@]GEUSYWKImErIqBxDKGq@g@MISOYSW`@y@pAmAnBaBlCuCvEgExGQX_@d@g@l@w@z@}A`BML{@~@a@b@o@t@[^uBbB[T_@VOJkAv@aAh@Ya@X`@k@ZiAf@cA`@kCdALZnEgBjAg@l@[lAs@hCeBfA}@v@q@fAoAFFXX\\\\`@LLb@d@FHJJ|BfCfBrBZ^DDv@~@~AfBn@t@zBfCj@n@PP`@a@Y]UUOQa@c@{DoEf@Yg@X[_@oAuAY]gBuBaAiAaAgAIAQDIHARJJ|BfCfBrBZ^DDv@~@~AfBn@t@zBfCj@n@PPJL^`@j@r@h@j@b@d@f@l@f@n@d@n@b@l@f@t@b@n@\\\\h@f@r@h@x@`@j@d@r@RXnC`ER\\\\`AxAF\\\\FJrAnBHPzA~BT`@Xp@x@~Bf@jA|@jBzAvBh@v@RZnDzFBFBFaAb@OJ^n@dA`BPFBH{BfByCzBeBoCaBiCKOcBmCy@qA}AaCbE}Cb@]\\\\ObBw@vC{BpAwAZ[h@e@BCNMTQ~AkAXULKx@o@bAu@DCj@c@fDgC`CgBVQxBcBb@[tADbA`BLR^n@Zd@`A|Al@~@vBbDdBjCfAfBiAz@eA?iCV_A]k@JSRG^SRRSF_@RSj@K~@\\\\hCWdA?hA{@VOsA_CiEsHKSU_@_BkCkB{CCEa@o@k@}@k@b@eAv@oCrBgJ`HiAz@e@\\\\}ErD]XUPuAf@WRiCnByAbBWTQJuElD_BlAA?k@d@mGxEMOOKMA{@wAKO}@{AoAkBoAiBuCuEcFxDoEfD_Ar@wEbDsEjDe@^sA~@}AhAyAdAw@j@eBnAq@gAc@w@S[Yc@xAeAyAdAe@s@i@y@wBcEvBbE`DaCnA_A|AmAfBqAd@]}BgEu@wAiAyBEIPqAd@UlBaAt@_@fCsAdAk@vAs@lBaAfAi@f@W`B{@`Ae@HIFODUBQ\\\\iCB]@MDo@De@Be@JuA@KH}@Fy@@SBU@Kd@yEn@_CD[@M@EC[AYG_AWkEl@W~AfBn@t@zBfCj@n@PP`@a@Y]UUOQa@c@{DoE[_@oAuAY]q@YeH_I{AcBgCyC{A{A[Yo@c@}@q@_@Q}@c@wBcA_CkAq@[y@_@{@c@_@S{As@yAu@}BiAw@a@kB}@WMWM}EaC{Aw@yAw@EC{Ao@_Ac@YIoDq@WGmCg@oASSE{@SSEeBUy@MyAUc@IICaAQ_@IaAUiBa@fAmGN_ALiADUZ_AZ{@Aq@WqD[kEg@yGWsDYsDGk@MwAEk@Gs@IeAM_BIy@KuAKiAEa@Em@M_BGq@q@eGe@yDKw@Iq@Ea@AG?EAS?W@K@SHq@NmAFc@DWNaA`@uBD[`@qBDUDW@CDQ?CH_@F[@GLm@Je@Lo@??FUDSF]Nq@XyAVoAp@qDVqALo@?ABQt@wDd@aCHe@TcALq@KKSGcBQuC[mIs@{AKyMaAuBO{L}@qAK}AK\",\"legs\":[{\"distance\":6795.3,\"duration\":1259.8,\"steps\":[],\"summary\":\"\",\"weight\":1259.8},{\"distance\":1792,\"duration\":669.6,\"steps\":[],\"summary\":\"\",\"weight\":669.6},{\"distance\":3498.2,\"duration\":732.7,\"steps\":[],\"summary\":\"\",\"weight\":732.7},{\"distance\":1885.2,\"duration\":385.6,\"steps\":[],\"summary\":\"\",\"weight\":385.6},{\"distance\":1641.7,\"duration\":297,\"steps\":[],\"summary\":\"\",\"weight\":297},{\"distance\":2794,\"duration\":2354.1,\"steps\":[],\"summary\":\"\",\"weight\":2354.1},{\"distance\":2208,\"duration\":1922.5,\"steps\":[],\"summary\":\"\",\"weight\":1922.5},{\"distance\":2359.7,\"duration\":517,\"steps\":[],\"summary\":\"\",\"weight\":517},{\"distance\":1501.2,\"duration\":331.9,\"steps\":[],\"summary\":\"\",\"weight\":331.9},{\"distance\":256.1,\"duration\":103.7,\"steps\":[],\"summary\":\"\",\"weight\":103.7},{\"distance\":1664.7,\"duration\":416.3,\"steps\":[],\"summary\":\"\",\"weight\":416.3},{\"distance\":7081.7,\"duration\":1205.7,\"steps\":[],\"summary\":\"\",\"weight\":1205.7}],\"weight\":10195.9,\"weight_name\":\"routability\"}],\"waypoints\":[{\"distance\":5.033847,\"location\":[21.039316,105.839922],\"place_id\":\"ODdMa1o-40e8qGVuabSBj1erbU-6XIrqQ7lTS7tbpI54zCUFoW3x4numVzO_Y6jw6HaZXL7Rvo4tAlMoMum24lUeqcUyMbsKOeqpcSIpviuMckE-mvG2aih0cgriKb_FH\",\"trips_index\":0,\"waypoint_index\":0},{\"distance\":2.754927,\"location\":[21.033061,105.791325],\"place_id\":\"l2yrQbYGlI1quXtGtmqEi27PZBK9LbHqYrpweKEyg85vRqgfjj5u6qetUnm6BG-JIZJsHIMli6Fdu0YfiC-DjHWRXnuKSZftb6tde4sdi-JukU4dvTabi27y5RhyLMvDL\",\"trips_index\":0,\"waypoint_index\":1},{\"distance\":22.416487,\"location\":[21.017809,105.803366],\"place_id\":\"x1-5tByCD5hlAT-u3Gql3maHTa2ZUufhcoFBKrFpkH3CqK3cdq3tVfGR5thixqEGbfFVBIa9UhOdJumMDsmeQ-k-CbyCQRbqefLhOWv5omPF9gl0OrocOmH-qVSWYiOPY\",\"trips_index\":0,\"waypoint_index\":4},{\"distance\":31.015553,\"location\":[21.007758,105.811269],\"place_id\":\"RGmsRfEQsjFpQ8kRiFO4_WqReW-SN33Mco5TN78btPRzvyQrvXz953C_WAGLU57R2WJchSrxrkcYZv1uGvHCWNFi-V86OR47Haq5YTI5-jueia0sYuBiejpS8QzOOGfXO\",\"trips_index\":0,\"waypoint_index\":3},{\"distance\":61.098363,\"location\":[20.998716,105.791924],\"place_id\":\"lW00XriJXGxel1QQjxzJzfLPUCa3am7PaIdQD4h7u8AwuCNoo3u0wTKTlia-Q1CLab-wJJF4mYul5b0A9j2iz-rm6XEqjRKTBbalfS4l5ieBsk0xEaL-ZiYG7H0QfHvJu\",\"trips_index\":0,\"waypoint_index\":7},{\"distance\":43.717286,\"location\":[21.005389,105.787887],\"place_id\":\"TEUEiGrIFsRpxlk6sU2uYWRMeEeFmKmzYf8uQC4efc5m2_E1NVoSQn7LCBUm-Y6bEZ4BWD75lqZl_hVk8sFA5xmXYJp-ASwKgZOmIQoBwgFZlgJoWthaQsmfATT2AF_us\",\"trips_index\":0,\"waypoint_index\":6},{\"distance\":15.681507,\"location\":[21.019095,105.788105],\"place_id\":\"Hmp2S0qkruFZimc9opCTaXa7QQyPjZhFWapdvidnmPJ1q3S2tney4F_PVROhHZf3b7ldMb55to9FqGJKj3r2w3OqVRa6RKGAbKheSohtiMB4kk0evm2Yi4G-6RTWIH_PI\",\"trips_index\":0,\"waypoint_index\":9},{\"distance\":69.479722,\"location\":[21.027187,105.795284],\"place_id\":\"T6Cgf3Fm1Hdqp1gWoXe09ke0U1SlX47PcqcwFrpbiomeq3MtjgDUcqDSnixDZorpp4xDLKQCmZUv0GZov1-s4RnRaQlGQWpWUcrZAVFtmlv9zjJYAUwCGlnGkWyuWAe3W\",\"trips_index\":0,\"waypoint_index\":2},{\"distance\":22.595256,\"location\":[21.012654,105.802381],\"place_id\":\"mVZa3nlbKRqlP30IYmWeY5GbfbwOYVYjHfLSHFqt7aN9_3ZRafVWXmJ2HtK5mXJQhSTeG6657hGlyuTYkkFKqq2XdY1XCU4jhfLgGTphomPF9gl0Org6ImH-qVSWYD-PY\",\"trips_index\":0,\"waypoint_index\":5},{\"distance\":8.278421,\"location\":[21.020549,105.789286],\"place_id\":\"gk_rS99ipGbrnFMcq3GeYtibfkeHY_Fj6cdfHatO-VOCnSwHg3C_U_WMWyy8YaRV8MYoBoVxrFXEmHpFsTudtLOcV2JUDIpEPVKLxQIJyZmeCmK4UtBSSZcJHsD-CFfln\",\"trips_index\":0,\"waypoint_index\":10},{\"distance\":38.043008,\"location\":[21.010007,105.789648],\"place_id\":\"vnFmc9yx2ZxSm0ZAskhntWViSUROr_izobNsP2Jj8chUnFVBvTqLxFH3HShe1Ar30fbJaGIVjg-p9nUZChHyt6IvEVjyFXo9-_KNVQYNzg-pmmUYVtRWTg06xZD6DFPjD\",\"trips_index\":0,\"waypoint_index\":8},{\"distance\":36.917935,\"location\":[21.013758,105.798614],\"place_id\":\"jMiErpx9Nbxok0GCpW8QxW0uwSSulR4SJZqBjKIxtqstdQVZOvBiirGvkVSiLbrL_aJNjF4tuvdNtjEEsphm2oF3yWTWmQZCwaKxaTox8uuVplkkaaRq6jIy-QTFrG_fM\",\"trips_index\":0,\"waypoint_index\":11}]}\n" +
        "\n"

fun decodePolyline(encoded: String): List<LatLng> {
    Log.i("API", encoded)
    val polyline = mutableListOf<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or ((b and 0x1f) shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or ((b and 0x1f) shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
        lng += dlng

        val latLng = LatLng(lat / 1E5, lng / 1E5)
        polyline.add(latLng)
    }

    return polyline
}
