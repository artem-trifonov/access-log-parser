import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /* -- Старое задание --
        System.out.println("Введите текст и нажмите <Enter>: ");
        String text = new Scanner(System.in).nextLine();
        System.out.println("Длина текста: " + text.length());
        */
        int countCorretPath = 0;
        do {
            System.out.println("Введите корректный путь к файлу");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if (!fileExists || isDirectory) {
                System.out.println("Указанный файл не существует или указанный путь является путём к папке\n");
                continue;
            } else {
                System.out.println("Путь указан верно");
                countCorretPath++;
            }
            System.out.println("Это файл номер " + countCorretPath + "\n");

            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);
                String line;

                int count = 0;
                int googleBotCount = 0;
                int yandexBotCount = 0;
                Statistics statistic = new Statistics();

                while ((line = reader.readLine()) != null) {
                    int length = line.length();
                    count++;
                    if (length > 1024)
                        throw new LongLineException("Обнаружена строка длиной " + length + " Максимально допустимая длина - 1024 символа.");

                    LogEntry entry = new LogEntry(line);
                    statistic.addEntry(entry);

                    String[] userAgent = line.split("\"");
                    String[] userAgentParts = userAgent[userAgent.length - 1].split(";");

                    if (userAgentParts.length >= 2) {
                        String fragment = userAgentParts[1].replaceAll(" ", "");
                        String[] firstFragment = fragment.split("/");
                        if (Objects.equals(firstFragment[0], "Googlebot"))
                            googleBotCount++;
                        if (Objects.equals(firstFragment[0], "YandexBot"))
                            yandexBotCount++;
                    }
                }

                System.out.printf("Объём часового трафика = %.2f\n",statistic.getTrafficRate());

                System.out.printf("Доля запросов от Googlebot: %.2f%%\n",(double)googleBotCount/count*100);
                System.out.printf("Доля запросов от YandexBot: %.2f%%\n",(double)yandexBotCount/count*100);
                System.out.println("Общее количество строк в файле: " + count+"\n");
            } catch (LongLineException ex) {
                ex.printStackTrace();
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } while (true);
    }
}

