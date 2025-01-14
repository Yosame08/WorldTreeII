package com.transAI.interceptors;

import com.transAI.mapper.UserSubmitMapper;
import com.transAI.utils.JwtUtil;
import com.transAI.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserSubmitMapper userSubmitMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 跨域请求前会先发一个OPTIONS请求，不含token，放行
        if(request.getMethod().equals(HttpMethod.OPTIONS.toString())) {
            System.out.println("OPTIONS request");
            return true;
        }
        String token = request.getHeader("Authorization");
//        return true;

        // 验证token
        try {
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            String redisToken = operations.get(token);

            if(redisToken == null) {
                throw new RuntimeException();
            }
            Map<String, Object> claims = JwtUtil.parseToken(token);
            ThreadLocalUtil.set(claims);
            try {
                int id = (int) claims.get("id");
                int size = userSubmitMapper.userSubmitSize(id);
                if(size < 10) {
                    userSubmitMapper.insertUserSubmit(id);
                }
                else {
                    LocalDateTime earliestTime = userSubmitMapper.getEarliestTime(id);

                    LocalDateTime now = LocalDateTime.now();

                    if(now.minusSeconds(1).isBefore(earliestTime)) {
                        throw new Exception();
                    }

                    userSubmitMapper.deleteUserSubmit(id, earliestTime);
                    userSubmitMapper.insertUserSubmit(id);
                }
            } catch(Exception e) {
                System.err.println("too frequent");
                response.setStatus(429);
                return false;
            }
            return true;
        } catch (Exception e) {
            System.err.println("Token failed:{");
            System.err.println(token+"\n}"+e.getMessage()+"\n");
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清空ThreadLocal
        ThreadLocalUtil.remove();
    }
}
