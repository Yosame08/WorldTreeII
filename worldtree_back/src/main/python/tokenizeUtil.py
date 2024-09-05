

# 分词
# seg_list = jieba.cut("我来到北京清华大学", cut_all=False)
# print("Default Mode: " + "/ ".join(seg_list))  # 精确模式

# # # 添加自定义词典
# # jieba.load_userdict('userdict.txt')

# # 关键词提取
# import jieba.analyse
# content = '我爱北京天安门'
# tags = jieba.analyse.extract_tags(content, topK=2)
# print(",".join(tags))

import argparse
import jieba
import sys
import io
import os

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf8')


def tokenize(text):
    # 分词
    seg_list = jieba.cut(text, cut_all=False)
    # return "/ ".join(seg_list)
    return list(seg_list)


if __name__ == '__main__':
    # 读取命令行参数 ：需要分词的文本
    parser = argparse.ArgumentParser()
    parser.add_argument('text', type=str)
    args = parser.parse_args()
    text = args.text
    print(tokenize(text))
    # print(text)