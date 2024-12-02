# Утилита для удаления вершин

## Описание

Эта утилита предоставляет функционал для удаления вершин из 3D моделей, гарантируя корректность структуры модели после модификации. При удалении утилита автоматически пересчитывает индексы вершин, нормалей и текстурных координат, а также корректирует полигоны модели, чтобы они оставались валидными. Утилита может работать как с созданием новой модели, так и с изменением существующей.

## Основные возможности

- Удаление указанных вершин из модели с автоматической корректировкой всех индексов.
- Перестройка полигонов для поддержания корректности модели.
- Возможность обработки висящих нормалей, текстурных координат и полигонов, которые становятся неиспользуемыми после удаления вершин.
- Гибкость в выборе: создать новую модель или изменить текущую.
- Простой интерфейс для интеграции в проекты.

## Функции

### Удаление вершин

Основной метод `vertexDelete` принимает модель, список индексов для удаления и набор опциональных параметров:

- **`model`**: Исходная 3D модель.
- **`index`**: Список индексов вершин, которые нужно удалить.
- **`new_file`**: Флаг, определяющий, будет ли создана новая модель (если `true`) или изменена существующая (если `false`).
- **`hanging_NormalIndices`**: Удалять ли нормали, которые больше не используются (если `false`).
- **`hanging_TexturelIndices`**: Удалять ли текстурные координаты, которые больше не используются (если `false`).
- **`hanging_polygons`**: Удалять ли полигоны, которые становятся некорректными после удаления вершин (если `false`).

### Автоматическое управление данными

При удалении утилита автоматически:

1. Удаляет заданные вершины и обновляет индексы.
2. Пересчитывает индексы текстурных координат и нормалей, чтобы исключить неиспользуемые элементы.
3. Обеспечивает корректность всех оставшихся полигонов.

## Пример использования

```java
// Исходная модель и список удаляемых вершин
Model myModel = ...;
List<Integer> verticesToDelete = Arrays.asList(0, 3, 7);

// Вызов метода с опцией создания новой модели
Model updatedModel = Eraser.vertexDelete(
    myModel,                  // Исходная модель
    verticesToDelete,         // Список индексов для удаления
    true,                     // Создать новую модель
    false,                    // Не сохранять висящие нормали
    false,                    // Не сохранять висящие текстурные координаты
    false                     // Не сохранять висящие полигоны
);
```

### Описание параметров

| Параметр               | Описание                                                                 |
|------------------------|------------------------------------------------------------------------|
| `model`               | Исходная 3D модель.                                                    |
| `index`               | Список индексов вершин, которые должны быть удалены.                   |
| `new_file`            | Флаг, создающий новую модель или изменяющий текущую.                   |
| `hanging_NormalIndices`| Флаг для удаления нормалей, не используемых после изменения.           |
| `hanging_TexturelIndices` | Флаг для удаления текстурных координат, не используемых после изменения.|
| `hanging_polygons`     | Флаг для удаления полигонов, которые становятся некорректными.         |

## Особенности реализации

- **Корректность данных**: Все изменения применяются с учетом зависимостей между вершинами, нормалями и текстурными координатами.
- **Гибкость**: Возможность выбора, какие данные сохранять или удалять (например, висящие нормали).
- **Производительность**: Утилита оптимизирована для работы с большими моделями.

## Требования

- Java 8 и выше.
- Библиотека с реализацией класса `Model` и `Polygon`.
