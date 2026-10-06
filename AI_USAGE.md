# AI usage (SIS3)

## Which AI tools I used and for what

- **A chat AI assistant** — to turn the SIS3 task into a detailed prompt: app idea, list of screens,
  design brief (calm teal colors, white cards) and a checklist of the requirements.
- **Claude Code** — to generate the project from that prompt: design sketches, theme,
  data, components, screens, navigation, screenshots and the README.

## My 3 most useful prompts

1. > Generate an Android project in Kotlin + Jetpack Compose for SIS3. Keep the code simple,
   > like a student who just started Android. No Hilt, Room, Retrofit or complex architecture.
   > 3 screens: Exchange Offices, Office Details, Favorites. Navigation Compose, Scaffold +
   > TopAppBar on every screen, LazyColumn with 12 offices, LazyRow with city filter chips,
   > an Image with contentDescription, empty state, long title with ellipsis, custom color
   > scheme with dark theme, 3 reusable components (OfficeCard, RateBlock, EmptyState).
2. > Do SIS3 carefully, strictly within the assignment. Don't leave AI comments, don't add
   > anything extra and don't write over-complicated code — correct and understandable is better.
3. > Explain step by step in simple words how the app works: where the colors come from,
   > how the id gets to the details screen and how favorites are stored.

## A case where the AI was wrong / inconsistent, and how I fixed it

- **Unclear code on the details screen.** The first version of `OfficeDetailsScreen` handled
  "office not found" with `return@Scaffold` inside the Scaffold content lambda. It worked, but
  it was a label-return that I could not easily explain, and it looked different from
  `FavoritesScreen`, which uses a plain `if / else` for the empty state. I asked to rewrite it
  as `if (office == null) { EmptyState } else { Column }`, so both screens follow the same pattern.
- **Overlapping labels in the sketches.** In the first generated sketches the "RateBlock"
  labels were drawn on top of the "Almaty · updated 10:30" text and the TopAppBar label
  overlapped the IconButton label. I noticed it when I opened the PNGs, and the cards
  were made taller so every label is readable.
- **Old theme was incomplete.** The theme from SIS1 defined only 4 colors for dark mode,
  so all other colors (background, surface, cards) fell back to Material's default purple
  palette. It was replaced with full light and dark schemes, and all color values now live
  only in `Color.kt`.

## What I changed or wrote by hand

- The app idea (RateRadar KZ, exchange offices in Kazakhstan) and the screen list come from
  my own SIS1 project description.
- I asked for the code to stay simple (no ViewModel, no database, no extra libraries) so that
  I can explain every line, and I reviewed every file before submitting.
