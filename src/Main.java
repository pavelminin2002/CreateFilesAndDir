import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
        path = path.replaceAll("src//main", "res");
        makeDirs(namesDir, 6, namesDir.size(), path, sb);
        path = path.replaceAll("res", "temp");
        makeFiles(nameFiles, 2, nameFiles.size(), path, sb);

        path = path.replaceAll("temp", "savegames");
        List<File> files = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int health = (int) (Math.random() * 101);
            int weapons = (int) (Math.random() * 101);
            int lvl = (int) (Math.random() * 101);
            double distance = Math.random() * 1001;

            File file = new File(path, "SaveFile" + (i + 1) + ".dat");
            files.add(file);
            try {
                if (file.createNewFile()) {
                    sb.append("Файл " + file.getName() + " для сохранения успешно создан\n");
                } else {
                    sb.append("Файл " + file.getName() + " для сохранения не создан\n");
                }
            } catch (IOException e) {
                sb.append("Файл " + file.getName() + " для сохранения не доступен\n");
                e.printStackTrace();
            }
            saveGame(new GameProgress(health, weapons, lvl, distance), file, sb);
        }

        zipFiles(path, files, sb);
        files.forEach(File::delete);

        path = path.replaceAll("savegames", "temp");
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

    public static void saveGame(GameProgress gameProgress, File file, StringBuilder sb) {

        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            oos.flush();
            sb.append("Файл " + file.getName() + " для сохранения удалось перезаписать\n");
        } catch (IOException e) {
            sb.append("Файл " + file.getName() + " для сохранения не удалось перезаписать\n");
            e.printStackTrace();
        }
    }

    public static void zipFiles(String path, List<File> files, StringBuilder sb) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(path + "//" + "ZipSave.zip"))) {
            for (File file : files) {
                ZipEntry ze = new ZipEntry(file.getName());
                zos.putNextEntry(ze);
                try (FileInputStream in = new FileInputStream(file)) {
                    byte[] b = new byte[in.available()];
                    in.read(b);

                    zos.write(b);
                }
                zos.closeEntry();
                sb.append("Файл " + file.getName() + " был записан в архив\n");
            }
        } catch (IOException e) {
            sb.append("Ошибка при записи файлов\n");
            e.printStackTrace();
        }
    }

}
