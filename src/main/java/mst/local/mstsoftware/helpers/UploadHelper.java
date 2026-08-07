package mst.local.mstsoftware.helpers;

public class UploadHelper {
    private static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
    }
}
