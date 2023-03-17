import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) {
        List<String> nameDirs = Arrays.asList("C:\\Games\\src", "C:\\Games\\res",
                "C:\\Games\\savegames", "C:\\Games\\temp", "C:\\Games\\src\\main",
                "C:\\Games\\src\\test");
        nameDirs.forEach(Main::createDirecory);

        List<String> files = Arrays.asList("Main.java", "Utils.java", "temp.txt");
        createFile(nameDirs.get(4), files.get(0));
        createFile(nameDirs.get(4), files.get(1));
        createFile(nameDirs.get(3), files.get(2));

        GameProgress gp1 = new GameProgress(60, 23, 10, 23.7);
        GameProgress gp2 = new GameProgress(75, 31, 80, 245.9);
        GameProgress gp3 = new GameProgress(95, 6, 2, 20.0);

        List<File> saveFiles = Arrays.asList(
                new File("C:\\Games\\savegames\\Save1.dat"),
                new File("C:\\Games\\savegames\\Save2.dat"),
                new File("C:\\Games\\savegames\\Save3.dat")
        );
        saveFiles.forEach(Main::createFile);

        saveGame(gp1, saveFiles.get(0));
        saveGame(gp2, saveFiles.get(1));
        saveGame(gp3, saveFiles.get(2));

        zipFiles(saveFiles, "C:\\Games\\savegames\\ZipSave.zip");
        saveFiles.forEach(File::delete);

        try (FileWriter fw = new FileWriter("C:\\Games\\temp\\temp.txt")) {
            fw.write(sb.toString());
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void createDirecory(String pathDir) {
        File dir = new File(pathDir);
        sb.append("Директория " + dir.getName());
        if (dir.mkdir()) {
            sb.append(" успешно создана");
        } else {
            sb.append(" не создана");
        }
        sb.append("\n");
    }

    public static void createFile(String path, String name) {
        File file = new File(path, name);
        sb.append("Файл " + file.getName() + " в директории " + file.getParent());
        try {
            if (file.createNewFile()) {
                sb.append(" успешно создан");
            } else {
                sb.append(" не создан");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sb.append("\n");
    }

    public static void createFile(File file) {
        createFile(file.getParent(), file.getName());
    }

    public static void saveGame(GameProgress gp, File file) {
        sb.append("Файл сохранения " + file.getName() + " в директории " + file.getParent());
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gp);
            oos.flush();
            sb.append(" перезаписан");
        } catch (IOException e) {
            sb.append(" не удалось перезаписать");
            e.printStackTrace();
        }
        sb.append("\n");
    }

    public static void zipFiles(List<File> files, String zipFile) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (File file : files) {
                sb.append("Файл " + file.getName() + " в директории " + file.getParent());
                ZipEntry ze = new ZipEntry(file.getName());
                zos.putNextEntry(ze);
                try (FileInputStream in = new FileInputStream(file)) {
                    byte[] b = new byte[in.available()];
                    in.read(b);

                    zos.write(b);
                }
                zos.closeEntry();
                sb.append(" записан в zip-архив" + "\n");
            }
        } catch (IOException e) {
            sb.append("При записи в zip архив что-то пошло не так" + "\n");
            e.printStackTrace();
        }
    }

}
