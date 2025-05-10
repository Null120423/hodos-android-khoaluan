package com.example.hodos_final_android.model

data class Route(
    val geocoded_waypoints: List<GeocodedWaypoint>,
    val routes: List<RouteDetail>
)

data class GeocodedWaypoint(
    val geocoder_status: String,
    val place_id: String
)

data class RouteDetail(
    val bounds: Bounds?,
    val legs: List<Leg>,
    val overview_polyline: OverviewPolyline,
    val summary: String?,
    val warnings: List<String>?,
    val waypoint_order: List<Int>?
)

data class Bounds(
    val northeast: LocationRoute?,
    val southwest: LocationRoute?
)

data class LocationRoute(
    val lat: Double,
    val lng: Double
)

data class Leg(
    val distance: Distance,
    val duration: Duration,
    val end_address: String,
    val end_location: LocationRoute,
    val start_address: String,
    val start_location: LocationRoute,
    val steps: List<Step>
)

data class Distance(
    val text: String,
    val value: Int
)

data class Duration(
    val text: String,
    val value: Int
)

data class Step(
    val distance: Distance,
    val duration: Duration,
    val end_location: LocationRoute,
    val html_instructions: String,
    val maneuver: String,
    val polyline: Polyline,
    val start_location: LocationRoute,
    val travel_mode: String
)

data class Polyline(
    val points: String
)

data class OverviewPolyline(
    val points: String
)


val exampleRouteData : String = "{\"geocoded_waypoints\":[{\"geocoder_status\":\"OK\",\"place_id\":\"TXZtrodoa2ekaVJOiByqi6TNewuOHqbKaJZdFrpFqtdpuIAzib4xc2mSbWy4sgf6z5ZsCLV6-9VpzkVKuWqQ9FiWRge5RPePRahsSoh4iOFekk0evh6YiG4-6RTWIH_PI\"},{\"geocoder_status\":\"OK\",\"place_id\":\"KmX7Q2GtL4RttSIks0j77W8WKiwS1Z3zsVbeEQYNOSYaIm4Q_ZbRSxlS2UUBmSaLwUrRVPbKoh4RWd14-g5uiwW6nf0aESYyqYKRSRoR05u1hnkESshKUhLK2STljE__E\"}],\"routes\":[{\"bounds\":{},\"legs\":[{\"distance\":{\"text\":\"5.84 km\",\"value\":5838},\"duration\":{\"text\":\"17 phút\",\"value\":1025},\"end_address\":\"Đường Lê Thánh Tôn, Quận 1, Hồ Chí Minh\",\"end_location\":{\"lat\":10.77657,\"lng\":106.70121},\"start_address\":\"134 Lý Thường Kiệt, Phường 7, Quận 10, Hồ Chí Minh\",\"start_location\":{\"lat\":10.76267,\"lng\":106.66034},\"steps\":[{\"distance\":{\"text\":\"131 m\",\"value\":131},\"duration\":{\"text\":\"25 giây\",\"value\":25},\"end_location\":{\"lat\":10.76381,\"lng\":106.66007},\"html_instructions\":\"Bắt đầu đi từ Lý Thường Kiệt\",\"maneuver\":\"left\",\"polyline\":{\"points\":\"uau`Acb_jSQDe@H{@P_@HI@A?GAe@HU?\"},\"start_location\":{\"lat\":10.76267,\"lng\":106.66034},\"travel_mode\":\"DRIVING\"},{\"distance\":{\"text\":\"636 m\",\"value\":636},\"duration\":{\"text\":\"2 phút\",\"value\":110},\"end_location\":{\"lat\":10.76656,\"lng\":106.66517},\"html_instructions\":\"Rẽ phải vào 3 Tháng 2\",\"maneuver\":\"right\",\"polyline\":{\"points\":\"yhu`Am`_jS_@y@Wm@_@y@Ui@Sc@Wm@IOu@cBe@iAw@eB{@oBw@gBqAwCg@iA\"},\"start_location\":{\"lat\":10.76381,\"lng\":106.66007},\"travel_mode\":\"DRIVING\"},{\"distance\":{\"text\":\"640 m\",\"value\":640},\"duration\":{\"text\":\"1 phút\",\"value\":82},\"end_location\":{\"lat\":10.76933,\"lng\":106.6703},\"html_instructions\":\"Hướng sang trái để vào Cầu vượt Nguyễn Tri Phương\",\"maneuver\":\"slight left\",\"polyline\":{\"points\":\"_zu`Ai``jSOMIOk@kAoCaGuEmKyBkFC[\"},\"start_location\":{\"lat\":10.76656,\"lng\":106.66517},\"travel_mode\":\"DRIVING\"},{\"distance\":{\"text\":\"1.61 km\",\"value\":1608},\"duration\":{\"text\":\"5 phút\",\"value\":279},\"end_location\":{\"lat\":10.77762,\"lng\":106.68186},\"html_instructions\":\"Hướng sang phải để vào 3 Tháng 2\",\"maneuver\":\"slight right\",\"polyline\":{\"points\":\"ikv`Ak`ajSy@eB]s@[s@ISa@{@]w@e@aAuA{CGO_@u@a@y@c@{@_@q@S_@[o@]m@Ua@c@{@a@u@_@s@[m@a@u@]m@[o@qBuDgAsB}AuCs@sAQY_@_@UOSK}G}CiBy@m@c@Oi@DEBG@G@G?G?GAG??CGCGEG\"},\"start_location\":{\"lat\":10.76933,\"lng\":106.6703},\"travel_mode\":\"DRIVING\"},{\"distance\":{\"text\":\"1.41 km\",\"value\":1413},\"duration\":{\"text\":\"4 phút\",\"value\":245},\"end_location\":{\"lat\":10.77145,\"lng\":106.69313},\"html_instructions\":\"Hướng sang phải để vào Cách Mạng Tháng 8\",\"maneuver\":\"slight right\",\"polyline\":{\"points\":\"c_x`AshcjSDStA_DdBuD|AmDl@uAXo@JUTk@h@kAl@uAl@wAXo@h@mApAsCvAiDTi@LW@EFKNa@jAcC|AkDtByExAcDHKPG\"},\"start_location\":{\"lat\":10.77762,\"lng\":106.68186},\"travel_mode\":\"DRIVING\"},{\"distance\":{\"text\":\"53 m\",\"value\":53},\"duration\":{\"text\":\"14 giây\",\"value\":14},\"end_location\":{\"lat\":10.77121,\"lng\":106.69334},\"html_instructions\":\"Hướng sang trái để vào Vòng xoay Ngã sáu Phù Đổng\",\"maneuver\":\"slight left\",\"polyline\":{\"points\":\"qxv`AaoejS@BDFFB??FAFCDG@C@A@C?CBI?I@AAG\"},\"start_location\":{\"lat\":10.77145,\"lng\":106.69313},\"travel_mode\":\"DRIVING\"},{\"distance\":{\"text\":\"26 m\",\"value\":26},\"duration\":{\"text\":\"5 giây\",\"value\":5},\"end_location\":{\"lat\":10.77135,\"lng\":106.69345},\"html_instructions\":\"Hướng sang trái để vào Vòng xoay Ngã sáu Phù Đổng\",\"maneuver\":\"slight left\",\"polyline\":{\"points\":\"awv`AkpejS?A?GAEAC??EC??A???GAA@A?C@\"},\"start_location\":{\"lat\":10.77121,\"lng\":106.69334},\"travel_mode\":\"DRIVING\"},{\"distance\":{\"text\":\"1.12 km\",\"value\":1118},\"duration\":{\"text\":\"4 phút\",\"value\":215},\"end_location\":{\"lat\":10.77794,\"lng\":106.701},\"html_instructions\":\"Rẽ phải vào Lý Tự Trọng\",\"maneuver\":\"right\",\"polyline\":{\"points\":\"}wv`AaqejSEDABeBsCm@_Ai@}@U]eAeBaA}Ac@q@AE_@m@MQU]IQw@oAmAmByA{AkEsD{DmDoAkAuCkC\"},\"start_location\":{\"lat\":10.77135,\"lng\":106.69345},\"travel_mode\":\"DRIVING\"},{\"distance\":{\"text\":\"116 m\",\"value\":116},\"duration\":{\"text\":\"30 giây\",\"value\":30},\"end_location\":{\"lat\":10.77724,\"lng\":106.70178},\"html_instructions\":\"Rẽ phải vào Đồng Khởi\",\"maneuver\":\"right\",\"polyline\":{\"points\":\"cax`Ag`gjSjC{C\"},\"start_location\":{\"lat\":10.77794,\"lng\":106.701},\"travel_mode\":\"DRIVING\"},{\"distance\":{\"text\":\"97 m\",\"value\":97},\"duration\":{\"text\":\"18 giây\",\"value\":18},\"end_location\":{\"lat\":10.77657,\"lng\":106.70121},\"html_instructions\":\"Rẽ phải vào Lê Thánh Tôn\",\"maneuver\":\"right\",\"polyline\":{\"points\":\"w|w`AcegjSdCpB\"},\"start_location\":{\"lat\":10.77724,\"lng\":106.70178},\"travel_mode\":\"DRIVING\"},{\"distance\":{\"text\":\"0 m\",\"value\":0},\"duration\":{\"text\":\"0 giây\",\"value\":0},\"end_location\":{\"lat\":10.77657,\"lng\":106.70121},\"html_instructions\":\"Bạn đã đến điểm đích\",\"maneuver\":\"right\",\"polyline\":{\"points\":\"qxw`AqagjS\"},\"start_location\":{\"lat\":10.77657,\"lng\":106.70121},\"travel_mode\":\"DRIVING\"}]}],\"overview_polyline\":{\"points\":\"uau`Acb_jSQDe@H{@P_@HI@A?GAe@HU?_@y@Wm@_@y@Ui@Sc@Wm@IOu@cBe@iAw@eB{@oBw@gBqAwCg@iAOMIOk@kAoCaGuEmKyBkFC[y@eB]s@[s@ISa@{@]w@e@aAuA{CGO_@u@a@y@c@{@_@q@S_@[o@]m@Ua@c@{@a@u@_@s@[m@a@u@]m@[o@qBuDgAsB}AuCs@sAQY_@_@UOSK}G}CiBy@m@c@Oi@DEBG@G@G?G?GAG??CGCGEGDStA_DdBuD|AmDl@uAXo@JUTk@h@kAl@uAl@wAXo@h@mApAsCvAiDTi@LW@EFKNa@jAcC|AkDtByExAcDHKPG@BDFFB??FAFCDG@C@A@C?CBI?I@AAG?A?GAEAC??EC??A???GAA@A?C@EDABeBsCm@_Ai@}@U]eAeBaA}Ac@q@AE_@m@MQU]IQw@oAmAmByA{AkEsD{DmDoAkAuCkCjC{CdCpB\"},\"summary\":\"\",\"warnings\":[],\"waypoint_order\":[]}]}"

val exampleTripData : String = "{\"code\":\"Ok\",\"trips\":[{\"distance\":33477.8,\"duration\":10195.9,\"geometry\":\"wfl_Coz~dS{CUEb@zF`@nAJzLz@tBPzM`AbAFbJp@w@jH]hDsArK_@bECXEFCLOrAOtAGh@QhBOrA?H@FU~AEXKNJp@Ff@TlBB\\\\@HHpA?FDTDb@DVNtATbBHp@Hp@h@|ERbCdAnM\\\\jEh@bHt@bKZ~DTnCBj@Qv@[`Bo@`EUtAId@Qz@Mz@Mn@Gd@YdBCL_@lBUtA]nBcAxFWzAQdAQ~@WrA]`B_@lAs@zA]j@s@bA_AvAOTe@t@e@r@INUZY^GHSVyApBg@p@mA`B_ApAu@`AiA|AmDzEeCjDUVs@`AQZIZI^G^C^@b@BhABzABd@FzABLFLLzBH`@HRLRBJBD|C~CLLHXD^@NBdACR@PDPFJFFLDN?LENIJOLGLCL?hATN@FAPDjBh@pAb@RFZJVHj@ZFD`Ah@zBvA`Al@|AfAs@lAjAr@CLBMkAs@r@mA}AgAaAm@{BwAaAi@Qk@Mq@F]j@cC`@kBXsATcAF]l@cC^sAN[LUBEd@k@b@e@dCgC^a@x@}@r@x@ORv@~@hAjAt@z@|@`AjA`A@@b@ZZRRL^XXP`DvBdCzA`@w@P_@l@Zh@Vd@Vn@No@Oe@Wi@Wm@[JSBGR_@FKTe@HQLWFKJSBILUVe@XeABI|@}BDOLa@L_@Tm@TLxAn@p@Tx@RnARp@Dr@E`@I\\\\MXOJI\\\\YrAaB~D}Ej@m@j@k@j@g@n@c@n@]`B{@lB{@TKbEgBnEgBjAg@l@[lAs@hCeBfA}@v@q@fAoA`@e@TWjEsEz@eA^k@fEwGfIyMv@mAVa@LS`F{JpAiCJJ`@\\\\d@`@HHJHXVTRFD^\\\\NLTTTPNNNLHF??NJdAt@B@DBARCDc@`@g@l@_@d@IJHK^e@f@m@b@a@BE@SECCAeAu@OK??IGOMOOUQUUOM_@]GEUSYWKImErIqBxDKGq@g@MISOYSW`@y@pAmAnBaBlCuCvEgExGQX_@d@g@l@w@z@}A`BML{@~@a@b@o@t@[^uBbB[T_@VOJkAv@aAh@Ya@X`@k@ZiAf@cA`@kCdALZnEgBjAg@l@[lAs@hCeBfA}@v@q@fAoAFFXX\\\\`@LLb@d@FHJJ|BfCfBrBZ^DDv@~@~AfBn@t@zBfCj@n@PP`@a@Y]UUOQa@c@{DoEf@Yg@X[_@oAuAY]gBuBaAiAaAgAIAQDIHARJJ|BfCfBrBZ^DDv@~@~AfBn@t@zBfCj@n@PPJL^`@j@r@h@j@b@d@f@l@f@n@d@n@b@l@f@t@b@n@\\\\h@f@r@h@x@`@j@d@r@RXnC`ER\\\\`AxAF\\\\FJrAnBHPzA~BT`@Xp@x@~Bf@jA|@jBzAvBh@v@RZnDzFBFBFaAb@OJ^n@dA`BPFBH{BfByCzBeBoCaBiCKOcBmCy@qA}AaCbE}Cb@]\\\\ObBw@vC{BpAwAZ[h@e@BCNMTQ~AkAXULKx@o@bAu@DCj@c@fDgC`CgBVQxBcBb@[tADbA`BLR^n@Zd@`A|Al@~@vBbDdBjCfAfBiAz@eA?iCV_A]k@JSRG^SRRSF_@RSj@K~@\\\\hCWdA?hA{@VOsA_CiEsHKSU_@_BkCkB{CCEa@o@k@}@k@b@eAv@oCrBgJ`HiAz@e@\\\\}ErD]XUPuAf@WRiCnByAbBWTQJuElD_BlAA?k@d@mGxEMOOKMA{@wAKO}@{AoAkBoAiBuCuEcFxDoEfD_Ar@wEbDsEjDe@^sA~@}AhAyAdAw@j@eBnAq@gAc@w@S[Yc@xAeAyAdAe@s@i@y@wBcEvBbE`DaCnA_A|AmAfBqAd@]}BgEu@wAiAyBEIPqAd@UlBaAt@_@fCsAdAk@vAs@lBaAfAi@f@W`B{@`Ae@HIFODUBQ\\\\iCB]@MDo@De@Be@JuA@KH}@Fy@@SBU@Kd@yEn@_CD[@M@EC[AYG_AWkEl@W~AfBn@t@zBfCj@n@PP`@a@Y]UUOQa@c@{DoE[_@oAuAY]q@YeH_I{AcBgCyC{A{A[Yo@c@}@q@_@Q}@c@wBcA_CkAq@[y@_@{@c@_@S{As@yAu@}BiAw@a@kB}@WMWM}EaC{Aw@yAw@EC{Ao@_Ac@YIoDq@WGmCg@oASSE{@SSEeBUy@MyAUc@IICaAQ_@IaAUiBa@fAmGN_ALiADUZ_AZ{@Aq@WqD[kEg@yGWsDYsDGk@MwAEk@Gs@IeAM_BIy@KuAKiAEa@Em@M_BGq@q@eGe@yDKw@Iq@Ea@AG?EAS?W@K@SHq@NmAFc@DWNaA`@uBD[`@qBDUDW@CDQ?CH_@F[@GLm@Je@Lo@??FUDSF]Nq@XyAVoAp@qDVqALo@?ABQt@wDd@aCHe@TcALq@KKSGcBQuC[mIs@{AKyMaAuBO{L}@qAK}AK\",\"legs\":[{\"distance\":6795.3,\"duration\":1259.8,\"steps\":[],\"summary\":\"\",\"weight\":1259.8},{\"distance\":1792,\"duration\":669.6,\"steps\":[],\"summary\":\"\",\"weight\":669.6},{\"distance\":3498.2,\"duration\":732.7,\"steps\":[],\"summary\":\"\",\"weight\":732.7},{\"distance\":1885.2,\"duration\":385.6,\"steps\":[],\"summary\":\"\",\"weight\":385.6},{\"distance\":1641.7,\"duration\":297,\"steps\":[],\"summary\":\"\",\"weight\":297},{\"distance\":2794,\"duration\":2354.1,\"steps\":[],\"summary\":\"\",\"weight\":2354.1},{\"distance\":2208,\"duration\":1922.5,\"steps\":[],\"summary\":\"\",\"weight\":1922.5},{\"distance\":2359.7,\"duration\":517,\"steps\":[],\"summary\":\"\",\"weight\":517},{\"distance\":1501.2,\"duration\":331.9,\"steps\":[],\"summary\":\"\",\"weight\":331.9},{\"distance\":256.1,\"duration\":103.7,\"steps\":[],\"summary\":\"\",\"weight\":103.7},{\"distance\":1664.7,\"duration\":416.3,\"steps\":[],\"summary\":\"\",\"weight\":416.3},{\"distance\":7081.7,\"duration\":1205.7,\"steps\":[],\"summary\":\"\",\"weight\":1205.7}],\"weight\":10195.9,\"weight_name\":\"routability\"}],\"waypoints\":[{\"distance\":5.033847,\"location\":[21.039316,105.839922],\"place_id\":\"ODdMa1o-40e8qGVuabSBj1erbU-6XIrqQ7lTS7tbpI54zCUFoW3x4numVzO_Y6jw6HaZXL7Rvo4tAlMoMum24lUeqcUyMbsKOeqpcSIpviuMckE-mvG2aih0cgriKb_FH\",\"trips_index\":0,\"waypoint_index\":0},{\"distance\":2.754927,\"location\":[21.033061,105.791325],\"place_id\":\"l2yrQbYGlI1quXtGtmqEi27PZBK9LbHqYrpweKEyg85vRqgfjj5u6qetUnm6BG-JIZJsHIMli6Fdu0YfiC-DjHWRXnuKSZftb6tde4sdi-JukU4dvTabi27y5RhyLMvDL\",\"trips_index\":0,\"waypoint_index\":1},{\"distance\":22.416487,\"location\":[21.017809,105.803366],\"place_id\":\"x1-5tByCD5hlAT-u3Gql3maHTa2ZUufhcoFBKrFpkH3CqK3cdq3tVfGR5thixqEGbfFVBIa9UhOdJumMDsmeQ-k-CbyCQRbqefLhOWv5omPF9gl0OrocOmH-qVSWYiOPY\",\"trips_index\":0,\"waypoint_index\":4},{\"distance\":31.015553,\"location\":[21.007758,105.811269],\"place_id\":\"RGmsRfEQsjFpQ8kRiFO4_WqReW-SN33Mco5TN78btPRzvyQrvXz953C_WAGLU57R2WJchSrxrkcYZv1uGvHCWNFi-V86OR47Haq5YTI5-jueia0sYuBiejpS8QzOOGfXO\",\"trips_index\":0,\"waypoint_index\":3},{\"distance\":61.098363,\"location\":[20.998716,105.791924],\"place_id\":\"lW00XriJXGxel1QQjxzJzfLPUCa3am7PaIdQD4h7u8AwuCNoo3u0wTKTlia-Q1CLab-wJJF4mYul5b0A9j2iz-rm6XEqjRKTBbalfS4l5ieBsk0xEaL-ZiYG7H0QfHvJu\",\"trips_index\":0,\"waypoint_index\":7},{\"distance\":43.717286,\"location\":[21.005389,105.787887],\"place_id\":\"TEUEiGrIFsRpxlk6sU2uYWRMeEeFmKmzYf8uQC4efc5m2_E1NVoSQn7LCBUm-Y6bEZ4BWD75lqZl_hVk8sFA5xmXYJp-ASwKgZOmIQoBwgFZlgJoWthaQsmfATT2AF_us\",\"trips_index\":0,\"waypoint_index\":6},{\"distance\":15.681507,\"location\":[21.019095,105.788105],\"place_id\":\"Hmp2S0qkruFZimc9opCTaXa7QQyPjZhFWapdvidnmPJ1q3S2tney4F_PVROhHZf3b7ldMb55to9FqGJKj3r2w3OqVRa6RKGAbKheSohtiMB4kk0evm2Yi4G-6RTWIH_PI\",\"trips_index\":0,\"waypoint_index\":9},{\"distance\":69.479722,\"location\":[21.027187,105.795284],\"place_id\":\"T6Cgf3Fm1Hdqp1gWoXe09ke0U1SlX47PcqcwFrpbiomeq3MtjgDUcqDSnixDZorpp4xDLKQCmZUv0GZov1-s4RnRaQlGQWpWUcrZAVFtmlv9zjJYAUwCGlnGkWyuWAe3W\",\"trips_index\":0,\"waypoint_index\":2},{\"distance\":22.595256,\"location\":[21.012654,105.802381],\"place_id\":\"mVZa3nlbKRqlP30IYmWeY5GbfbwOYVYjHfLSHFqt7aN9_3ZRafVWXmJ2HtK5mXJQhSTeG6657hGlyuTYkkFKqq2XdY1XCU4jhfLgGTphomPF9gl0Org6ImH-qVSWYD-PY\",\"trips_index\":0,\"waypoint_index\":5},{\"distance\":8.278421,\"location\":[21.020549,105.789286],\"place_id\":\"gk_rS99ipGbrnFMcq3GeYtibfkeHY_Fj6cdfHatO-VOCnSwHg3C_U_WMWyy8YaRV8MYoBoVxrFXEmHpFsTudtLOcV2JUDIpEPVKLxQIJyZmeCmK4UtBSSZcJHsD-CFfln\",\"trips_index\":0,\"waypoint_index\":10},{\"distance\":38.043008,\"location\":[21.010007,105.789648],\"place_id\":\"vnFmc9yx2ZxSm0ZAskhntWViSUROr_izobNsP2Jj8chUnFVBvTqLxFH3HShe1Ar30fbJaGIVjg-p9nUZChHyt6IvEVjyFXo9-_KNVQYNzg-pmmUYVtRWTg06xZD6DFPjD\",\"trips_index\":0,\"waypoint_index\":8},{\"distance\":36.917935,\"location\":[21.013758,105.798614],\"place_id\":\"jMiErpx9Nbxok0GCpW8QxW0uwSSulR4SJZqBjKIxtqstdQVZOvBiirGvkVSiLbrL_aJNjF4tuvdNtjEEsphm2oF3yWTWmQZCwaKxaTox8uuVplkkaaRq6jIy-QTFrG_fM\",\"trips_index\":0,\"waypoint_index\":11}]}\n" +
        "\n" +
        "=== Code Execution Successful ==="