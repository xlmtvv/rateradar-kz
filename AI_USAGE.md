# AI usage - SIS3

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
- AI-assisted coding and review were part of the workflow. The current app uses local demo data and Compose state; it does not include a database, a live currency API, or a ViewModel.
