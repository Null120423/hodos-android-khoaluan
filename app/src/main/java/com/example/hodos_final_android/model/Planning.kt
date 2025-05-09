package com.example.hodos_final_android.model


data class PlanTripRes(
    val result: Trip,
    val message: String?
)

data class Trip(
    val totalDays: Int,
    val typeTrip: String,
    val startDate: String,
    val endDate: String,
    val budget: String,
    val favorites: List<String>,
    val days: List<TripDay>
)

data class TripDay(
    val dayNumber: Int,
    val date: String,
    val dayOfWeek: String,
    val activities: List<TripActivity>
)

data class TripActivity(
    val id: String,
    val timeStart: String,
    val timeEnd: String,
    val totalTime: String,
    val name: String,
    val description: String,
    val address: String,
    val coordinates: String,
    val img: String
)

data class SaveTripResponse(
    val message: String,
    val isSave: Boolean
)

// Sample data based on the provided JSON
val sampleTrip = Trip(
    totalDays = 2,
    typeTrip = "couple",
    startDate = "08-05-2025",
    endDate = "09-05-2025",
    budget = "luxury",
    favorites = listOf("Culture", "Food"),
    days = listOf(
        TripDay(
            dayNumber = 1,
            date = "08-05-2025",
            dayOfWeek  = "Thursday",
            activities = listOf(
                TripActivity(
                    id = "a70b8a32-f6b4-4338-a944-a82af623dc71",
                    timeStart = "09:00",
                    timeEnd = "11:00",
                    totalTime = "02:00",
                    name = "Independence Palace",
                    address = "135 Nam Ky Khoi Nghia Street, Ben Thanh Ward, District 1",
                    coordinates = "10.7765164,106.6957253",
                    img = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/48/c4/e4/back-entrance-of-ho-chi.jpg?w=1200&h=-1&s=1",
                    description = "Independence Palace is a historical landmark that preserves proud milestones of the Vietnamese nation in the struggle to protect the homeland. Designed by architect Ngô Viết Thụ with the intention of creating a culturally significant structure, Independence Palace embodies Eastern cultural elements throughout its interior, exterior, and landscaping, seamlessly blended with modern design. Specifically, the architecture of the building is modeled after Han-Vietnamese characters with auspicious meanings. With a total area of \u200B\u200B120,000 square meters, the site is divided into three distinct zones (Fixed Zone, Specialized Zone, Supplementary Zone), each with its own unique characteristics. Independence Palace is open to tourists for visits every day of the week during two time slots: from 7:30 am to 11:30 am in the morning and from 1:00 pm to 5:00 pm in the afternoon."
                ),
                TripActivity(
                    id = "a70b8a32-f6b4-4338-a944-a82af623dc71",
                    timeStart = "09:00",
                    timeEnd = "11:00",
                    totalTime = "02:00",
                    name = "Independence Palace",
                    address = "135 Nam Ky Khoi Nghia Street, Ben Thanh Ward, District 1",
                    coordinates = "10.7765164,106.6957253",
                    img = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/48/c4/e4/back-entrance-of-ho-chi.jpg?w=1200&h=-1&s=1",
                    description = "Independence Palace is a historical landmark that preserves proud milestones of the Vietnamese nation in the struggle to protect the homeland. Designed by architect Ngô Viết Thụ with the intention of creating a culturally significant structure, Independence Palace embodies Eastern cultural elements throughout its interior, exterior, and landscaping, seamlessly blended with modern design. Specifically, the architecture of the building is modeled after Han-Vietnamese characters with auspicious meanings. With a total area of \u200B\u200B120,000 square meters, the site is divided into three distinct zones (Fixed Zone, Specialized Zone, Supplementary Zone), each with its own unique characteristics. Independence Palace is open to tourists for visits every day of the week during two time slots: from 7:30 am to 11:30 am in the morning and from 1:00 pm to 5:00 pm in the afternoon."
                ),
                TripActivity(
                    id = "a70b8a32-f6b4-4338-a944-a82af623dc71",
                    timeStart = "09:00",
                    timeEnd = "11:00",
                    totalTime = "02:00",
                    name = "Independence Palace",
                    address = "135 Nam Ky Khoi Nghia Street, Ben Thanh Ward, District 1",
                    coordinates = "10.7765164,106.6957253",
                    img = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/48/c4/e4/back-entrance-of-ho-chi.jpg?w=1200&h=-1&s=1",
                    description = "Independence Palace is a historical landmark that preserves proud milestones of the Vietnamese nation in the struggle to protect the homeland. Designed by architect Ngô Viết Thụ with the intention of creating a culturally significant structure, Independence Palace embodies Eastern cultural elements throughout its interior, exterior, and landscaping, seamlessly blended with modern design. Specifically, the architecture of the building is modeled after Han-Vietnamese characters with auspicious meanings. With a total area of \u200B\u200B120,000 square meters, the site is divided into three distinct zones (Fixed Zone, Specialized Zone, Supplementary Zone), each with its own unique characteristics. Independence Palace is open to tourists for visits every day of the week during two time slots: from 7:30 am to 11:30 am in the morning and from 1:00 pm to 5:00 pm in the afternoon."
                ),
                TripActivity(
                    id = "a70b8a32-f6b4-4338-a944-a82af623dc71",
                    timeStart = "09:00",
                    timeEnd = "11:00",
                    totalTime = "02:00",
                    name = "Independence Palace",
                    address = "135 Nam Ky Khoi Nghia Street, Ben Thanh Ward, District 1",
                    coordinates = "10.7765164,106.6957253",
                    img = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/48/c4/e4/back-entrance-of-ho-chi.jpg?w=1200&h=-1&s=1",
                    description = "Independence Palace is a historical landmark that preserves proud milestones of the Vietnamese nation in the struggle to protect the homeland. Designed by architect Ngô Viết Thụ with the intention of creating a culturally significant structure, Independence Palace embodies Eastern cultural elements throughout its interior, exterior, and landscaping, seamlessly blended with modern design. Specifically, the architecture of the building is modeled after Han-Vietnamese characters with auspicious meanings. With a total area of \u200B\u200B120,000 square meters, the site is divided into three distinct zones (Fixed Zone, Specialized Zone, Supplementary Zone), each with its own unique characteristics. Independence Palace is open to tourists for visits every day of the week during two time slots: from 7:30 am to 11:30 am in the morning and from 1:00 pm to 5:00 pm in the afternoon."
                )
            )
        ),
        TripDay(
            dayNumber = 1,
            date = "08-05-2025",
            dayOfWeek  = "Thursday",
            activities = listOf(
                TripActivity(
                    id = "a70b8a32-f6b4-4338-a944-a82af623dc71",
                    timeStart = "09:00",
                    timeEnd = "11:00",
                    totalTime = "02:00",
                    name = "Independence Palace",
                    address = "135 Nam Ky Khoi Nghia Street, Ben Thanh Ward, District 1",
                    coordinates = "10.7765164,106.6957253",
                    img = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/48/c4/e4/back-entrance-of-ho-chi.jpg?w=1200&h=-1&s=1",
                    description = "Independence Palace is a historical landmark that preserves proud milestones of the Vietnamese nation in the struggle to protect the homeland. Designed by architect Ngô Viết Thụ with the intention of creating a culturally significant structure, Independence Palace embodies Eastern cultural elements throughout its interior, exterior, and landscaping, seamlessly blended with modern design. Specifically, the architecture of the building is modeled after Han-Vietnamese characters with auspicious meanings. With a total area of \u200B\u200B120,000 square meters, the site is divided into three distinct zones (Fixed Zone, Specialized Zone, Supplementary Zone), each with its own unique characteristics. Independence Palace is open to tourists for visits every day of the week during two time slots: from 7:30 am to 11:30 am in the morning and from 1:00 pm to 5:00 pm in the afternoon."
                ),
                TripActivity(
                    id = "a70b8a32-f6b4-4338-a944-a82af623dc71",
                    timeStart = "09:00",
                    timeEnd = "11:00",
                    totalTime = "02:00",
                    name = "Independence Palace",
                    address = "135 Nam Ky Khoi Nghia Street, Ben Thanh Ward, District 1",
                    coordinates = "10.7765164,106.6957253",
                    img = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/48/c4/e4/back-entrance-of-ho-chi.jpg?w=1200&h=-1&s=1",
                    description = "Independence Palace is a historical landmark that preserves proud milestones of the Vietnamese nation in the struggle to protect the homeland. Designed by architect Ngô Viết Thụ with the intention of creating a culturally significant structure, Independence Palace embodies Eastern cultural elements throughout its interior, exterior, and landscaping, seamlessly blended with modern design. Specifically, the architecture of the building is modeled after Han-Vietnamese characters with auspicious meanings. With a total area of \u200B\u200B120,000 square meters, the site is divided into three distinct zones (Fixed Zone, Specialized Zone, Supplementary Zone), each with its own unique characteristics. Independence Palace is open to tourists for visits every day of the week during two time slots: from 7:30 am to 11:30 am in the morning and from 1:00 pm to 5:00 pm in the afternoon."
                ),
                TripActivity(
                    id = "a70b8a32-f6b4-4338-a944-a82af623dc71",
                    timeStart = "09:00",
                    timeEnd = "11:00",
                    totalTime = "02:00",
                    name = "Independence Palace",
                    address = "135 Nam Ky Khoi Nghia Street, Ben Thanh Ward, District 1",
                    coordinates = "10.7765164,106.6957253",
                    img = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/48/c4/e4/back-entrance-of-ho-chi.jpg?w=1200&h=-1&s=1",
                    description = "Independence Palace is a historical landmark that preserves proud milestones of the Vietnamese nation in the struggle to protect the homeland. Designed by architect Ngô Viết Thụ with the intention of creating a culturally significant structure, Independence Palace embodies Eastern cultural elements throughout its interior, exterior, and landscaping, seamlessly blended with modern design. Specifically, the architecture of the building is modeled after Han-Vietnamese characters with auspicious meanings. With a total area of \u200B\u200B120,000 square meters, the site is divided into three distinct zones (Fixed Zone, Specialized Zone, Supplementary Zone), each with its own unique characteristics. Independence Palace is open to tourists for visits every day of the week during two time slots: from 7:30 am to 11:30 am in the morning and from 1:00 pm to 5:00 pm in the afternoon."
                ),
                TripActivity(
                    id = "a70b8a32-f6b4-4338-a944-a82af623dc71",
                    timeStart = "09:00",
                    timeEnd = "11:00",
                    totalTime = "02:00",
                    name = "Independence Palace",
                    address = "135 Nam Ky Khoi Nghia Street, Ben Thanh Ward, District 1",
                    coordinates = "10.7765164,106.6957253",
                    img = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/48/c4/e4/back-entrance-of-ho-chi.jpg?w=1200&h=-1&s=1",
                    description = "Independence Palace is a historical landmark that preserves proud milestones of the Vietnamese nation in the struggle to protect the homeland. Designed by architect Ngô Viết Thụ with the intention of creating a culturally significant structure, Independence Palace embodies Eastern cultural elements throughout its interior, exterior, and landscaping, seamlessly blended with modern design. Specifically, the architecture of the building is modeled after Han-Vietnamese characters with auspicious meanings. With a total area of \u200B\u200B120,000 square meters, the site is divided into three distinct zones (Fixed Zone, Specialized Zone, Supplementary Zone), each with its own unique characteristics. Independence Palace is open to tourists for visits every day of the week during two time slots: from 7:30 am to 11:30 am in the morning and from 1:00 pm to 5:00 pm in the afternoon."
                )
            )
        ),
        TripDay(
            dayNumber = 1,
            date = "08-05-2025",
            dayOfWeek  = "Thursday",
            activities = listOf(
                TripActivity(
                    id = "a70b8a32-f6b4-4338-a944-a82af623dc71",
                    timeStart = "09:00",
                    timeEnd = "11:00",
                    totalTime = "02:00",
                    name = "Independence Palace",
                    address = "135 Nam Ky Khoi Nghia Street, Ben Thanh Ward, District 1",
                    coordinates = "10.7765164,106.6957253",
                    img = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/48/c4/e4/back-entrance-of-ho-chi.jpg?w=1200&h=-1&s=1",
                    description = "Independence Palace is a historical landmark that preserves proud milestones of the Vietnamese nation in the struggle to protect the homeland. Designed by architect Ngô Viết Thụ with the intention of creating a culturally significant structure, Independence Palace embodies Eastern cultural elements throughout its interior, exterior, and landscaping, seamlessly blended with modern design. Specifically, the architecture of the building is modeled after Han-Vietnamese characters with auspicious meanings. With a total area of \u200B\u200B120,000 square meters, the site is divided into three distinct zones (Fixed Zone, Specialized Zone, Supplementary Zone), each with its own unique characteristics. Independence Palace is open to tourists for visits every day of the week during two time slots: from 7:30 am to 11:30 am in the morning and from 1:00 pm to 5:00 pm in the afternoon."
                ),
                TripActivity(
                    id = "a70b8a32-f6b4-4338-a944-a82af623dc71",
                    timeStart = "09:00",
                    timeEnd = "11:00",
                    totalTime = "02:00",
                    name = "Independence Palace",
                    address = "135 Nam Ky Khoi Nghia Street, Ben Thanh Ward, District 1",
                    coordinates = "10.7765164,106.6957253",
                    img = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/48/c4/e4/back-entrance-of-ho-chi.jpg?w=1200&h=-1&s=1",
                    description = "Independence Palace is a historical landmark that preserves proud milestones of the Vietnamese nation in the struggle to protect the homeland. Designed by architect Ngô Viết Thụ with the intention of creating a culturally significant structure, Independence Palace embodies Eastern cultural elements throughout its interior, exterior, and landscaping, seamlessly blended with modern design. Specifically, the architecture of the building is modeled after Han-Vietnamese characters with auspicious meanings. With a total area of \u200B\u200B120,000 square meters, the site is divided into three distinct zones (Fixed Zone, Specialized Zone, Supplementary Zone), each with its own unique characteristics. Independence Palace is open to tourists for visits every day of the week during two time slots: from 7:30 am to 11:30 am in the morning and from 1:00 pm to 5:00 pm in the afternoon."
                ),
                TripActivity(
                    id = "a70b8a32-f6b4-4338-a944-a82af623dc71",
                    timeStart = "09:00",
                    timeEnd = "11:00",
                    totalTime = "02:00",
                    name = "Independence Palace",
                    address = "135 Nam Ky Khoi Nghia Street, Ben Thanh Ward, District 1",
                    coordinates = "10.7765164,106.6957253",
                    img = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/48/c4/e4/back-entrance-of-ho-chi.jpg?w=1200&h=-1&s=1",
                    description = "Independence Palace is a historical landmark that preserves proud milestones of the Vietnamese nation in the struggle to protect the homeland. Designed by architect Ngô Viết Thụ with the intention of creating a culturally significant structure, Independence Palace embodies Eastern cultural elements throughout its interior, exterior, and landscaping, seamlessly blended with modern design. Specifically, the architecture of the building is modeled after Han-Vietnamese characters with auspicious meanings. With a total area of \u200B\u200B120,000 square meters, the site is divided into three distinct zones (Fixed Zone, Specialized Zone, Supplementary Zone), each with its own unique characteristics. Independence Palace is open to tourists for visits every day of the week during two time slots: from 7:30 am to 11:30 am in the morning and from 1:00 pm to 5:00 pm in the afternoon."
                ),
                TripActivity(
                    id = "a70b8a32-f6b4-4338-a944-a82af623dc71",
                    timeStart = "09:00",
                    timeEnd = "11:00",
                    totalTime = "02:00",
                    name = "Independence Palace",
                    address = "135 Nam Ky Khoi Nghia Street, Ben Thanh Ward, District 1",
                    coordinates = "10.7765164,106.6957253",
                    img = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/48/c4/e4/back-entrance-of-ho-chi.jpg?w=1200&h=-1&s=1",
                    description = "Independence Palace is a historical landmark that preserves proud milestones of the Vietnamese nation in the struggle to protect the homeland. Designed by architect Ngô Viết Thụ with the intention of creating a culturally significant structure, Independence Palace embodies Eastern cultural elements throughout its interior, exterior, and landscaping, seamlessly blended with modern design. Specifically, the architecture of the building is modeled after Han-Vietnamese characters with auspicious meanings. With a total area of \u200B\u200B120,000 square meters, the site is divided into three distinct zones (Fixed Zone, Specialized Zone, Supplementary Zone), each with its own unique characteristics. Independence Palace is open to tourists for visits every day of the week during two time slots: from 7:30 am to 11:30 am in the morning and from 1:00 pm to 5:00 pm in the afternoon."
                )
            )
        )
    ),
)
