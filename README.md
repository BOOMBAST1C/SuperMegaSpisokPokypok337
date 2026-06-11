Super SpiSok Pokypok

Приложение для управления списком покупок на Android.

Функционал:

- Добавление и удаление товаров
- Отметка о покупке (зачёркивание)
- Переключение светлой/тёмной темы
- Сохранение данных между запусками
- Современный Material Design UI

Технологии:

- **Jetpack Compose** - современный UI toolkit
- **MVVM + Clean Architecture** - чистая архитектура
- **DataStore Preferences** - локальное хранилище данных
- **Kotlin Coroutines Flow** - реактивное программирование
- **Material 3** - дизайн система

Структура проекта:

com.example.shoppinglistapp
 -data/ Слой данных (DataStore)
 -domain/ Бизнес-модели
 -presentation/ ViewModel и UI (Compose)
 -ui.theme/ Темы и цвета


Видео-обзор кода(без звука,скрины прилагаются):

https://drive.google.com/drive/folders/16g0_ciXltk5aFNoaId2h1mZPCEAeBjTu?usp=sharing

Установка:

1. Клонируй репозиторий
2. Открой проект в Android Studio
3. Собери и запусти (Run)

Объяснение:

Я использовал Clean Architecture с разделением на три основных слоя:
data — слой данных.Здесь находится AppRepository, который работает с DataStore для сохранения настроек темы и списка покупок;

domain — доменный слой. Здесь находится модель данных ShoppingItem — это простой data class с полями id, name и isChecked;

presentation — слой представления. Здесь ShoppingViewModel управляет бизнес-логикой, а ShoppingListScreen отвечает за UI на Jetpack Compose;

ui.theme — здесь настроены темы приложения (светлая и тёмная);

MainActivity.kt:

Здесь я создаю экземпляр AppRepository, передаю его в ViewModel через фабрику, и в Compose наблюдаю за состоянием.
Тема приложения передаётся в ShoppingListAppTheme из ViewModel, что позволяет динамически менять тему во время работы приложения.

ShoppingViewModel.kt:

ViewModel использует StateFlow для реактивного обновления UI. В init блоке я запускаю observeData, который собирает изменения из DataStore.

Функции:

-toggleTheme() — меняет тему и сохраняет в DataStore

-addItem() — добавляет новый товар

-toggleItemCheck() — отмечает товар как купленный

-deleteItem() — удаляет товар

Все изменения сразу сохраняются через repository.saveItems()."

AppRepository.kt:

Repository — это мост между ViewModel и DataStore.
Для темы я использую booleanPreferencesKey, а для списка — stringPreferencesKey.

Список сериализуется в строку формата: "id|name|isChecked,id2|name2|false"

isDarkThemeFlow и shoppingItemsFlow — это Flow, которые автоматически уведомляют ViewModel об изменениях.

ShoppingListScreen.kt:

UI полностью написан на Jetpack Compose.

Здесь есть:

Row с заголовком и Switch для переключения темы.

OutlinedTextField для ввода нового товара.

LazyColumn для отображения списка.

ShoppingItemRow — отдельный компонент для каждого товара.
