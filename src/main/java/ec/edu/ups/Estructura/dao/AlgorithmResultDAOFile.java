package ec.edu.ups.Estructura.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ec.edu.ups.Estructura.models.AlgorithmResult;

public class AlgorithmResultDAOFile implements AlgorithmResultDAO {
        private final File file;

    public AlgorithmResultDAOFile(String paramString) {
        this.file = new File(paramString);
    }

    public void save(AlgorithmResult paramAlgorithmResult) {
        List<AlgorithmResult> list = findAll();
        boolean bool = false;
        for (byte b = 0; b < list.size(); b++) {
            if (((AlgorithmResult)list.get(b)).getAlgorithmName().equalsIgnoreCase(paramAlgorithmResult.getAlgorithmName())) {
                list.set(b, paramAlgorithmResult);
                bool = true;
                break;
            }
        }
        if (!bool)
            list.add(paramAlgorithmResult);
        try {
            FileWriter fileWriter = new FileWriter(this.file, false);
            try {
                for (AlgorithmResult algorithmResult : list)
                    fileWriter.write(algorithmResult.toString() + "\n");
                fileWriter.close();
            } catch (Throwable throwable) {
                try {
                    fileWriter.close();
                } catch (Throwable throwable1) {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        } catch (IOException iOException) {
            System.err.println("Error writing result to file: " + iOException.getMessage());
        }
    }

    public List<AlgorithmResult> findAll() {
        ArrayList<AlgorithmResult> arrayList = new ArrayList();
        if (!this.file.exists())
            return arrayList;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));
            try {
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    String[] arrayOfString = str.split(",");
                    if (arrayOfString.length == 3) {
                        String str1 = arrayOfString[0];
                        int i = Integer.parseInt(arrayOfString[1]);
                        long l = Long.parseLong(arrayOfString[2]);
                        arrayList.add(new AlgorithmResult(str1, i, l));
                    }
                }
                bufferedReader.close();
            } catch (Throwable throwable) {
                try {
                    bufferedReader.close();
                } catch (Throwable throwable1) {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        } catch (IOException|NumberFormatException iOException) {
            System.err.println("Error reading results from file: " + iOException.getMessage());
        }
        return arrayList;
    }

    public void clear() {
        try {
            FileWriter fileWriter = new FileWriter(this.file, false);
            fileWriter.close();
        } catch (IOException iOException) {
            System.err.println("Error al limpiar el archivo: " + iOException.getMessage());
        }
    }

}
