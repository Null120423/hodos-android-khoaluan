package com.example.hodos_final_android.model

data class Category(
    val title: String,
    val thumbnail: String
)

val categories = listOf(
    Category(
        title = "History",
        thumbnail = "https://www.nxbtre.com.vn/Images/Book/NXBTreStoryFull_23172012_041758.jpg"
    ),
    Category(
        title = "Culture",
        thumbnail = "https://dulichokela.com/wp-content/uploads/2024/05/van-hoa-viet-nam.jpg"
    ),
    Category(
        title = "Entertainment",
        thumbnail = "https://is.vnecdn.net/v710/55/80/49/4498055/assets/images/img-1.png"
    ),
    Category(
        title = "Meals",
        thumbnail = "https://thoidai.com.vn/stores/news_dataimages/anh.van/042022/28/06/3639_1601262245503-63737441452448.jpg?rt=20220428063646"
    ),
    Category(
        title = "Adventure",
        thumbnail = "https://portal.vtc.gov.vn/Storage/nguyenletan/attachfiles_15/KhamphaVN_Banner/KhamphaVN_Banner.jpg"
    )
)
