import json
import re
from bs4 import BeautifulSoup
from .base import HttpClient
from .models import Course

class ICourseScraper:
    def __init__(self):
        self.client = HttpClient()

    def scrape_channel(self, channel_url: str):
        """
        从频道页面抓取课程，如：
        https://www.icourse163.org/channel/3002.htm
        """
        html = self.client.get(channel_url).text
        soup = BeautifulSoup(html, "html.parser")

        # 页面中包含一个 window.__INITIAL_STATE__ 的 JSON
        scripts = soup.find_all("script")

        json_data = None
        for script in scripts:
            if "window.__INITIAL_STATE__" in script.text:
                match = re.search(r"window.__INITIAL_STATE__\s*=\s*(\{.*\});", script.text)
                if match:
                    json_data = json.loads(match.group(1))
                    break

        if not json_data:
            return []

        # JSON 中课程信息一般在 homePage.courseCardList 内
        course_cards = json_data.get("homePage", {}).get("courseCardList", [])

        courses = []
        for c in course_cards:
            title = c.get("name")
            university = c.get("school", "")
            instructor = c.get("teacherName", "")
            link = f"https://www.icourse163.org/course/{c.get('schoolId')}-{c.get('courseId')}"
            image = c.get("imgUrl")

            courses.append(Course(
                title=title,
                university=university,
                instructor=instructor,
                imageUrl=image,
                courseLink=link
            ))

        return courses
