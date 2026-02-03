package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.dto.*;
import com.digitalhorsepower.learnbuddy.entity.JobPosition;
import com.digitalhorsepower.learnbuddy.entity.UserCareerPreference;
import com.digitalhorsepower.learnbuddy.repository.JobPositionRepository;
import com.digitalhorsepower.learnbuddy.repository.UserCareerPreferenceRepository;
import com.digitalhorsepower.learnbuddy.repository.UserSkillRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobMatchingService {

    private final JobPositionRepository jobPositionRepository;
    private final UserSkillRepository userSkillRepository;
    private final UserCareerPreferenceRepository careerPreferenceRepository;

    // 行业映射表 - 将用户选择的行业映射到相关的公司
    private final Map<String, List<String>> industryCompanyMap = Map.of(
            "游戏", Arrays.asList("腾讯游戏", "网易游戏", "米哈游", "完美世界", "巨人网络", "畅游"),
            "互联网", Arrays.asList("阿里巴巴", "腾讯", "百度", "字节跳动", "美团", "京东"),
            "医疗健康", Arrays.asList("阿里健康", "平安好医生", "微医", "丁香园", "腾讯医疗"),
            "金融", Arrays.asList("蚂蚁集团", "京东数科", "平安科技", "招商银行", "中国银行"),
            "教育", Arrays.asList("猿辅导", "作业帮", "好未来", "新东方", "网易有道"),
            "电子商务", Arrays.asList("阿里巴巴", "京东", "拼多多", "唯品会", "小红书"),
            "人工智能", Arrays.asList("百度AI", "商汤科技", "旷视科技", "云从科技", "依图科技"),
            "企业服务", Arrays.asList("钉钉", "企业微信", "飞书", "用友", "金蝶")
    );

    public JobMatchResponseDTO matchJobs(JobMatchRequestDTO request) {
        Long userId = request.getUserId();

        // 1. 获取用户技能
        List<String> userSkills = userSkillRepository.findSkillNamesByUserId(userId);
        if (userSkills == null) {
            userSkills = new ArrayList<>();
        }

        // 2. 获取用户职业偏好
        UserCareerPreference preference = careerPreferenceRepository.findByUserId(userId)
                .orElse(new UserCareerPreference());

        // 3. 从数据库获取所有岗位
        List<JobPosition> allJobs = jobPositionRepository.findAll();

        // 4. 计算匹配度并排序
        List<String> finalUserSkills = userSkills;
        List<MatchedJobDTO> matchedJobs = allJobs.stream()
                .map(job -> calculateJobMatch(job, finalUserSkills, preference))
                .filter(job -> job.getMatchScore() > 0.3) // 过滤掉匹配度过低的
                .sorted((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()))
                .collect(Collectors.toList());

        // 5. 构建响应
        JobMatchResponseDTO response = new JobMatchResponseDTO();
        response.setUserId(userId);
        response.setMatchedJobs(matchedJobs);
        response.setRecommendation(generateRecommendation(matchedJobs, userSkills, preference));

        return response;
    }

    private MatchedJobDTO calculateJobMatch(JobPosition job, List<String> userSkills, UserCareerPreference preference) {
        MatchedJobDTO matchedJob = new MatchedJobDTO();
        matchedJob.setJobId(job.getId());
        matchedJob.setJobTitle(job.getJobTitle());
        matchedJob.setCompanyName(job.getCompanyName());
        matchedJob.setLocation(job.getLocation());
        matchedJob.setJobDescription(job.getJobDescription());
        matchedJob.setSalaryRange(job.getSalaryRange());

        // 解析 required_skills JSON 字段
        List<String> requiredSkills = parseRequiredSkills(job.getRequiredSkills());
        matchedJob.setRequiredSkillTags(convertToSkillTags(requiredSkills));

        // 计算技能匹配
        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (String skill : requiredSkills) {
            if (userSkills.contains(skill)) {
                matchedSkills.add(skill);
            } else {
                missingSkills.add(skill);
            }
        }

        matchedJob.setMatchedSkills(matchedSkills);
        matchedJob.setMissingSkills(missingSkills);

        // 计算综合匹配度 - 行业匹配权重最高
        double skillMatchScore = requiredSkills.isEmpty() ? 0 : (double) matchedSkills.size() / requiredSkills.size();
        double positionMatchScore = calculatePositionMatch(job.getJobTitle(), preference.getExpectedPosition());
        double industryMatchScore = calculateIndustryMatch(job.getCompanyName(), preference.getExpectedIndustry());
        double locationMatchScore = calculateLocationMatch(job.getLocation(), preference.getExpectedLocation());

        // 调整权重：行业匹配最重要(40%)，职位匹配次之(30%)，技能匹配(20%)，地点匹配(10%)
        double finalScore = industryMatchScore * 0.4 + positionMatchScore * 0.3 + skillMatchScore * 0.2 + locationMatchScore * 0.1;
        matchedJob.setMatchScore(Math.min(finalScore, 1.0));

        // 设置匹配分析信息
        matchedJob.setPositionMatch(positionMatchScore > 0.7);
        matchedJob.setIndustryMatch(industryMatchScore > 0.7);

        return matchedJob;
    }

    private double calculateIndustryMatch(String companyName, String expectedIndustry) {
        if (expectedIndustry == null || expectedIndustry.trim().isEmpty()) {
            return 0.5;
        }

        // 根据公司名称判断行业
        List<String> targetCompanies = industryCompanyMap.get(expectedIndustry);
        if (targetCompanies != null) {
            for (String company : targetCompanies) {
                if (companyName.contains(company)) {
                    return 1.0;
                }
            }
        }

        // 如果公司不在映射表中，使用默认逻辑
        if (expectedIndustry.equals("游戏") && (companyName.contains("游戏") || companyName.contains("互娱"))) {
            return 0.9;
        }

        return 0.2; // 行业不匹配
    }

    private List<String> parseRequiredSkills(String requiredSkillsJson) {
        try {
            if (requiredSkillsJson == null || requiredSkillsJson.trim().isEmpty()) {
                return Arrays.asList("Java", "Spring Boot", "MySQL");
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(requiredSkillsJson, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Arrays.asList("Java", "Spring Boot", "MySQL");
        }
    }

    private List<SkillTagDTO> convertToSkillTags(List<String> skills) {
        return skills.stream().map(skill -> {
            SkillTagDTO tag = new SkillTagDTO();
            tag.setName(skill);
            tag.setColor("blue");
            return tag;
        }).collect(Collectors.toList());
    }

    private double calculatePositionMatch(String jobTitle, String expectedPosition) {
        if (expectedPosition == null || expectedPosition.trim().isEmpty()) {
            return 0.5;
        }

        String jobTitleLower = jobTitle.toLowerCase();
        String expectedPositionLower = expectedPosition.toLowerCase();

        if (jobTitleLower.contains(expectedPositionLower)) {
            return 1.0;
        } else if (jobTitleLower.contains("后端") && expectedPositionLower.contains("后端")) {
            return 0.9;
        } else if (jobTitleLower.contains("java") && expectedPositionLower.contains("java")) {
            return 0.9;
        } else if (jobTitleLower.contains("开发") && expectedPositionLower.contains("开发")) {
            return 0.7;
        } else {
            return 0.3;
        }
    }

    private double calculateLocationMatch(String jobLocation, String expectedLocation) {
        if (expectedLocation == null || expectedLocation.trim().isEmpty()) {
            return 0.5;
        }
        return jobLocation.equals(expectedLocation) ? 1.0 : 0.3;
    }

    private String generateRecommendation(List<MatchedJobDTO> matchedJobs, List<String> userSkills, UserCareerPreference preference) {
        if (matchedJobs.isEmpty()) {
            return "暂无匹配的岗位，建议完善技能和求职意向";
        }

        // 分析行业匹配情况
        long industryMatchCount = matchedJobs.stream()
                .filter(MatchedJobDTO::isIndustryMatch)
                .count();

        if (industryMatchCount == 0 && preference.getExpectedIndustry() != null) {
            return "当前" + preference.getExpectedIndustry() + "行业的岗位较少，建议关注其他行业或调整求职意向";
        }

        // 分析缺失技能
        Set<String> allMissingSkills = matchedJobs.stream()
                .flatMap(job -> job.getMissingSkills().stream())
                .limit(3)
                .collect(Collectors.toSet());

        if (!allMissingSkills.isEmpty()) {
            return "建议重点学习以下技能以提升匹配度：" + String.join("、", allMissingSkills);
        }

        return "您的技能与目标岗位匹配度较高，建议积极投递简历";
    }
}