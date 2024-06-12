# Yemek Tarifleri - Recipe App

Yemek Tarifleri is a mobile application for browsing and managing recipes. The app allows users to search for recipes, view recipe details, and mark recipes as favorite.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)

## Features

- **Recipe Search**: Users can search for recipes based on keywords.
- **Recipe Details**: Users can view detailed information about a recipe, including ingredients, summary, and instructions.
- **Favorite Recipes**: Users can mark recipes as favorites and manage their favorite recipes.
- **Offline Access**: Recipes are stored locally to allow offline access.

## Technologies Used

- **Kotlin**: The programming language used for Android development.
- **Jetpack Compose**: A modern toolkit for building native Android UI.
- **Coil**: An image loading library for Android backed by Kotlin Coroutines.
- **Hilt**: A dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project.
- **Room**: A persistence library that provides an abstraction layer over SQLite to allow fluent database access.
- **Retrofit**: A type-safe HTTP client for Android and Java to simplify network requests.
- **Material3**: For implementing Material Design components and theming.
- **Spoonacular API**: Used to fetch recipes and their details.

### Tools

- **Android Studio**: The official IDE for Android development.
- **Gradle**: A build automation tool used for managing dependencies and build configurations.

## Installation

### Prerequisites

- **Operating System**: Windows
- **Android Studio**: Installed on your system.
- **Android Device or Emulator**: To run the app.

### Steps

1. **Clone the repository**:
    ```sh
    git clone https://github.com/yourusername/yemek-tarifleri.git
    cd yemek-tarifleri
    ```

2. **Open the project in Android Studio**:
    - Start Android Studio and select `Open an existing Android Studio project`.
    - Navigate to the cloned repository and open it.

3. **Build the project**:
    - Android Studio will download all the required dependencies.
    - Click on `Build` > `Make Project`.

4. **Run the project**:
    - Connect your Android device or start an emulator.
    - Click on `Run` > `Run 'app'`.

## Usage

1. **Search Recipes**:
    - Use the search bar to find recipes based on keywords.
2. **View Recipe Details**:
    - Click on any recipe to view detailed information.
3. **Favorite Recipes**:
    - Click the heart icon to mark a recipe as favorite or remove it from favorites.
4. **Offline Access**:
    - Favorite recipes are stored locally for offline access.

## Project Structure

```plaintext
com.muhammedbuga.yemektarifleri
│
├── data
│   ├── local
│   │   ├── RecipeDao
│   │   └── RecipeDatabase
│   ├── remote
│   │   ├── RecipeApi
│   │   ├── RecipeDto
│   │   └── RecipeSearchResponse
│   └── repository
│       └── RecipeRepository
│ 
├── di
│   ├── AppModule
│   ├── LocalDataModule
│   ├── NetworkModule
│   └── RepositoryModule
│ 
├── domain
│   ├── model
│   │   └── Recipe
│   ├── repository
│   │   └── RecipeRepository
│   └── usecase
│       ├── GetRecipeDetailUseCase
│       └── GetRecipeListUseCase
│ 
├── presentation
│   ├── recipe_detail
│   │   ├── RecipeDetailScreen
│   │   └── RecipeDetailViewModel
│   ├── recipe_list
│   │   ├── RecipeListScreen
│   │   └── RecipeListViewModel
│   └── recipe_fav
│       └── FavoriteRecipesScreen
│    
├── util
│   ├── Constants
│   ├── NetworkResult
│   └── NetworkUtil
│
├── BaseApplication
├── MainActivity.kt
...
