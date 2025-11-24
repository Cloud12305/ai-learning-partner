from dataclasses import dataclass

@dataclass
class Course:
    title: str
    university: str
    instructor: str
    imageUrl: str
    courseLink: str
    platform: str = "icourse163"
    category: str = "计算机"
    difficulty: str = "intermediate"
