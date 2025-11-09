import java.io.File;
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
        } while (true);
    }
}

