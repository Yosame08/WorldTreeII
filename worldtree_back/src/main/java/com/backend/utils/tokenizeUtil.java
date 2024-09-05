package com.backend.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class tokenizeUtil {

    public static String callTokenize(String content) {
        content = content.concat("\"");
        content = "\"" + content;

        String pythonOutput = callPythonScript(content);

        return pythonOutput.concat(" (from jieba tokenizeUtil)");
    }

    private static String callPythonScript(String content) {
        String pythonScriptPath = "src\\main\\python\\tokenizeUtil.py"; // 请替换为你的Python脚本路径
        String[] cmd = new String[3];
        cmd[0] = "python"; // 如果你的系统默认的Python版本不是Python 3，你可能需要改为"python3"
        cmd[1] = pythonScriptPath;
        cmd[2] = content;

        String output = "";
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            Process process = pb.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                output += line + "\n";
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}

