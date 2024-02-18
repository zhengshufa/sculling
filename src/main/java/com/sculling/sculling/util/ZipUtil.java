package com.sculling.sculling.util;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author hpdata
 * @DATE 2024/2/1810:00
 */
@Slf4j
public class ZipUtil {

    /**
     * 解压压缩包缓冲流大小
     */
    private static final Integer BUFFER_SIZE = 1024 * 1024 * 5;

    /**
     * 解析文件线程池
     */
    private static final ThreadPoolExecutor PARSE_FILE_EXECUTOR = new ThreadPoolExecutor(5, 20, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10), ThreadFactoryBuilder.create().build(), new ThreadPoolExecutor.CallerRunsPolicy());


    /**
     * 解压多个zip压缩包
     *
     * @param fileBytesList 压缩包字节流
     * @return 解压缩后的文件流列表
     */
    public static List<InputStream> unzips(Collection<byte[]> fileBytesList) {
        List<InputStream> result = new ArrayList<>(fileBytesList.size());

        // 使用多线程进行解压
        List<Future<List<InputStream>>> futureTaskList = new ArrayList<>();
        for (byte[] bytes : fileBytesList) {
            Future<List<InputStream>> future = PARSE_FILE_EXECUTOR.submit(() -> upzip(bytes));
            futureTaskList.add(future);
        }
        // 获取解压缩后的文件
        for (Future<List<InputStream>> future : futureTaskList) {
            try {
                List<InputStream> inputStreams = future.get();
                result.addAll(inputStreams);
            } catch (InterruptedException | ExecutionException e) {
                log.error("解压缩zip文件包失败", e);
            }
        }
        return result;
    }

    /**
     * 解压缩单个zip包
     *
     * @param bytes zip包的字节数组
     * @return zip中的输入流
     */
    public static List<InputStream> upzip(byte[] bytes) {
        List<InputStream> inputStreams = new ArrayList<>();
        if (bytes.length == 0) {
            return inputStreams;
        }
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bytes));
        log.info("创建zis对象成功,对象：{}", zis);
        ZipEntry nextEntry = null;
        while (true) {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                if ((nextEntry = zis.getNextEntry()) == null) {
                    break;
                }
                String entryName = nextEntry.getName();
                long size = nextEntry.getSize();
                if (size == 0) {
                    log.warn("读取zip中的文件:[{}]的文件字节流为空", entryName);
                    continue;
                }
                log.info("开启读取zip中的文件：[{}]，文件大小：[{}]", entryName, size);
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = zis.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
                InputStream fileStream = new ByteArrayInputStream(outputStream.toByteArray());
                inputStreams.add(fileStream);
            } catch (Exception e) {
                log.error("解析zip压缩包出错", e);
                break;
            }
        }
        return inputStreams;
    }

}
