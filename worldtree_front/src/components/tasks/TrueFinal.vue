<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { universalPost } from '@/services/universalService';
import store from '@/services/storeService';

const isSubmitting = ref(false);
const taskAnswer = ref('');
const showPic = ref(true);

const download = () => {
  const link = document.createElement('a');
  link.href = '/static/Tru3_fina1_Qu3st10n.jpg';
  link.download = 'Tru3_fina1_Qu3st10n.jpg';
  link.click();
};

const submitAnswer = async () => {
  if (isSubmitting.value) return;
  isSubmitting.value = true;
  try {
    const response = await universalPost('/api/task/submit', { taskId: 22, answer: taskAnswer.value });
    if (response.data.code === 0) {
      let pass = response.data.data;
      if (pass) {
        showPic.value = false;
        store.commit("setSuccessMsg", "答案正确，恭喜通关隐藏关卡！");
      } else {
        store.commit("setErrorMsg", "答案错误");
      }
    }
  } catch (error) {
    console.error('Error submitting answer:', error);
  } finally {
    isSubmitting.value = false;
  }
};

const showImage = () => {
  showPic.value = true;
};

const screenHeight = ref(window.innerHeight);
const screenWidth = ref(window.innerWidth);

const containerWidth = computed(() => {
  return screenWidth.value > screenHeight.value ? `${screenHeight.value}px` : '100%';
});

const updateDimensions = () => {
  screenHeight.value = window.innerHeight;
  screenWidth.value = window.innerWidth;
};

const showCredits = () => {
  // 打开新网页/credits
  window.open('/credits', '_blank');
};

onMounted(() => {
  window.addEventListener('resize', updateDimensions);
});

onUnmounted(() => {
  window.removeEventListener('resize', updateDimensions);
});
</script>

<template>
  <div class="centered-container" :style="{ width: containerWidth }">
    <p v-if="!showPic">
      随着论坛中名为“乐逝大魔王”的神秘人的指引，我在校园中不断走着。回过神来，天空早已染上墨色，细碎的雨珠朝我的脸上打来。我发现自己置身草坪之上，正对着那栋高耸入云的双子楼。
      <br>
      狂风呼啸，又一棵巨树轰然倒地，暴雨从四面八方席卷而来，几乎将我淹没。我狂奔向面前的建筑，越级跳上台阶，在只剩一步之遥时忽然脚下一滑——差点摔倒。我稳住脚步，望向建筑物的入口，原本是大门的位置此时只剩一个空荡荡的洞，附近的地面上铺满了碎裂的玻璃。
      <br>
      建筑内部不复平日的明亮，整个大厅一片漆黑。我小心地穿过门洞，在黑暗中摸索着前进。忽然，一道身影凭空出现，并朝我而来。借着微弱的光线，我看到了面前人的身形：一个穿着长风衣的高个男子。
      <br>
      “你可以叫我青金。<b><i>跟我来。</i></b>”
      <br>
      风衣男子的话语似乎蕴含着某种魔力，使我下意识地想要遵从他的吩咐。我想问点什么，但没敢问出口，只是默默地跟在他身后，一路登上顶楼。
      <br>
      “来这里做什么？”来到顶楼，我终于鼓起勇气发问。
      <br>
      “你什么都不用做。”
      <br>
      我愣在原地。
      <br>
    </p>
    <span v-if="!showPic" class="poem">
      「房宿，临兹土；<br>
      觜宿，客兹宫；<br>
      司怪，收兹邪；<br>
      键闭，守兹室；<br>
      钩钤，封兹扉；<br>
      列星，陈如予述！」
    </span>
    <p v-if="!showPic">
      <br>
      巨大的落地窗前，“青金”背对着我，无源之风将他的风衣撑开数秒，诡异的蓝光若隐若现，东方的天边显现出几颗极为明亮的……星？随后，万籁俱寂。撞击玻璃的狂风迅速平息，暴雨也随之骤然变小，原本一片漆黑的街道也被点点灯光点亮。
      <br>
      一切似乎已经回归正常，然而我却感到双腿有些发软。
      <br>
      “我来这是干什么的？”我的声音在颤抖。
      <br>
      “只要你在这，我就能封印那个来自另一个世界的你——那是这一切的源头。不过，那不再能影响你了。”
      <br>
      “什么意思？那位‘破界者’是一切的源头？”
      <br>
      “‘破界者’从一开始就不存在。宇宙已经包揽了全部的时间和全部的空间，不同的宇宙之间并无真正的差异，你就是破界者，破界者就是你。”
      <br>
      “那究竟是谁在影响我？乐逝大魔王吗？”
      <br>
      “乐逝大魔王……呵呵呵，真是个有趣的名字呢。虽然宇宙之间并无差异，但智能体的意识，或者用更正式的说法——模因，是超脱于宇宙之外的。”
      <br>
      “所以，这个名叫‘乐逝大魔王’的模因影响了我，而我自己引发了这场危机。”
      <br>
      “不如说，这场危机本来也是虚构的。模因从人的意识中来，也智能作用在人的意识上……这股顽皮的模因能量感受到了你平凡的大学生活中对于快乐的渴望，附着在了你的身上，虚构出了整个蒸旦宇宙世界观。怎么样，成为救世主，一点一点解决各种谜题，还有最了解你自己的破界者随时陪你聊天，这些时光很快乐吧？”
      <br>
      “……那你又是谁？青金?”
      <br>
      “我？不过是另一股模因能量而已。我与‘乐逝’彼此相反，追寻‘乐逝’模因的踪迹而来，追求宿命的湮灭。”
      <br>
      那些星星正在暗下去。
      <br>
      “走吧。你将会忘掉这些事。”
      <br>
      但我不想。我听说场景和细节能在失忆后帮助记忆恢复，虽然他可能用的是什么魔法，但我仍然要试试。
      <br>
      看着墙角，我偷偷记下了墙边金属牌上人名里的第3个字【三】，然后在他的注视下迈入了敞开的电梯门。
      <br>
      我特意带他路过我最熟悉的地方：那两栋教学楼的中间。我想，这会对我产生帮助。在那里，我依次记下了来自《阁夜》中的对联的第2个字【更】和第10个字【星】，一首四言诗的第11个字【光】，和楼梯侧歌颂梅花词句的第37和38个字【烂漫】——它们都挂在其中一栋教学楼一楼的墙上。
      <br>
      3,2,10,11,37,38……我不断在心里默念。不知不觉，我们到达了目的地。
      <br>
      “青金”缓缓对我说：“记忆不能产生大片空区。选一个主题吧，我会把你所有和异常有关的记忆变成这个主题下的故事。”
      <br>
      “我饿了，就选美食主题吧。”
      <br>
      ---
      我从宿舍的床上醒来。
      <br>
      3,2,10,11,37,38……它们在敲击我的记忆。
      <br>
      对，我要连起这串文字，去找回丢失的记忆。
    </p>
    <el-button type="primary" v-if="!showPic" @click="showCredits">下一步</el-button>
    <img src="/static/Tru3_fina1_Qu3st10n.jpg" v-if="showPic" alt="Final Stage Picture" style="width: 100%; margin: 20px 0;" />
    <el-input v-if="showPic" v-model="taskAnswer" placeholder="输入答案" />
    <el-button v-if="showPic" @click="download" style="margin: 0;" >下载图片</el-button>
    <el-button type="primary" v-if="showPic" @click="submitAnswer" :disabled="isSubmitting" style="margin: 0;" >提交答案</el-button>
  </div>
</template>

<style scoped>
.centered-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  margin: 0 auto;
  max-width: 80%;
  background-color: #e0e0e0; /* 设置背景颜色为灰色 */
}

.centered-container p {
  margin-bottom: 20px;
  text-align: left;
}

.centered-container button {
  margin: 10px 0;
}

.poem {
  font-family: 'HYCuZhuanF', sans-serif;
  font-size: 2em;
  text-align: center;
}

@font-face {
  font-family: 'HYCuZhuanF';
  src: url('@/assets/HYCuZhuanF-Regular.woff2') format('woff2');
  font-weight: normal;
  font-style: normal;
}

</style>