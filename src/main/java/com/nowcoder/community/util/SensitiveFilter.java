package com.nowcoder.community.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author xuming
 * @Date 2019/11/6 15:20
 * 敏感词过滤器
 */
@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    /** 若发现敏感词,则用***代替 */
    private static final String REPLACEMENT = "***";

    // 3.当前bean构造完成后,即读取敏感词词库,将前缀树构建完成
    @PostConstruct
    public void init() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("sensitive-words.txt");
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String lineTxt;
        try {
            while ((lineTxt = bufferedReader.readLine()) != null) {
                addWord(lineTxt.trim());
            }
        }
        catch (IOException e) {
            logger.error("读取敏感词词库失败: " + e.getMessage());
        } finally {
            bufferedReader.close();
            reader.close();
            is.close();
        }
    }

    // 2.拿到一个敏感词,将其构建在前缀树中
    public void addWord(String lineTxt) {
        // 定义当前结点tempNode,初始时刻也指向根结点
        TrieNode tempNode = rootNode;
        for (int i = 0; i < lineTxt.length(); i++) {
            Character key = lineTxt.charAt(i);
            TrieNode node = tempNode.getSubNode(key);
            if (node == null) {
                node = new TrieNode();
                tempNode.addSubNode(key, node);
            }
            // 当前结点下移
            tempNode = node;
            // 设置结束标识
            if (i == lineTxt.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    // 1.定义前缀树的结构
    private class TrieNode {
        // 当前结点是否为某敏感词结尾
        private boolean end = false;
        // 当前结点的子结点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public void setKeywordEnd(boolean end) {
            this.end = end;
        }

        public boolean isKeywordEnd() {
            return end;
        }

        public void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        public TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }
    }
    // 直接创建一个空的根结点
    TrieNode rootNode = new TrieNode();

    /**
     * 5.对filter方法的增强,用于跳过特殊符号
     * @param c
     * @return
     */
    public boolean isSymbol(char c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        // 若字符c既不是Ascii字母数字,又不是东亚文字,则返回true
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    /**
     * 4.敏感词过滤核心方法
     * @param text 待过滤文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        StringBuilder result = new StringBuilder();
        // 仨指针
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;

        while (position < text.length()) {
            char c = text.charAt(position);

            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            // 复用tempNode
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                result.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                result.append(REPLACEMENT);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                position++;
            }
        }

        // 处理最后的一部分
        result.append(text.substring(begin));
        return result.toString();
    }
}
