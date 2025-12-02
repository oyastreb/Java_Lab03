package org.example;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class AdvancedListPerformanceTest {
    private static final int[] TEST_SIZES = {1000, 5000, 10000};
    private static final Random random = new Random(42); // Фиксированный seed для воспроизводимости

    public static void main(String[] args) {
        System.out.println("СРАВНИТЕЛЬНЫЙ ТЕСТ ПРОИЗВОДИТЕЛЬНОСТИ");
        System.out.println("ArrayList vs LinkedList");
        System.out.println("=".repeat(80));

        for (int size : TEST_SIZES) {
            System.out.println("\nТестирование для " + size + " операций:");
            System.out.println("-".repeat(80));

            Map<String, Long> arrayListResults = testCollection(new ArrayList<>(), size);
            Map<String, Long> linkedListResults = testCollection(new LinkedList<>(), size);

            printComparisonTable(arrayListResults, linkedListResults, size);
        }
    }

    private static Map<String, Long> testCollection(List<Integer> list, int operations) {
        Map<String, Long> results = new LinkedHashMap<>();

        // Заполняем список начальными данными
        for (int i = 0; i < operations; i++) {
            list.add(i);
        }

        // 1. Тест добавления в конец
        long startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            list.add(i);
        }
        long endTime = System.nanoTime();
        results.put("add (в конец)", endTime - startTime);

        // 2. Тест добавления в начало
        startTime = System.nanoTime();
        for (int i = 0; i < operations / 10; i++) {
            list.add(0, i);
        }
        endTime = System.nanoTime();
        results.put("add (в начало)", endTime - startTime);

        // 3. Тест получения по индексу (случайный доступ)
        startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            list.get(random.nextInt(list.size()));
        }
        endTime = System.nanoTime();
        results.put("get (случайный)", endTime - startTime);

        // 4. Тест получения по индексу (последовательный доступ)
        startTime = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            list.get(i % list.size());
        }
        endTime = System.nanoTime();
        results.put("get (последовательный)", endTime - startTime);

        // 5. Тест удаления из начала
        startTime = System.nanoTime();
        for (int i = 0; i < operations / 10; i++) {
            if (!list.isEmpty()) {
                list.remove(0);
            }
        }
        endTime = System.nanoTime();
        results.put("delete (из начала)", endTime - startTime);

        // 6. Тест удаления из конца
        startTime = System.nanoTime();
        for (int i = 0; i < operations / 10; i++) {
            if (!list.isEmpty()) {
                list.remove(list.size() - 1);
            }
        }
        endTime = System.nanoTime();
        results.put("delete (из конца)", endTime - startTime);

        // 7. Тест итерации по всем элементам
        startTime = System.nanoTime();
        for (Integer value : list) {
            // Просто итерация
            int temp = value;
        }
        endTime = System.nanoTime();
        results.put("iterate", endTime - startTime);

        return results;
    }

    private static void printComparisonTable(Map<String, Long> arrayListResults,
                                             Map<String, Long> linkedListResults,
                                             int operations) {
        System.out.printf("%-25s | %-15s | %-15s | %-15s%n",
                "Операция", "ArrayList (нс)", "LinkedList (нс)", "Быстрее");
        System.out.println("-".repeat(80));

        for (String operation : arrayListResults.keySet()) {
            long arrayListTime = arrayListResults.get(operation);
            long linkedListTime = linkedListResults.get(operation);

            String faster = arrayListTime < linkedListTime ? "ArrayList" : "LinkedList";
            if (Math.abs(arrayListTime - linkedListTime) < 1000) {
                faster = "≈ одинаково";
            }

            System.out.printf("%-25s | %15d | %15d | %-15s%n",
                    operation, arrayListTime, linkedListTime, faster);
        }

        // Вывод в миллисекундах для удобства чтения
        System.out.println("\nВ миллисекундах:");
        System.out.printf("%-25s | %-15s | %-15s | %-15s%n",
                "Операция", "ArrayList (мс)", "LinkedList (мс)", "Быстрее");
        System.out.println("-".repeat(80));

        for (String operation : arrayListResults.keySet()) {
            double arrayListMs = arrayListResults.get(operation) / 1_000_000.0;
            double linkedListMs = linkedListResults.get(operation) / 1_000_000.0;

            String faster = arrayListMs < linkedListMs ? "ArrayList" : "LinkedList";
            if (Math.abs(arrayListMs - linkedListMs) < 0.001) {
                faster = "≈ одинаково";
            }

            System.out.printf("%-25s | %15.3f | %15.3f | %-15s%n",
                    operation, arrayListMs, linkedListMs, faster);
        }
    }
}