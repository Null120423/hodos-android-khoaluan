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