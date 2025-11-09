import request from '@/utils/request';

export const psychologyService = {
    // 分析情绪
    analyzeEmotion(text, userId = '') {
        return request.post('/api/psychology', {
            text,
            userId
        });
    }
};