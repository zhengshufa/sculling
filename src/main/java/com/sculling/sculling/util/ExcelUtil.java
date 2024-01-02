package com.sculling.sculling.util;

import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.awt.Font;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

/**
 * POI实现excel文件读写(导入/导出)操作工具类
 */
public  class ExcelUtil {

    private static Logger mLogger = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 对外提供读取excel的方法， 当且仅当只有一个sheet， 默认从第一个sheet读取数据
     * @param file
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(File file) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        if (file.exists() && file.isFile()) {
            Workbook wb = getWorkbook(file);
            if (wb != null) {
                Sheet sheet = getSheet(wb, 0);
                list = getSheetData(wb, sheet);
            }
        }
        return list;
    }

    /**
     * 对外提供读取excel的方法， 根据sheet下标索引读取sheet数据
     * @param file
     * @param sheetIndex
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(File file, int sheetIndex) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        if (file.exists() && file.isFile()) {
            Workbook wb = getWorkbook(file);
            if (wb != null) {
                Sheet sheet = getSheet(wb, sheetIndex);
                list = getSheetData(wb, sheet);
            }
        }
        return list;
    }

    /**
     * 对外提供读取excel的方法， 根据sheet下标索引读取sheet对象， 并指定行列区间获取数据[startRowIndex, endRowIndex), [startColumnIndex, endColumnIndex)
     * @param file
     * @param sheetIndex
     * @param startRowIndex
     * @param endRowIndex
     * @param startColumnIndex
     * @param endColumnIndex
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(File file, int sheetIndex, int startRowIndex, int endRowIndex,
                                               int startColumnIndex, int endColumnIndex) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        Workbook wb = getWorkbook(file);
        if (wb != null) {
            Sheet sheet = getSheet(wb, sheetIndex);
            list = getSheetData(wb, sheet, startRowIndex, endRowIndex, startColumnIndex, endColumnIndex);
        }
        return list;
    }

    /**
     * 对外提供读取excel的方法， 根据sheet名称读取sheet数据
     * @param file
     * @param sheetName
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(File file, String sheetName) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        Workbook wb = getWorkbook(file);
        if (wb != null) {
            Sheet sheet = getSheet(wb, sheetName);
            list = getSheetData(wb, sheet);
        }
        return list;
    }

    /**
     * 对外提供读取excel的方法， 根据sheet名称读取sheet对象， 并指定行列区间获取数据[startRowIndex, endRowIndex), [startColumnIndex, endColumnIndex)
     * @param file
     * @param sheetName
     * @param startRowIndex
     * @param endRowIndex
     * @param startColumnIndex
     * @param endColumnIndex
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(File file, String sheetName, int startRowIndex, int endRowIndex,
                                               int startColumnIndex, int endColumnIndex) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        Workbook wb = getWorkbook(file);
        if (wb != null) {
            Sheet sheet = getSheet(wb, sheetName);
            list = getSheetData(wb, sheet, startRowIndex, endRowIndex, startColumnIndex, endColumnIndex);
        }
        return list;
    }

    /**
     * 读取excel的正文内容
     * @param file
     * @param sheetIndex sheet的下标索引值
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcelBody(File file, int sheetIndex) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        if (file.exists() && file.isFile()) {
            Workbook wb = getWorkbook(file);
            if (wb != null) {
                Sheet sheet = getSheet(wb, sheetIndex);
                list = getSheetBodyData(wb, sheet);
            }
        }
        return list;
    }

    /**
     * 读取excel的正文内容
     * @param file
     * @param sheetName sheet的名称
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcelBody(File file, String sheetName) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        if (file.exists() && file.isFile()) {
            Workbook wb = getWorkbook(file);
            if (wb != null) {
                Sheet sheet = getSheet(wb, sheetName);
                list = getSheetBodyData(wb, sheet);
            }
        }
        return list;
    }

    /**
     * @date: 2019年4月11日
     * @author: wangchaoyong
     * @title: readExcelBody
     * @param inputStream
     * @param sheetIndex
     * @param fileName
     * @return
     * @throws IOException
     * @exception:
     * @version: 1.0
     * @description: 读取excel的正文内容
     * update_version: update_date: update_author: update_note:
     */
    public static List<List<Object>> readExcelBody(InputStream inputStream, int sheetIndex, String fileName) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        Workbook wb = getWorkbook(inputStream, fileName);
        if (wb != null) {
            Sheet sheet = getSheet(wb, sheetIndex);
            list = getSheetBodyData(wb, sheet);
        }
        return list;
    }

    /**
     * @date: 2019年4月11日
     * @author: wangchaoyong
     * @title: readExcelBody
     * @param inputStream
     * @param sheetIndex
     * @param fileName
     * @return
     * @throws IOException
     * @exception:
     * @version: 1.0
     * @description: 读取excel的正文内容
     * update_version: update_date: update_author: update_note:
     */
    public static List<List<Object>> readExcelBody(InputStream inputStream, int sheetIndex,int startRow,int headRow, String fileName) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        Workbook wb = getWorkbook(inputStream, fileName);
        if (wb != null) {
            Sheet sheet = getSheet(wb, sheetIndex);
            list = getSheetBodyData(wb, sheet,startRow,headRow);
        }
        return list;
    }

    /**
     * 对外提供读取excel的方法， 当且仅当只有一个sheet， 默认从第一个sheet读取数据
     * @param filePath
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(String filePath) throws IOException {
        File file = new File(filePath);
        return readExcel(file);
    }

    /**
     * 对外提供读取excel的方法， 根据sheet下标索引读取sheet数据
     * @param filePath
     * @param sheetIndex
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(String filePath, int sheetIndex) throws IOException {
        File file = new File(filePath);
        return readExcel(file, sheetIndex);
    }

    /**
     * 对外提供读取excel的方法， 根据sheet下标索引读取sheet对象， 并指定行列区间获取数据[startRowIndex, endRowIndex), [startColumnIndex, endColumnIndex)
     * @param filePath
     * @param sheetIndex
     * @param startRowIndex
     * @param endRowIndex
     * @param startColumnIndex
     * @param endColumnIndex
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(String filePath, int sheetIndex, int startRowIndex, int endRowIndex,
                                               int startColumnIndex, int endColumnIndex) throws IOException {
        File file = new File(filePath);
        return readExcel(file, sheetIndex, startRowIndex, endRowIndex, startColumnIndex, endColumnIndex);
    }

    /**
     * 对外提供读取excel的方法， 根据sheet名称读取sheet数据
     * @param filePath
     * @param sheetName
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(String filePath, String sheetName) throws IOException {
        File file = new File(filePath);
        return readExcel(file, sheetName);
    }

    /**
     * 对外提供读取excel的方法， 根据sheet名称读取sheet对象， 并指定行列区间获取数据[startRowIndex, endRowIndex), [startColumnIndex, endColumnIndex)
     * @param filePath
     * @param sheetName
     * @param startRowIndex
     * @param endRowIndex
     * @param startColumnIndex
     * @param endColumnIndex
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(String filePath, String sheetName, int startRowIndex, int endRowIndex,
                                               int startColumnIndex, int endColumnIndex) throws IOException {
        File file = new File(filePath);
        return readExcel(file, sheetName, startRowIndex, endRowIndex, startColumnIndex, endColumnIndex);
    }

    /**
     * 读取excel的正文内容
     * @param filePath
     * @param sheetIndex sheet的下标索引值
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcelBody(String filePath, int sheetIndex) throws IOException {
        File file = new File(filePath);
        return readExcelBody(file, sheetIndex);
    }

    /**
     * 读取excel的正文内容
     * @param filePath
     * @param sheetName sheet的名称
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcelBody(String filePath, String sheetName) throws IOException {
        File file = new File(filePath);
        return readExcelBody(file, sheetName);
    }

    /**
     * 根据workbook获取该workbook的所有sheet
     * @param wb
     * @return List<Sheet>
     */
    public static List<Sheet> getAllSheets(Workbook wb) {
        int numOfSheets = wb.getNumberOfSheets();
        List<Sheet> sheets = new ArrayList<Sheet>();
        for (int i = 0; i < numOfSheets; i++) {
            sheets.add(wb.getSheetAt(i));
        }
        return sheets;
    }

    /**
     * 根据excel文件来获取workbook
     * @param file
     * @return workbook
     * @throws IOException
     */
    public static Workbook getWorkbook(File file) throws IOException {
        Workbook wb = null;
        if (file.exists() && file.isFile()) {
            String fileName = file.getName();
            String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1); // 获取文件后缀

            if ("xls".equals(extension)) { // for office2003
                wb = new HSSFWorkbook(new FileInputStream(file));
            } else if ("xlsx".equals(extension)) { // for office2007
                wb = new XSSFWorkbook(new FileInputStream(file));
            } else {
                throw new IOException("不支持的文件类型");
            }
        }
        return wb;
    }

    /**
     * @date: 2019年4月11日
     * @author: wangchaoyong
     * @title: getWorkbook
     * @param inputStream
     * @param fileName
     * @return
     * @throws IOException
     * @exception:
     * @version: 1.0
     * @description: 获取Workbook
     * update_version: update_date: update_author: update_note:
     */
    public static Workbook getWorkbook(InputStream inputStream, String fileName) throws IOException {
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);

        if ("xls".equals(fileType)) { // for office2003
            wb = new HSSFWorkbook(inputStream);
        } else if ("xlsx".equals(fileType)) { // for office2007
            wb = new XSSFWorkbook(inputStream);
        } else {
            throw new IOException("不支持的文件类型");
        }
        return wb;
    }

    /**
     * 根据excel文件来获取workbook
     * @param filePath
     * @return workbook
     * @throws IOException
     */
    public static Workbook getWorkbook(String filePath) throws IOException {
        File file = new File(filePath);
        return getWorkbook(file);
    }

    /**
     * 根据excel文件输出路径来获取对应的workbook
     * @param filePath
     * @return
     * @throws IOException
     */
    public static Workbook getExportWorkbook(String filePath) throws IOException {
        Workbook wb = null;
        File file = new File(filePath);

        String fileName = file.getName();
        String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1); // 获取文件后缀

        if ("xls".equals(extension)) { // for 少量数据
            wb = new HSSFWorkbook();
        } else if ("xlsx".equals(extension)) { // for 大量数据
            wb = new SXSSFWorkbook(5000); // 定义内存里一次只留5000行
        } else {
            throw new IOException("不支持的文件类型");
        }
        return wb;
    }

    /**
     * 根据workbook和sheet的下标索引值来获取sheet
     * @param wb
     * @param sheetIndex
     * @return sheet
     */
    public static Sheet getSheet(Workbook wb, int sheetIndex) {
        return wb.getSheetAt(sheetIndex);
    }

    /**
     * 根据workbook和sheet的名称来获取sheet
     * @param wb
     * @param sheetName
     * @return sheet
     */
    public static Sheet getSheet(Workbook wb, String sheetName) {
        return wb.getSheet(sheetName);
    }

    /**
     * 根据sheet获取该sheet的所有数据
     * @param sheet
     * @return
     */
    public static List<List<Object>> getSheetData(Workbook wb, Sheet sheet) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            boolean allRowIsBlank = isBlankRow(wb, row);
            if (allRowIsBlank) { // 整行都空，就跳过
                continue;
            }
            List<Object> rowData = getRowData(wb, row);
            list.add(rowData);
        }
        return list;
    }

    /**
     * 读取正文数据， 从第二行起
     * @param wb
     * @param sheet
     * @return
     * @throws IOException
     */
    public static List<List<Object>> getSheetBodyData(Workbook wb, Sheet sheet) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        // 获取总行数
        int rowNum = sheet.getPhysicalNumberOfRows();
        // 获取标题行
        Row headerRow = sheet.getRow(0);
        // 标题总列数
        int colNum = headerRow.getPhysicalNumberOfCells();
        // 获取正文内容， 正文内容应该从第二行开始,第一行为表头的标题
        list.addAll(getSheetData(wb, sheet, 1, rowNum, 0, colNum));
        return list;
    }

    /**
     * @date: 2019年4月11日
     * @author: wangchaoyong
     * @title: getSheetBodyData
     * @param wb
     * @param sheet
     * @param startRowIndex
     * @return
     * @throws IOException
     * @exception:
     * @version: 1.0
     * @description: 通过startRowIndex指定的行号开始读取sheet页数据
     * update_version: update_date: update_author: update_note:
     */
    public static List<List<Object>> getSheetBodyData(Workbook wb, Sheet sheet, int startRowIndex) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        // 获取总行数
        int rowNum = sheet.getPhysicalNumberOfRows();
        // 获取标题行
        Row headerRow = sheet.getRow(0);
        // 标题总列数
        int colNum = headerRow.getPhysicalNumberOfCells();
        // 获取正文内容， 正文内容应该从第二行开始,第一行为表头的标题
        list.addAll(getSheetData(wb, sheet, startRowIndex, rowNum, 0, colNum));
        return list;
    }

    /**
     * @date: 2019年4月11日
     * @author: wangchaoyong
     * @title: getSheetBodyData
     * @param wb
     * @param sheet
     * @param startRowIndex
     * @return
     * @throws IOException
     * @exception:
     * @version: 1.0
     * @description: 通过startRowIndex指定的行号开始读取sheet页数据
     * update_version: update_date: update_author: update_note:
     */
    public static List<List<Object>> getSheetBodyData(Workbook wb, Sheet sheet, int startRowIndex,int titleRow) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        // 获取总行数
        int rowNum = sheet.getPhysicalNumberOfRows();
        // 获取标题行
        Row headerRow = sheet.getRow(titleRow);
        // 标题总列数
        int colNum = headerRow.getPhysicalNumberOfCells();
        // 获取正文内容， 正文内容应该从第二行开始,第一行为表头的标
        list.addAll(getSheetData(wb, sheet, startRowIndex, rowNum, 0, colNum));
        return list;
    }

    /**
     * 根据sheet获取该sheet的指定行列的数据[startRowIndex, endRowIndex), [startColumnIndex, endColumnIndex)
     * @param wb
     * @param sheet
     * @param startRowIndex	开始行索引值
     * @param endRowIndex	结束行索引值
     * @param startColumnIndex	开始列索引值
     * @param endColumnIndex	结束列索引值
     * @return
     * @throws IOException
     */
    public static List<List<Object>> getSheetData(Workbook wb, Sheet sheet, int startRowIndex, int endRowIndex,
                                                  int startColumnIndex, int endColumnIndex) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        if (startRowIndex > endRowIndex || startColumnIndex > endColumnIndex) {
            return list;
        }

        /**
         * getLastRowNum方法能够正确返回最后一行的位置；
         * getPhysicalNumberOfRows方法能够正确返回物理的行数；
         */
        // 获取总行数
        int rowNum = sheet.getPhysicalNumberOfRows();
        // int rowNum = sheet.getLastRowNum();
        // 获取标题行
        Row headerRow = sheet.getRow(1);
        // 标题总列数
        int colNum = headerRow.getPhysicalNumberOfCells();

        if (endRowIndex > rowNum) {
            throw new IOException("行的最大下标索引超过了该sheet实际总行数(包括标题行)" + rowNum);
        }
        if (endColumnIndex > colNum) {
            throw new IOException("列的最大下标索引超过了实际标题总列数" + colNum);
        }
        for (int i = startRowIndex; i < endRowIndex; i++) {
            Row row = sheet.getRow(i);
            boolean allRowIsBlank = isBlankRow(wb, row);
            if (allRowIsBlank) { // 整行都空，就跳过
                continue;
            }
            List<Object> rowData = getRowData(wb, sheet,row, startColumnIndex, endColumnIndex);
            list.add(rowData);
        }
        return list;
    }

    /**
     * 根据指定列区间获取行的数据
     * @param wb
     * @param row
     * @param startColumnIndex	开始列索引值
     * @param endColumnIndex	结束列索引值
     * @return
     */
    public static List<Object> getRowData(Workbook wb, Sheet sheet, Row row, int startColumnIndex, int endColumnIndex) {
        List<Object> rowData = new ArrayList<Object>();
        for (int j = startColumnIndex; j < endColumnIndex; j++) {
            Cell cell = row.getCell(j);
            //增加具有合并单元格的内容读取
            boolean isMerge = isMergedRegion(sheet, row.getRowNum(), j);
            //判断是否具有合并单元格
            if(isMerge) {
                Object rs = getMergedRegionValue(wb, sheet, row.getRowNum(), j);
                rowData.add(rs);
            } else {
                rowData.add(getCellValue(wb, cell));
            }
        }
        return rowData;
    }

    /**
     * 判断指定的单元格是否是合并单元格
     * @param sheet
     * @param row 行下标
     * @param column 列下标
     * @return
     */
    private static boolean isMergedRegion(Sheet sheet,int row ,int column) {

        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {

            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取合并单元格的值
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static Object getMergedRegionValue(Workbook wb, Sheet sheet , int row , int column){

        int sheetMergeCount = sheet.getNumMergedRegions();

        for(int i = 0 ; i < sheetMergeCount ; i++){
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if(row >= firstRow && row <= lastRow){

                if(column >= firstColumn && column <= lastColumn){
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(wb, fCell) ;
                }
            }
        }

        return null;
    }

    /**
     * 判断整行是不是都为空
     * @param row
     * @return
     */
    public static boolean isBlankRow(Workbook wb, Row row) {
        boolean allRowIsBlank = true;
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Object cellValue = getCellValue(wb, cellIterator.next());
            if (cellValue != null && !"".equals(cellValue)) {
                allRowIsBlank = false;
                break;
            }
        }
        return allRowIsBlank;
    }

    /**
     * 获取行的数据
     * @param row
     * @param wb
     * @return
     */
    public static List<Object> getRowData(Workbook wb, Row row) {
        List<Object> rowData = new ArrayList<Object>();
        /**
         * 不建议用row.cellIterator(), 因为空列会被跳过， 后面的列会前移， 建议用for循环， row.getLastCellNum()是获取最后一个不为空的列是第几个
         * 结论：空行可以跳过， 空列最好不要跳过
         */
        /*Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
        	Cell cell = cellIterator.next();
            Object cellValue = getCellValue(wb, cell);
            rowData.add(cellValue);
        }*/
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            Object cellValue = getCellValue(wb, cell);
            rowData.add(cellValue);
        }
        return rowData;
    }

    /**
     * 获取单元格值
     * @param cell
     * @return
     */
    public static Object getCellValue(Workbook wb, Cell cell) {
        if (cell == null
                || (cell.getCellType() == Cell.CELL_TYPE_STRING && StringUtils.isBlank(cell.getStringCellValue()))) {
            return null;
        }
        /*if (cell == null) {
            return "";
        }*/
        // 如果该单元格为数字， 则设置该单元格类型为文本格式
        /*CellStyle cellStyle = wb.createCellStyle();
        DataFormat dataFormat = wb.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("@"));
        cell.setCellStyle(cellStyle);
        cell.setCellType(Cell.CELL_TYPE_STRING);*/

        DecimalFormat df = new DecimalFormat("0");// 格式化 number String字符
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 格式化日期字符串
        DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字

        int cellType = cell.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_BLANK:
                // return "";
                return null;
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_ERROR:
                return cell.getErrorCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                    String value = nf.format(cell.getNumericCellValue());
                    if (StringUtils.isBlank(value)) {
                        return null;
                    }
                    return value;
                } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    String originValue = String.valueOf(cell.getNumericCellValue());
                    if (StringUtils.isBlank(originValue)) {
                        return null;
                    }
                    int pointValue = Integer.parseInt(originValue.substring(originValue.indexOf(".")+1));
                    if (0 == pointValue) {
                        return df.format(cell.getNumericCellValue());
                    }else if(0<pointValue){
                        return nf.format(cell.getNumericCellValue());
                    }
                } else {
                    return cell.getNumericCellValue();
                    /*long longValue = (long) doubleValue;
                    if (doubleValue - longValue > 0) {
                        return String.valueOf(doubleValue);
                    } else {
                        return longValue;
                    }*/
                    /*DecimalFormat df = new DecimalFormat("#");
                    String value = df.format(cell.getNumericCellValue()).toString();
                    return value;*/
                }
            case Cell.CELL_TYPE_STRING:
                String value = cell.getStringCellValue();
                if (StringUtils.isBlank(value)) {
                    return "";
                } else {
                    return value;
                }
                // return cell.getRichStringCellValue();
            default:
                return "";
        }
    }

    /**
     * 根据sheet返回该sheet的物理总行数
     * sheet.getPhysicalNumberOfRows方法能够正确返回物理的行数
     * @param sheet
     * @return
     */
    public static int getSheetPhysicalRowNum(Sheet sheet) {
        // 获取总行数
        int rowNum = sheet.getPhysicalNumberOfRows();
        return rowNum;
    }

    /**
     * 获取操作的行数
     * @param startRowIndex sheet的开始行位置索引
     * @param endRowIndex sheet的结束行位置索引
     * @return
     */
    public static int getSheetDataPhysicalRowNum(int startRowIndex, int endRowIndex) {
        int rowNum = -1;
        if (startRowIndex >= 0 && endRowIndex >= 0 && startRowIndex <= endRowIndex) {
            rowNum = endRowIndex - startRowIndex + 1;
        }
        return rowNum;
    }

    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于单个sheet
     *
     * @param <T>
     * @param headers   表格属性列名数组
     * @param dataset   需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                  javabean属性的数据类型有基本数据类型及String,Date,String[],Double[]
     * @param filePath  excel文件输出路径     
     */
    public static <T> void exportExcel(String[] headers, Collection<T> dataset, String filePath) {
        exportExcel(headers, dataset, filePath, null);
    }

    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于单个sheet
     *
     * @param <T>
     * @param headers 表格属性列名数组
     * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                javabean属性的数据类型有基本数据类型及String,Date,String[],Double[]
     * @param filePath  excel文件输出路径
     * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    public static <T> void exportExcel(String[] headers, Collection<T> dataset, String filePath, String pattern) {
        try {
            // 声明一个工作薄
            Workbook workbook = getExportWorkbook(filePath);
            if (workbook != null) {
                // 生成一个表格
                Sheet sheet = workbook.createSheet();

                write2Sheet(sheet, headers, dataset, pattern);
                OutputStream out = new FileOutputStream(new File(filePath));
                workbook.write(out);
                out.close();
            }
        } catch (IOException e) {
            mLogger.error(e.toString(), e);
        }
    }

    /**
     * 导出数据到Excel文件
     * @param dataList 要输出到Excel文件的数据集
     * @param filePath  excel文件输出路径
     */
    public static void exportExcel(String[][] dataList, String filePath) {
        try {
            // 声明一个工作薄
            Workbook workbook = getExportWorkbook(filePath);
            if (workbook != null) {
                // 生成一个表格
                Sheet sheet = workbook.createSheet();

                for (int i = 0; i < dataList.length; i++) {
                    String[] r = dataList[i];
                    Row row = sheet.createRow(i);
                    for (int j = 0; j < r.length; j++) {
                        Cell cell = row.createCell(j);
                        // cell max length 32767
                        if (r[j].length() > 32767) {
                            mLogger.warn("异常处理", "--此字段过长(超过32767),已被截断--" + r[j]);
                            r[j] = r[j].substring(0, 32766);
                        }
                        cell.setCellValue(r[j]);
                    }
                }
                // 自动列宽
                if (dataList.length > 0) {
                    int colcount = dataList[0].length;
                    for (int i = 0; i < colcount; i++) {
                        sheet.autoSizeColumn(i);
                    }
                }
                OutputStream out = new FileOutputStream(new File(filePath));
                workbook.write(out);
                out.close();
            }
        } catch (IOException e) {
            mLogger.error(e.toString(), e);
        }
    }

    /**
     * 根据模板导出excel
     * @param response
     * @param fileName	导出文件名称
     * @param startNum	数据从第几行开始写，下标从0开始
     * @param dataList	导出数据
     * @param fields	导出字段（为空，则导出查询结果中所有字段）
     * @param templatePath	模板文件路径
     * @throws IOException
     */
    public static void exportToExcelByTemplate(HttpServletResponse response,
                                               String fileName, int startNum, List<Map<String, Object>> dataList,
                                               String[] fields, String templatePath) throws IOException {
        try {
            // 声明一个工作薄
            Workbook workbook = readTemplateByClasspath(templatePath);
            if(workbook != null) {
                Sheet sheet = workbook.getSheetAt(0);
                //生成数据部分
                if(dataList != null && dataList.size()>0){
                    for(Map<String, Object> record : dataList){
                        Row row = sheet.createRow(startNum);
                        if(fields == null || fields.length <= 0) {
                            int cellNum = 0;
                            for (String key : record.keySet()) {
                                Cell cell = row.createCell(cellNum);
                                Object value = record.get(key);
                                if (value == null) {
                                    cell.setCellValue(StringUtils.EMPTY);
                                } else {
                                    cell.setCellValue(String.valueOf(value));
                                }
                                cellNum++;
                            }
                        } else {
                            int cellNum = 0;
                            for (int i = 0; i < fields.length; i++) {
                                /*Object value = record.get(fields[i].toLowerCase());*/
                                Object value = record.get(fields[i]);
                                Cell cell = row.createCell(cellNum);
                                if (value == null) {
                                    cell.setCellValue(StringUtils.EMPTY);
                                } else {
                                    cell.setCellValue(String.valueOf(value));
                                }
                                cellNum++;
                            }
                        }
                        startNum++;
                    }
                }
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("gb2312"),"ISO8859-1")+".xlsx");
                OutputStream stream=response.getOutputStream();
                //导出
                workbook.write(stream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出excel
     * @param response
     * @param fileName	导出文件名称
     * @param headerMapping	字段与表头名称对应关系如{"xm":"姓名","col1":"列1"}
     * @param dataList	导出数据
     * @param isWriteHeader 是否需要写标题行
     * @throws IOException
     */
    public static void exportToExcel(HttpServletResponse response, String fileName, Map<String, String> headerMapping,
                                     List<Map<String, Object>> dataList, boolean isWriteHeader) throws IOException {
        try {
            //创建一个工作薄
            Workbook workbook = new SXSSFWorkbook(5000);
            if (workbook != null) {
                //生成一个表格
                Sheet sheet = workbook.createSheet();

                List<String> headers = new ArrayList<String>();
                List<String> fields = new ArrayList<String>();
                for(String key : headerMapping.keySet()) {
                    fields.add(key);
                    headers.add(headerMapping.get(key));
                }

                //生成表格标题行
                if(isWriteHeader) {
                    Row row = sheet.createRow(0);
                    for (int i = 0; i < headers.size(); i++) {
                        Cell cell = row.createCell(i);
                        cell.setCellValue(headers.get(i));
                    }
                }

                //生成数据部分
                if(dataList != null && dataList.size()>0){
                    int index = 0;
                    if(isWriteHeader) {
                        index = 0;
                    } else {
                        index = -1;
                    }
                    for(Map<String, Object> record : dataList){
                        index++;
                        Row newRow = sheet.createRow(index);
                        for (int i = 0; i < fields.size(); i++) {
                            //工单模型数据下载时，部分表字段名称是大写，转成小写之后，无法获取到取
                            //Object value = record.get(fields.get(i).toLowerCase());
                            Object value = record.get(fields.get(i));
                            Cell cell = newRow.createCell(i);
                            if (value == null) {
                                cell.setCellValue(StringUtils.EMPTY);
                            } else {
                                cell.setCellValue(String.valueOf(value));
                            }
							
							/*if(i == 0) {
								sheet.autoSizeColumn(i);
							}*/
                        }
                    }
                }
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("gb2312"),"ISO8859-1")+".xlsx");
            OutputStream stream=response.getOutputStream();
            //导出
            workbook.write(stream);
        } catch (IOException e) {
            mLogger.error(e.toString(), e);
        }
    }

    public static void exportToExcelMerge(HttpServletResponse response, String fileName, Map<String, String> headerMapping,
                                          List<Map<String, Object>> dataList, boolean isWriteHeader, boolean isMergeCell, List<Map<String, Integer>> mergeCells) throws IOException {
        try {
            //创建一个工作薄
            Workbook workbook = new SXSSFWorkbook(5000);
            if (workbook != null) {
                //生成一个表格
                Sheet sheet = workbook.createSheet();

                //合并单元格
                if(isMergeCell) {
                    for(int i=0; i<mergeCells.size(); i++) {
                        ////参数说明：1：开始行 2：结束行  3：开始列 4：结束列
                        Map<String, Integer> map = mergeCells.get(i);
                        int firstRow = map.get("firstRow");
                        int lastRow = map.get("lastRow");
                        int firstCol = map.get("firstCol");
                        int lastCol = map.get("lastCol");
                        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
                    }
                }

                List<String> headers = new ArrayList<String>();
                List<String> fields = new ArrayList<String>();
                for(String key : headerMapping.keySet()) {
                    fields.add(key);
                    headers.add(headerMapping.get(key));
                }

                //生成表格标题行
                if(isWriteHeader) {
                    Row row = sheet.createRow(0);
                    for (int i = 0; i < headers.size(); i++) {
                        Cell cell = row.createCell(i);
                        cell.setCellValue(headers.get(i));
                    }
                }

                //生成数据部分
                if(dataList != null && dataList.size()>0){
                    int index = 0;
                    if(isWriteHeader) {
                        index = 0;
                    } else {
                        index = -1;
                    }
                    for(Map<String, Object> record : dataList){
                        index++;
                        Row newRow = sheet.createRow(index);
                        for (int i = 0; i < fields.size(); i++) {
                            Object value = record.get(fields.get(i).toLowerCase());
                            Cell cell = newRow.createCell(i);
                            if (value == null) {
                                cell.setCellValue(StringUtils.EMPTY);
                            } else {
                                cell.setCellValue(String.valueOf(value));
                            }

                            if(i == 0) {
                                sheet.autoSizeColumn(i);
                            }
                        }
                    }
                }
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("gb2312"),"ISO8859-1")+".xlsx");
            OutputStream stream=response.getOutputStream();
            //导出
            workbook.write(stream);
        } catch (IOException e) {
            mLogger.error(e.toString(), e);
        }
    }



    /**
     * @Author: wujianwen
     * @Description: excel生成工具
     * @DateTime: 2021/10/19 14:22
     * @Params: [response 得到输出流, fileName 文件名不带后缀, headerMapping 表头可为空, dataList 数据]
     * @Return void
     */
    public static Workbook exportExcelToWorkbook( String fileName, List<Map<String, String>> headerList,
                                                  List<Map<String, String>> dataList)  {
        SXSSFWorkbook workbook = new SXSSFWorkbook(200);
        //sheet页名和表头
        SXSSFSheet sheet = workbook.createSheet(fileName);


        //设置列宽
        Map<String, String> columnWidthData = dataList==null||dataList.size()==0?new HashMap<>():dataList.get(0);
        for (int j = 0; j < headerList.size(); j++) {
            String cellValue = String.valueOf(columnWidthData.get(headerList.get(j).get("headKey")));
            int width = StringUtils.isEmpty(cellValue)||cellValue.length()<5?5:cellValue.length();
            sheet.setColumnWidth(j,512*width+323);
        }

        // 生成表头样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setBorderBottom(BorderStyle.THICK);
        headerStyle.setBorderLeft(BorderStyle.THICK);
        headerStyle.setBorderRight(BorderStyle.THICK);
        headerStyle.setBorderTop(BorderStyle.THICK);

        //生成表头
        Row headRow = sheet.createRow(0);
        for (int j = 0; j < headerList.size(); j++) {
            String cellValue = headerList.get(j).get("headName");
            Cell cell = headRow.createCell(j);
            cell.setCellValue(cellValue);
            cell.setCellStyle(headerStyle);
        }

        // 生成数据样式
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);

        //生成表格数据
        for (int i = 0; i < dataList.size(); i++) {
            Row rows = sheet.createRow(i+1);
            Map<String, String> map = dataList.get(i);
            for (int j = 0; j < headerList.size(); j++) {
                String cellValue =  String.valueOf(map.get(headerList.get(j).get("headKey")));
                Cell cell = rows.createCell(j);
                cell.setCellValue(cellValue);
                cell.setCellStyle(dataStyle);
            }
        }

        return workbook;
    }

    public static  void excelWriteToStream(HttpServletResponse response,Workbook workbook,String fileName){

        try {

            response.setCharacterEncoding("UTF-8");
            OutputStream output = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流
            fileName = URLEncoder.encode(fileName,"UTF-8");

            response.setHeader("Content-disposition", "attachment; filename*=UTF-8''"
                    + fileName + ".xls"); // 设定输出文件头,该方法有两个参数，分别表示应答头的名字和值。
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            workbook.write(output);

            output.flush();
            output.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    /**
     * @Author: wujianwen
     * @Description: 给EXCEL添加水印
     * @DateTime: 2021/10/22 10:13
     * @Params: [request, workbook, fileName, watermark]
     * @Return org.apache.poi.xssf.usermodel.XSSFWorkbook
     */
    public static  XSSFWorkbook excelAddWatermark(HttpServletRequest request,Workbook workbook, String fileName,Watermark watermark)  {
        //得到加水印的临时文件目录
        String filePath = System.getProperty("user.dir") + "/exportPath/";  // 获取项目当前路径

        File desFile = new File(filePath + fileName + ".xls");
        String parent = desFile.getParent();
        if(!desFile.getParentFile().exists()){
            desFile.getParentFile().mkdirs();
        }
        try {
            boolean newFile = desFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //加水印需要先保存文件再读取出来然后加
        try {
            FileOutputStream fout = new FileOutputStream(desFile);
            workbook.write(fout);
            fout.flush();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ZipSecureFile.setMinInflateRatio(-1.0d);
        XSSFWorkbook xsWorkbook = null;
        try {
            xsWorkbook = new XSSFWorkbook(filePath + fileName + ".xls");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //生成水印图片
        BufferedImage image = createWatermarkImage(watermark);

        // 导出到字节流B
        //XSSFWorkbook workbook = workbookSXSS.getXSSFWorkbook();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int pictureIdx = xsWorkbook.addPicture(os.toByteArray(), xsWorkbook.PICTURE_TYPE_PNG);
        POIXMLDocumentPart poixmlDocumentPart = xsWorkbook.getAllPictures().get(pictureIdx);
        for (int i = 0; i < xsWorkbook.getNumberOfSheets(); i++) {//获取每个Sheet表
            XSSFSheet sheet = xsWorkbook.getSheetAt(i);
            PackagePartName ppn = poixmlDocumentPart.getPackagePart().getPartName();
            String relType = XSSFRelation.IMAGES.getRelation();
            //add relation from sheet to the picture data
            PackageRelationship pr = sheet.getPackagePart().addRelationship(ppn, TargetMode.INTERNAL, relType, null);
            //set background picture to sheet
            sheet.getCTWorksheet().addNewPicture().setId(pr.getId());
        }

        return xsWorkbook;

    }

    /**
    * 不再使用HttpServletRequest获取文件路径，去除无用参数版本
    */
    public static  XSSFWorkbook excelAddWatermark(Workbook workbook, String fileName,Watermark watermark)  {
        //得到加水印的临时文件目录
        String filePath = System.getProperty("user.dir") + "/exportPath/";  // 获取项目当前路径

        File desFile = new File(filePath + fileName + ".xls");
        String parent = desFile.getParent();
        if(!desFile.getParentFile().exists()){
            desFile.getParentFile().mkdirs();
        }
        try {
            boolean newFile = desFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //加水印需要先保存文件再读取出来然后加
        try {
            FileOutputStream fout = new FileOutputStream(desFile);
            workbook.write(fout);
            fout.flush();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ZipSecureFile.setMinInflateRatio(-1.0d);
        XSSFWorkbook xsWorkbook = null;
        try {
            xsWorkbook = new XSSFWorkbook(filePath + fileName + ".xls");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //生成水印图片
        BufferedImage image = createWatermarkImage(watermark);

        // 导出到字节流B
        //XSSFWorkbook workbook = workbookSXSS.getXSSFWorkbook();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int pictureIdx = xsWorkbook.addPicture(os.toByteArray(), xsWorkbook.PICTURE_TYPE_PNG);
        POIXMLDocumentPart poixmlDocumentPart = xsWorkbook.getAllPictures().get(pictureIdx);
        for (int i = 0; i < xsWorkbook.getNumberOfSheets(); i++) {//获取每个Sheet表
            XSSFSheet sheet = xsWorkbook.getSheetAt(i);
            PackagePartName ppn = poixmlDocumentPart.getPackagePart().getPartName();
            String relType = XSSFRelation.IMAGES.getRelation();
            //add relation from sheet to the picture data
            PackageRelationship pr = sheet.getPackagePart().addRelationship(ppn, TargetMode.INTERNAL, relType, null);
            //set background picture to sheet
            sheet.getCTWorksheet().addNewPicture().setId(pr.getId());
        }

        return xsWorkbook;

    }


    /**
     * @Author: wujianwen
     * @Description:  生成水印图片
     * @DateTime: 2021/10/19 17:49
     * @Params: [watermark]
     * @Return java.awt.image.BufferedImage
     */
    public static BufferedImage createWatermarkImage(Watermark watermark) {
        if (watermark == null) {
            watermark = new Watermark();
            watermark.setEnable(true);
//            watermark.setText("userName");
            watermark.setText("内部资料");
            watermark.setColor("#C5CBCF");
            watermark.setDateFormat("yyyy-MM-dd HH:mm");
        } else {
            if (StringUtils.isEmpty(watermark.getDateFormat())) {
                watermark.setDateFormat("yyyy-MM-dd HH:mm");
            } else if (watermark.getDateFormat().length() == 16) {
                watermark.setDateFormat("yyyy-MM-dd HH:mm");
            } else if (watermark.getDateFormat().length() == 10) {
                watermark.setDateFormat("yyyy-MM-dd");
            }
            if (StringUtils.isEmpty(watermark.getText())) {
                watermark.setText("内部资料");
            }
            if (StringUtils.isEmpty(watermark.getColor())) {
                watermark.setColor("#C5CBCF");
            }
        }
        String[] textArray = watermark.getText().split("\n");
        Font font = new Font("lucida Cansole",Font.BOLD,20);
        Integer width = 400;
        Integer height = 150;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 背景透明 开始
        Graphics2D g = image.createGraphics();
        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g.dispose();
        // 背景透明 结束
        g = image.createGraphics();
        g.setColor(new Color(Integer.parseInt(watermark.getColor().substring(1), 16)));// 设定画笔颜色
        g.setFont(font);// 设置画笔字体
        g.shear(0.1, -0.26);// 设定倾斜度

//        设置字体平滑
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int y = 90;
        for (int i = 0; i < textArray.length; i++) {
            g.drawString(textArray[i], 0, y);// 画出字符串
            y = y + font.getSize();
        }
        String userName = (StringUtils.isEmpty(watermark.getUserName())?"":watermark.getUserName()+"  ");
        String dateStr =userName + DateUtils.getNowDateFormatCustom(watermark.getDateFormat());
        g.drawString(dateStr, 0, y);// 画出字符串

        g.dispose();// 释放画笔


        return image;


    }

    /**
     * @Author: wujianwen
     * @Description: 水印内容内部实体类
     * @DateTime: 2021/10/19 17:48
     * @Params:
     * @Return
     */
    public static class Watermark {
        private Boolean enable;
        private String text;
        private String dateFormat;
        private String color;
        private String userName;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getDateFormat() {
            return dateFormat;
        }

        public void setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    public static Workbook readTemplateByClasspath(String path) throws Exception {
        try {
            Workbook wb = WorkbookFactory.create(ExcelUtil.class.getResourceAsStream(path));
            return wb;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读取模板错误！", e);
        }
    }



    /**
     * 每个sheet的写入
     * @param sheet   页签
     * @param headers 表头
     * @param dataset 数据集合
     * @param pattern 日期格式
     */
    public static <T> void write2Sheet(Sheet sheet, String[] headers, Collection<T> dataset, String pattern) {
        // 产生表格标题行
        Row row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
        }
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            if (t instanceof Map) { // row data is map
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) t;
                int cellNum = 0;
                for (String k : headers) {
                    if (map.containsKey(k) == false) {
                        mLogger.error("Map 中 不存在 key [" + k + "]");
                        continue;
                    }
                    Cell cell = row.createCell(cellNum);
                    Object value = map.get(k);
                    if (value == null) {
                        cell.setCellValue(StringUtils.EMPTY);
                    } else {
                        cell.setCellValue(String.valueOf(value));
                    }
                    cellNum++;
                }
            } else if (t instanceof Object[]) { // row data is Object[]
                Object[] tObjArr = (Object[]) t;
                for (int i = 0; i < tObjArr.length; i++) {
                    Cell cell = row.createCell(i);
                    Object value = tObjArr[i];
                    if (value == null) {
                        cell.setCellValue(StringUtils.EMPTY);
                    } else {
                        cell.setCellValue(String.valueOf(value));
                    }
                }
            } else if (t instanceof List<?>) { // row data is List
                List<?> rowData = (List<?>) t;
                for (int i = 0; i < rowData.size(); i++) {
                    Cell cell = row.createCell(i);
                    Object value = rowData.get(i);
                    if (value == null) {
                        cell.setCellValue(StringUtils.EMPTY);
                    } else {
                        cell.setCellValue(String.valueOf(value));
                    }
                }
            } else { // row data is vo
                // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
                Field[] fields = t.getClass().getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    Cell cell = row.createCell(i);
                    Field field = fields[i];
                    String fieldName = field.getName();
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                    try {
                        Class<?> tClazz = t.getClass();
                        Method getMethod = tClazz.getMethod(getMethodName, new Class[] {});
                        Object value = getMethod.invoke(t, new Object[] {});
                        String textValue = null;
                        if (value instanceof Integer) {
                            int intValue = (Integer) value;
                            cell.setCellValue(intValue);
                        } else if (value instanceof Float) {
                            float fValue = (Float) value;
                            cell.setCellValue(fValue);
                        } else if (value instanceof Double) {
                            double dValue = (Double) value;
                            cell.setCellValue(dValue);
                        } else if (value instanceof Long) {
                            long longValue = (Long) value;
                            cell.setCellValue(longValue);
                        } else if (value instanceof Boolean) {
                            boolean bValue = (Boolean) value;
                            cell.setCellValue(bValue);
                        } else if (value instanceof Date) {
                            Date date = (Date) value;
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            textValue = sdf.format(date);
                        } else {
                            // 其它数据类型都当作字符串简单处理
                            textValue = value.toString();
                        }
                        if (textValue != null) {
                            // HSSFRichTextString richString = new
                            // HSSFRichTextString(textValue);
                            cell.setCellValue(textValue);
                        } else {
                            cell.setCellValue(StringUtils.EMPTY);
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        // 设定自动宽度
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
    * @Author: wujianwen
    * @Description: 删除添加水印产生的临时文件
    * @DateTime: 2021/10/22 10:10
    * @Params: [path, response]
    * @Return void
    */
    public static void delWatermarkFile(HttpServletRequest request,String fileName) {
        //得到加水印的临时文件目录
//        String realPath = request.getServletContext().getRealPath("/WEB-INF");
//        String filePath = realPath+"/"+fileName+".xlsx";
        String filePath = System.getProperty("user.dir") + "/exportPath/";
        File file = new File(filePath + fileName + ".xls");
        if(file.exists()) file.delete();
    }

    /**
     * 不再使用HttpServletRequest获取文件路径，去除无用参数版本
     */
    public static void delWatermarkFile(String fileName) {
        //得到加水印的临时文件目录
        String filePath = System.getProperty("user.dir") + "/exportPath/";
        File file = new File(filePath + fileName + ".xls");
        if(file.exists()) file.delete();
    }

    /**
     * EXCEL文件下载
     * @param path
     * @param response
     */
    public static void download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public  static XSSFWorkbook addBgOfExcel(Map<String, String> paramMap, XSSFWorkbook workbook) throws IOException {
        PoiUtil.Watermark watermark = new PoiUtil.Watermark();
        watermark.setText(paramMap.get("bgChar"));
        watermark.setUserName(paramMap.get("userName"));
        BufferedImage image = PoiUtil.createWatermarkImage(watermark);
        // 导出到字节流B
        //XSSFWorkbook workbook = workbookSXSS.getXSSFWorkbook();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);

        int pictureIdx = workbook.addPicture(os.toByteArray(), Workbook.PICTURE_TYPE_PNG);
        POIXMLDocumentPart poixmlDocumentPart = workbook.getAllPictures().get(pictureIdx);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {//获取每个Sheet表
            XSSFSheet sheet = workbook.getSheetAt(i);
            PackagePartName ppn = poixmlDocumentPart.getPackagePart().getPartName();
            String relType = XSSFRelation.IMAGES.getRelation();
            //add relation from sheet to the picture data
            PackageRelationship pr = sheet.getPackagePart().addRelationship(ppn, TargetMode.INTERNAL, relType, null);
            //set background picture to sheet
            sheet.getCTWorksheet().addNewPicture().setId(pr.getId());
        }

        return workbook;
    }


    //复写导出Excel方法，导出格式不同
    public static  void excelWriteToXLSX(HttpServletResponse response,Workbook workbook,String fileName){

        try {

            response.setCharacterEncoding("UTF-8");
            OutputStream output = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流
            fileName = URLEncoder.encode(fileName,"UTF-8");

            response.setHeader("Content-disposition", "attachment; filename*=UTF-8''"
                    + fileName + ".xlsx"); // 设定输出文件头,该方法有两个参数，分别表示应答头的名字和值。
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            workbook.write(output);

            output.flush();
            output.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    @SneakyThrows
    public static void createLocalExcelFile (String filePath, List<Object> headList, List<List<Object>> dataList){
        HSSFWorkbook workbook = new HSSFWorkbook();
//        Workbook workbook = new SXSSFWorkbook(200);
        //sheet页名和表头
        Sheet sheet = workbook.createSheet("sheet1");

        CellStyle cellStyle = workbook.createCellStyle();
        DataFormat dataFormat = workbook.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("@")); // 设置为文本格式

        Row row1 = sheet.createRow(0);
        for (int i = 0; i < headList.size(); i++) {
            row1.createCell(i).setCellValue(String.valueOf(headList.get(i)));
            row1.getCell(i).setCellType(CellType.STRING);
            sheet.setDefaultColumnStyle(i,cellStyle);//将此列样式设为文本

        }
        for (int i = 0; i < dataList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            List<Object> data = dataList.get(i);
            for (int j = 0; j < data.size(); j++) {
                row.createCell(j).setCellValue(String.valueOf(data.get(j)));
                sheet.setDefaultColumnStyle(i,cellStyle);//将此列样式设为文本
            }
        }

        FileOutputStream fout = new FileOutputStream(filePath);

        try {
            workbook.write(fout);
            fout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            fout.close();
        }
    }

    public static List<String> readExcelToList(String filePath){
        List<String> datas = new ArrayList<>();
        try{
            // 获取文件输入流
            InputStream inputStream = new FileInputStream(filePath);
            // 定义一个org.apache.poi.ss.usermodel.Workbook的变量
            Workbook workbook = null;
            // 截取路径名 . 后面的后缀名，判断是xls还是xlsx
            // 如果这个判断不对，就把equals换成 equalsIgnoreCase()
            if (filePath.substring(filePath.lastIndexOf(".") + 1).equals("xls")){
                workbook = new HSSFWorkbook(inputStream);
            }else if (filePath.substring(filePath.lastIndexOf(".") + 1).equals("xlsx")){
                workbook = new XSSFWorkbook(inputStream);
            }

            // 获取第一张表
            Sheet sheet = workbook.getSheetAt(0);
            // sheet.getPhysicalNumberOfRows()获取总的行数
            // 循环读取每一行
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                // 循环读取每一个格
                Row row = sheet.getRow(i);
                // row.getPhysicalNumberOfCells()获取总的列数
                String rowData = "";
                for (int index = 0; index < row.getPhysicalNumberOfCells(); index++) {

                    // 获取数据，但是我们获取的cell类型
                    Cell cell = row.getCell(index);
                    // 转换为字符串类型
                    cell.setCellType(CellType.STRING);
                    // 获取得到字符串
                    String value = cell.getStringCellValue();
                    rowData += value + ",";
                }
                datas.add(rowData.substring(0,rowData.length()-1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }


}
