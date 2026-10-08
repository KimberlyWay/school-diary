# Дневник учащегося (Android, Kotlin, SQLite)

Простое учебное приложение: сначала вход по логину и паролю, затем дневник с предметами, оценками и преподавателями. Интерфейс сделан обычными элементами Android: текст, поля ввода, кнопки и таблица. Собственной иконки нет; Android показывает стандартное изображение.

**Учебный аккаунт:**

| Поле | Значение |
|---|---|
| Логин | `student` |
| Пароль | `123456` |
| Ученик | Иванов Иван |
| Класс | 9 А |

[Скачать APK v1.0.0](https://github.com/KimberlyWay/school-diary/releases/tag/v1.0.0) · [Скачать текущие исходники ZIP](https://github.com/KimberlyWay/school-diary/archive/refs/heads/main.zip) · [Сборки Actions](https://github.com/KimberlyWay/school-diary/actions)

Репозиторий закрытый. Для скачивания нужно войти в GitHub под аккаунтом, которому предоставлен доступ.

Документация рассчитана на начинающего: здесь объясняется не только куда нажать, но и почему приложение работает именно так. Фрагменты в разделе разбора взяты из файлов проекта. Примеры изменения данных отдельно отмечены как примеры: сами по себе они не добавляют новые возможности в приложение.

## Содержание

1. [Часть 1. Открытие, запуск и сборка с нуля](#часть-1-открытие-запуск-и-сборка-с-нуля)
2. [Часть 2. Структура проекта и связь файлов](#часть-2-структура-проекта-и-связь-файлов)
3. [Часть 3. Азбука XML, Kotlin и SQL](#часть-3-азбука-xml-kotlin-и-sql)
4. [Часть 4. Разбор файлов сборки](#часть-4-разбор-файлов-сборки)
5. [Часть 5. Разбор манифеста и экранов](#часть-5-разбор-манифеста-и-экранов)
6. [Часть 6. Разбор базы данных](#часть-6-разбор-базы-данных)
7. [Часть 7. Разбор Kotlin-кода экранов](#часть-7-разбор-kotlin-кода-экранов)
8. [Часть 8. Как поменять данные и приложение](#часть-8-как-поменять-данные-и-приложение)
9. [Часть 9. Проверки и тесты](#часть-9-проверки-и-тесты)
10. [Часть 10. GitHub Actions, подпись и релизы](#часть-10-github-actions-подпись-и-релизы)
11. [Часть 11. Работа без Git и перенос в Android Studio](#часть-11-работа-без-git-и-перенос-в-android-studio)
12. [Часть 12. Ошибки и ответы на вопросы](#часть-12-ошибки-и-ответы-на-вопросы)
13. [Официальная документация](#официальная-документация)

---

# Часть 1. Открытие, запуск и сборка с нуля

## 1.1. Какие версии использует проект

| Компонент | Версия в проекте | Где указана |
|---|---|---|
| Kotlin | 2.0.21 | Корневой `build.gradle.kts` |
| Android Gradle Plugin (AGP) | 8.7.3 | Корневой `build.gradle.kts` |
| Gradle | 8.9 | `gradle/wrapper/gradle-wrapper.properties` |
| Java для проверенной сборки | JDK 17 | Workflow и настройки компиляции |
| Android SDK для компиляции | API 35, Android 15 | `compileSdk = 35` |
| Целевая версия Android | API 34, Android 14 | `targetSdk = 34` |
| Минимальная версия телефона | API 21, Android 5.0 | `minSdk = 21` |
| Идентификатор приложения | `com.example.diary` | `applicationId` |
| Версия приложения | 1.0, код версии 1 | `versionName` и `versionCode` |

**SDK 35 на компьютере не означает, что телефон обязан быть на Android 15.** SDK нужен для сборки, а допустимые телефоны определяет `minSdk`.

Android Studio — редактор и среда разработки. Gradle — программа, которая собирает проект. AGP — дополнение к Gradle для сборки Android-приложений. Kotlin-плагин добавляет компиляцию Kotlin-кода.

## 1.2. Установить Android Studio

1. Скачать Android Studio с [официального сайта](https://developer.android.com/studio).
2. Установить и запустить.
3. Если предложен импорт старых настроек, выбрать `Do not import settings`, когда импортировать нечего.
4. В мастере настройки выбрать `Standard`.
5. Проверить список устанавливаемых компонентов Android SDK.
6. Прочитать условия лицензий и принять те, с которыми согласны.
7. Дождаться загрузки и нажать `Finish`.

Внешний вид мастера и названия меню меняются между версиями Studio. Далее приведены обычные английские названия; если пункт назван иначе, ориентируйтесь на действие и на команды Gradle ниже.

Для первой синхронизации нужен интернет: Studio скачивает SDK, Gradle, плагины и библиотеки. Само установленное приложение интернетом не пользуется.

Android Studio обычно поставляется со своей Java — JBR. Отдельную Java ставить нужно только если подходящей версии нет. Проверенная сборка этого проекта использует JDK 17.

## 1.3. Скачать исходники

### Способ А: ZIP без Git

1. Войти в GitHub и открыть [репозиторий](https://github.com/KimberlyWay/school-diary).
2. Нажать `Code → Download ZIP`.
3. Распаковать архив, например в `C:\Projects\Diary`.
4. Найти внутри файл `settings.gradle.kts`.

Можно воспользоваться [прямой ссылкой на ZIP ветки main](https://github.com/KimberlyWay/school-diary/archive/refs/heads/main.zip).

В ZIP нет папки `.git`. Git для такого способа вообще не нужен. Не открывайте ZIP как проект: сначала распакуйте его.

### Способ Б: клонирование

В окне приветствия Studio выбрать `Clone Repository` / `Get from VCS` и указать:

~~~text
https://github.com/KimberlyWay/school-diary.git
~~~

Для закрытого репозитория понадобится авторизация в GitHub. Токен не нужно вставлять в код, README, адрес репозитория или Gradle-файлы.

После клонирования в папке будет `.git` с историей изменений. Она не участвует в сборке APK.

## 1.4. Открыть правильную папку

В Android Studio выбрать `Open` и указать папку, содержащую:

~~~text
settings.gradle.kts
build.gradle.kts
gradlew
gradlew.bat
app/
gradle/
~~~

Открывается **корень проекта**, а не отдельная папка `app` и не файл `MainActivity.kt`.

Если появится `Trust Project`, подтверждайте доверие только для знакомого проекта: при открытии Studio запускает сборочные скрипты.

## 1.5. Настроить SDK и Java

В `Tools → SDK Manager`:

1. На вкладке `SDK Platforms` установить Android 15 / API 35.
2. На вкладке `SDK Tools` проверить Android SDK Platform-Tools и Android SDK Build-Tools.
3. Для эмулятора также установить Android Emulator.
4. Применить изменения и дождаться загрузки.

Если при сборке потребуется конкретная версия Build-Tools, Studio/Gradle обычно предложит загрузить её. В проекте вручную версия Build-Tools не закреплена.

Открыть настройки Gradle:

~~~text
File → Settings
→ Build, Execution, Deployment
→ Build Tools
→ Gradle
→ Gradle JDK
~~~

На macOS начало пути — `Android Studio → Settings`. Выбрать JDK 17, если он установлен; нужный JDK также можно скачать через список `Download JDK` в Studio.

**Java в терминале и Gradle JDK в Studio могут быть разными.** Если Studio собирает проект, а терминал пишет ошибку Java, проверьте `JAVA_HOME` и `java -version`.

## 1.6. Дождаться Gradle Sync

Studio читает настройки и скачивает Gradle 8.9, AGP 8.7.3, Kotlin 2.0.21 и зависимости тестов. На первой синхронизации это может занять несколько минут.

Результат смотрите в панели `Build`. Если синхронизация не запустилась, используйте `Sync Project with Gradle Files`.

Для воспроизведения текущей сборки оставьте указанные версии. Предложение Studio обновить AGP само по себе не является ошибкой проекта.

## 1.7. Где искать код в Studio

В левой панели `Project` есть переключатель вида:

- `Android` группирует файлы как manifests, java и res.
- `Project` показывает настоящие папки на диске.

Папка исходников называется `java`, но внутри находятся файлы Kotlin с расширением `.kt`. Это обычное устройство Android-проекта.

Для XML-разметки используйте `Code`, `Split` или `Design`. В `Design` у дневника будет видна шапка таблицы; предметы добавляются Kotlin-кодом при запуске, поэтому все строки в предпросмотре не появятся.

## 1.8. Запустить на телефоне

1. В настройках телефона открыть сведения об устройстве.
2. Несколько раз нажать «Номер сборки», чтобы включить режим разработчика. На некоторых телефонах название пункта другое.
3. В параметрах разработчика включить отладку по USB.
4. Подключить телефон кабелем, поддерживающим передачу данных.
5. Разрешить отладку на телефоне для своего компьютера.
6. В Studio выбрать конфигурацию `app` и подключённый телефон.
7. Нажать `Run` — зелёный треугольник.

Android Studio соберёт отладочную версию, установит и откроет её. На первом экране введите `student` и `123456`.

Если на телефоне уже стоит APK из GitHub Releases, локальная debug-сборка может не установиться поверх него из-за другой подписи. Решение описано в части 12.

## 1.9. Запустить на эмуляторе

1. Открыть `Tools → Device Manager`.
2. Выбрать создание виртуального устройства.
3. Выбрать модель телефона.
4. Скачать подходящий образ Android, например API 29 или новее.
5. Создать устройство и запустить его кнопкой ▶.
6. Выбрать его в Studio и нажать `Run`.

В Actions автоматические тесты запускаются на эмуляторе API 29 — это Android 10. Поддержка Android 5.0 задана в конфигурации; отдельный запуск тестов на API 21 в workflow не настроен.

## 1.10. Собрать APK для передачи на телефон

В меню `Build` найти создание APK: в разных версиях это `Build APK(s)` или `Generate APKs` в разделе `Generate App Bundles or APKs`.

Для обычной debug-сборки файл будет здесь:

~~~text
app/build/outputs/apk/debug/app-debug.apk
~~~

Не путайте `Run` и создание распространяемого APK: Studio при запуске может создавать тестовый APK для установки через adb. Для файла, который будете отправлять вручную, используйте сборку APK или `assembleDebug`.

Скопируйте APK на телефон и откройте его. Android может попросить разрешить установку приложений для конкретного файлового менеджера или браузера.

## 1.11. Сборка командами

Открыть терминал **в корне проекта**.

Windows PowerShell:

~~~powershell
.\gradlew.bat assembleDebug
~~~

Linux/macOS:

~~~bash
chmod +x gradlew
./gradlew assembleDebug
~~~

Команды вызывают Gradle Wrapper — он скачает закреплённый Gradle, если его ещё нет. Системная установка Gradle не требуется.

Для терминала нужен JDK 17 и доступный Android SDK. Studio обычно создаёт `local.properties` с путём к SDK. Если собираете без Studio, путь нужно задать самостоятельно через `local.properties` или настройки среды Android SDK.

Пример `local.properties` на Windows; замените путь на свой:

~~~properties
sdk.dir=C:/Users/YourName/AppData/Local/Android/Sdk
~~~

Варианты сборки:

| Команда | Результат |
|---|---|
| `assembleDebug` | Отладочный APK |
| `assembleRelease` | APK варианта release |
| `lintDebug` | Статическая проверка debug-варианта |
| `connectedDebugAndroidTest` | Тесты на подключённом устройстве/эмуляторе |
| `clean` | Удаление результатов предыдущей сборки |

`clean` не удаляет БД приложения на телефоне. Очистка проекта и очистка данных установленного приложения — разные действия.

---

# Часть 2. Структура проекта и связь файлов

## 2.1. Настоящие папки

~~~text
Diary/
├─ README.md
├─ settings.gradle.kts
├─ build.gradle.kts
├─ gradle.properties
├─ gradlew
├─ gradlew.bat
├─ gradle/
│  └─ wrapper/
│     ├─ gradle-wrapper.jar
│     └─ gradle-wrapper.properties
├─ .gitignore
├─ .github/
│  └─ workflows/
│     ├─ android.yml
│     └─ signing-backup.yml
└─ app/
   ├─ build.gradle.kts
   └─ src/
      ├─ main/
      │  ├─ AndroidManifest.xml
      │  ├─ java/com/example/diary/
      │  │  ├─ MainActivity.kt
      │  │  ├─ DiaryActivity.kt
      │  │  └─ DatabaseHelper.kt
      │  └─ res/
      │     ├─ layout/
      │     │  ├─ activity_main.xml
      │     │  └─ activity_diary.xml
      │     └─ values/
      │        ├─ strings.xml
      │        └─ styles.xml
      └─ androidTest/java/com/example/diary/
         └─ DiaryTest.kt
~~~

Название папки после распаковки может быть `school-diary-main`. Это не влияет на работу приложения.

## 2.2. Какой файл за что отвечает

| Файл | Для чего нужен |
|---|---|
| `settings.gradle.kts` | Подключает модуль app и задаёт склады плагинов и библиотек |
| Корневой `build.gradle.kts` | Задаёт версии Android- и Kotlin-плагинов |
| `gradle.properties` | Параметры Gradle, памяти, AndroidX |
| `gradle-wrapper.properties` | Фиксирует Gradle 8.9 |
| `gradle-wrapper.jar` | Запускает загрузку/запуск Gradle |
| `gradlew` / `gradlew.bat` | Запуск Wrapper на разных ОС |
| `app/build.gradle.kts` | SDK, пакет, версия, подпись и тестовые зависимости |
| `AndroidManifest.xml` | Название, тема, список Activity, главный экран |
| `activity_main.xml` | Поля логина и пароля, ошибка и кнопка входа |
| `activity_diary.xml` | Заголовок, ученик, шапка таблицы, кнопка выхода |
| `strings.xml` | Подписи и сообщения |
| `styles.xml` | Тема приложения и оформление ячеек |
| `MainActivity.kt` | Проверяет вход и открывает дневник |
| `DiaryActivity.kt` | Читает ученика и предметы, строит таблицу, выполняет выход |
| `DatabaseHelper.kt` | Создаёт SQLite, заполняет примеры и выполняет запросы |
| `DiaryTest.kt` | Проверяет БД и переходы между экранами |
| `android.yml` | Собирает APK, выполняет проверки и выпускает релиз |
| `signing-backup.yml` | По ручному запросу выдаёт резервную копию ключа подписи |
| `.gitignore` | Исключает локальные настройки, сборки и ключи из Git |

`local.properties`, `.idea`, `.gradle` и каталоги `build` могут появиться после открытия и сборки. Это создаваемые инструментариями файлы, а не недостающие исходники.

## 2.3. Путь от запуска до дневника

~~~text
Запуск приложения
    ↓
AndroidManifest.xml выбирает MainActivity
    ↓
MainActivity проверяет сохранённый student_id
    ├─ найден ученик → DiaryActivity
    └─ ученика нет → activity_main.xml
                         ↓
                    ввод логина и пароля
                         ↓
                    DatabaseHelper.login()
                         ├─ null → сообщение об ошибке
                         └─ Student → сохранить ID → DiaryActivity
                                                        ↓
                                                  getStudent()
                                                  getSubjects()
                                                        ↓
                                                  строки таблицы
~~~

`SQLite` хранит учётную запись и учебные данные. `SharedPreferences` хранит только номер вошедшего ученика. Пароль из поля ввода в настройки сессии не записывается.

При нажатии «Выйти» настройки сессии очищаются, открывается вход, экран дневника закрывается. Сама БД с оценками сохраняется.


---

# Часть 3. Азбука XML, Kotlin и SQL

## 3.1. Символы XML

XML описывает состав экрана: какие элементы на нём стоят и какие у них свойства.

| Запись | Значение на примере проекта |
|---|---|
| `<?xml version="1.0" encoding="utf-8"?>` | Служебная строка: формат XML и кодировка текста |
| `<LinearLayout>` | Начало контейнера |
| `</LinearLayout>` | Конец контейнера |
| `<Button ... />` | Элемент без вложенных элементов: открыт и сразу закрыт |
| `имя="значение"` | Свойство элемента; значение заключается в кавычки |
| `xmlns:android="..."` | Объявляет приставку android для атрибутов |
| `android:` | Свойство из словаря Android |
| `@string/login` | Строка login из strings.xml |
| `@style/TableCell` | Стиль TableCell из styles.xml |
| `@+id/loginInput` | Создать идентификатор loginInput |
| `@id/loginInput` | Сослаться на уже созданный идентификатор |
| `<!-- комментарий -->` | Пояснение для человека |
| `#FFFFFF` | Белый цвет |
| `#000000` | Чёрный цвет |
| `#666666` | Серый цвет |

Адрес в `xmlns:android` обозначает пространство имён. Приложение не обращается по этому адресу в интернет.

Размеры:

| Запись | Что означает |
|---|---|
| `dp` | Размер или отступ с учётом плотности экрана |
| `sp` | Размер текста с учётом плотности и пользовательского размера шрифта |
| `match_parent` | Занять доступный размер родительского контейнера |
| `wrap_content` | Подобрать размер под содержимое |
| `0dp` вместе с `layout_weight` | Отдать распределение ширины контейнеру по весу |

Пример:

~~~xml
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:layout_marginTop="12dp"
android:textSize="16sp"
~~~

Здесь элемент занимает ширину родителя, высоту подбирает по содержимому, сверху имеет внешний отступ, а текст использует размер 16sp. `margin` — снаружи элемента, `padding` — внутри.

## 3.2. Основные слова Kotlin

| Запись | Объяснение |
|---|---|
| `package com.example.diary` | Пространство имён исходника |
| `import android.widget.Button` | Подключить имя класса Button для использования в файле |
| `class` | Объявить класс |
| `data class` | Класс для хранения данных; Kotlin добавляет сравнение, копирование и другие методы |
| `class MainActivity : Activity()` | Наш класс наследует стандартную Activity |
| `fun` | Объявить функцию |
| `override` | Заменить реализацию метода родительского класса |
| `private` | Используется только внутри класса |
| `val` | Ссылку/значение нельзя присвоить заново после инициализации |
| `var` | Переменную можно переприсваивать |
| `lateinit var` | Ненулевое свойство будет инициализировано позже |
| `String` | Текст |
| `Int` | Целое число |
| `Float` | Дробное число, например вес колонки |
| `Boolean` | true или false |
| `List<Subject>` | Список объектов Subject |
| `Student?` | Student или null |
| `return` | Вернуть результат и выйти из функции |
| `this` | Текущий объект Activity |
| `null` | Отсутствие объекта/результата |

`val` запрещает переприсваивание, но не обязательно изменение самого объекта. Например, `val subjects = mutableListOf<Subject>()` можно дополнять через `add`.

`lateinit` не создаёт объект. Если обратиться к свойству до присваивания, будет ошибка. В наших Activity база присваивается в `onCreate` до её использования.

## 3.3. Знаки Kotlin, встречающиеся в дневнике

| Знак | Для чего используется |
|---|---|
| `{ ... }` | Блок функции, класса, условия или обработчика |
| `( ... )` | Аргументы вызова или условие |
| `.шаг` | Обратиться к свойству/методу объекта |
| `: Тип` | Указать тип параметра или результата |
| `=` | Присвоить значение |
| `==` | Сравнить значения |
| `!=` | Проверить, что значения различаются |
| `\|\|` | Логическое «или» |
| `&&` | Логическое «и» |
| `?.` | Выполнить обращение, только если объект не null |
| `->` | Отделить параметры лямбды от её тела |
| `_` | Неиспользуемый параметр |
| `//` | Комментарий до конца строки |
| `1.3f` | Число типа Float |
| `::class.java` | Передать Android объект Java-класса |

Пример условия:

~~~kotlin
if (login.isEmpty() || password.isEmpty()) {
    return
}
~~~

`isEmpty()` проверяет пустую строку. Условие выполняется, если пуст хотя бы один из двух вводов.

Пример обработчика:

~~~kotlin
setOnClickListener { signIn() }
~~~

`{ signIn() }` — блок, который Android вызовет при нажатии кнопки. В момент назначения обработчика `signIn` ещё не выполняется.

Пример с ненужными параметрами:

~~~kotlin
{ _, action, _ -> }
~~~

Обработчик получает три параметра, но код использует только второй — `action`. Остальные заменены на `_`.

## 3.4. Что такое R

`R` — автоматически создаваемый класс с номерами ресурсов:

~~~kotlin
R.layout.activity_main
R.id.loginInput
R.string.fill_fields
~~~

Первая запись обозначает XML-разметку, вторая — элемент экрана, третья — текст из ресурсов.

`R` не нужно создавать вручную. Если он подсвечен красным, сначала проверьте ошибки XML и синхронизации: при сломанном ресурсе генерация может не завершиться.

## 3.5. Минимум SQL

SQL — язык запросов к базе. В проекте применяется SQLite, которая уже есть в Android.

| Запись | Что делает |
|---|---|
| `CREATE TABLE` | Создаёт таблицу |
| `INTEGER` | Целое число |
| `TEXT` | Текст |
| `PRIMARY KEY` | Основной уникальный идентификатор строки |
| `UNIQUE` | Запрещает повторяющиеся значения |
| `INSERT INTO` | Добавляет строку |
| `SELECT` | Читает данные |
| `FROM` | Выбирает таблицу |
| `WHERE` | Ограничивает, какие строки читать |
| `AND` | Требует выполнения обоих условий |
| `ORDER BY id` | Сортирует по ID |
| `?` | Место для отдельно переданного значения |

В SQL текст записывается в одинарных кавычках, например `'student'`. Сам SQL в Kotlin лежит внутри строки с двойными кавычками.

~~~kotlin
"SELECT id FROM students WHERE login = ? AND password = ?"
~~~

Два `?` получают значения из массива аргументов в том же порядке. Это не знаки nullable-типов Kotlin: здесь они находятся внутри SQL-строки и относятся к SQL.

---

# Часть 4. Разбор файлов сборки

## 4.1. settings.gradle.kts

Файл сначала указывает, где искать плагины:

~~~kotlin
pluginManagement {
    repositories { google(); mavenCentral(); gradlePluginPortal() }
}
~~~

- `pluginManagement` — настройки подключения плагинов.
- `repositories` — источники загрузки.
- `google()` — репозиторий Google, в том числе Android-плагин.
- `mavenCentral()` — репозиторий библиотек и плагинов JVM.
- `gradlePluginPortal()` — каталог плагинов Gradle.
- `;` разделяет несколько команд на одной строке. В Kotlin при отдельных строках точка с запятой обычно не нужна.

Следующий блок задаёт источники библиотек:

~~~kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { google(); mavenCentral() }
}
~~~

`FAIL_ON_PROJECT_REPOS` требует задавать репозитории зависимостей здесь, а не отдельно в каждом модуле.

~~~kotlin
rootProject.name = "Diary"
include(":app")
~~~

`Diary` — имя проекта для инструментов разработки. `:app` — единственный модуль приложения. Название на телефоне отдельно задаётся в `strings.xml`.

## 4.2. Корневой build.gradle.kts

~~~kotlin
plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
}
~~~

`com.android.application` учит Gradle собирать Android APK. `org.jetbrains.kotlin.android` добавляет Kotlin.

`version` фиксирует версию плагина. `apply false` объявляет его здесь, но не применяет к корневому проекту. Оба плагина применяются в `app/build.gradle.kts`.

## 4.3. gradle.properties

~~~properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
kotlin.code.style=official
~~~

- `-Xmx2048m` — ограничение максимальной Java-памяти Gradle примерно до 2 ГБ.
- `-Dfile.encoding=UTF-8` — кодировка для работы сборки с текстом.
- `android.useAndroidX=true` — включает использование AndroidX; он нужен тестовым зависимостям.
- `kotlin.code.style=official` — указание стандартного стиля Kotlin для инструментов.

Эти строки настраивают сборку, а не размер памяти установленного APK.

## 4.4. Gradle Wrapper

В `gradle/wrapper/gradle-wrapper.properties`:

~~~properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.9-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
~~~

`distributionUrl` указывает Gradle 8.9. `bin.zip` содержит программу Gradle без полного комплекта исходников и документации.

`GRADLE_USER_HOME` — пользовательский каталог Gradle. `wrapper/dists` — подпапка скачанных дистрибутивов. `networkTimeout=10000` задаёт сетевой тайм-аут Wrapper в миллисекундах, а не максимальное время сборки.

`gradle-wrapper.jar` — служебная программа Wrapper. `gradlew` запускает её на Linux/macOS, `gradlew.bat` — на Windows. Это файлы проекта, и удалять их при отказе от Git не нужно.

## 4.5. app/build.gradle.kts: плагины и Android

~~~kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.example.diary"
    compileSdk = 35
~~~

Версии плагинов уже заданы в корневом файле.

`namespace` задаёт пространство имён ресурсов, включая `R`. Kotlin-файлы этого проекта используют такой же `package`.

`compileSdk` задаёт SDK для компиляции. Использование нового API всё равно требует учитывать минимальную версию телефона.

~~~kotlin
    defaultConfig {
        applicationId = "com.example.diary"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
~~~

- `applicationId` — идентификатор установленного приложения.
- `minSdk` — нижняя граница поддерживаемых Android.
- `targetSdk` — версия Android, под правила которой настроено поведение приложения.
- `versionCode` — внутренний числовой номер версии; для нормального обновления его увеличивают.
- `versionName` — понятная человеку версия.
- `testInstrumentationRunner` — исполнитель тестов на Android-устройстве.

Для установки обновления мало одинакового `applicationId`: нужна ещё та же подпись и подходящий `versionCode`.

## 4.6. app/build.gradle.kts: подпись

~~~kotlin
    signingConfigs {
        create("releaseKey") {
            val keyPath = System.getenv("DIARY_KEYSTORE")
            if (keyPath != null) {
                storeFile = file(keyPath)
                storePassword = System.getenv("DIARY_STORE_PASSWORD")
                keyAlias = "diary"
                keyPassword = System.getenv("DIARY_KEY_PASSWORD")
            }
        }
    }
~~~

`System.getenv` читает переменную среды процесса Gradle. Настоящие пароли в файле не записаны.

`storeFile` — путь к файлу ключа, `storePassword` — пароль хранилища, `keyAlias` — имя ключа внутри хранилища, `keyPassword` — пароль самого ключа.

Если `DIARY_KEYSTORE` задан, остальные переменные должны содержать правильные пароли. Существование пути и корректность ключа проверит сборка.

~~~kotlin
    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = if (System.getenv("DIARY_KEYSTORE") != null) {
                signingConfigs.getByName("releaseKey")
            } else {
                signingConfigs.getByName("debug")
            }
        }
    }
~~~

`isMinifyEnabled = false` отключает уменьшение и обфускацию кода.

С заданным `DIARY_KEYSTORE` используется постоянный ключ. Без него локальный release-вариант подписывается debug-ключом. **Название варианта release не гарантирует подпись ключом опубликованного релиза.**

Workflow для тега отдельно останавливается, если секретов подписи нет, поэтому случайная публикация релиза с новым debug-ключом не допускается этим workflow.

## 4.7. Компиляция и библиотеки тестов

~~~kotlin
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}
dependencies {
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("junit:junit:4.13.2")
}
~~~

Настройки Java и Kotlin согласуют целевую JVM-версию для компиляции. Android-сборка затем преобразует код в формат для Android.

`androidTestImplementation` добавляет библиотеки именно к тестам на устройстве. В интерфейсе приложения нет зависимости от Compose или AppCompat: используются стандартные `android.app.Activity` и `android.widget`.

## 4.8. .gitignore

~~~gitignore
.gradle/
.idea/
local.properties
**/build/
*.iml
*.jks
*.keystore
signing.properties
~~~

`*` означает любое имя; `**/build/` — каталоги build на разных уровнях.

Игнорируются кэш, локальные пути SDK, настройки редактора, результаты сборки и ключи подписи. `.gitignore` не удаляет файлы с диска и не участвует в APK.

Если секретный файл уже добавлен в Git раньше, новая строка в `.gitignore` не удалит его из истории.


---

# Часть 5. Разбор манифеста и экранов

## 5.1. AndroidManifest.xml

~~~xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application android:allowBackup="false" android:label="@string/app_name"
        android:supportsRtl="true" android:theme="@style/AppTheme">
~~~

`manifest` — корень описания приложения. `application` — настройки приложения целиком.

- `allowBackup="false"` отключает стандартное резервное копирование данных приложения.
- `label="@string/app_name"` берёт название «Дневник» из строк.
- `supportsRtl="true"` объявляет поддержку направления справа налево.
- `theme="@style/AppTheme"` выбирает нашу тему.
- Атрибут `android:icon` не задан: собственной иконки нет.

В манифесте нет разрешения `INTERNET`: приложение не обращается к серверу.

~~~xml
        <activity android:name=".DiaryActivity" android:exported="false" />
        <activity android:name=".MainActivity" android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
~~~

`.MainActivity` — краткое имя класса в пространстве имён проекта.

`MAIN` и `LAUNCHER` вместе обозначают экран, открываемый при запуске приложения из списка приложений.

Главная Activity экспортирована, чтобы система могла её запускать. `DiaryActivity` не экспортирована: другие приложения не могут запускать её как обычный внешний экран. Кроме этого, она сама проверяет сохранённый ID ученика.

XML-манифест объявляет Activity, но переход между ними выполняет Kotlin-код.

## 5.2. activity_main.xml: контейнеры

~~~xml
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent" android:layout_height="match_parent"
    android:fillViewport="true">
    <LinearLayout android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical" android:padding="24dp">
~~~

`ScrollView` позволяет прокрутить содержимое на маленьком экране или при открытой клавиатуре. У него один непосредственный дочерний элемент — `LinearLayout`; уже внутри него размещены все элементы.

`fillViewport="true"` позволяет содержимому занимать доступную высоту, если оно короче экрана. `orientation="vertical"` ставит элементы друг под другом. `padding="24dp"` создаёт отступ от краёв.

## 5.3. Заголовок входа

~~~xml
        <TextView android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="32dp" android:text="@string/login_title"
            android:textColor="#000000" android:textSize="24sp" />
~~~

`TextView` показывает текст, но не даёт его редактировать. Строка `login_title` — «Вход в дневник». Чёрный цвет и 24sp задают оформление заголовка.

## 5.4. Поле логина

~~~xml
        <EditText android:id="@+id/loginInput"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" android:layout_marginTop="24dp"
            android:hint="@string/login" android:inputType="textNoSuggestions"
            android:singleLine="true" android:imeOptions="actionNext"
            android:autofillHints="username" />
~~~

`EditText` — поле редактирования.

- `loginInput` — ID, по которому поле находит `findViewById`.
- `hint` — подсказка «Логин», когда поле пустое.
- `textNoSuggestions` просит клавиатуру не предлагать обычные текстовые исправления.
- `singleLine` ограничивает поле одной строкой.
- `actionNext` обозначает действие «Далее» на клавиатуре.
- `autofillHints` сообщает системе автозаполнения, что это имя пользователя.

Подсказка не является введённым значением. Пока пользователь ничего не написал, `text` поля пуст.

## 5.5. Поле пароля

~~~xml
        <EditText android:id="@+id/passwordInput"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" android:layout_marginTop="12dp"
            android:hint="@string/password" android:inputType="textPassword"
            android:singleLine="true" android:imeOptions="actionDone"
            android:autofillHints="password" />
~~~

`textPassword` скрывает пароль на экране. Это не шифрование данных в SQLite: оно относится к показу вводимых символов.

`actionDone` — кнопка завершения ввода. В `MainActivity` для неё назначен обработчик входа, поэтому можно войти с клавиатуры.

## 5.6. Ошибка и кнопка входа

~~~xml
        <TextView android:id="@+id/errorText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" android:layout_marginTop="12dp"
            android:textColor="#B00020" android:accessibilityLiveRegion="polite"
            android:visibility="gone" />
        <Button android:id="@+id/loginButton"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" android:layout_marginTop="16dp"
            android:text="@string/sign_in" android:textAllCaps="false" />
~~~

`errorText` сначала скрыт. `gone` означает, что элемент не показывается и не занимает место. При ошибке Kotlin задаёт текст и переключает видимость на `View.VISIBLE`.

`accessibilityLiveRegion="polite"` помогает службе доступности сообщить об изменении текста, не прерывая всё немедленно.

`textAllCaps="false"` оставляет подпись «Войти» в обычном регистре. Нажатие связывается с кодом через `setOnClickListener`, а не через XML-атрибут `onClick`.

## 5.7. activity_diary.xml: заголовок и ученик

У дневника такие же `ScrollView` и вертикальный `LinearLayout`, но отступы 16dp. Затем идут:

~~~xml
        <TextView android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/diary_title" android:textSize="22sp"
            android:textColor="#000000" />
        <TextView android:id="@+id/studentText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" android:layout_marginTop="12dp"
            android:textSize="16sp" />
~~~

Первый `TextView` содержит постоянный заголовок. Второй изначально без текста: имя ученика и класс подставляются из базы при запуске.

## 5.8. Таблица и её шапка

~~~xml
        <TableLayout android:id="@+id/subjectsTable"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" android:layout_marginTop="20dp">
            <TableRow android:layout_width="match_parent"
                android:layout_height="wrap_content" android:background="#DDDDDD">
                <TextView style="@style/TableCell" android:layout_weight="1.3"
                    android:text="@string/subject" android:textStyle="bold" />
                <TextView style="@style/TableCell" android:layout_weight="0.9"
                    android:text="@string/grades" android:textStyle="bold" />
                <TextView style="@style/TableCell" android:layout_weight="1.2"
                    android:text="@string/teacher" android:textStyle="bold" />
            </TableRow>
        </TableLayout>
~~~

`TableLayout` хранит строки таблицы. `TableRow` — одна строка, внутри неё три ячейки `TextView`.

В XML есть **только шапка**: «Предмет», «Оценки», «Преподаватель». Девять строк предметов добавляет `DiaryActivity`. Поэтому менять оценки нужно в данных, а не в этом XML.

`style="@style/TableCell"` применяет общие свойства ячеек. `bold` делает заголовки жирными.

Веса колонок 1.3, 0.9 и 1.2 задают относительное распределение доступной ширины. Колонка предмета получает больше места, чем оценки. Чтобы шапка совпадала с данными, одинаковые веса используются в XML и `makeCell`.

## 5.9. Кнопка выхода

~~~xml
        <Button android:id="@+id/logoutButton"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" android:layout_marginTop="24dp"
            android:text="@string/sign_out" android:textAllCaps="false" />
~~~

По ID `logoutButton` Kotlin назначает обработчик. Сам XML не очищает сессию.

## 5.10. strings.xml

Все постоянные подписи собраны в `<resources>`:

~~~xml
<string name="app_name">Дневник</string>
<string name="login_title">Вход в дневник</string>
<string name="fill_fields">Введите логин и пароль</string>
<string name="wrong_credentials">Неверный логин или пароль</string>
<string name="student_info">%1$s, %2$s</string>
~~~

`name` — имя ресурса, текст между тегами — его значение.

`%1$s` и `%2$s` — места для двух строк. `getString(R.string.student_info, student.name, student.schoolClass)` подставит имя и класс и получит «Иванов Иван, 9 А».

Знак `$` в этой XML-строке относится к формату подстановки, а не к переменным GitHub Actions или Kotlin.

Полный список назначений:

| Ресурс | Где используется |
|---|---|
| `app_name` | Название в манифесте |
| `login_title` | Заголовок входа |
| `login` / `password` | Подсказки полей |
| `sign_in` | Кнопка входа |
| `fill_fields` | Ошибка пустого ввода |
| `wrong_credentials` | Ошибка проверки логина/пароля |
| `diary_title` | Заголовок дневника |
| `student_info` | Имя и класс |
| `subject` / `grades` / `teacher` | Шапка таблицы |
| `sign_out` | Кнопка выхода |

## 5.11. styles.xml: тема

~~~xml
<style name="AppTheme" parent="android:style/Theme.Material.Light.NoActionBar">
    <item name="android:fontFamily">sans</item>
    <item name="android:colorAccent">#666666</item>
    <item name="android:statusBarColor">#666666</item>
    <item name="android:navigationBarColor">#666666</item>
    <item name="android:windowBackground">#FFFFFF</item>
</style>
~~~

`parent` берёт стандартную светлую тему Android без верхней ActionBar. `sans` — обычный шрифт без засечек. `colorAccent` задаёт цвет акцентов стандартных элементов.

`statusBarColor` — системная полоса со временем и индикаторами. `navigationBarColor` — системная область навигации. `windowBackground` — фон окна.

Это встроенная тема Android, поэтому отдельная библиотека Material Components не требуется.

## 5.12. styles.xml: ячейка

~~~xml
<style name="TableCell">
    <item name="android:layout_width">0dp</item>
    <item name="android:layout_height">wrap_content</item>
    <item name="android:padding">6dp</item>
    <item name="android:textSize">14sp</item>
    <item name="android:textColor">#000000</item>
</style>
~~~

Ширину распределяют веса, высота зависит от содержимого. Поэтому длинный предмет или фамилия могут занимать несколько строк.

Этот стиль применяется к шапке из XML. Ячейки с данными создаются программно, и аналогичные свойства для них задаёт `makeCell`. Если менять размер текста всей таблицы, нужно изменить оба места.


---

# Часть 6. Разбор базы данных

## 6.1. Почему SQLite

SQLite — база данных в файле на самом телефоне. Android умеет работать с ней без отдельного сервера.

Для этого учебного проекта достаточно хранить одного ученика и несколько предметов. MySQL потребовала бы сервер и промежуточный API для проверки входа и выдачи оценок. Прямое подключение APK к MySQL с паролем сервера внутри приложения здесь не используется.

В текущем приложении:

- База называется `diary.db`.
- Создаётся при первом обращении к `readableDatabase`/`writableDatabase`, если её ещё нет.
- Каждый телефон получает собственную копию.
- Изменения на одном телефоне не передаются другому.
- Оценки читаются; экрана редактирования оценок нет.
- Логин и пароль учебного аккаунта заранее добавляются кодом.

## 6.2. Какие данные хранятся

Таблица `students`:

| Столбец | Тип | Смысл | Пример |
|---|---|---|---|
| `id` | INTEGER PRIMARY KEY | Номер ученика | 1 |
| `login` | TEXT UNIQUE | Логин, без повторений | student |
| `password` | TEXT | Учебный пароль | 123456 |
| `name` | TEXT | Имя для экрана | Иванов Иван |
| `school_class` | TEXT | Класс | 9 А |

Таблица `subjects`:

| Столбец | Тип | Смысл | Пример |
|---|---|---|---|
| `id` | INTEGER PRIMARY KEY | Номер строки предмета | 1 |
| `student_id` | INTEGER | К какому ученику относится | 1 |
| `name` | TEXT | Название предмета | Математика |
| `grades` | TEXT | Оценки одной строкой | 5, 4, 5 |
| `teacher` | TEXT | Фамилия и инициалы | Петрова А. В. |

Связь выглядит так:

~~~text
students.id = 1
     ↑
subjects.student_id = 1
~~~

Это связь по договорённости кода. В SQL-схеме нет ограничения `FOREIGN KEY`: SQLite сама не запрещает добавить предмет для несуществующего ученика.

Оценки хранятся как текст. Программа показывает их как есть: не рассчитывает средний балл, не проверяет диапазон 2–5 и не связывает оценку с датой. Для такой логики понадобилась бы отдельная таблица оценок и дополнительные функции.

## 6.3. Данные по умолчанию

| Предмет | Оценки | Преподаватель |
|---|---|---|
| Математика | 5, 4, 5 | Петрова А. В. |
| Русский язык | 4, 4, 5 | Смирнова Е. Н. |
| Литература | 5, 5, 4 | Смирнова Е. Н. |
| История | 4, 5, 4 | Кузнецов И. С. |
| География | 5, 4, 4 | Орлова Т. П. |
| Биология | 4, 4, 5 | Соколова М. А. |
| Физика | 3, 4, 4 | Волков Д. Н. |
| Английский язык | 5, 4, 5 | Морозова О. В. |
| Физкультура | 5, 5, 5 | Попов С. И. |

Это придуманные учебные данные.

## 6.4. DatabaseHelper.kt: импорты и модели

~~~kotlin
package com.example.diary

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Student(val id: Int, val name: String, val schoolClass: String)
data class Subject(val name: String, val grades: String, val teacher: String)
~~~

`Context` даёт доступ к окружению Android, в том числе к месту для базы приложения.

`SQLiteDatabase` представляет открытую БД. `SQLiteOpenHelper` помогает создавать и обновлять её.

`Student` содержит то, что нужно экрану: ID, имя и класс. Поля пароля в этой модели нет. `Subject` содержит три значения для строки таблицы.

Объекты этих классов — данные в памяти. Сама запись в SQLite получается через SQL, а не автоматически через `data class`.

## 6.5. Конструктор помощника

~~~kotlin
class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "diary.db", null, 1) {
~~~

Параметры родительского конструктора:

1. `context` — окружение приложения.
2. `"diary.db"` — имя файла.
3. `null` — стандартная фабрика курсоров, без собственной реализации.
4. `1` — **версия схемы БД**, а не версия APK.

Создание объекта `DatabaseHelper(this)` ещё не означает, что `onCreate` уже выполнился. Он вызывается помощником при открытии отсутствующей базы.

## 6.6. Создание таблиц и ученика

~~~kotlin
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE students (id INTEGER PRIMARY KEY, login TEXT UNIQUE, password TEXT, name TEXT, school_class TEXT)")
        db.execSQL("CREATE TABLE subjects (id INTEGER PRIMARY KEY, student_id INTEGER, name TEXT, grades TEXT, teacher TEXT)")

        // Учебный аккаунт. Данные добавляются один раз при создании базы.
        db.execSQL("INSERT INTO students VALUES (1, 'student', '123456', 'Иванов Иван', '9 А')")
~~~

`override` означает, что это наша реализация метода `SQLiteOpenHelper`.

`execSQL` выполняет команду, не возвращающую таблицу результата: здесь создание таблиц и вставку строки.

В `INSERT INTO students VALUES ...` список столбцов не указан, поэтому значения соответствуют порядку объявления столбцов: ID, логин, пароль, имя, класс.

Пароль хранится открытым текстом в учебной локальной БД. Это простая демонстрация запроса входа; такую схему нельзя считать серверной авторизацией для настоящих школьных данных.

## 6.7. Добавление предметов

В `onCreate` следуют девять вызовов:

~~~kotlin
        addSubject(db, "Математика", "5, 4, 5", "Петрова А. В.")
        addSubject(db, "Русский язык", "4, 4, 5", "Смирнова Е. Н.")
        addSubject(db, "Литература", "5, 5, 4", "Смирнова Е. Н.")
        addSubject(db, "История", "4, 5, 4", "Кузнецов И. С.")
        addSubject(db, "География", "5, 4, 4", "Орлова Т. П.")
        addSubject(db, "Биология", "4, 4, 5", "Соколова М. А.")
        addSubject(db, "Физика", "3, 4, 4", "Волков Д. Н.")
        addSubject(db, "Английский язык", "5, 4, 5", "Морозова О. В.")
        addSubject(db, "Физкультура", "5, 5, 5", "Попов С. И.")
    }
~~~

Вспомогательная функция:

~~~kotlin
    private fun addSubject(db: SQLiteDatabase, name: String, grades: String, teacher: String) {
        db.execSQL("INSERT INTO subjects (student_id, name, grades, teacher) VALUES (1, ?, ?, ?)",
            arrayOf(name, grades, teacher))
    }
~~~

В `student_id` всегда записывается `1`. Три `?` получают название, оценки и преподавателя из `arrayOf`.

`id` предмета не передаётся. Для столбца `INTEGER PRIMARY KEY` SQLite сама выбирает ID при вставке без указанного значения.

`private` скрывает помощник от других классов: его вызывает только `DatabaseHelper`.

## 6.8. Проверка логина и пароля

~~~kotlin
    fun login(login: String, password: String): Student? {
        readableDatabase.rawQuery(
            "SELECT id, name, school_class FROM students WHERE login = ? AND password = ?",
            arrayOf(login, password)
        ).use {
            if (it.moveToFirst()) return Student(it.getInt(0), it.getString(1), it.getString(2))
        }
        return null
    }
~~~

Разбор по порядку:

1. Функция получает два текста и возвращает `Student?`.
2. `readableDatabase` открывает базу для чтения. Если её нет, помощник сначала создаёт её.
3. `rawQuery` выполняет SELECT и возвращает `Cursor` — объект для чтения результата по строкам.
4. Условия требуют совпадения **и** логина, **и** пароля.
5. Значения передаются отдельно, без склеивания SQL с вводом пользователя.
6. `use` закрывает курсор после работы, включая ранний `return`.
7. `moveToFirst()` переходит к первой строке. Если строк нет, возвращает false.
8. `getInt(0)` читает первый выбранный столбец — ID.
9. `getString(1)` и `getString(2)` читают имя и класс.
10. При отсутствии совпадения функция возвращает `null`.

Нумерация столбцов начинается с нуля и соответствует порядку `SELECT`, а не всему порядку таблицы. Измените порядок SELECT — придётся изменить чтение результата.

`it` здесь — курсор, автоматически названный параметр блока `use`.

Параметры `?` защищают запрос от превращения текста ввода в SQL-команды. Они не шифруют пароль и не превращают локальный пример в полноценную систему безопасности.

## 6.9. Поиск ученика по ID

~~~kotlin
    fun getStudent(id: Int): Student? {
        readableDatabase.rawQuery(
            "SELECT id, name, school_class FROM students WHERE id = ?", arrayOf(id.toString())
        ).use {
            if (it.moveToFirst()) return Student(it.getInt(0), it.getString(1), it.getString(2))
        }
        return null
    }
~~~

Это тот же способ чтения курсора, но условие только по ID. `toString()` нужен для массива строковых аргументов запроса.

Этот метод используют при восстановлении сессии. Сохранённого номера недостаточно: код дополнительно проверяет, что такой ученик ещё есть в базе.

## 6.10. Получение предметов

~~~kotlin
    fun getSubjects(studentId: Int): List<Subject> {
        val subjects = mutableListOf<Subject>()
        readableDatabase.rawQuery(
            "SELECT name, grades, teacher FROM subjects WHERE student_id = ? ORDER BY id",
            arrayOf(studentId.toString())
        ).use {
            while (it.moveToNext()) subjects.add(Subject(it.getString(0), it.getString(1), it.getString(2)))
        }
        return subjects
    }
~~~

`mutableListOf` создаёт изменяемый список. В `while` курсор последовательно проходит все строки результата. На каждой создаётся `Subject` и добавляется через `add`.

`WHERE student_id = ?` ограничивает данные выбранным учеником. `ORDER BY id` сохраняет порядок по ID, а не сортирует названия по алфавиту.

Если предметов нет, возвращается пустой список. Сам вызов не является ошибкой; экран покажет шапку без предметов.

## 6.11. onUpgrade и версия базы

~~~kotlin
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Для первой версии обновление базы не требуется.
    }
}
~~~

Метод пока пуст. Он предназначен для перехода между версиями схемы. Поэтому **просто поменять 1 на 2 в конструкторе — не способ применить новые предметы**: `onCreate` не вызовется повторно, а пустой `onUpgrade` ничего не изменит.

Для учебного изменения примеров можно очистить данные приложения. Для сохранения существующих данных нужно написать миграцию: SQL-команды изменения таблиц/записей в `onUpgrade`.

---

# Часть 7. Разбор Kotlin-кода экранов

## 7.1. MainActivity.kt: класс и жизненный цикл

~~~kotlin
class MainActivity : Activity() {
    private lateinit var database: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = DatabaseHelper(this)
~~~

`Activity` — стандартный экран Android. `onCreate` вызывается системой при создании экземпляра экрана, в том числе при некоторых пересозданиях, например после поворота.

`savedInstanceState: Bundle?` — возможные сохранённые данные состояния. В нашем коде они передаются родительскому методу, отдельная ручная обработка Bundle не написана.

`super.onCreate` выполняет работу родительской Activity. `DatabaseHelper(this)` получает текущую Activity как `Context`.

Импорты `Intent`, `View`, `EditorInfo`, `Button`, `EditText` и `TextView` позволяют пользоваться этими классами без полного адреса в каждой строке.

## 7.2. Проверка сохранённого входа

~~~kotlin
        val studentId = getSharedPreferences("session", MODE_PRIVATE).getInt("student_id", -1)
        if (database.getStudent(studentId) != null) {
            openDiary()
            return
        }
~~~

`getSharedPreferences("session", MODE_PRIVATE)` открывает небольшое хранилище настроек приложения. `session` — имя, `student_id` — ключ внутри.

`getInt` возвращает записанное число, а если ключ отсутствует — `-1`. Учебный ученик имеет ID 1, поэтому -1 означает отсутствие подходящей сессии.

Если `getStudent` нашёл ученика, открывается дневник. `return` завершает `onCreate`: форма входа больше не нужна.

Срок действия сессии не задан. Повторный ввод пароля не требуется до выхода, очистки данных или исчезновения ученика из базы.

## 7.3. Показ формы и нажатия

~~~kotlin
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.loginButton).setOnClickListener { signIn() }
        findViewById<EditText>(R.id.passwordInput).setOnEditorActionListener { _, action, _ ->
            if (action == EditorInfo.IME_ACTION_DONE) {
                signIn()
                true
            } else {
                false
            }
        }
    }
~~~

`setContentView` создаёт элементы из XML. Поэтому `findViewById` вызывается после него.

`findViewById<Button>` находит кнопку по ID и указывает ожидаемый тип. Неверный ID или тип приведёт к проблеме при работе с элементом.

Нажатие кнопки вызывает `signIn`. Нажатие действия Done на клавиатуре вызывает ту же функцию.

Возвращаемое `true` сообщает, что действие клавиатуры обработано; `false` оставляет другие действия стандартному обработчику.

## 7.4. Чтение ввода

~~~kotlin
    private fun signIn() {
        val login = findViewById<EditText>(R.id.loginInput).text.toString().trim()
        val password = findViewById<EditText>(R.id.passwordInput).text.toString()
        val error = findViewById<TextView>(R.id.errorText)
~~~

`text` поля — редактируемый текст; `toString` превращает его в обычную строку.

`trim()` удаляет пробелы в начале и конце **логина**. У пароля trim нет: пробелы считаются частью пароля. Поэтому `123456 ` не совпадёт с `123456`.

Регистр автоматически не меняется. В учебных данных логин задан как `student`.

## 7.5. Пустой ввод

~~~kotlin
        if (login.isEmpty() || password.isEmpty()) {
            error.setText(R.string.fill_fields)
            error.visibility = View.VISIBLE
            return
        }
~~~

Если поле пустое, появляется «Введите логин и пароль». Ранний `return` не позволяет выполнять запрос и переходить дальше.

Логин из одних пробелов после `trim` станет пустым. Пароль из пробелов не считается пустой строкой, но с учебным паролем он не совпадёт.

## 7.6. Результат проверки

~~~kotlin
        val student = database.login(login, password)
        if (student == null) {
            error.setText(R.string.wrong_credentials)
            error.visibility = View.VISIBLE
        } else {
            getSharedPreferences("session", MODE_PRIVATE).edit().putInt("student_id", student.id).apply()
            openDiary()
        }
    }
~~~

`null` означает отсутствие совпадения; экран показывает «Неверный логин или пароль».

При успехе `edit` открывает изменение настроек, `putInt` записывает ID, `apply` применяет изменение в памяти и планирует запись на диск. В сессии нет ни пароля, ни копии всех оценок.

Оценки остаются в SQLite и читаются заново при создании дневника.

## 7.7. Переход и закрытие входа

~~~kotlin
    private fun openDiary() {
        startActivity(Intent(this, DiaryActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        database.close()
        super.onDestroy()
    }
}
~~~

`Intent` описывает, какой экран открыть. `DiaryActivity::class.java` передаёт класс нужной Activity.

`startActivity` запускает его. `finish` закрывает вход, чтобы он не оставался предыдущим экраном в стеке.

В `onDestroy` закрывается помощник базы. Это отдельное действие от закрытия каждого курсора через `use`.

## 7.8. DiaryActivity.kt: повторная проверка сессии

~~~kotlin
class DiaryActivity : Activity() {
    private lateinit var database: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = DatabaseHelper(this)
        val session = getSharedPreferences("session", MODE_PRIVATE)
        val student = database.getStudent(session.getInt("student_id", -1))
        if (student == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
~~~

У дневника собственный помощник БД. Он читает сессию и загружает ученика.

Если подходящего ученика нет, открывается вход, дневник закрывается, `return` прекращает подготовку таблицы. Так экран не предполагает, что вход обязательно был выполнен только потому, что его запустили.

## 7.9. Показ имени и класса

~~~kotlin
        setContentView(R.layout.activity_diary)
        findViewById<TextView>(R.id.studentText).text =
            getString(R.string.student_info, student.name, student.schoolClass)
~~~

`getString` получает шаблон и подставляет в него две строки. Перенос вызова на следующую строку здесь сделан для читаемости; смысл тот же, что в исходнике.

## 7.10. Создание строк предметов

~~~kotlin
        val table = findViewById<TableLayout>(R.id.subjectsTable)
        for ((index, subject) in database.getSubjects(student.id).withIndex()) {
            val row = TableRow(this)
            row.setBackgroundColor(if (index % 2 == 0) Color.WHITE else Color.rgb(242, 242, 242))
            row.addView(makeCell(subject.name, 1.3f))
            row.addView(makeCell(subject.grades, 0.9f))
            row.addView(makeCell(subject.teacher, 1.2f))
            table.addView(row)
        }
~~~

`withIndex` даёт порядковый номер и сам предмет. Запись `(index, subject)` раскладывает их в две переменные.

`index % 2` — остаток от деления на 2. Чётные строки белые, нечётные светло-серые. `Color.rgb(242, 242, 242)` создаёт серый цвет из трёх компонентов RGB.

Для каждого предмета создаётся новая `TableRow`. В неё добавляются три ячейки, а строка добавляется в `TableLayout` через `addView`.

При текущих данных таблица содержит десять строк: одну шапку из XML и девять строк из базы.

## 7.11. Что происходит при выходе

~~~kotlin
        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            session.edit().clear().apply()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
~~~

`clear` очищает настройки именно файла `session`. База SQLite при этом не удаляется.

Теперь у входа нет сохранённого ID, поэтому он покажет форму. Закрытие дневника через `finish` не оставляет его в стеке для возврата кнопкой «Назад».

## 7.12. makeCell: создание ячейки

~~~kotlin
    private fun makeCell(value: String, weight: Float): TextView {
        val cell = TextView(this)
        cell.text = value
        cell.textSize = 14f
        cell.setTextColor(Color.BLACK)
        val padding = (6 * resources.displayMetrics.density).toInt()
        cell.setPadding(padding, padding, padding, padding)
        cell.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight)
        return cell
    }
~~~

Функция получает текст и вес, возвращает готовый `TextView`.

`textSize = 14f` задаёт размер текста через API TextView в sp. В `setPadding` нужны пиксели, поэтому 6dp умножаются на плотность экрана `density` и превращаются в целое число.

`TableRow.LayoutParams` определяет размещение **внутри строки**: ширина 0 для распределения весом, высота по содержимому, относительный вес колонки.

`return cell` передаёт созданный элемент вызывающему `row.addView`.

## 7.13. Освобождение базы

~~~kotlin
    override fun onDestroy() {
        database.close()
        super.onDestroy()
    }
}
~~~

Экран закрывает собственный помощник БД и вызывает метод родителя. Записи базы от этого не исчезают: закрывается соединение, а файл остаётся.

В маленьком примере запросы выполняются прямо в обработчиках экрана. Для больших объёмов данных и сетевой работы потребовалось бы вынести длительные операции из главного потока.


---

# Часть 8. Как поменять данные и приложение

## 8.1. Главное правило изменения примеров

Записи из `onCreate` добавляются при создании базы один раз. Если база уже существует, установка обновлённого APK обычно сохраняет её.

Чтобы увидеть изменённые начальные данные в учебном варианте:

1. Изменить Kotlin-код.
2. Собрать и установить новую версию.
3. На телефоне открыть настройки приложения «Дневник».
4. Выбрать очистку **данных/хранилища**, а не только кэша.
5. Запустить приложение снова.

Очистка данных удаляет текущую локальную БД и сессию. При следующем запуске они создаются заново из нового кода. Если нужно сохранить прежние записи, вместо очистки потребуется миграция.

## 8.2. Поменять логин, пароль, имя или класс

В `DatabaseHelper.kt` найти:

~~~kotlin
db.execSQL("INSERT INTO students VALUES (1, 'student', '123456', 'Иванов Иван', '9 А')")
~~~

Пример замены:

~~~kotlin
db.execSQL("INSERT INTO students VALUES (1, 'pupil', '654321', 'Сидоров Павел', '8 Б')")
~~~

Порядок значений:

~~~text
1 → ID
pupil → логин
654321 → пароль
Сидоров Павел → имя на экране
8 Б → класс
~~~

Оставьте ID 1, если не меняете привязку предметов: существующая `addSubject` вставляет `student_id = 1`.

После замены примените правило из 8.1. Также обновите описания учебного аккаунта и соответствующие ожидания тестов.

## 8.3. Изменить предмет, оценки и преподавателя

Найти вызов, например:

~~~kotlin
addSubject(db, "Физика", "3, 4, 4", "Волков Д. Н.")
~~~

Пример замены:

~~~kotlin
addSubject(db, "Информатика", "5, 5, 4", "Егоров П. А.")
~~~

Первый текст — предмет, второй — отображаемые оценки, третий — фамилия и инициалы преподавателя.

Второй аргумент не разбирается на отдельные числа. Если написать `"зачёт"`, экран покажет «зачёт»: проверки формата сейчас нет.

Для добавления предмета достаточно ещё одного вызова `addSubject` в `onCreate`. Для удаления — убрать соответствующий вызов. Затем пересоздать учебную БД и скорректировать ожидаемое количество строк в тестах.

## 8.4. Пример добавления второго ученика

**В текущей версии второго аккаунта нет.** Ниже пример доработки исходника.

В `onCreate` добавить:

~~~kotlin
db.execSQL("INSERT INTO students VALUES (2, 'student2', '123456', 'Петрова Анна', '9 Б')")
db.execSQL(
    "INSERT INTO subjects (student_id, name, grades, teacher) VALUES (?, ?, ?, ?)",
    arrayOf<Any>(2, "Математика", "4, 5, 4", "Петрова А. В.")
)
~~~

ID нового ученика и `student_id` его предметов должны совпадать — здесь 2.

Для большого числа предметов лучше изменить `addSubject`, добавив параметр `studentId: Int`. В нынешнем помощнике ID жёстко равен 1, поэтому его обычный вызов не добавит предмет второму ученику.

Методы `login`, `getStudent` и `getSubjects` уже принимают логин или ID и не выбирают всегда ученика 1. Но начальное заполнение, тесты и документацию для нескольких учеников нужно доработать.

## 8.5. Поменять название приложения

В `app/src/main/res/values/strings.xml`:

~~~xml
<string name="app_name">Дневник</string>
~~~

Это название в манифесте. `rootProject.name = "Diary"` влияет на имя проекта для разработки, а не на подпись приложения на телефоне.

## 8.6. Поменять подписи и размеры

- Подписи полей/кнопок/ошибок — `strings.xml`.
- Отступы и заголовки входа — `activity_main.xml`.
- Заголовок дневника, шапка и отступы — `activity_diary.xml`.
- Шрифт шапки — `TableCell` в `styles.xml`.
- Шрифт и отступы строк с данными — `makeCell` в `DiaryActivity.kt`.
- Веса колонок — одновременно XML шапки и вызовы `makeCell`.

Изменение XML, строк и стилей не требует удаления базы. Нужно пересобрать и запустить новую версию.

## 8.7. Поменять порядок предметов

Для новой учебной базы переставьте вызовы `addSubject` в нужном порядке. Текущий запрос сортирует по ID.

Для алфавитного порядка можно заменить конец запроса:

~~~sql
ORDER BY name
~~~

Это пример изменения запроса. SQLite-сортировка кириллицы с базовым сопоставлением не обязана совпадать со всеми правилами русского словарного порядка.

## 8.8. Посмотреть настоящую БД в Android Studio

Для поддерживаемого эмулятора/устройства с Android 8.0 или новее:

1. Запустить debug-версию приложения.
2. Открыть `App Inspection`, обычно через `View → Tool Windows`.
3. Выбрать процесс `com.example.diary` и `Database Inspector`.
4. Открыть `diary.db`, таблицы `students` и `subjects`.

Inspector зависит от версии Android и возможностей среды. Если не видит БД, сначала убедитесь, что выбран именно debug-процесс и приложение уже открыло базу.

Пример запроса просмотра:

~~~sql
SELECT id, login, name, school_class FROM students;
SELECT student_id, name, grades, teacher FROM subjects ORDER BY id;
~~~

Если менять записи напрямую в Inspector, пересоздайте экран дневника, чтобы он перечитал данные. Автоматического обновления таблицы на экране при внешней правке БД нет.

---

# Часть 9. Проверки и тесты

## 9.1. Где они лежат

~~~text
app/src/androidTest/java/com/example/diary/DiaryTest.kt
~~~

`androidTest` — тесты, которые выполняются на настоящем Android-устройстве или эмуляторе. Обычная задача `test` для JVM не заменяет их.

В проекте три тестовых метода. Они проверяют не только функции Kotlin, но и настоящую SQLite и переходы между Activity.

## 9.2. Как тест получает окружение

~~~kotlin
private val instrumentation = InstrumentationRegistry.getInstrumentation()
private val context = instrumentation.targetContext

@get:Rule
val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

@Before
fun clearSession() {
    context.getSharedPreferences("session", Context.MODE_PRIVATE).edit().clear().commit()
}
~~~

`InstrumentationRegistry` даёт механизм запуска тестов Android. `targetContext` — окружение тестируемого приложения.

`@get:Rule` подключает правило управления Activity к JUnit. Последний `false` означает, что Activity не запускается автоматически: каждый тест делает это тогда, когда готов.

`@Before` запускает метод перед каждым тестом. Здесь очищается сессия, но не удаляется БД.

В тестовой подготовке используется `commit`: он выполняет запись синхронно и возвращает результат. В коде приложения используется `apply`.

## 9.3. Проверка базы

Метод `databaseChecksCredentialsAndFiltersSubjects` проверяет:

- Правильный вход возвращает «Иванов Иван».
- Неправильный пароль возвращает null.
- Строка, похожая на SQL-инъекцию, не открывает аккаунт.
- Пустые логин и пароль не дают результата.
- У ученика 1 есть девять предметов.
- У первого предмета ожидаемый преподаватель.
- Для неизвестного ID 999 возвращается пустой список.

Пример:

~~~kotlin
assertEquals("Иванов Иван", database.login("student", "123456")?.name)
assertNull(database.login("student", "wrong"))
assertNull(database.login("' OR 1=1 --", "123456"))
assertEquals(9, database.getSubjects(1).size)
assertTrue(database.getSubjects(999).isEmpty())
~~~

`assertEquals` сравнивает ожидаемое и фактическое значения. `assertNull` требует отсутствия результата, `assertTrue` — истинности условия.

`?.name` безопасно читает имя: если вход вернул null, результат обращения тоже null и сравнение не пройдёт.

Помощник закрывается в `finally`, даже если проверка завершилась ошибкой.

## 9.4. Ошибки ввода

`emptyAndWrongPasswordShowErrors` открывает вход и программно нажимает кнопку:

~~~kotlin
val activity = activityRule.launchActivity(Intent())
instrumentation.runOnMainSync {
    activity.findViewById<Button>(R.id.loginButton).performClick()
    assertEquals(View.VISIBLE, activity.findViewById<TextView>(R.id.errorText).visibility)
}
~~~

`runOnMainSync` выполняет работу с экраном в главном потоке. `performClick` имитирует нажатие.

В полном методе также вводятся `student` и неверный пароль, проверяется сообщение, а после действий — отсутствие сохранённого ID.

## 9.5. Вход, таблица и выход

`loginOpensDiaryAndLogoutClearsSession`:

1. Открывает форму входа.
2. Вводит `student / 123456`.
3. Нажимает «Войти».
4. Ждёт запуска `DiaryActivity`.
5. Проверяет имя и класс.
6. Проверяет десять строк таблицы вместе с шапкой.
7. Нажимает «Выйти».
8. Ждёт возврата к `MainActivity`.
9. Проверяет, что сохранённого ID больше нет.

Для ожидания используются `addMonitor` и `waitForMonitorWithTimeout` с тайм-аутом 5000 мс. `waitForIdleSync` ждёт завершения текущей работы интерфейса перед чтением элементов.

Мониторы после использования удаляются через `removeMonitor`, чтобы не мешать следующему ожиданию.

## 9.6. Как запустить проверки самостоятельно

Подключить тестовый телефон или запустить эмулятор, затем:

~~~powershell
.\gradlew.bat lintDebug connectedDebugAndroidTest
~~~

На Linux/macOS заменить `.\gradlew.bat` на `./gradlew`.

Проверка использует данные приложения и очищает сессию. Для тестов лучше отдельный эмулятор с неизменёнными учебными данными.

Отчёты:

~~~text
app/build/reports/lint-results-debug.html
app/build/reports/androidTests/connected/
~~~

Lint читает код/ресурсы и ищет проблемы без запуска экрана. Инструментальные тесты выполняют приложение на Android. Успех этих тестов не означает, что приложение проверено на каждой модели телефона.

Если вы изменили учебные записи, но не изменили ожидания тестов, проверки закономерно упадут: например, после добавления предмета старое ожидание `9` уже неверно.

---

# Часть 10. GitHub Actions, подпись и релизы

## 10.1. Что такое workflow

`.github/workflows/android.yml` — инструкция для GitHub, а не часть интерфейса приложения. Она выполняется на удалённом компьютере GitHub.

Workflow описывается YAML:

- Вложенность задаётся пробелами.
- `имя: значение` задаёт параметр.
- `-` начинает элемент списка.
- `uses` подключает готовое действие.
- `run` выполняет команду.
- `with` передаёт параметры действию.
- `env` задаёт переменные среды.
- `if` ограничивает выполнение условием.

## 10.2. События запуска

~~~yaml
name: Android APK
on:
  push:
    branches: [main]
    tags: ['v*']
  workflow_dispatch:
permissions:
  contents: read
~~~

`name` — название на вкладке Actions.

`push` запускает workflow при отправке в `main` или при отправке тега, начинающегося с `v`. `workflow_dispatch` добавляет ручной запуск.

Разрешение `contents: read` позволяет читать исходники. Право публикации отдельно выдаётся только задаче `release`.

| Как запущено | build | test | release |
|---|---|---|---|
| Push в main | Да | Пропуск | Пропуск |
| Push тега v1.0.1 | Да | Да | После успеха build и test |
| Ручной запуск на ветке main | Да | Пропуск | Пропуск |

У workflow нет поля ввода «тег релиза». Ручной запуск на main не создаёт тег и релиз. Поведение примера Lean в этом месте отличается.

Условия смотрят на `github.ref`. Если запускать вручную именно на тег через CLI/API, условия test/release тоже выполнятся; повторная публикация уже существующего релиза командой `gh release create` завершится ошибкой.

## 10.3. Задача build: окружение

~~~yaml
build:
  runs-on: ubuntu-latest
  timeout-minutes: 20
~~~

`runs-on` выбирает компьютер Linux. `timeout-minutes` ограничивает время задачи.

Шаги подготовки:

| Действие | Назначение |
|---|---|
| `actions/checkout@v4` | Получить исходники нужной ветки/тега |
| `actions/setup-java@v4` | Подготовить Temurin JDK 17 |
| `android-actions/setup-android@v3` | Подготовить Android SDK |
| `gradle/actions/setup-gradle@v4` | Подготовить Gradle и работу с кэшем |

Для Android SDK задано:

~~~yaml
with:
  packages: platform-tools
  log-accepted-android-sdk-licenses: false
~~~

Устанавливаются platform-tools. Старый пакет `tools` не запрашивается. Полный текст принимаемых SDK-лицензий не выводится в журнал; это не отключение требований лицензирования.

## 10.4. Откуда берётся ключ подписи

Для `Prepare release signing` workflow передаёт три секрета в переменные:

| GitHub Secret | Переменная шага | Назначение |
|---|---|---|
| `DIARY_KEYSTORE_BASE64` | KEYSTORE | Файл ключа, закодированный Base64 |
| `DIARY_STORE_PASSWORD` | STORE_PASSWORD | Пароль хранилища |
| `DIARY_KEY_PASSWORD` | KEY_PASSWORD | Пароль ключа |

Base64 — способ представить байты текстом, а не шифрование. Конфиденциальность обеспечивает хранение в Secrets и доступ к репозиторию.

~~~bash
printf '%s' "$KEYSTORE" | base64 --decode > "$RUNNER_TEMP/diary.jks"
echo "DIARY_KEYSTORE=$RUNNER_TEMP/diary.jks" >> "$GITHUB_ENV"
echo "DIARY_STORE_PASSWORD=$STORE_PASSWORD" >> "$GITHUB_ENV"
echo "DIARY_KEY_PASSWORD=$KEY_PASSWORD" >> "$GITHUB_ENV"
~~~

`printf` отдаёт текст декодеру Base64. `>` записывает полученные байты в временный файл.

`RUNNER_TEMP` — временная папка машины GitHub. `GITHUB_ENV` — специальный файл: значения из него станут переменными для следующих шагов.

`app/build.gradle.kts` читает эти переменные и подписывает APK ключом с alias `diary`.

Если ключ не задан при запуске на тег, шаг выдаёт ошибку и прекращает задачу. Секреты не добавлены в исходники.

## 10.5. Сборка и артефакты

~~~bash
chmod +x gradlew
./gradlew assembleDebug assembleRelease lintDebug --no-daemon
cp app/build/outputs/apk/release/app-release.apk Diary.apk
~~~

`chmod +x` разрешает запуск скрипта в Linux. Далее собираются оба варианта и выполняется lint для debug-варианта.

`--no-daemon` завершает процесс Gradle после сборки. `cp` копирует релизный APK под понятным именем `Diary.apk`.

Шаг `actions/upload-artifact@v4` сохраняет:

- `Diary-APK` — APK как артефакт сборки.
- `Lint-report` — HTML-отчёт lint, если файл был создан.

Артефакт — файл, связанный с конкретным запуском Actions. Он не является самим GitHub Release и хранится ограниченное время по правилам GitHub/настройкам репозитория.

Для отчёта используется `if: always()`: попытка сохранить его выполняется даже после ошибки предыдущего шага. Если отчёт ещё не был создан, приложить нечего.

## 10.6. Задача test

~~~yaml
test:
  if: startsWith(github.ref, 'refs/tags/')
  runs-on: ubuntu-latest
  timeout-minutes: 25
~~~

`startsWith` проверяет начало строки ссылки на Git. Для тега ссылка имеет вид `refs/tags/v1.0.0`.

После подготовки SDK и Java шаг `Enable KVM` настраивает доступ к ускорению виртуализации на машине GitHub. Это настройка сервера сборки, её не нужно переносить в Kotlin-код или выполнять на телефоне.

Эмулятор запускается через `reactivecircus/android-emulator-runner@v2`:

~~~yaml
with:
  api-level: 29
  arch: x86_64
  profile: Nexus 5
  disable-animations: true
  script: chmod +x gradlew && ./gradlew connectedDebugAndroidTest --no-daemon
~~~

API 29 — Android 10; x86_64 — архитектура виртуального устройства. Анимации отключены для более предсказуемой работы тестов.

После проверки сохраняется `Android-test-report`. Если эмулятор не смог запуститься до тестов, отчёта может ещё не быть.

## 10.7. Задача release

~~~yaml
release:
  if: startsWith(github.ref, 'refs/tags/')
  needs: [build, test]
  runs-on: ubuntu-latest
  permissions:
    contents: write
~~~

`needs` требует завершения build и test. Если обязательная задача упала, обычная публикация не выполняется.

`contents: write` разрешает создание GitHub Release.

Сначала `actions/download-artifact@v4` получает `Diary-APK` из того же запуска. Затем:

~~~bash
gh release create "$GITHUB_REF_NAME" Diary.apk --title "Дневник $GITHUB_REF_NAME" --notes 'Простой учебный дневник. Вход: student / 123456. Android 5.0 и выше. Данные хранятся в SQLite на устройстве.'
~~~

`gh` — GitHub CLI. `GITHUB_REF_NAME` содержит короткое имя тега, например v1.0.1.

`GH_TOKEN` в этом шаге берётся из автоматического `github.token`, а `GH_REPO` — из имени текущего репозитория. Это служебная авторизация задачи, а не токен, записанный в приложении.

## 10.8. Скачать APK из Actions

1. Открыть вкладку `Actions`.
2. Выбрать workflow `Android APK`.
3. Открыть успешный запуск.
4. Прокрутить до `Artifacts`.
5. Скачать `Diary-APK`.
6. Распаковать ZIP и взять `Diary.apk`.

Если build упал до сохранения APK, артефакта Diary-APK не будет. Сначала откройте журнал ошибочного шага.

## 10.9. Создать новый релиз

Перед новым релизом изменить `app/build.gradle.kts`, например:

~~~kotlin
versionCode = 2
versionName = "1.0.1"
~~~

Потом отправить изменения и новый тег. Команды выполняются в клонированном репозитории:

~~~bash
git add .
git commit -m "Update diary to version 1.0.1"
git push origin main
git tag v1.0.1
git push origin v1.0.1
~~~

Пример предполагает, что v1.0.1 ещё не существует. Для следующего релиза выбирайте новый номер.

`git add .` включает все неигнорируемые изменения: перед коммитом проверьте `git status`, особенно если создавали свои файлы с паролями.

Название тега и `versionName` не меняют друг друга автоматически. Нужно самостоятельно согласовать их, а `versionCode` увеличить.

Существующий опубликованный тег v1.0.0 не нужно передвигать на новый код: исходники старого релиза должны оставаться воспроизводимыми.

## 10.10. Получить резервную копию ключа

Workflow `Signing backup` запускается только вручную:

1. Открыть `Actions → Signing backup`.
2. Нажать `Run workflow` для ветки main.
3. Дождаться успеха.
4. Скачать артефакт `Private-signing-backup`.
5. Распаковать и сохранить отдельно от исходников.

В архиве будут `diary.jks` и `passwords.txt` с alias и паролями. Срок хранения артефакта задан `retention-days: 1` — один день. Позже workflow можно запустить снова, пока исходные Secrets доступны.

Текущий репозиторий закрытый. Копию ключа не следует публиковать в Releases или коммитить: она позволяет подписывать APK этого приложения.

## 10.11. Подписанный release на своём компьютере

Получить резервную копию ключа, затем задать переменные в PowerShell. Здесь указаны **шаблоны**, а не действующие пароли:

~~~powershell
$env:DIARY_KEYSTORE = "C:\Keys\diary.jks"
$env:DIARY_STORE_PASSWORD = "ПАРОЛЬ_ХРАНИЛИЩА"
$env:DIARY_KEY_PASSWORD = "ПАРОЛЬ_КЛЮЧА"
.\gradlew.bat assembleRelease
~~~

На Linux/macOS:

~~~bash
export DIARY_KEYSTORE="/home/you/keys/diary.jks"
export DIARY_STORE_PASSWORD="STORE_PASSWORD"
export DIARY_KEY_PASSWORD="KEY_PASSWORD"
./gradlew assembleRelease
~~~

Файл результата:

~~~text
app/build/outputs/apk/release/app-release.apk
~~~

Переменные нужно задавать в том процессе, где запустится Gradle. Настройка в открытом терминале не обязательно передаётся уже работающей Android Studio.

Для сборки через меню Studio также можно использовать мастер `Generate Signed Bundle / APK`, выбрать APK, существующий keystore, alias `diary` и пароли из резервной копии.

Для обычной проверки на своём телефоне достаточно `assembleDebug`. Постоянный релизный ключ нужен, когда важно установить обновление поверх APK из Releases.


---

# Часть 11. Работа без Git и перенос в Android Studio

## 11.1. Что можно удалить

Для локального проекта можно удалить:

| Файл/папка | Что исчезнет |
|---|---|
| `.git` | Локальная история, ветки и связь с Git |
| `.github` | Инструкции автоматизации GitHub |
| `.gitignore` | Правила исключения файлов для Git |

В скачанном ZIP `.git` уже нет. Удаление локальной `.github` не выключает workflow в репозитории на GitHub, пока эти файлы остаются там.

После этого проект по-прежнему открывается и собирается через Android Studio или Wrapper.

## 11.2. Что оставить

Обязательно сохранить:

~~~text
settings.gradle.kts
build.gradle.kts
gradle.properties
gradlew
gradlew.bat
gradle/wrapper/gradle-wrapper.jar
gradle/wrapper/gradle-wrapper.properties
app/build.gradle.kts
app/src/
~~~

`gradle` не является папкой Git. Без Wrapper при обычном запуске `gradlew` сборка не сможет стартовать.

`app/src/androidTest` содержит тесты, а `app/src/main` — приложение. Тесты можно не запускать для обычной сборки, но сохранять их полезно для проверки изменений.

Каталоги `build` можно пересоздать сборкой. `.gradle` — кэш. `.idea` — настройки IDE. `local.properties` содержит путь к SDK конкретного компьютера.

## 11.3. Перенос на другой компьютер

1. Скопировать исходники или распаковать ZIP.
2. Если в копии есть `local.properties` со старым путём SDK, исправить путь или дать Studio создать файл заново.
3. Открыть корень проекта.
4. Настроить SDK 35 и Gradle JDK 17.
5. Дождаться Sync.
6. Запустить debug-сборку.

Ключ подписи в ZIP исходников отсутствует. Для release-обновления поверх опубликованного APK дополнительно перенесите резервную копию ключа отдельно.

---

# Часть 12. Ошибки и ответы на вопросы

## 12.1. Gradle требует Java 17

Пример ошибки:

~~~text
Android Gradle plugin requires Java 17 to run
~~~

В Studio проверить `Gradle JDK`, а в терминале:

~~~powershell
java -version
$env:JAVA_HOME
~~~

Если терминал запускает старую Java, задать `JAVA_HOME` на папку установленного JDK 17 или настроить PATH. Папка JDK должна содержать `bin/java`, а не указывать прямо на исполняемый файл.

После изменения системных переменных открыть новый терминал. Настройки уже запущенных процессов могут остаться прежними.

## 12.2. SDK не найден

Примеры:

~~~text
SDK location not found
Failed to find Platform SDK with path: platforms;android-35
~~~

Первое означает, что сборка не знает путь к SDK. Проверить `local.properties` и настройки SDK в Studio.

Второе означает, что не установлена платформа API 35. Установить её через SDK Manager и повторить Sync/сборку.

## 12.3. Не скачиваются плагины или Gradle

Сообщения `Could not resolve`, `Connection timed out`, `Unknown host` обычно относятся к сетевой загрузке.

Проверить доступ к серверам Google, Maven Central, Gradle Plugin Portal и services.gradle.org, затем повторить синхронизацию. Если включён Offline Mode, для первой загрузки его потребуется выключить.

Сама работа установленного дневника не зависит от этих серверов.

## 12.4. После изменения кода остались старые оценки

Это ожидаемо для уже созданной базы: `onCreate` не выполняется повторно при каждом запуске.

Пересобрать APK и очистить **данные приложения** для применения новых учебных примеров. Очистка только кэша или `gradlew clean` не заменяет очистку базы на телефоне.

Если удалять данные нельзя, написать обновление записей или миграцию.

## 12.5. После запуска сразу открывается дневник

Это сохранённая сессия. Нажать «Выйти», чтобы снова увидеть форму входа.

Закрытие приложения и кнопка «Назад» не равнозначны выходу из аккаунта. Настройки `session` сохраняются между запусками.

## 12.6. Правильный пароль не подходит

Проверить:

- Логин `student`, без изменения регистра.
- Пароль `123456`, без пробелов.
- Не менялись ли начальные данные или сама база через Inspector.
- Запущена ли именно новая сборка.

Если логин/пароль менялись в коде после первого запуска, существующая БД могла сохранить старые значения. Применить правило пересоздания учебных данных.

## 12.7. APK не устанавливается поверх другого APK

Типичный случай: на телефоне стоит GitHub release, а Studio устанавливает локальный debug APK. Их ключи подписи разные.

Варианты:

1. Для сохранения данных собрать новую версию с тем же постоянным ключом и подходящим versionCode.
2. Для учебного запуска удалить старое приложение и установить новое; удаление также удаляет его локальные данные.

Сообщения `INSTALL_FAILED_UPDATE_INCOMPATIBLE` обычно относятся к несовпадению подписи; `INSTALL_FAILED_VERSION_DOWNGRADE` — к понижению кода версии.

## 12.8. В Actions нет релиза

Сначала проверить тип запуска. Push/ручной запуск на main сохраняет артефакт, но не выпускает релиз.

Для релиза нужен новый тег `v…`. Затем должны успешно закончиться build и test. Если одна задача упала, release будет пропущен.

Также проверить наличие трёх Secrets подписи. Релизный запуск без ключа намеренно останавливается.

## 12.9. Повторная публикация сообщает, что релиз уже существует

Текущий workflow выполняет `gh release create`, а не обновление существующего релиза. Для новой версии создайте новый тег.

Перезапуск успешного старого релизного workflow сам по себе не создаёт новую версию приложения.

## 12.10. Не видно строк таблицы в Design

В `activity_diary.xml` находится только шапка. Предметы создаются циклом в `DiaryActivity` после чтения SQLite.

Чтобы увидеть реальные строки, запустить приложение и войти.

## 12.11. Тест ожидает 9 предметов, а теперь их 10

Нужно обновить тестовые ожидания:

- `getSubjects(1).size` проверяет число предметов.
- `subjectsTable.childCount` проверяет число строк, включая шапку.

Для десяти предметов число строк таблицы будет одиннадцать. База тестового устройства также должна соответствовать новым примерам.

## 12.12. Где хранится база и нужна ли MySQL

`diary.db` находится в личном каталоге данных приложения Android, обычно внутри `databases`. Обычный файловый менеджер без дополнительных прав не обязан видеть этот каталог.

MySQL для текущего примера не нужна. Если в будущем преподаватель должен менять оценки для многих телефонов, потребуется общая серверная БД и API. Это будет отдельная доработка, а не переключатель в Gradle.

## 12.13. Какие возможности пока отсутствуют

В текущем учебном варианте нет:

- Регистрации и смены пароля из интерфейса.
- Кабинета преподавателя и редактирования оценок.
- Дат, расписания, домашних заданий и среднего балла.
- Синхронизации между устройствами.
- Сервера и удалённой проверки доступа.
- Полноценного хранения паролей для реальной системы.

Эти ограничения описывают фактическое устройство первой версии. Начальные данные можно изменить в коде по примерам выше.

## 12.14. Последовательность проверки после своей правки

1. Убедиться, что изменён нужный файл.
2. Выполнить Gradle Sync, если менялись настройки сборки.
3. Собрать APK.
4. Если менялись начальные записи, пересоздать учебную БД.
5. Проверить пустой ввод и неправильный пароль.
6. Проверить правильный вход, предметы и преподавателей.
7. Выйти и убедиться, что снова показан вход.
8. Обновить тестовые ожидания, если поменялись данные.
9. Выполнить lint и тесты на отдельном эмуляторе.
10. Для нового релиза увеличить versionCode, согласовать versionName и создать новый тег.

---

# Официальная документация

Ниже источники по инструментам и API, которые используются в проекте. Точные версии и поведение самого дневника определяются файлами этого репозитория.

- [Установка Android Studio](https://developer.android.com/studio)
- [Выбор Java/JDK для Android-сборки](https://developer.android.com/build/jdks)
- [Совместимость Android Gradle Plugin 8.7](https://developer.android.com/build/releases/agp-8-7-0-release-notes)
- [Запуск Android-приложения](https://developer.android.com/studio/run)
- [Создание APK для распространения](https://developer.android.com/build/build-for-release)
- [Сборка из командной строки](https://developer.android.com/build/building-cmdline)
- [Хранение данных через SQLite](https://developer.android.com/training/data-storage/sqlite)
- [SQLiteOpenHelper](https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper)
- [Database Inspector](https://developer.android.com/studio/inspect/database)
- [SharedPreferences](https://developer.android.com/training/data-storage/shared-preferences)
- [Подпись Android-приложения](https://developer.android.com/studio/publish/app-signing)
- [Ручной запуск workflow](https://docs.github.com/en/actions/how-tos/manage-workflow-runs/manually-run-a-workflow)
- [GitHub CLI: запуск workflow на ветке или теге](https://cli.github.com/manual/gh_workflow_run)
