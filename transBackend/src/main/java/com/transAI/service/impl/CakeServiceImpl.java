package com.transAI.service.impl;

import com.transAI.service.CakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CakeServiceImpl implements CakeService {


    @Autowired
    private TartsServiceImpl tartsServiceImpl;

    @Override
    public String submit(String answer) {
        StringBuilder result = new StringBuilder("000000000");
        boolean pass = true;
        for (int i = 0; i < 9; i++) {
            char c = check(i, answer);
            result.setCharAt(i, c);
            if (c == '0') {
                pass = false;
            }
        }
        if(pass) {
            tartsServiceImpl.passTask(5);
        }
        return result.toString();
    }

    private char check(int index, String answer) {
        switch (index) {
            case 0: // 如果字符串中包含数字 "2"，返回 '1'
                if (answer.contains("2")) return '1';
                return '0';
            case 1: // 如果字符串长度小于等于 11，返回 '1'
                if (answer.length() <= 11) return '1';
                return '0';
            case 2: // 如果字符串以大写字母开头，返回 '1'
                if (!answer.isEmpty() && Character.isUpperCase(answer.charAt(0))) return '1';
                return '0';
            case 3: // 如果字符串不包含 'z', 'x', 'c', 'v', 'b', 'n', 'm'，返回 '1'
                if (!answer.matches(".*[zxcvbnm].*")) return '1';
                return '0';
            case 4: // 如果字符串中相同的字符全都相邻，返回 '1'
                if (areAllSameCharactersAdjacent(answer)) return '1';
                return '0';
            case 5: // 如果字符串至多包含两种元音字符，返回 '1'
                if (countDistinctVowels(answer) <= 2) return '1';
                return '0';
            case 6: // 如果字符串的 ASCII 码和在 1090 到 1100 之间，返回 '1'
                if (isAsciiSumInRange(answer, 1000, 1100)) return '1';
                return '0';
            case 7: // 如果相邻字符的 ASCII 码差值为 ±17，返回 '1'
                if (hasAdjacentAsciiDifference(answer, 17)) return '1';
                if (hasAdjacentAsciiDifference(answer, -17)) return '1';
                return '0';
            case 8: // 如果字符串包含字母 'y' 或 'Y'，返回 '1'
                if (answer.toLowerCase().contains("y")) return '1';
                return '0';
            default:
                return '0';
        }
    }

    private boolean areAllSameCharactersAdjacent(String answer) {
        // 遍历字符串，检查相同字符是否相邻
        int length = answer.length();
        if (length <= 1) return true; // 字符串为空或仅有一个字符时，返回 true

        // 使用 map 存储字符的最后一个出现位置
        for (int i = 0; i < length - 1; i++) {
            if (answer.charAt(i) == answer.charAt(i + 1)) {
                // 如果相邻字符相同，继续检查
                continue;
            } else {
                // 如果字符不同，检查后面是否还有之前的字符出现
                for (int j = i + 2; j < length; j++) {
                    if (answer.charAt(i) == answer.charAt(j)) {
                        return false; // 如果找到相同字符但不相邻，则返回 false
                    }
                }
            }
        }
        return true;
    }

    private int countDistinctVowels(String answer) {
        // 统计字符串中不同元音字符的数量
        String vowels = "aeiouAEIOU";
        return (int) answer.chars()
                .filter(c -> vowels.indexOf(c) != -1)
                .distinct()
                .count();
    }

    private boolean isAsciiSumInRange(String answer, int min, int max) {
        // 计算字符串中所有字符的 ASCII 码之和，并判断是否在给定范围内
        int sum = answer.chars().sum();
        return sum >= min && sum <= max;
    }

    private boolean hasAdjacentAsciiDifference(String answer, int difference) {
        // 检查相邻字符的 ASCII 码差值是否为给定值 ±difference
        for (int i = 0; i < answer.length() - 1; i++) {
            int diff = Math.abs(answer.charAt(i) - answer.charAt(i + 1));
            if (diff == difference) {
                return true;
            }
        }
        return false;
    }
}
