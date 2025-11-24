#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
中国大学MOOC课程爬虫 - 修复编码版本
"""

import json
import sys
import os
import io

def setup_encoding():
    """强制设置UTF-8编码"""
    # 设置标准输出的编码
    if sys.stdout.encoding != 'UTF-8':
        sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8', errors='replace')
    if sys.stderr.encoding != 'UTF-8':
        sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8', errors='replace')

def scrape_courses():
    """
    爬取课程信息
    返回课程列表
    """
    try:
        import requests
        from bs4 import BeautifulSoup

        url = "https://www.icourse163.org/channel/3002.htm"

        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
            'Accept-Language': 'zh-CN,zh;q=0.9,en;q=0.8',
        }

        response = requests.get(url, headers=headers, timeout=15)
        response.encoding = 'utf-8'

        soup = BeautifulSoup(response.text, 'html.parser')

        courses = []

        # 尝试从页面中提取课程信息
        # 方法1: 查找课程卡片
        course_cards = soup.select('a[href*="/course/"]')

        seen_titles = set()

        for card in course_cards:
            try:
                title = card.get_text(strip=True)
                href = card.get('href', '')

                # 确保是有效的课程链接
                if not title or len(title) < 5:
                    continue

                if title in seen_titles:
                    continue

                seen_titles.add(title)

                # 构建完整URL
                if href.startswith('http'):
                    course_link = href
                elif href.startswith('/'):
                    course_link = 'https://www.icourse163.org' + href
                else:
                    course_link = 'https://www.icourse163.org/course/' + href

                # 生成图片URL
                image_url = f"https://picsum.photos/400/200?random={abs(hash(title)) % 10000}"

                course = {
                    "title": title,
                    "university": "中国大学MOOC",
                    "instructor": "待获取",
                    "imageUrl": image_url,
                    "courseLink": course_link,
                    "platform": "icourse163",
                    "category": "计算机",
                    "difficulty": "intermediate"
                }

                courses.append(course)

                if len(courses) >= 12:
                    break

            except Exception as e:
                continue

        # 如果没有找到课程，返回一些示例数据
        if not courses:
            courses = get_fallback_courses()

        return courses

    except ImportError as e:
        # 如果缺少依赖，返回示例数据
        return get_fallback_courses()
    except Exception as e:
        # 其他错误，返回示例数据
        return get_fallback_courses()


def get_fallback_courses():
    """
    当爬虫失败时，返回一些示例课程数据
    """
    # http://localhost:3004/


def main():
    """
    主函数：抓取课程并输出JSON - 修复编码问题
    """
    try:
        # 设置编码
        setup_encoding()

        courses = scrape_courses()

        # 方法1: 直接写入二进制输出（推荐）
        output = json.dumps(courses, ensure_ascii=False, indent=2)

        # 使用二进制写入避免编码问题
        if hasattr(sys.stdout, 'buffer'):
            sys.stdout.buffer.write(output.encode('utf-8'))
            sys.stdout.buffer.write(b'\n')
            sys.stdout.buffer.flush()
        else:
            # 备用方法
            print(output)
            sys.stdout.flush()

        sys.exit(0)

    except Exception as e:
        # 即使出错也输出空列表，避免Java端解析失败
        error_output = json.dumps([], ensure_ascii=False)
        if hasattr(sys.stdout, 'buffer'):
            sys.stdout.buffer.write(error_output.encode('utf-8'))
            sys.stdout.buffer.flush()
        else:
            print(error_output)

        sys.stderr.write(f"Error: {str(e)}\n")
        sys.exit(1)


if __name__ == "__main__":
    main()