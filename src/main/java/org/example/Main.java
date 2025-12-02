import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("СРАВНЕНИЕ ПРОИЗВОДИТЕЛЬНОСТИ ARRAYLIST И LINKEDLIST");
        System.out.println("=".repeat(70));

        // Создаем тестер
        ListPerformanceTester tester = new ListPerformanceTester();

        // Запускаем тесты с разными размерами
        int[] testSizes = {1000, 5000, 10000, 20000};

        for (int size : testSizes) {
            System.out.println("\n\n" + "★".repeat(30));
            System.out.println("ТЕСТИРОВАНИЕ ДЛЯ " + size + " ОПЕРАЦИЙ");
            System.out.println("★".repeat(30));

            // Запускаем тесты
            List<ListPerformanceTester.TestResult> results = tester.runPerformanceTests(size);

            // Выводим результаты
            System.out.println("\nРезультаты в наносекундах:");
            ListPerformanceTester.printResultsTable(results);

            System.out.println("\nРезультаты в миллисекундах:");
            ListPerformanceTester.printResultsInMs(results);

            // Выводим краткое сравнение
            ListPerformanceTester.printPerformanceSummary(results);

            // Добавляем задержку между тестами
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Дополнительный тест с выводом детальной информации
        System.out.println("\n\n" + "★".repeat(40));
        System.out.println("ДЕТАЛЬНЫЙ АНАЛИЗ ПРОИЗВОДИТЕЛЬНОСТИ");
        System.out.println("★".repeat(40));

        runDetailedAnalysis(tester);
    }

    private static void runDetailedAnalysis(ListPerformanceTester tester) {
        int size = 10000;
        List<ListPerformanceTester.TestResult> results = tester.runPerformanceTests(size);

        System.out.println("\nДетальный анализ для " + size + " операций:");
        System.out.println("-".repeat(80));

        for (ListPerformanceTester.TestResult result : results) {
            System.out.printf("%n%s:%n", result.testName);
            System.out.printf("  ArrayList:  %d нс (%.3f мс)%n",
                    result.arrayListTime, result.arrayListTime / 1_000_000.0);
            System.out.printf("  LinkedList: %d нс (%.3f мс)%n",
                    result.linkedListTime, result.linkedListTime / 1_000_000.0);

            if (result.speedRatio > 1.5) {
                System.out.printf("  ★ %s быстрее в %.1f раз%n",
                        result.faster, result.speedRatio);
            } else if (result.speedRatio > 1.1) {
                System.out.printf("  %s немного быстрее (в %.1f раз)%n",
                        result.faster, result.speedRatio);
            } else {
                System.out.println("  Разница незначительная");
            }
        }

        // Выводим итоговые рекомендации
        System.out.println("\n" + "=".repeat(80));
        System.out.println("ИТОГОВЫЕ ВЫВОДЫ:");
        System.out.println("=".repeat(80));
        System.out.println("""
            1. ArrayList обычно быстрее для:
               - Доступа по индексу (get)
               - Добавления в конец (add)
               - Удаления из конца (remove)
               
            2. LinkedList обычно быстрее для:
               - Добавления в начало (addFirst)
               - Удаления из начала (removeFirst)
               - Вставки в середину (при больших размерах)
               
            3. Память:
               - ArrayList использует меньше памяти (нет ссылок на следующий/предыдущий элемент)
               - LinkedList имеет накладные расходы на объекты Node
               
            4. Практические рекомендации:
               - Для операций чтения (get, search) используйте ArrayList
               - Для операций записи (add/remove) в начале используйте LinkedList
               - Для смешанных операций выбирайте исходя из преобладающих операций
            """);
    }
}