import requests
from fake_useragent import UserAgent

class HttpClient:
    def __init__(self):
        self.ua = UserAgent()

    def get(self, url, **kwargs):
        headers = kwargs.pop("headers", {})
        headers["User-Agent"] = self.ua.random
        return requests.get(url, headers=headers, timeout=15, **kwargs)
