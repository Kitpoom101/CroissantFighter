# 🥐 CroissantFighter

## Welcome to CroissantFighter!!

**CroissantFighter** is a **2-player fighting game built with Java and
JavaFX**. Players select characters with unique abilities and battle
each other in a fast-paced arena.

Each character belongs to a different combat class, giving them
different playstyles and strengths.

------------------------------------------------------------------------

## 🎮 Features

-   Multiple playable characters
-   Character selection screen
-   Different combat classes
  -   ⚔️ **Melee** -- strong close combat fighters
  -   🏹 **Ranged** -- attack from a distance
  -   🧪 **Hybrid** -- mix of melee and ranged abilities
-   Attack animations
-   Projectile combat system
-   Local multiplayer gameplay

------------------------------------------------------------------------

## 🛠 Technologies Used

-   Java
-   JavaFX
-   Gradle
-   JUnit

------------------------------------------------------------------------

## 🚀 How to Run the Game

Make sure **Java** and **JavaFX** are installed on your machine.

Run the game using the following command:

``` bash
java --module-path "E:\javafx-sdk-25.0.1\lib" --add-modules javafx.controls,javafx.media -jar CroissantFighter-1-with-sources.jar
```

If your JavaFX SDK is installed somewhere else, replace the path with
your own JavaFX `lib` directory.

------------------------------------------------------------------------

## 📁 Project Structure

    CroissantFighter
    │
    ├── src/
    │   ├── main/
    │   │   ├── java/          # All main game source code
    │   │   │   ├── application
    │   │   │   ├── component
    │   │   │   └── logic
    │   │   │
    │   │   └── resources/     # Game assets
    │   │       ├── animations
    │   │       ├── sprites
    │   │       ├── audio
    │   │       └── images
    │   │
    │   └── test/
    │       └── java/          # JUnit test files
    │
    ├── CharacterUML.png       # UML diagram of the character class structure
    │
    ├── build.gradle
    ├── gradlew
    └── README.md

------------------------------------------------------------------------

## 📊 UML Diagram

The **class design of the characters** can be found in:

    CharacterUML.png

This diagram shows the inheritance structure between: - Base
`Character` - `MeleeClass` - `RangedClass` - `HybridClass` - Individual
playable characters.

------------------------------------------------------------------------


## 🎥 Presentation Video

The **presentation video of the project** can be found in:

https://youtu.be/Lc1Iv6tWehU

[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/IY9augGa)
