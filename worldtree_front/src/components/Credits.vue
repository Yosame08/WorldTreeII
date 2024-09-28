<script setup>
</script>

<template>
  <div class="credits-container">
    <div class="credits-content">
      <p><strong>反转！蒸旦宇宙</strong></p>
      <p><strong>复旦信息网络安全协会 × 复旦算法协会</strong></p>
      <br>
      <p><strong>题目来源</strong></p>
      <p>鸳鸯锅    Paky</p>
      <p>蛋挞    Paky</p>
      <p>大锅饭/蒸小笼包    lc174</p>
      <p>探店    Yosame</p>
      <p>生日蛋糕    lc174</p>
      <p>掰与狼与孤独摇滚    反转平行宇宙</p>
      <p>蛋卷    Cameudis</p>
      <p>华夫饼    Cameudis</p>
      <p>魔塔电报员    Yosame</p>
      <p>嗑瓜子    Paky</p>
      <p>蒸盐    Cameudis</p>
      <p>菜谱    lc174</p>
      <p>时光的逆行者    Paky</p>
      <p>上古方块    lc174</p>
      <br>
      <p><strong>美术资源</strong></p>
      <p>Thymol</p>
      <p>Cameudis</p>
      <br>
      <p><strong>网页搭建</strong></p>
      <p>Yosame</p>
      <p>Tarjan01</p>
      <p>全球通</p>
      <p>秋静葉</p>
      <p>Cameudis</p>
      <br>
      <p><strong>以及，感谢用心游玩游戏的你！</strong></p>
      <br>
      <p>（所以……为什么隐藏最终关卡的贴纸长这样呢？）</p>
    </div>
  </div>
</template>

<style scoped>
.credits-container {
  position: relative;
  width: 100%;
  height: 100vh;
  background-color: black;
  color: white;
  overflow: hidden;
}

.credits-content {
  position: absolute;
  bottom: -100%;
  width: 100%;
  animation: scroll-up 30s linear infinite;
}

@keyframes scroll-up {
  0% {
    bottom: -100%;
  }
  100% {
    bottom: 100%;
  }
}

p {
  margin: 0;
  padding: 5px 0;
  text-align: center;
}
</style>