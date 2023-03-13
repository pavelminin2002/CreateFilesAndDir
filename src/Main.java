import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> namesDir = Arrays.asList("src", "res", "savegames", "temp", "main", "test", "drawables", "vectors", "icons");
        List<String> nameFiles = Arrays.asList("Main.java", "Utils.java", "temp.txt");
        String path = "C://Games";
        StringBuilder sb = new StringBuilder();
        makeDirs(namesDir, 0, 4, path, sb);
        path = path + "//src";
        makeDirs(namesDir, 4, 6, path, sb);
        path = path + "//main";
        makeFiles(nameFiles, 0, 2, path, sb);
        path = path.replaceAll("//src//main", "//res");
        makeDirs(namesDir, 6, namesDir.size(), path, sb);
        path = path.replaceAll("//res", "//temp");
        makeFiles(nameFiles, 2, nameFiles.size(), path, sb);
        try (FileWriter fw = new FileWriter(new File(path, "temp.txt"))) {
            fw.write(sb.toString());
            fw.write("Файл temp.txt успешно перезаписан");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void makeFiles(List<String> namesFiles, int start, int end, String path, StringBuilder sb) {
        if (namesFiles.isEmpty())
            throw new RuntimeException("Передан пустой список");

        if (start < 0 || end > namesFiles.size())
            throw new RuntimeException("Неккоректный индекс");

        for (int i = start; i < end; i++) {
            File file = new File(path, namesFiles.get(i));
            try {
                if (file.createNewFile()) {
                    sb.append("Файл " + namesFiles.get(i) + " успешно создан \n");
                } else {
                    sb.append("Файл " + namesFiles.get(i) + " не создан \n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void makeDirs(List<String> namesDir, int start, int end, String path, StringBuilder sb) {
        if (namesDir.isEmpty())
            throw new RuntimeException("Передан пустой список");

        if (start < 0 || end > namesDir.size())
            throw new RuntimeException("Неккоректный индекс");

        for (int i = start; i < end; i++) {
            File dir = new File(path + "//" + namesDir.get(i));

            if (dir.mkdir()) {
                sb.append("Директория " + namesDir.get(i) + " успешно создана \n");
            } else {
                sb.append("Директория " + namesDir.get(i) + " не создана \n");
            }
        }
    }

}
