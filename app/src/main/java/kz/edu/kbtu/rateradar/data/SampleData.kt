package kz.edu.kbtu.rateradar.data

import kz.edu.kbtu.rateradar.model.ExchangeOffice

// Demo data: the app has no internet, all offices are written here by hand
object SampleData {

    const val ALL_CITIES = "All"

    // Karaganda has no offices on purpose, so the filter can show an empty state
    val cities = listOf(ALL_CITIES, "Almaty", "Astana", "Shymkent", "Karaganda")

    val offices = listOf(
        ExchangeOffice(1, "Abay Exchange", "Almaty", "Abay Ave 52", "09:00 – 20:00", 512.50, 515.00, "10:30"),
        ExchangeOffice(2, "Dostyk Currency", "Almaty", "Dostyk Ave 85", "10:00 – 21:00", 512.80, 515.40, "10:25"),
        ExchangeOffice(
            3,
            "Mega Alma-Ata Shopping Center Ground Floor Currency Exchange Office",
            "Almaty",
            "Rozybakiev St 247A",
            "10:00 – 22:00",
            511.90,
            516.10,
            "10:15",
        ),
        ExchangeOffice(4, "Tole Bi Obmen", "Almaty", "Tole Bi St 101", "09:00 – 19:00", 512.20, 514.90, "10:40"),
        ExchangeOffice(5, "Zhibek Zholy Point", "Almaty", "Zhibek Zholy Ave 64", "09:30 – 20:30", 512.60, 515.20, "10:05"),
        ExchangeOffice(6, "Baiterek Exchange", "Astana", "Nurzhol Blvd 14", "09:00 – 21:00", 513.00, 515.80, "10:35"),
        ExchangeOffice(7, "Khan Shatyr Currency", "Astana", "Turan Ave 37", "10:00 – 22:00", 512.40, 516.00, "10:20"),
        ExchangeOffice(8, "Kenesary Obmen", "Astana", "Kenesary St 40", "09:00 – 19:00", 512.90, 515.30, "09:55"),
        ExchangeOffice(9, "Expo Money Point", "Astana", "Mangilik El Ave 55", "08:00 – 20:00", 512.70, 515.60, "10:10"),
        ExchangeOffice(10, "Tauke Khan Exchange", "Shymkent", "Tauke Khan Ave 23", "09:00 – 20:00", 511.80, 514.70, "10:00"),
        ExchangeOffice(11, "Shymkent Plaza Obmen", "Shymkent", "Kunaev Blvd 9", "10:00 – 21:00", 512.10, 515.10, "10:45"),
        ExchangeOffice(12, "Respublika Currency", "Shymkent", "Respublika Ave 6", "09:00 – 18:00", 511.60, 514.50, "09:50"),
    )
}
