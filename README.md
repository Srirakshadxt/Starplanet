# Star Planet App

## Introduction

Star Planet is an Android app that showcases the planets from the Star Wars universe using
the [Star Wars API](https://swapi.dev/api/planets/). The app allows users to explore a list of
planets, view detailed information about each planet, and provides offline functionality for a
seamless user experience.

## Setup

- Android Studio
- Gradle 8.4.0
- Kotlin 1.9.0

## Features

- Displays a list of Star Wars planets on the main screen
- Allows users to select a planet and view its detailed information
- Handles offline use cases by caching data for offline access
- Follows the latest Android architecture recommendations and utilizes popular libraries
- Implements unit tests and instrumentation tests to ensure code quality and reliability
- Optimized for performance to provide a smooth user experience
- Handles edge cases and positive/negative scenarios for robust functionality

## Technologies Used

- Kotlin: The app is developed using the Kotlin programming language, which provides a concise and
  expressive way of writing Android code.
- MVVM Architecture: The app follows the Model-View-ViewModel (MVVM) architectural pattern,
  separating the concerns of data, UI, and business logic.
- Room: The Room persistence library is used for offline caching of planet data, allowing the app to
  function without an internet connection.
- Retrofit: Retrofit, a type-safe HTTP client, is used to make API calls to the Star Wars API and
  retrieve planet data.
- Coroutines: Kotlin coroutines are used for asynchronous programming, enabling efficient and
  responsive network operations.
- Flows: The Kotlin Flow library is used for reactive programming, allowing seamless management of
  data streams and updates.
- Hilt: The dependency injection library, is used to manage dependencies and promote a modular
  and testable codebase.
- Jetpack Compose: The app's UI is built using Jetpack Compose, a modern and declarative UI toolkit
  for building native Android UI.
- Unit Testing: The app includes comprehensive unit tests to verify the correctness of business and
  presentation
  logic and ensure code reliability.
- Instrumentation Testing: Implemented using Mockito and Compose Testing
  to validate the app's UI components and user interactions.
- Jacoco: Used for code coverage analysis, providing insights into the effectiveness of
  the app's test suite and identifying areas that require additional testing.

## SOLID Principles

The Star Planet app follows the SOLID principles of object-oriented design to ensure a clean and
maintainable codebase. Interfaces and abstraction play a crucial role in achieving this.

Interfaces are used to define contracts between classes and enable loose coupling. They provide a
level of abstraction and allow for easy mocking during testing. By depending on interfaces rather
than concrete implementations, the app becomes more flexible and testable.

For example, the `PlanetRepository` interface defines the contract for accessing planet data, while
the `PlanetRepositoryImpl` class provides the actual implementation. This separation allows for easy
mocking of the repository in unit tests, ensuring that the tests focus on the behavior of the code
under test without relying on external dependencies.

## Demonstration

![starplanet](https://github.com/Srirakshadxt/Starplanet/assets/158619201/6df2e40e-ab6a-4854-a452-90c76e285e41)

## Future Improvements

- Add search functionality to allow users to search for specific planets
- Enhance the UI with animations, transitions, and improved UX using Jetpack Compose
- Implement Material Design principles for a polished and consistent UI
- Implement a favorites feature to allow users to mark and save their favorite planets
- Expand the app to include other Star Wars entities such as characters, starships, and films
- Improve accessibility and localization support for a broader user base
- Integrate Firebase Crashlytics to monitor and report crashes, providing a crash-free experience
  for users

## Conclusion

The Star Planet app demonstrates the use of modern Android development practices and libraries to
create a robust and user-friendly application. By leveraging Kotlin, MVVM architecture, Room,
Retrofit, coroutines, flows, Hilt, and Jetpack Compose, the app provides a seamless experience for
exploring Star Wars planets.

The app's architecture and design decisions prioritize code quality, testability, and performance.
The use of SOLID principles, interfaces, and abstraction promotes a clean and maintainable codebase,
while unit tests and instrumentation tests ensure the reliability and correctness of the app's
functionality.

With its offline capabilities, the Star Planet app offers a reliable and enjoyable way for Star Wars
enthusiasts to discover and learn about the planets in the Star Wars universe, even in the absence
of an internet connection.
