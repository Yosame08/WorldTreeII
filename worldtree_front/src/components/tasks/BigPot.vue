<template>
  <div class="page-background">
    <p class="level-dest"> 请用4个数字烹饪出24点 </p>
    <div class="number-container">
      <div v-for="(num, index) in numbers" :key="index" class="number" @click="addToPot(num)">
        {{ num }}
      </div>
    </div>
    <font-awesome-icon :icon="['fas', 'fire-burner']" class="fire-icon"/>
    <div class="bigpot-container">
      <div class="operators">
        <div class="operator-title">烹饪方式</div>
        <button v-for="op in [0, 1, 2]" :key="op" class="operator" @click="setOperator(op)">
          <img :src="require(`@/assets/bigpot/${op}.png`)" :alt="`operator ${op}`" />
        </button>
      </div>
      <div class="pot-contents">
        <div class="pot-contents-title">锅的内部</div>
        <div v-if="pot.length === 0">空</div>
        <div v-else>
          <div v-for="(num, index) in pot" :key="index" class="number" @click="removeFromPot(index)">
            {{ num }}
          </div>
        </div>
      </div>
    </div>
    <div class="button-container">
      <button @click="resetGame">重来！</button>
      <button @click="cook" :disabled="pot.length < 2 || operator === null">开煮！</button>
    </div>
    <div v-if="pass" class="modal">
      <div class="modal-content">
        <h2>{{ passMessage }}</h2>
        <p>{{ level !== 4 ? "继续努力，完成第" + (level+1) + "锅！" : "获得笔记残页和贴纸，请及时查看" }}</p>
        <button @click="nextLevel">下一锅</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import {universalGet, universalPost} from "@/services/universalService";
import store from "@/services/storeService";

export default {
  data() {
    return {
      level: null,
      numbers: [],
      pot: [],
      operator: null,
      pass: false,
      gameToken: '',
      passMessage: '',
    };
  },
  methods: {
    async initGame() {
      try {
        const response = await universalGet('/api/subtask/bigpot/init');
        if (response && response.data && response.data.code === 0) {
          let data = response.data.data;
          this.numbers = [data.x, data.y, data.z, data.w];
          this.gameToken = data.gameToken;
          this.level = data.level; // 获取并存储关卡 level
        } else {
          console.error(response?.data?.message || 'Unexpected error');
        }
      } catch (error) {
        console.error(error);
      }
    },
    addToPot(num) {
      if (this.pot.length < 2) {
        this.pot.push(num);
        this.numbers.splice(this.numbers.indexOf(num), 1);
        const icon = document.querySelector('.fire-icon');
        if (icon) {
          icon.classList.add('shake');
          setTimeout(() => icon.classList.remove('shake'), 500); // Remove the class after the animation
        }
      }
    },
    removeFromPot(index) {
      this.numbers.push(this.pot[index]);
      this.pot.splice(index, 1);
    },
    setOperator(op) {
      if (this.operator === op) {
        // If the same operator is clicked, deselect it
        this.operator = null;
        document.querySelectorAll('.operator').forEach(button => button.classList.remove('selected'));
      } else {
        // Otherwise, select the new operator
        this.operator = op;
        const buttons = document.querySelectorAll('.operator');
        buttons.forEach(button => button.classList.remove('selected'));
        buttons[op].classList.add('selected');
      }
    },
    async cook() {
      try {
        const response = await universalPost('/api/subtask/bigpot/cook', {
          gameToken: this.gameToken,
          x: this.pot[0],
          y: this.pot[1],
          operator: this.operator,
        });
        if (response.data.code === 0) {
          this.numbers.push(response.data.data.result);
          this.pot = [];
          this.operator = null;
          document.querySelectorAll('.operator').forEach(button => button.classList.remove('selected'));
          if (response.data.data.pass === 1) {
            this.passMessage = this.level === 4 ? '煮完4锅大锅饭，终于可以下班了！' : '大锅饭一端上来，同学们都馋哭了！';
            this.pass = true;
          }
        } else {
          console.error(response.data.message);
        }
      } catch (error) {
        console.error(error);
      }
    },
    async nextLevel() {
      this.pass = false;
      await this.resetGame();
    },
    async resetGame() {
      // 重置游戏状态并重新初始化游戏
      this.pot = [];
      this.operator = null;
      document.querySelectorAll('.operator').forEach(button => button.classList.remove('selected'));
      await this.initGame();
    }
  },
  mounted() {
    this.initGame();
  },
};
</script>

<style scoped>
.page-background {
  background-color: #ffdb98;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  height: calc(100vh - 43px);
}

.number-container {
  display: flex;
  flex-direction: row;
  position: absolute;
  top: 25%;
  width: 100%;
  justify-content: center;
}

.bigpot-container {
  flex-direction: row;
  padding: 20px;
  border-radius: 10px;
  display: flex;
  align-items: center;
}

.number {
  background-color: #555;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 20px;
  width: 20px;
  margin-left: 10px;
  cursor: pointer;
  box-shadow: 0 4px #333;
  transition: background-color 0.3s, box-shadow 0.1s;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.1em;
  aspect-ratio: 1 / 1; /* Ensure the block is square */
}

@keyframes shake {
  0%, 100% { transform: translateX(0) translateY(-50%); }
  25% { transform: translateX(-6px) translateY(-50%); }
  50% { transform: translateX(6px) translateY(-50%); }
  75% { transform: translateX(-6px) translateY(-50%); }
}

.shake {
  animation: shake 0.5s;
}

.pot-contents {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 20px;
  position: absolute;
  left: 25%; /* Adjust as needed to position it to the left of the pot */
  top: 50%;
  transform: translateY(-50%);
  align-items: center;
}

.pot-contents-title {
  font-size: 1.2em;
  margin-bottom: 10px;
  text-align: center;
}

.operators {
  display: flex;
  flex-direction: column; /* Change to column for vertical alignment */
  gap: 10px;
  margin-bottom: 20px;
  position: absolute;
  right: 25%; /* Adjust as needed to position it to the right of the pot */
  top: 50%;
  transform: translateY(-50%);
  align-items: center;
}

.operator-title {
  font-size: 1.2em;
  margin-bottom: 10px;
  text-align: center;
}

.operator {
  background-color: #ffebcc; /* Lighter yellow color */
  border: none;
  border-radius: 5px;
  padding: 20px;
  width: 50px;
  height: 50px;
  cursor: pointer;
  box-shadow: 0 4px #ccc;
  transition: background-color 0.3s, box-shadow 0.1s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.operator:active, .operator.selected {
  background-color: #ec6b00; /* Darker yellow when pressed */
  box-shadow: 0 2px #ccc;
  transform: translateY(2px);
}

.operators img {
  width: 50px; /* Adjust the width as needed */
  height: auto; /* Maintain aspect ratio */
  scale: 100%;
  user-select: none; /* 禁用文本选择 */
  -webkit-user-drag: none; /* 禁用图片拖动 */
}

button {
  padding: 10px 20px;
  background-color: #900;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-left: 10px;
}

button:disabled {
  background-color: #555;
}

button:hover:not(:disabled) {
  background-color: #ffaf6d;
}

.fire-icon{
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  font-size: 4em;
  color: #dd3300;
}

.button-container{
  position: absolute;
  bottom: 25%;
}

.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  text-align: center;
  max-width: 90%; /* 自适应宽度 */
  word-wrap: break-word; /* 防止长单词溢出 */
}

.modal-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.modal-content h2 {
  margin-bottom: 20px;
}

.modal-content button {
  padding: 10px 20px;
  background-color: #900;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.modal-content button:hover {
  background-color: #ffaf6d;
}

.level-dest{
  position: absolute;
  top: 20%;
}
</style>
