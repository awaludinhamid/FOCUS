/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Utility yg berhubungan dengan modifikasi file
 * @created Dec 10, 2012
 * @author awal
 */
public class FileUtil {

    /**
     * Delete file/directory (directory will be deleted recursively)
     * @param path File/directory path
     */
    public static void delete(String path) {
        File file = new File(path);
        if(file.isDirectory()){
            if(file.list().length == 0) {
                file.delete();
            } else {
                String[] files = file.list();
                for(String tempFile : files) {
                    delete(file.getAbsolutePath() + File.separatorChar + tempFile);
                }
                if(file.list().length == 0) {
                    file.delete();
                }
            }
        } else {
            file.delete();
        }
    }

    /**
     * Create new directory
     * @param path Path to make a directory
     */
    public static void createDir(String path) {
        File file = new File(path);
        file.mkdir();
    }

    /**
     * Write content to a file
     * @param path File path
     * @param content File content
     * @throws IOException see Javadoc
     */
    public static void writeFile(String path, String content) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        // aware "null" string instead of null object
        fileWriter.write(content.replace("\"null\"", "null"));
        fileWriter.close();
    }

    public static void moveFile(String oldPath, String newPath) {
        File file = new File(oldPath);
        file.renameTo(new File(newPath));
    }

    /**
     * Zip the list of file/directory
     * @param fileName, String[] list file/directory name
     * @param zipFile, String zip file name
     * @throws IOException
     */
    public static void execZip(String[] fileNames, String zipName) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipName)));
        // path to cut off file path before zip file
        String path = (new File(zipName)).getParent();
        for(String fileName : fileNames) {
            File file = new File(fileName);
            if(file.isDirectory()) {
                zipFolder(file, path, zos);
            } else {
                zipFile(file, path, zos);
            }
        }
        zos.close();
    }

    /*
     * Zip folder recursively
     * @param file File/folder to zip recursively
     * @param path Path to cut off before zip file
     * @param zos Zip file
     * @throws IOException see Javadoc
     */
    private static void zipFolder(File file, String path, ZipOutputStream zos) throws IOException {
        if(file.isDirectory()) {
            String[] files = file.list();
            for(String tempFile : files) {
                zipFolder(new File(file.getAbsolutePath() + File.separatorChar + tempFile), path, zos);
            }
        } else {
            zipFile(file, path, zos);
        }
    }

    /*
     * Zip file
     * @param file File to zip
     * @param path Path to cut off before zip file
     * @param zos Zip file
     * @throws IOException see Javadoc
     */
    private static void zipFile(File file, String path, ZipOutputStream zos) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        zos.putNextEntry(new ZipEntry(file.getAbsolutePath().substring(path.length() + 1)));
        byte[] data = new byte[1024];
        int count;
        while((count = in.read(data)) != -1) {
            zos.write(data, 0, count);
        }
        in.close();
        zos.closeEntry();
    }

}
