package com.database.eventmania.backend;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class Utils {
    // copy a multipart file to a location
    public static void copyFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(convFile);
            os = new FileOutputStream("C:\\Users\\Furkan\\Desktop\\eventmania\\src\\main\\resources\\static\\img\\" + file.getOriginalFilename());
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buf)) > 0) {
                os.write(buf, 0, bytesRead);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}
