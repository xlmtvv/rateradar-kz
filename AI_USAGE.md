# AI usage - SIS3 and SIS4

## Tools and tasks

- **Claude Code:** assistance with the Kotlin/Compose implementation, including code drafts, sample data, reusable components, navigation, theme setup, and the preparation and labeling of design sketches.
- **ChatGPT / Codex:** checking the assignment requirements, reviewing the project, and explaining Kotlin syntax, Compose state, navigation, and Android lifecycle concepts during preparation for the defense.

## Three useful prompts

The following are verbatim excerpts from my actual follow-up requests for review and explanation. They are not reconstructed Claude Code prompts.

1. > Помоги мне с моим проектом по андроиду, не делай за меня, будь моим преподавателем

2. > мне нужно прям полное понимание чтоб я все абсолютно мог понять как что работает

3. > давай лучше реверс инженернем все или как сказать, ну то есть как будто правильнее будет если я щас с тобой попробую воссоздать такой же проект с нуля да ты мне объяснишь простыми словами какие команды вводить там что еще делать и т д какие файлы папки создавать какой код туда вписывать и я буду вписывать и как раз будем идти и по пути все разбирать пояснять типо вот это то се, как будто такой подход намного разумнее

## An issue found and corrected

During AI-assisted review, the accessibility description of the details image was found to be inaccurate: it said "Photo", but `exchange_placeholder.xml` is a vector illustration. The description in `strings.xml` was changed to "Illustration of the %1$s exchange office" so that it matches the resource actually displayed. This correction was made with AI assistance.

## My contribution

- I supplied the RateRadar KZ app idea from SIS1 and requested a simple implementation within the assignment scope.
- I used follow-up questions and a separate practice project to study the implementation, and requested explanations of the data flow and UI behavior.
- AI-assisted coding and review were part of the workflow. The SIS3 version used local demo data and Compose state without a database, a live currency API or a ViewModel.

## SIS4: MVVM refactor

Codex assisted with the architecture plan, Kotlin refactoring, coroutine tests and emulator checks. The refactor kept the existing three screens and theme, added Repository loading/error simulation, and moved important state to a shared ViewModel. The architecture plan and diagram were committed before the implementation.

A request for this work was:

> строго по заданию прям строго что просят то сделай ничего лишнего

### Logic in the wrong layer

The AI-assisted SIS3 version filtered offices in `ExchangeOfficeListScreen`, filtered favorites in `FavoritesScreen`, and looked up a details record in `OfficeDetailsScreen`. For MVVM, these data operations belonged outside the composables. They were moved to `RateRadarViewModel.updatedState`, which prepares `offices`, `favoriteOffices` and `selectedOffice`. Screens now receive a `RateRadarUiState` and event callbacks; Routes collect the StateFlow and forward actions.

The review also checked that the ViewModel was created above the NavHost, so all destinations use the same Activity-scoped instance. Rotation logs and emulator checks are recorded in the README. Code, tests and these documentation updates received AI assistance.
