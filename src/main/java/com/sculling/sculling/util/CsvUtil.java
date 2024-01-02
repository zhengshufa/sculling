//package com.sculling.sculling.util;
//
//
//import cn.hutool.core.io.BOMInputStream;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
///**
// * Created by chentianhua on 2021/9/3.
// */
//public class CsvUtil {
//
//    /**
//     * 获取CSV文件的list
//     * @param csvFile
//     * @return
//     */
// public static List<String>  getListFromCsv(File csvFile){
//        List<String> csvContent = new ArrayList<>();
//        try{
//            InputStreamReader reader = new InputStreamReader(new BOMInputStream(new FileInputStream(csvFile)), "UTF-8");
//            BufferedReader bufferedReader = new BufferedReader(reader);
//            Stream<String> lines = bufferedReader.lines();
//            csvContent = lines.collect(Collectors.toList());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//         return csvContent;
//
//    }
//
//    /**
//     * 获取CSV文件的list
//     * @param csvFile
//     * @return
//     */
//    public static List<String>  getListFromCsvGBK(File csvFile){
//        List<String> csvContent = new ArrayList<>();
//        try{
//            InputStreamReader reader = new InputStreamReader(new BOMInputStream(new FileInputStream(csvFile)), "GBK");
//            BufferedReader bufferedReader = new BufferedReader(reader);
//            Stream<String> lines = bufferedReader.lines();
//            csvContent = lines.collect(Collectors.toList());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return csvContent;
//
//    }
//
//    /**
//     * 获取CSVRecord
//     * @param csvFile
//     * @return
//     */
//    public static List<CSVRecord>  getRecordListFromCsv(File csvFile){
//        List<CSVRecord> csvContent = new ArrayList<>();
//        try{
//            InputStreamReader reader = new InputStreamReader(new FileInputStream(csvFile), "utf-8");
//            BufferedReader bufferedReader = new BufferedReader(reader);
//            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(bufferedReader);
//            for(CSVRecord record :records){
//                csvContent.add(record);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return csvContent;
//
//    }
//
//    /**
//     * CSV文件生成方法
//     * @param head 文件头
//     * @param dataList 数据列表
//     * @param outPutPath 文件输出路径
//     * @param filename 文件名
//     * @return
//     */
//    public static File createCSVFile(List<Object> head, List<List<Object>> dataList,String outPutPath, String filename) {
//
//        File csvFile = null;
//        BufferedWriter csvWtriter = null;
//        try {
//            csvFile = new File(outPutPath + File.separator + filename);
//            File parent = csvFile.getParentFile();
//            if (parent != null && !parent.exists()) {
//                parent.mkdirs();
//            }
//            csvFile.createNewFile();
//
//            // GB2312使正确读取分隔符","
//            csvWtriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
//                    csvFile), "UTF-8"), 1024);
//            // 写入文件头部
//            writeRow(head, csvWtriter);
//
//            // 写入文件内容
//            for (List<Object> row : dataList) {
//                writeRow(row, csvWtriter);
//            }
//            csvWtriter.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                csvWtriter.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return csvFile;
//    }
//
//    /**
//     * 写一行数据方法
//     * @param row
//     * @param csvWriter
//     * @throws IOException
//     */
//    private static void writeRow(List<Object> row, BufferedWriter csvWriter) throws IOException {
//        // 写入文件头部
//        for (Object data : row) {
//            StringBuffer sb = new StringBuffer();
//            String rowStr = sb.append(data).append("|").toString();
//            csvWriter.write(rowStr);
//        }
//        csvWriter.newLine();
//    }
//
//}
