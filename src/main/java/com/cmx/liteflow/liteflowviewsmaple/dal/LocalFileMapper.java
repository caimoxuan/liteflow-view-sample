package com.cmx.liteflow.liteflowviewsmaple.dal;

import com.cmx.extension.Extensions;
import com.cmx.model.ScriptDetail;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.Arrays;
import java.util.Optional;

@Repository
public class LocalFileMapper {

    private final String LOCAL_DIR = "C:\\Users\\caimoxuan\\data";


    public void saveOrUpdateLocalFile(String loadKey, String extCode, ScriptDetail scriptDetail) {
        String fileName = scriptDetail.getFileName();
        if (fileName == null) {
            fileName = extCode + "_" + loadKey + "." + scriptDetail.getScriptType();
        }
        File file = new File(LOCAL_DIR + "/" + loadKey + "/" + fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(scriptDetail.getScriptText());
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果是从 一个语言换成另一个语言，由于是文件模式 需要自己实现删除原文件逻辑，否则会有2个文件，找的时候只会找第一个

        // 清除当前内存缓存，分布式场景下需要异步通知所有机器清除当前缓存， 不开启缓存对java脚本性能影响较大
        Extensions.clearExtensionCache(loadKey, extCode);
    }


    public ScriptDetail getLocalFile(String loadKey, String extCode) {
        ScriptDetail scriptDetail = new ScriptDetail();
        File file = new File(LOCAL_DIR + "/" + loadKey);
        if (!file.isDirectory()) {
            return scriptDetail;
        } else {
            String[] list = file.list();
            if (list != null && list.length != 0) {
                Optional<String> first = Arrays.stream(list).filter((s) -> s.startsWith(extCode)).findFirst();
                if (first.isEmpty()) {
                    return scriptDetail;
                } else {
                    String fileName = first.get();
                    try {
                        InputStream resource = new FileInputStream(LOCAL_DIR + "/" + loadKey + "/" + first.get());
                        String fileContext = getFileContent(resource);
                        if (StringUtils.isBlank(fileContext)) {
                            return scriptDetail;
                        }

                        scriptDetail.setScriptText(fileContext);
                        scriptDetail.setScriptType(fileName.split("\\.")[1]);
                        return scriptDetail;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return scriptDetail;
    }

    private static String getFileContent(InputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (stream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        }
        return builder.toString();
    }


}
