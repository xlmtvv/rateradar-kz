package kz.edu.kbtu.rateradar.ui.preview

import kz.edu.kbtu.rateradar.model.ExchangeOffice
import kz.edu.kbtu.rateradar.viewmodel.RateRadarUiState

internal object PreviewData {
    val office = ExchangeOffice(1, "Abay Exchange", "Almaty", "Abay Ave 52", "09:00 - 20:00", 512.5, 515.0, "10:30")
    val longOffice = office.copy(id = 3, name = "Mega Alma-Ata Shopping Center Ground Floor Currency Exchange Office")
    val content = RateRadarUiState(
        offices = listOf(office, longOffice),
        favoriteOffices = listOf(office),
        favoriteIds = setOf(1),
        cities = listOf("All", "Almaty", "Astana", "Shymkent", "Karaganda"),
        selectedOfficeId = office.id,
        selectedOffice = office,
        isLoading = false,
    )
    val loading = content.copy(isLoading = true)
    val empty = content.copy(offices = emptyList(), favoriteOffices = emptyList(), favoriteIds = emptySet(), selectedOfficeId = null, selectedOffice = null)
    val error = content.copy(error = "Unable to load exchange offices. Please try again.")
}
