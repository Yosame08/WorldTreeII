package com.transAI;

import org.junit.jupiter.api.Test;

public class GptApiUtilTest {

    @Test
    public void testCallGptApi() {
        String content = "缆车又颠了一下，停了。没法朝前开了，大雪给风刮得严严实实地积在车道上。冲刷高山裸露表层的狂风把向风一面的雪刮成一层冰壳。尼克正在行李车厢里给滑雪板上蜡，把靴尖塞进滑雪板上的铁夹，牢牢扣上夹子。他从车厢边缘跳下，落脚在硬邦邦的冰壳上，来一个弹跳旋转，蹲下身子，把滑雪杖拖在背后，一溜烟滑下山坡。";
        String response = GptApiUtil.callGptApi(content);
        System.out.println(response);
    }
}
