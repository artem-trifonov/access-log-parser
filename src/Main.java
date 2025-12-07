import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
                int maxL = 0;
                int minL = Integer.MAX_VALUE;

                while ((line = reader.readLine()) != null) {
                    int length = line.length();
                    count++;
                    maxL = Math.max(maxL, length);
                    minL = Math.min(minL, length);
                    if (length > 1024)
                        throw new LongLineException("Обнаружена строка длиной " + length + " Максимально допустимая длина - 1024 символа.");
                }

                System.out.println("Общее количество строк в файле: " + count);
                System.out.println("Максимальная длина строки в файле: " + maxL);
                System.out.println("Минимальная длина строки в файле: " + minL + "\n");
            } catch (LongLineException ex) {
                ex.printStackTrace();
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } while (true);
    }
}

