package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.dto.CourseDTO;
import com.digitalhorsepower.learnbuddy.dto.CourseSearchRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@Slf4j
public class CourseService {

    @Value("${python.crawler.path:python-crawler/run_scraper.py}")
    private String pythonCrawlerPath;

    private final ObjectMapper mapper = new ObjectMapper();

    public List<CourseDTO> getRecommendedCourses() {
        try {
            // 首先尝试Python爬虫
            List<CourseDTO> py = tryPythonCrawler();
            if (py != null && !py.isEmpty()) {
                log.info("Python爬虫成功获取 {} 门课程", py.size());
                return py;
            }

            // 其次尝试Java爬虫
            List<CourseDTO> javaList = tryJavaCrawler();
            if (javaList != null && !javaList.isEmpty()) {
                log.info("Java爬虫成功获取 {} 门课程", javaList.size());
                return javaList;
            }

            // 最后返回Mock数据
            log.info("使用模拟数据");
            return generateHighQualityMockData();

        } catch (Exception e) {
            log.error("获取推荐课程失败: {}", e.getMessage(), e);
            return generateHighQualityMockData();
        }
    }

    private List<CourseDTO> tryPythonCrawler() {
        try {
            // 尝试多个可能的路径
            String[] possiblePaths = {
                    pythonCrawlerPath,
                    "src/main/java/com/digitalhorsepower/learnbuddy/service/python-crawler/run_scraper.py",
                    "learn-buddy-backend/src/main/java/com/digitalhorsepower/learnbuddy/service/python-crawler/run_scraper.py",
                    "../python-crawler/run_scraper.py",
                    "services/python-crawler/run_scraper.py"  // 添加这个路径
            };

            Path scriptPath = null;
            for (String path : possiblePaths) {
                Path p = Paths.get(path);
                if (Files.exists(p)) {
                    scriptPath = p;
                    log.info("找到Python脚本: {}", p.toAbsolutePath());
                    break;
                }
            }

            if (scriptPath == null) {
                log.warn("Python脚本不存在，尝试的路径: {}", Arrays.toString(possiblePaths));
                return new ArrayList<>();
            }

            // 检测Python命令
            String pythonCmd = detectPythonCommand();
            if (pythonCmd == null) {
                log.warn("未找到Python解释器");
                return new ArrayList<>();
            }

            log.info("使用Python命令: {}, 脚本: {}", pythonCmd, scriptPath.toAbsolutePath());

            ProcessBuilder pb = new ProcessBuilder(pythonCmd, scriptPath.toAbsolutePath().toString());
            pb.redirectErrorStream(true);

            // 设置环境变量确保UTF-8编码
            Map<String, String> env = pb.environment();
            env.put("PYTHONIOENCODING", "utf-8");
            env.put("PYTHONUTF8", "1");
            env.put("LANG", "en_US.UTF-8");

            // 设置工作目录为脚本所在目录
            pb.directory(scriptPath.getParent().toFile());

            Process process = pb.start();

            // 使用UTF-8读取输出
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                    log.debug("Python输出: {}", line);
                }
            }

            boolean finished = process.waitFor(30, java.util.concurrent.TimeUnit.SECONDS);
            if (!finished) {
                process.destroy();
                log.warn("Python进程超时");
                return new ArrayList<>();
            }

            int exitCode = process.exitValue();
            log.info("Python进程退出码: {}", exitCode);

            if (exitCode != 0) {
                log.warn("Python脚本执行失败，退出码: {}", exitCode);
                return new ArrayList<>();
            }

            String jsonOutput = output.toString().trim();
            if (jsonOutput.isEmpty()) {
                log.warn("Python脚本无输出");
                return new ArrayList<>();
            }

            log.info("Python输出长度: {} 字符", jsonOutput.length());
            log.debug("Python输出内容: {}", jsonOutput);

            // 解析JSON
            List<Map<String, Object>> list = mapper.readValue(
                    jsonOutput,
                    new TypeReference<List<Map<String, Object>>>() {}
            );

            List<CourseDTO> result = convertToDTOs(list);
            log.info("Python解析到 {} 门课程", result.size());

            // 调试输出前几个课程的标题
            for (int i = 0; i < Math.min(result.size(), 3); i++) {
                log.debug("课程 {}: {}", i + 1, result.get(i).getTitle());
            }

            return result;

        } catch (Exception e) {
            log.warn("调用Python爬虫失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 检测可用的Python命令
     */
    private String detectPythonCommand() {
        String[] commands = {"python", "python3", "py"};
        for (String cmd : commands) {
            try {
                Process process = new ProcessBuilder(cmd, "--version").start();
                boolean finished = process.waitFor(5, java.util.concurrent.TimeUnit.SECONDS);
                if (finished && process.exitValue() == 0) {
                    log.info("检测到Python命令: {}", cmd);
                    return cmd;
                }
            } catch (Exception e) {
                // 继续尝试下一个
            }
        }
        return null;
    }

    private List<CourseDTO> tryJavaCrawler() {
        try {
            log.info("使用Java爬虫尝试抓取icourse163课程");
            String url = "https://www.icourse163.org/channel/3002.htm";

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .timeout(20000)
                    .ignoreHttpErrors(true)
                    .get();

            List<CourseDTO> courses = new ArrayList<>();

            // 尝试多种选择器
            String[] selectors = {
                    "a[href*='/course/']",
                    "div.course-card a",
                    ".g-box a",
                    "a.course-link"
            };

            for (String selector : selectors) {
                Elements links = doc.select(selector);
                log.info("选择器 '{}' 找到 {} 个链接", selector, links.size());

                for (Element link : links) {
                    String text = link.text().trim();
                    String href = link.attr("abs:href");

                    // 过滤有效的课程链接
                    if (text.length() > 5 && href.contains("icourse163.org/course/")) {
                        CourseDTO course = new CourseDTO();
                        course.setTitle(text);
                        course.setUniversity("中国大学MOOC");
                        course.setInstructor("待获取");
                        course.setPlatform("icourse163");
                        course.setCategory("计算机");
                        course.setDifficulty("intermediate");
                        course.setImageUrl(generateCourseImage(text));
                        course.setCourseLink(href);

                        courses.add(course);

                        if (courses.size() >= 12) {
                            break;
                        }
                    }
                }

                if (!courses.isEmpty()) {
                    break;
                }
            }

            log.info("Java爬虫找到 {} 门课程", courses.size());
            return courses;

        } catch (Exception e) {
            log.warn("Java爬虫失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<CourseDTO> convertToDTOs(List<Map<String, Object>> list) {
        List<CourseDTO> dtos = new ArrayList<>();
        for (Map<String, Object> m : list) {
            try {
                CourseDTO dto = new CourseDTO();

                // 确保中文字段正确处理
                String title = Objects.toString(m.get("title"), "");
                String university = Objects.toString(m.get("university"), "");
                String instructor = Objects.toString(m.get("instructor"), "");

                // 调试输出
                log.debug("转换课程 - 标题: {}, 学校: {}, 讲师: {}", title, university, instructor);

                dto.setTitle(title);
                dto.setUniversity(university);
                dto.setInstructor(instructor);
                dto.setImageUrl(Objects.toString(m.get("imageUrl"), ""));
                dto.setCourseLink(Objects.toString(m.get("courseLink"), ""));
                dto.setPlatform(Objects.toString(m.get("platform"), "icourse163"));
                dto.setCategory(Objects.toString(m.get("category"), "计算机"));
                dto.setDifficulty(Objects.toString(m.get("difficulty"), "intermediate"));

                dtos.add(dto);
            } catch (Exception ex) {
                log.warn("转换单条Python结果失败: {}", ex.getMessage());
            }
        }
        return dtos;
    }

    private String generateCourseImage(String title) {
        int seed = Math.abs(title.hashCode());
        return String.format("https://picsum.photos/400/200?random=%d", seed);
    }

    private List<CourseDTO> generateHighQualityMockData() {
        List<CourseDTO> courses = new ArrayList<>();
        Object[][] data = {
                {"Python语言程序设计", "北京理工大学", "嵩天", "计算机", "beginner", "https://www.icourse163.org/course/BIT-268001"},
                {"Java程序设计", "浙江大学", "翁恺", "计算机", "beginner", "https://www.icourse163.org/course/ZJU-1001541001"},
                {"数据结构", "浙江大学", "陈越", "计算机", "intermediate", "https://www.icourse163.org/course/ZJU-93001"},
                {"操作系统", "南京大学", "骆斌", "计算机", "advanced", "https://www.icourse163.org/course/NJU-1001571004"},
                {"高等数学", "西安交通大学", "李继成", "数学", "beginner", "https://www.icourse163.org/course/XJTU-1001756006"},
                {"线性代数", "同济大学", "靳全勤", "数学", "intermediate", "https://www.icourse163.org/course/TONGJI-481001"},
                {"机器学习", "浙江大学", "胡浩基", "计算机", "advanced", "https://www.icourse163.org/course/ZJU-1206573810"},
                {"深度学习", "复旦大学", "赵卫东", "计算机", "advanced", "https://www.icourse163.org/course/FUDAN-1205806833"},
                {"Web前端开发", "北京大学", "李戈", "计算机", "intermediate", "https://www.icourse163.org/course/PKU-1001541001"},
                {"数据库系统", "中国人民大学", "王珊", "计算机", "intermediate", "https://www.icourse163.org/course/RUC-1001541001"},
                {"计算机网络", "哈尔滨工业大学", "李全龙", "计算机", "intermediate", "https://www.icourse163.org/course/HIT-1001541001"},
                {"算法设计与分析", "北京大学", "屈婉玲", "计算机", "advanced", "https://www.icourse163.org/course/PKU-1001539001"},
                {"线性代数精讲", "汕头大学", "张小向", "数学", "advanced", "https://www.icourse163.org/course/STDX-1466006162"},
                {"离散数学", "电子科技大学", "王丽杰", "数学", "advanced", "https://www.icourse163.org/course/UESTC-1002268006"},
                {"数学建模", "厦门大学", "谭思", "数学", "intermediate", "https://www.icourse163.org/course/XMU-1001556009"},
                {"数学实验", "北京理工大学", "王宏洲", "数学", "intermediate", "https://www.icourse163.org/course/BIT-1001756010"},
                {"大学英语（1）", "郑州西亚斯学院", "王银平", "语言", "beginner", "https://www.icourse163.org/course/SIAS-1460230164"},
                {"大学日语", "西安交通大学", "张文丽", "语言", "intermediate", "https://www.icourse163.org/course/XJTU-1002533017"},
                {"大学英语进阶课程", "厦门大学", "张龙海／陈婷／刘岩", "语言", "intermediate", "https://www.icourse163.org/course/XMU-1002332006"},
                {"俄语基础语法", "北京航空航天大学", "武晓霞", "语言", "intermediate", "https://www.icourse163.org/course/BUAA-1206676813"},
                {"大学英语(Ⅲ) — 一带一路沿线国家文化专题", "北京联合大学", "–", "语言", "intermediate", "https://www.icourse163.org/course/BUU-1461302162"},
                {"基本西班牙语", "广东外语外贸大学", "陈星", "语言", "beginner", "https://www.icourse163.org/course/GDUFS-1207482806"},
                {"大学英语词汇", "北京交通大学", "唐学华", "语言", "intermediate", "https://www.icourse163.org/course/NJTU-1002528009"},
                {"深度学习及其应用", "复旦大学", "赵卫东", "计算机 / AI", "advanced", "https://www.icourse163.org/course/FUDAN-1205806833"},
                {"机器视觉与边缘计算应用", "复旦大学", "赵卫东", "计算机 / AI", "advanced", "https://www.icourse163.org/course/FUDAN-1456632162"},
                {"图形编程技术", "北京林业大学", "杨刚", "计算机", "intermediate", "https://www.icourse163.org/course/BJFU-1205908812"},
                {"计算机视觉", "东北大学", "贾同", "计算机", "intermediate", "https://www.icourse163.org/course/NEU-1465996192"},
                {"计算机算法与程序设计(python)", "电子科技大学", "戴波", "计算机", "intermediate", "https://www.icourse163.org/course/UESTC-0809ESTC112-1206680838"},
                {"人工智能、语言与伦理", "复旦大学", "徐英瑾", "计算机 + 语言", "advanced", "https://www.icourse163.org/course/FUDAN-1205951803"}


        };

        for (int i = 0; i < data.length; i++) {
            Object[] d = data[i];
            CourseDTO c = new CourseDTO();
            c.setTitle((String) d[0]);
            c.setUniversity((String) d[1]);
            c.setInstructor((String) d[2]);
            c.setCategory((String) d[3]);
            c.setDifficulty((String) d[4]);
            c.setCourseLink((String) d[5]);
            c.setPlatform("icourse163");
            c.setImageUrl(generateCourseImage(c.getTitle()));
            courses.add(c);
        }

        log.info("生成 {} 门模拟课程", courses.size());
        return courses;
    }

    public List<CourseDTO> searchCourses(CourseSearchRequest request) {
        List<CourseDTO> all = getRecommendedCourses();
        List<CourseDTO> filtered = new ArrayList<>();

        for (CourseDTO c : all) {
            boolean match = true;

            // 关键词搜索
            if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
                String kw = request.getKeyword().toLowerCase();
                if (!(c.getTitle().toLowerCase().contains(kw) ||
                        c.getUniversity().toLowerCase().contains(kw) ||
                        c.getInstructor().toLowerCase().contains(kw))) {
                    match = false;
                }
            }

            // 分类过滤
            if (match && request.getCategory() != null && !request.getCategory().isEmpty()) {
                if (!request.getCategory().equals(c.getCategory())) {
                    match = false;
                }
            }

            // 难度过滤
            if (match && request.getDifficulty() != null && !request.getDifficulty().isEmpty()) {
                if (!request.getDifficulty().equals(c.getDifficulty())) {
                    match = false;
                }
            }

            if (match) {
                filtered.add(c);
            }
        }

        log.info("搜索结果: {} 门课程", filtered.size());
        return filtered;
    }
}