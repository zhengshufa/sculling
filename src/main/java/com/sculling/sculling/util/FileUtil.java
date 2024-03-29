//package com.sculling.sculling.util;
//
///**
// * Created by chentianhua on 2021/8/19.
// */
//
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.StopWatch;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//
///**
// * 文件操控工具类
// */
//public class FileUtil {
//    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
//    private static final int  BUFFER_SIZE = 2 * 1024;
//
//    //逗号
//    private static int comma = 0;
//    //    竖杆
//    private static int vertical = 1;
//    //    分隔符是tab
//    private static int tab = 2;
//
//    private static int txtType = 1;
//    private static int csvType = 0;
//    /**
//     * 获取文件类型 比如***.zip 返回为zip,没有zip前面那个点
//     * @param fileWholeName 文件全称 比如***.zip
//     * @return
//     */
//    public static String getFileType(String fileWholeName){
//        String[] split = fileWholeName.split("\\.");
//        return split[split.length - 1];
//    }
//
//    /**
//     * 获取文件名称,不包含文件类型,比如比如***.zip 返回为***
//     * @param fileWholeName
//     * @return
//     */
//    public static String getFileName(String fileWholeName){
//
//        if(fileWholeName.contains("__")){
//            int index = fileWholeName.lastIndexOf("__");
//            return fileWholeName.substring(0, index);
//        }else if(fileWholeName.contains(".csv")){
//            return StringUtils.substringBeforeLast(fileWholeName,".csv");
//        }
//        return fileWholeName;
//
//
//
//    }
//
//    /**
//     * 获取一个文件夹里的第一个文件的类型
//     * @param pathFile  文件夹
//     * @return 返回如"zip"
//     */
//    public static String getFileTypeFromFilePath(File pathFile){
//        File[] files = pathFile.listFiles();
//        File singleFile = files[0];
//        return getFileType(singleFile.getName());
//    }
//
//    /**
//     * 删除文件夹（强制删除）
//     *
//     * @param path
//     */
//    public static void deleteAllFilesOfDir(File path) {
//
//        if (null != path) {
//            if (!path.exists()){
//                return;
//            }
//
//            if (path.isFile()) {
//                boolean result = path.delete();
//                int tryCount = 0;
//                while (!result && tryCount++ < 10) {
//                    System.gc(); // 回收资源
//                    result = path.delete();
//                }
//            }
//            File[] files = path.listFiles();
//            if (null != files) {
//                for (int i = 0; i < files.length; i++) {
//                    File singleFile = files[i];
//                    String fileName = singleFile.getName();
//                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
//                    if(!suffix.equals("zip")){
//                        deleteAllFilesOfDir(singleFile);
//                    }
//
//                }
//            }
//            path.delete();
//        }
//    }
//
//
//
//
//    /**
//     * 把一个文件的内容写入到另外一个文件
//     * @param firstFile
//     * @param secondFile
//     * @throws Exception
//     */
//    public static void FileContentWriteToAnotherFile(File firstFile,File secondFile)throws IOException{
//            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(secondFile)));
//            BufferedReader bufferedReader = new BufferedReader(new FileReader(firstFile));
//            Stream<String> lines = bufferedReader.lines();
////            获取文件所有内容
//            List<String> contents = lines.collect(Collectors.toList());
//            //            该文件总行数
//            long fileCount = contents.size();
//            for(int i = 0; i < fileCount; i++){
//                bufferedWriter.write(contents.get(i));
//                bufferedWriter.newLine();
//            }
////            关流
//            bufferedWriter.flush();
//            bufferedWriter.close();
//    }
//
//
//    /**
//     * 压缩成ZIP 方法
//     * @param srcFiles 需要压缩的文件列表
//     * @param out           压缩文件输出流
//     * @throws RuntimeException 压缩失败会抛出运行时异常
//     */
//    public static void toZip(List<File> srcFiles , OutputStream out)throws Exception {
//        System.out.println("开始压缩");
//        long start = System.currentTimeMillis();
//        ZipOutputStream zos = null ;
//        try {
//            zos = new ZipOutputStream(out);
//            for (File srcFile : srcFiles) {
//                byte[] buf = new byte[BUFFER_SIZE];
//                zos.putNextEntry(new ZipEntry(srcFile.getName()));
//                int len;
//                FileInputStream in = new FileInputStream(srcFile);
//                while ((len = in.read(buf)) != -1){
//                    zos.write(buf, 0, len);
//                }
//                zos.closeEntry();
//                in.close();
//            }
//            long end = System.currentTimeMillis();
//            System.out.println("压缩完成，耗时：" + (end - start) +" ms");
//        } catch (Exception e) {
//            throw new RuntimeException("zip error from ZipUtils",e);
//        }finally{
//            if(zos != null){
//                try {
//                    zos.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//
//    /** 一次性压缩多个文件，文件存放至一个文件夹中*/
//    public static void ZipMultiFile(String filepath ,String zippath) {
//        try {
//            File file = new File(filepath);// 要被压缩的文件夹
//            File zipFile = new File(zippath);
//            InputStream input = null;
//            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
//            if(file.isDirectory()){
//                File[] files = file.listFiles();
//                for(int i = 0; i < files.length; ++i){
//                    input = new FileInputStream(files[i]);
//                    zipOut.putNextEntry(new ZipEntry(file.getName() + File.separator + files[i].getName()));
//                    int temp = 0;
//                    while((temp = input.read()) != -1){
//                        zipOut.write(temp);
//                    }
//                    input.close();
//                }
//            }
//            zipOut.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 指定文件夹下的所有txt文件都转换为csv文件,txt分隔符不是逗号都转为逗号
//     * @param txtFilesPathFile  文件夹
//     * @param seperateType
//     * @throws IOException
//     */
//
//    public static void txtConvertToCsv(File txtFilesPathFile,int seperateType)throws Exception{
//        File[] txtfiles = txtFilesPathFile.listFiles();
//        logger.info("txt转换为CSV");
//        for(File txtfile:txtfiles){
//           if (getFileType(txtfile.getName()).equals("txt")){
////               先创建文件
//               String newCsvFilePath = txtFilesPathFile.getPath()+File.separator+getFileName(txtfile.getName())+".csv";
//               File newCsvFile = new File(newCsvFilePath);
//               newCsvFile.createNewFile();
//               BufferedWriter newBufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newCsvFile)));
//
////读取文件内容,并转换成逗号后在写入
//               BufferedReader bufferedReader = new BufferedReader(new FileReader(txtfile));
//               Stream<String> lines = bufferedReader.lines();
//               List<String> contents = lines.collect(Collectors.toList());
//               long fileCount = contents.size();
//
//               for(int i = 0; i < fileCount; i++){
//                   String singlecontentString = contents.get(i);
//                   String newSingleContentString = singlecontentString;
//                   if(seperateType == vertical){
//                       newSingleContentString =  singlecontentString.replace("|",",");
//                   }
//                   newBufferedWriter.write(newSingleContentString);
//                   newBufferedWriter.newLine();
//               }
//
//               txtfile.delete();
////关流
//               newBufferedWriter.flush();
//               newBufferedWriter.close();
//           }
//        }
//    }
//
//    public static  File transferToFile(MultipartFile file) {
//        File toFile = null;
//        try{
//            if (file.equals("") || file.getSize() <= 0) {
//                file = null;
//            } else {
//                InputStream ins = null;
//                ins = file.getInputStream();
//                toFile = new File(file.getOriginalFilename());
//                inputStreamToFile(ins, toFile);
//                ins.close();
//            }
//
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//        return toFile;
//    }
//    //获取流文件
//    private static void inputStreamToFile(InputStream ins, File file) {
//        try {
//            OutputStream os = new FileOutputStream(file);
//            int bytesRead = 0;
//            byte[] buffer = new byte[8192];
//            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
//                os.write(buffer, 0, bytesRead);
//            }
//            os.close();
//            ins.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    /**
//     * 删除文件
//     */
//    public  boolean deleteFile(String pathname) {
//        boolean result = false;
//        File file = new File(pathname);
//        if (file.exists()) {
//            file.delete();
//            result = true;
//            System.out.println("文件已经被成功删除");
//        }
//        return result;
//    }
//    /***
//     * 删除文件夹
//     *
//     */
//    public static void delFolder(String folderPath) {
//        try {
//            delAllFile(folderPath); // 删除完里面所有内容
//            String filePath = folderPath;
//            filePath = filePath.toString();
//            File myFilePath = new File(filePath);
//            myFilePath.delete(); // 删除空文件夹
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /***
//     * 删除指定文件夹下所有文件
//     *
//     * @param path 文件夹完整绝对路径
//     * @return
//     */
//    public static boolean delAllFile(String path) {
//        boolean flag = false;
//        File file = new File(path);
//        if (!file.exists()) {
//            return flag;
//        }
//        if (!file.isDirectory()) {
//            return flag;
//        }
//        String[] tempList = file.list();
//        File temp = null;
//        for (int i = 0; i < tempList.length; i++) {
//            if (path.endsWith(File.separator)) {
//                temp = new File(path + tempList[i]);
//            } else {
//                temp = new File(path + File.separator + tempList[i]);
//            }
//            if (temp.isFile()) {
//                temp.delete();
//            }
//            if (temp.isDirectory()) {
//                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
//                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
//                flag = true;
//            }
//        }
//        return flag;
//    }
//
//    /**
//     * 拆分csv文件并返回文件夹路径,getFileType 0为csv,1为txt
//     *
//     * @param inputStream
//     * @param filename
//     * @param splitSize
//     * @return
//     */
//    public static String splitCsvFile(InputStream inputStream, String outerPath,String filename, int splitSize,int getFileType) {
////        为了计算任务执行时间
//        logger.info("开始拆分");
//        StopWatch stopWatch = new StopWatch("拆分任务耗时秒表工具");
//        stopWatch.start();
//        try {
//            InputStreamReader reader = new InputStreamReader(inputStream);
//            BufferedReader bufferedReader = new BufferedReader(reader);
//            Stream<String> lines = bufferedReader.lines();
////            获取文件所有内容
//            List<String> contents = lines.collect(Collectors.toList());
////            该文件总行数
//            long fileCount = contents.size();
////            拆分文件的个数
//            int splitNumber = (int) ((fileCount % splitSize == 0) ? (fileCount / splitSize) : (fileCount / splitSize + 1));
//            logger.info("csv文件总行数： {}行  拆分文件个数：{}个", fileCount, splitNumber);
//            //将创建的拆分文件写入流放入集合中
//            List<BufferedWriter> listWriters = new ArrayList<>();
//            String filePath = outerPath + filename;
//            //创建存放拆分文件的目录
//            File dir = new File(outerPath + filename);
//            //文件夹存在，可能里面有内容，删除所有内容
//            if (dir.exists()) {
//                delAllFile(dir.getAbsolutePath());
//            }
//            dir.mkdirs();
////            创建文件,目前文件都是空的
//            String fileType = "";
//            if(getFileType == txtType){
//                fileType="'.txt";
//            }else {
//                fileType = ".csv";
//            }
//            for (int i = 0; i < splitNumber; i++) {
//
//                String splitFilePath = outerPath + filename + File.separator + filename + i + fileType;
//                File splitFileName = new File(splitFilePath);
//                splitFileName.createNewFile();
//                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(splitFileName)));
//                listWriters.add(bufferedWriter);
//            }
////            为每个文件写入内容
//            for (int i = 0; i < fileCount; i++) {
////                第一行特殊处理,每个文件的行头都是一样,都是字段
//                if (i == 0) {
////                    每个文件都把头先填入
//                    for (int count = 0; count < splitNumber; count++) {
//                        listWriters.get(count).write(contents.get(i));
//                        listWriters.get(count).newLine();
//                    }
//                } else {
////                    其他行按照数据行数写入
//                    for(int j=0;j<splitNumber;j++){
//                        if(i>(splitSize*j)&&i<splitSize*(j+1)){
//                            listWriters.get(j).write(contents.get(i));
//                            listWriters.get(j).newLine();
//                        }
//                    }
////                    listWriters.get(i % splitNumber).write(contents.get(i));
////                    listWriters.get(i % splitNumber).newLine();
//                }
//            }
//            //关流
//            listWriters.forEach(it -> {
//                try {
//                    it.flush();
//                    it.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//        } catch (IOException e) {
//            logger.info("csv拆分文件失败  ：" + e);
//            e.printStackTrace();
//        }
//        stopWatch.stop();
//        logger.info("拆分结束,csv文件拆分共花费：  " + stopWatch.getTotalTimeMillis() + " ms"+"路径为:"+outerPath + filename);
//        return outerPath + filename + File.separator;
//    }
//
//
//    /**
//     * txt文件分解
//     * @param inputStream  输入流
//     * @param filename 文件名称
//     * @param splitSize  每个文件分解的大小
//     * @param seperateType    分隔符类型
//     * @param getFileType    最终文件类型
//     * @return
//     */
//    public static String splitTextFile(InputStream inputStream, String outerPath,String filename, int splitSize,int seperateType,int getFileType){
//        logger.info("txt开始拆分");
//        StopWatch stopWatch = new StopWatch("拆分任务耗时秒表工具");
//        stopWatch.start();
//        InputStreamReader reader = new InputStreamReader(inputStream);
//        BufferedReader bufferedReader = new BufferedReader(reader);
////        fileCodeString
//        Stream<String> lines = bufferedReader.lines();
////            获取文件所有内容
//        List<String> contents = lines.collect(Collectors.toList());
//        String contentString = contents.get(0);
//        List<String> contentsList = new ArrayList<>();
//        for(String retval: contentString.split("|")){
//            contentsList.add(retval);
//        }
////            该文件总行数
//        long fileCount = contents.size();
////            拆分文件的个数
//        int splitNumber = (int) ((fileCount % splitSize == 0) ? (fileCount / splitSize) : (fileCount / splitSize + 1));
//        logger.info("txt文件总行数： {}行  拆分文件个数：{}个", fileCount, splitNumber);
//        //将创建的拆分文件写入流放入集合中
//        List<BufferedWriter> listWriters = new ArrayList<>();
//        String filePath = outerPath + filename;
//        //创建存放拆分文件的目录
//        File dir = new File(outerPath + filename);
//        //文件夹存在，可能里面有内容，删除所有内容
//        if (dir.exists()) {
//            delAllFile(dir.getAbsolutePath());
//        }
//        dir.mkdirs();
////            创建文件,目前文件都是空的
//        try{
//            String fileType = "";
//            if(getFileType == txtType ){
//                fileType="'.txt";
//            }else if(getFileType == csvType ){
//                fileType = ".csv";
//            }
//            for (int i = 0; i < splitNumber; i++) {
//                String splitFilePath = outerPath + filename + File.separator + filename + i + fileType;
//                File splitFileName = new File(splitFilePath);
//                splitFileName.createNewFile();
//                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(splitFileName)));
//                listWriters.add(bufferedWriter);
//            }
//
//            //            为每个文件写入内容
//            for (int i = 0; i < fileCount; i++) {
//                String singlecontentString = contents.get(i);
//                String newSingleContentString = "";
////                Txt转csv
//                if(getFileType == csvType && seperateType == vertical){
////                    如果最终要的是CSV,当前确实txt,分隔符还是竖杆,就要把竖杆替换为逗号
//                    newSingleContentString =  singlecontentString.replace("|",",");
//                }else{
////                    其他情况直接转
//                    newSingleContentString = singlecontentString;
//                }
////                第一行特殊处理,每个文件的行头都是一样,都是字段
//                if (i == 0) {
////                    每个文件都把头先填入
//                    for (int count = 0; count < splitNumber; count++) {
//                        listWriters.get(count).write(newSingleContentString);
//                        listWriters.get(count).newLine();
//                    }
//                } else {
////                    其他行按照数据行数写入
//                    for(int j=0;j<splitNumber;j++){
//                        if(i>(splitSize*j)&&i<splitSize*(j+1)){
//                            listWriters.get(j).write(newSingleContentString);
//                            listWriters.get(j).newLine();
//                        }
//                    }
//                }
//            }
//
//            //关流
//            listWriters.forEach(it -> {
//                try {
//                    it.flush();
//                    it.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//
//        }catch (IOException e){
//            logger.info("txt拆分文件失败  ：" + e);
//            e.printStackTrace();
//        }
//        stopWatch.stop();
//        logger.info("成功拆分结束,txt文件拆分共花费：  " + stopWatch.getTotalTimeMillis() + " ms"+"路径为:"+outerPath + filename);
//        return outerPath + filename + File.separator;
//
//    }
//
//    /**
//     * 合并文件方法,支持csv和txt两种格式
//     * @param path   需要合并的文件夹路径
//     * @param filename  文件名称
//     * @param outerPath  合并后所在的位置
//     * @param outFileType   合并后的文件类型,CSV:0或者是TXT:1,
//     * @param seperateType   要是txt的,txt的分隔类型,comma:逗号:0;vertical:竖杆:1;tab:空格:2;
//     * @return
//     */
//    public static String mergeFile(File path, String filename,String outerPath,int outFileType,int seperateType ){
//        logger.info("文件开始合并");
//        StopWatch stopWatch = new StopWatch("合并任务耗时秒表工具");
//        stopWatch.start();
//        String fileType = "";
//        String  finalFilePath = "";
//        try{
////            如果是TXT就要转换成CSV
//            if (FileUtil.getFileTypeFromFilePath(path).equals("txt")){
//                FileUtil.txtConvertToCsv(path,seperateType);
//            }
//
////          目录底下所有需要合并的文件
//            File[] files = path.listFiles();
//            String singleFileCode = fileCodeString(files[0]);
//            List<List<String>> menuArrayList = new ArrayList<>();
//
//
//            for(int i=0;i<files.length;i++){
//                File singleFile = files[i];
//                if(!singleFile.isDirectory()){
//                    BufferedReader bufferedReader = new BufferedReader(new FileReader(singleFile));
//                    Stream<String> lines = bufferedReader.lines();
//                    List<String> contents = lines.collect(Collectors.toList());
//                    String headContentString = contents.get(0);
//                    String[] headArr = headContentString.split(",");
//                    List<String> headArrList = new ArrayList<>(Arrays.asList(headArr));
//                    menuArrayList.add(headArrList);
//                }
//
//            }
//            List<String> minimumMenu = getIntersectionFromArrays(menuArrayList);
////            最小交集
////获取第一个文件来获取文件类型,是csv还是txt
//            if (files.length != 0){
//                File singleFile = files[0];
//                fileType = FileUtil.getFileType(singleFile.getName());
//
//            }
//            String outFileTypeString = "";
//            if(outFileType == csvType){
//                outFileTypeString = "csv";
//            }else{
//                outFileTypeString = "txt";
//            }
//            String  splitFilePath = outerPath + filename + "."+outFileTypeString;
//            finalFilePath = splitFilePath;
////            先创建一个空的文本
////            File splitFileName = new File(splitFilePath);
//            FileOutputStream fos = new FileOutputStream(splitFilePath, true);
//            byte[] uft8bom={(byte)0xef,(byte)0xbb,(byte)0xbf};
//            fos.write(uft8bom);
//            OutputStreamWriter out = new OutputStreamWriter(fos,singleFileCode);
//            //FileWriter out = new FileWriter(splitFilePath);
//            String[] minimumheadArr = minimumMenu.toArray(new String[minimumMenu.size()]);
//            CSVPrinter outFileprinter = CSVFormat.DEFAULT
//                    .withHeader(minimumheadArr).print(out);
//            logger.info("创建一个空的文件,文件路径为:{}.{}",splitFilePath,fileType);
//            logger.info("合并的文件个数为:{}",files.length);
//            for(int j=0;j<files.length;j++){
//                File singleFile = files[j];
//                String currentSingleFileCode = fileCodeString(singleFile);
//                InputStreamReader isr = new InputStreamReader(new FileInputStream(singleFile), currentSingleFileCode);
//                //Reader in = new FileReader(singleFile);
//                BufferedReader read = new BufferedReader(isr);
////                Reader  in = new BufferedReader(new InputStreamReader(new FileInputStream(isr)));
//                Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(read);
////                record开始乱码
//                for(CSVRecord record :records){
//                    for(int i =0;i<minimumMenu.size();i++){
//                        String menuName = minimumMenu.get(i);
//                        String str = record.get(menuName);
//                        outFileprinter.print(record.get(menuName));
//                        if(i == (minimumMenu.size() -1)){
//                            outFileprinter.println();
//                        }
//                    }
////                        outFileprinter.printRecord(record);
//                }
//            }
//
//            //关流
//            outFileprinter.flush();
//            outFileprinter.close();
//
//        }catch (Exception e){
//            logger.info("合并失败  ：" + e);
//            e.printStackTrace();
//        }
//
//        stopWatch.stop();
//        return finalFilePath;
//    }
//
//    //    获取交集
//    public static List<String> getIntersectionFromArrays(List<List<String>> menuArrayList){
//        List<String> menuArray = new ArrayList<>();
//        for(int i=0;i<menuArrayList.size();i++){
//            if(i==0){
//                menuArray = cleanString(menuArrayList.get(i)) ;
//            }else{
//                List<String> singleMenuArray = cleanString(menuArrayList.get(i));
//                menuArray.retainAll(singleMenuArray);
//                logger.info("获取交集");
//            }
//        }
//        return menuArray;
//    }
//
//
//    /**
//     * 判断文件的编码格式
//     * @param fileName :file
//     * @return 文件编码格式
//     * @throws Exception
//     */
//    public static String fileCodeString(File fileName) throws Exception{
//        BufferedInputStream bin = new BufferedInputStream(
//                new FileInputStream(fileName));
//        int p = (bin.read() << 8) + bin.read();
//        String code = null;
//
//        switch (p) {
//            case 0xefbb:
//                code = "UTF-8";
//                break;
//            case 0xfffe:
//                code = "Unicode";
//                break;
//            case 0xfeff:
//                code = "UTF-16BE";
//                break;
//            default:
//                code = "GBK";
//        }
//        IOUtils.closeQuietly(bin);
//        return code;
//    }
//
//
//    public  static List<String> cleanString(List<String> needStringList) {
//        List<String> finalStringList = new ArrayList<>();
//        for (String singleString : needStringList) {
//            String replace = "";
//            if (singleString.startsWith("\uFEFF")) {
//                replace = singleString.replace("\uFEFF", "");
//            } else if (singleString.endsWith("\uFEFF")) {
//                replace = singleString.replace("\uFEFF", "");
//            }else{
//                replace = singleString;
//            }
//            finalStringList.add(replace);
//        }
//
//        return finalStringList;
//    }
//
//}
