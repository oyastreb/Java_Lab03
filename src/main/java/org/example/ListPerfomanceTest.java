package org.example;
import java.util.*;

/**
 * Класс для сравнительного тестирования производительности ArrayList и LinkedList.
 * Содержит методы для измерения времени выполнения основных операций над коллекциями
 * и предоставляет инструменты для анализа результатов.
 *
 * <p>Тестируются следующие операции:
 * <ul>
 *   <li>Добавление элементов (в конец, начало, середину)</li>
 *   <li>Получение элементов (случайный и последовательный доступ)</li>
 *   <li>Удаление элементов (из начала, конца, середины)</li>
 * </ul>
 *
 * @author Performance Test Team
 * @version 1.0
 * @see ArrayList
 * @see LinkedList
 * @see List
 */
public class ListPerformanceTest {
    private final Random random = new Random();

    /**
     * Класс для хранения результатов одного теста производительности.
     * Содержит информацию о времени выполнения операции для ArrayList и LinkedList,
     * а также определяет, какая коллекция показала лучшую производительность.
     */
    public static class TestResult {
        /** Название теста */
        public final String testName;

        /** Количество выполненных операций */
        public final int operations;

        /** Время выполнения в наносекундах для ArrayList */
        public final long arrayListTime;

        /** Время выполнения в наносекундах для LinkedList */
        public final long linkedListTime;

        /** Коллекция, показавшая лучшую производительность */
        public final String faster;

        /** Коэффициент производительности (во сколько раз быстрее) */
        public final double speedRatio;

        /**
         * Конструктор для создания результата теста.
         *
         * @param testName название теста
         * @param operations количество операций
         * @param arrayListTime время выполнения для ArrayList (в наносекундах)
         * @param linkedListTime время выполнения для LinkedList (в наносекундах)
         */
        public TestResult(String testName, int operations, long arrayListTime, long linkedListTime) {
            this.testName = testName;
            this.operations = operations;
            this.arrayListTime = arrayListTime;
            this.linkedListTime = linkedListTime;
            this.faster = determineFaster();
            this.speedRatio = calculateSpeedRatio();
        }

        /**
         * Определяет, какая коллекция показала лучшую производительность.
         *
         * @return "ArrayList", "LinkedList" или "Одинаково"
         */
        private String determineFaster() {
            if (arrayListTime < linkedListTime) {
                return "ArrayList";
            } else if (linkedListTime < arrayListTime) {
                return "LinkedList";
            } else {
                return "Одинаково";
            }
        }

        /**
         * Вычисляет коэффициент производительности (во сколько раз одна коллекция быстрее другой).
         *
         * @return коэффициент производительности (всегда ≥ 1)
         */
        private double calculateSpeedRatio() {
            if (arrayListTime == 0 || linkedListTime == 0) return 0;
            if (arrayListTime < linkedListTime) {
                return (double) linkedListTime / arrayListTime;
            } else {
                return (double) arrayListTime / linkedListTime;
            }
        }
    }

    /**
     * Запускает полный набор тестов производительности для ArrayList и LinkedList.
     *
     * <p>Выполняет следующие тесты:
     * <ol>
     *   <li>Добавление элементов в конец списка (100% операций)</li>
     *   <li>Добавление элементов в начало списка (10% операций)</li>
     *   <li>Добавление элементов в середину списка (10% операций)</li>
     *   <li>Случайный доступ к элементам (100% операций)</li>
     *   <li>Последовательный доступ к элементам (100% операций)</li>
     *   <li>Удаление элементов из начала списка (10% операций)</li>
     *   <li>Удаление элементов из конца списка (10% операций)</li>
     *   <li>Удаление элементов из середины списка (5% операций)</li>
     * </ol>
     *
     * @param operationCount базовое количество операций для тестирования.
     *                       Для некоторых тестов используется меньшее количество операций
     *                       для предотвращения слишком долгого выполнения.
     * @return список результатов тестирования {@code List<TestResult>}, содержащий
     *         результаты всех выполненных тестов
     * @throws IllegalArgumentException если {@code operationCount} меньше или равно 0
     *
     * @see #testAddToEnd
     * @see #testAddToBeginning
     * @see #testAddToMiddle
     * @see #testRandomGet
     * @see #testSequentialGet
     * @see #testRemoveFromBeginning
     * @see #testRemoveFromEnd
     * @see #testRemoveFromMiddle
     */
    public List<TestResult> runPerformanceTests(int operationCount) {
        if (operationCount <= 0) {
            throw new IllegalArgumentException("Количество операций должно быть положительным числом");
        }

        List<TestResult> results = new ArrayList<>();

        // Создаем коллекции для тестирования
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        // Запускаем все тесты
        results.add(testAddToEnd(arrayList, linkedList, "Добавление в конец", operationCount));
        results.add(testAddToBeginning(arrayList, linkedList, "Добавление в начало", operationCount / 10));
        results.add(testAddToMiddle(arrayList, linkedList, "Добавление в середину", operationCount / 10));
        results.add(testRandomGet(arrayList, linkedList, "Получение (случайный)", operationCount));
        results.add(testSequentialGet(arrayList, linkedList, "Получение (последовательный)", operationCount));
        results.add(testRemoveFromBeginning(arrayList, linkedList, "Удаление из начала", operationCount / 10));
        results.add(testRemoveFromEnd(arrayList, linkedList, "Удаление из конца", operationCount / 10));
        results.add(testRemoveFromMiddle(arrayList, linkedList, "Удаление из середины", operationCount / 20));

        return results;
    }

    /**
     * Тестирует производительность добавления элементов в конец списка.
     *
     * @param arrayList экземпляр ArrayList для тестирования
     * @param linkedList экземпляр LinkedList для тестирования
     * @param testName название теста
     * @param operations количество операций добавления
     * @return результат тестирования {@code TestResult}
     */
    private TestResult testAddToEnd(List<Integer> arrayList, List<Integer> linkedList,
                                    String testName, int operations) {
        arrayList.clear();
        linkedList.clear();

        // Тестируем ArrayList
        long startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            arrayList.add(i);
        }
        long arrayListTime = System.nanoTime() - startTime;

        // Тестируем LinkedList
        startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            linkedList.add(i);
        }
        long linkedListTime = System.nanoTime() - startTime;

        return new TestResult(testName, operations, arrayListTime, linkedListTime);
    }

    /**
     * Тестирует производительность добавления элементов в начало списка.
     *
     * @param arrayList экземпляр ArrayList для тестирования
     * @param linkedList экземпляр LinkedList для тестирования
     * @param testName название теста
     * @param operations количество операций добавления
     * @return результат тестирования {@code TestResult}
     */
    private TestResult testAddToBeginning(List<Integer> arrayList, List<Integer> linkedList,
                                          String testName, int operations) {
        arrayList.clear();
        linkedList.clear();

        // Заполняем немного для теста
        for (int i = 0; i < operations; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        long startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            arrayList.add(0, i);
        }
        long arrayListTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            linkedList.add(0, i);
        }
        long linkedListTime = System.nanoTime() - startTime;

        return new TestResult(testName, operations, arrayListTime, linkedListTime);
    }

    /**
     * Тестирует производительность добавления элементов в середину списка.
     *
     * @param arrayList экземпляр ArrayList для тестирования
     * @param linkedList экземпляр LinkedList для тестирования
     * @param testName название теста
     * @param operations количество операций добавления
     * @return результат тестирования {@code TestResult}
     */
    private TestResult testAddToMiddle(List<Integer> arrayList, List<Integer> linkedList,
                                       String testName, int operations) {
        arrayList.clear();
        linkedList.clear();

        for (int i = 0; i < operations; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        long startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            int index = arrayList.size() / 2;
            arrayList.add(index, i);
        }
        long arrayListTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            int index = linkedList.size() / 2;
            linkedList.add(index, i);
        }
        long linkedListTime = System.nanoTime() - startTime;

        return new TestResult(testName, operations, arrayListTime, linkedListTime);
    }

    /**
     * Тестирует производительность случайного доступа к элементам списка.
     *
     * @param arrayList экземпляр ArrayList для тестирования
     * @param linkedList экземпляр LinkedList для тестирования
     * @param testName название теста
     * @param operations количество операций получения
     * @return результат тестирования {@code TestResult}
     */
    private TestResult testRandomGet(List<Integer> arrayList, List<Integer> linkedList,
                                     String testName, int operations) {
        arrayList.clear();
        linkedList.clear();

        // Заполняем коллекции
        for (int i = 0; i < operations; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        long startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            int index = random.nextInt(arrayList.size());
            arrayList.get(index);
        }
        long arrayListTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            int index = random.nextInt(linkedList.size());
            linkedList.get(index);
        }
        long linkedListTime = System.nanoTime() - startTime;

        return new TestResult(testName, operations, arrayListTime, linkedListTime);
    }

    /**
     * Тестирует производительность последовательного доступа к элементам списка.
     *
     * @param arrayList экземпляр ArrayList для тестирования
     * @param linkedList экземпляр LinkedList для тестирования
     * @param testName название теста
     * @param operations количество операций получения
     * @return результат тестирования {@code TestResult}
     */
    private TestResult testSequentialGet(List<Integer> arrayList, List<Integer> linkedList,
                                         String testName, int operations) {
        arrayList.clear();
        linkedList.clear();

        for (int i = 0; i < operations; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        long startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            arrayList.get(i % arrayList.size());
        }
        long arrayListTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            linkedList.get(i % linkedList.size());
        }
        long linkedListTime = System.nanoTime() - startTime;

        return new TestResult(testName, operations, arrayListTime, linkedListTime);
    }

    /**
     * Тестирует производительность удаления элементов из начала списка.
     *
     * @param arrayList экземпляр ArrayList для тестирования
     * @param linkedList экземпляр LinkedList для тестирования
     * @param testName название теста
     * @param operations количество операций удаления
     * @return результат тестирования {@code TestResult}
     */
    private TestResult testRemoveFromBeginning(List<Integer> arrayList, List<Integer> linkedList,
                                               String testName, int operations) {
        // Подготавливаем данные
        List<Integer> tempList = new ArrayList<>();
        for (int i = 0; i < operations * 2; i++) {
            tempList.add(i);
        }

        // Тестируем ArrayList
        arrayList.clear();
        arrayList.addAll(tempList);

        long startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            arrayList.remove(0);
        }
        long arrayListTime = System.nanoTime() - startTime;

        // Тестируем LinkedList
        linkedList.clear();
        linkedList.addAll(tempList);

        startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            linkedList.remove(0);
        }
        long linkedListTime = System.nanoTime() - startTime;

        return new TestResult(testName, operations, arrayListTime, linkedListTime);
    }

    /**
     * Тестирует производительность удаления элементов из конца списка.
     *
     * @param arrayList экземпляр ArrayList для тестирования
     * @param linkedList экземпляр LinkedList для тестирования
     * @param testName название теста
     * @param operations количество операций удаления
     * @return результат тестирования {@code TestResult}
     */
    private TestResult testRemoveFromEnd(List<Integer> arrayList, List<Integer> linkedList,
                                         String testName, int operations) {
        List<Integer> tempList = new ArrayList<>();
        for (int i = 0; i < operations * 2; i++) {
            tempList.add(i);
        }

        arrayList.clear();
        arrayList.addAll(tempList);

        long startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            arrayList.remove(arrayList.size() - 1);
        }
        long arrayListTime = System.nanoTime() - startTime;

        linkedList.clear();
        linkedList.addAll(tempList);

        startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            linkedList.remove(linkedList.size() - 1);
        }
        long linkedListTime = System.nanoTime() - startTime;

        return new TestResult(testName, operations, arrayListTime, linkedListTime);
    }

    /**
     * Тестирует производительность удаления элементов из середины списка.
     *
     * @param arrayList экземпляр ArrayList для тестирования
     * @param linkedList экземпляр LinkedList для тестирования
     * @param testName название теста
     * @param operations количество операций удаления
     * @return результат тестирования {@code TestResult}
     */
    private TestResult testRemoveFromMiddle(List<Integer> arrayList, List<Integer> linkedList,
                                            String testName, int operations) {
        List<Integer> tempList = new ArrayList<>();
        for (int i = 0; i < operations * 3; i++) {
            tempList.add(i);
        }

        arrayList.clear();
        arrayList.addAll(tempList);

        long startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            int index = arrayList.size() / 2;
            arrayList.remove(index);
        }
        long arrayListTime = System.nanoTime() - startTime;

        linkedList.clear();
        linkedList.addAll(tempList);

        startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            int index = linkedList.size() / 2;
            linkedList.remove(index);
        }
        long linkedListTime = System.nanoTime() - startTime;

        return new TestResult(testName, operations, arrayListTime, linkedListTime);
    }

    /**
     * Выводит таблицу с результатами тестирования в наносекундах.
     *
     * @param results список результатов тестирования
     *
     * @see TestResult
     */
    public static void printResultsTable(List<TestResult> results) {
        System.out.println("\n" + "=".repeat(110));
        System.out.println("РЕЗУЛЬТАТЫ ТЕСТИРОВАНИЯ");
        System.out.println("=".repeat(110));
        System.out.printf("%-30s | %-15s | %-20s | %-20s | %-15s | %-10s%n",
                "Метод", "Операций", "ArrayList (нс)", "LinkedList (нс)", "Коэффициент", "Быстрее");
        System.out.println("-".repeat(110));

        for (TestResult result : results) {
            System.out.printf("%-30s | %-15d | %-20d | %-20d | %-15.1f | %-10s%n",
                    result.testName,
                    result.operations,
                    result.arrayListTime,
                    result.linkedListTime,
                    result.speedRatio,
                    result.faster);
        }
    }

    /**
     * Выводит таблицу с результатами тестирования в миллисекундах.
     *
     * @param results список результатов тестирования
     *
     * @see TestResult
     */
    public static void printResultsInMs(List<TestResult> results) {
        System.out.println("\n" + "=".repeat(110));
        System.out.println("РЕЗУЛЬТАТЫ В МИЛЛИСЕКУНДАХ");
        System.out.println("=".repeat(110));
        System.out.printf("%-30s | %-15s | %-20s | %-20s | %-15s | %-10s%n",
                "Метод", "Операций", "ArrayList (мс)", "LinkedList (мс)", "Коэффициент", "Быстрее");
        System.out.println("-".repeat(110));

        for (TestResult result : results) {
            double arrayListMs = result.arrayListTime / 1_000_000.0;
            double linkedListMs = result.linkedListTime / 1_000_000.0;

            System.out.printf("%-30s | %-15d | %-20.3f | %-20.3f | %-15.1f | %-10s%n",
                    result.testName,
                    result.operations,
                    arrayListMs,
                    linkedListMs,
                    result.speedRatio,
                    result.faster);
        }
    }

    /**
     * Анализирует результаты тестирования и выводит итоговое сравнение.
     * Подсчитывает, в скольких тестах каждая коллекция показала лучший результат,
     * и предоставляет рекомендации по выбору коллекции.
     *
     * @param results список результатов тестирования
     *
     * @see TestResult
     */
    public static void printPerformanceSummary(List<TestResult> results) {
        int arrayListWins = 0;
        int linkedListWins = 0;
        int ties = 0;

        for (TestResult result : results) {
            if (result.faster.equals("ArrayList")) {
                arrayListWins++;
            } else if (result.faster.equals("LinkedList")) {
                linkedListWins++;
            } else {
                ties++;
            }
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.println("ИТОГОВОЕ СРАВНЕНИЕ");
        System.out.println("=".repeat(70));
        System.out.println("Побед ArrayList: " + arrayListWins);
        System.out.println("Побед LinkedList: " + linkedListWins);
        System.out.println("Ничьих: " + ties);

        System.out.println("\nРЕКОМЕНДАЦИИ:");
        System.out.println("-".repeat(70));
        if (arrayListWins > linkedListWins) {
            System.out.println("★ В большинстве тестов ArrayList показал лучшую производительность");
            System.out.println("ArrayList лучше подходит для:");
            System.out.println("  • Частого обращения к элементам по индексу");
            System.out.println("  • Операций в конце списка (добавление/удаление)");
            System.out.println("  • Ситуаций, где важна экономия памяти");
        } else if (linkedListWins > arrayListWins) {
            System.out.println("★ В большинстве тестов LinkedList показал лучшую производительность");
            System.out.println("LinkedList лучше подходит для:");
            System.out.println("  • Частых операций в начале/середине списка");
            System.out.println("  • Реализации очередей (FIFO) и стеков (LIFO)");
            System.out.println("  • Ситуаций с частыми вставками/удалениями");
        } else {
            System.out.println("★ Производительность ArrayList и LinkedList сравнима");
            System.out.println("Выбор зависит от конкретных операций:");
            System.out.println("  • Для индексации - ArrayList");
            System.out.println("  • Для вставок/удалений - LinkedList");
        }

        System.out.println("\n" + "=".repeat(70));
    }
}