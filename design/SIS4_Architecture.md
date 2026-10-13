# SIS4 architecture plan

![RateRadar MVVM architecture](04_mvvm_architecture.png)

## Layers

- View: `ExchangeOfficeListScreen`, `OfficeDetailsScreen`, `FavoritesScreen`. Each receives `RateRadarUiState` and event callbacks. Screens only display state.
- Routes: `ExchangeOfficeListRoute`, `OfficeDetailsRoute`, `FavoritesRoute` collect the state with `collectAsStateWithLifecycle()` and pass events to the ViewModel. Navigation remains in `RateRadarNavGraph`.
- ViewModel: one `RateRadarViewModel`, created above the NavHost and scoped to `MainActivity`, serves all three screens. It owns search, city selection, favorite IDs, selected office and loading status.
- Model: `ExchangeOffice` is the existing data class.
- Repository: `ExchangeOfficeRepository` owns the twelve demo records and the city list. `loadOffices()` suspends for 1000 ms and can fail once when `shouldFail` is enabled. A Retry then succeeds.

## Order board: RateRadarUiState

All properties use `val`:

| Field | Purpose |
|---|---|
| `offices: List<ExchangeOffice>` | Main list after search and city filtering |
| `favoriteOffices: List<ExchangeOffice>` | Favorite records, independent of the main filter |
| `favoriteIds: Set<Int>` | Shared favorite IDs |
| `searchQuery: String` | Search text |
| `selectedCity: String` | Selected city, initially All |
| `cities: List<String>` | Filter choices |
| `selectedOfficeId: Int?` | Current details ID |
| `selectedOffice: ExchangeOffice?` | Resolved details record |
| `isLoading: Boolean` | Repository request in progress |
| `error: String?` | Load error, or null |

Loading has `isLoading = true`; error has a message; content has records; empty has no matching records. The ViewModel has a private `MutableStateFlow` and exposes its `asStateFlow()` view. Changes are made with `copy()`.

## Events

- `onSearchChange(query)` searches names, cities and addresses, ignoring case and surrounding whitespace.
- `onCitySelected(city)` selects a city.
- `onFavoriteClick(id)` toggles an existing office's favorite status.
- `onOfficeSelected(id)` resolves the record for details.
- `onRetry()` starts another load, without dropping search, filter or favorites.

## Trace one click

The user taps an OfficeCard heart. The screen calls its `onFavoriteClick(office.id)` callback. The Route forwards the ID to `RateRadarViewModel.onFavoriteClick`. The ViewModel creates a new favorite ID set and updates `favoriteOffices` with `copy()`. StateFlow emits the new state, the Route collects it, and the main list heart, details button and Favorites list show the same selection.

## Implementation and checks

1. Commit this plan and diagram before refactoring any product code.
2. Add the delayed/error-capable repository, immutable UiState and shared ViewModel. Check loading, query/city combinations, empty results, favorites, invalid IDs, failure and Retry with coroutine tests.
3. Connect stateless screens through Routes, keep the three screens and existing theme, add loading/error display and four state previews per screen. Main screen starts with the list.
4. Add Activity and ViewModel lifecycle logs. In an emulator, search, filter and mark a favorite, rotate, and capture actual logs and screenshots.
5. Update README and AI_USAGE with real names, the click trace, observed rotation logs and the logic moved out of composables. Do not add bonus domain/use-case or separate details-ViewModel layers.

Rotation retains the Activity-scoped ViewModel. Process death and permanent disk persistence are outside this assignment.
