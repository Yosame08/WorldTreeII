<template>
  <div class="page-background">
    <div class="number-container">
      <div v-for="(num, index) in numbers" :key="index" class="number" @click="addToPot(num)">
        {{ num }}
      </div>
    </div>
    <div class="bigpot-container">
      <div class="pot">
        <div v-for="(num, index) in pot" :key="index" class="number" @click="removeFromPot(index)">
          {{ num }}
        </div>
      </div>
      <div class="operators">
        <img src="@/assets/bigpot/0.png" @click="setOperator(0)" alt="operator 0" />
        <img src="@/assets/bigpot/1.png" @click="setOperator(1)" alt="operator 1" />
        <img src="@/assets/bigpot/2.png" @click="setOperator(2)" alt="operator 2" />
      </div>
    </div>
    <div class="button-container">
      <button @click="cook" :disabled="pot.length < 2">Cook</button>
      <button v-if="pass" @click="nextLevel">Next Level</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import {universalGet} from "@/services/userService";

export default {
  data() {
    return {
      numbers: [],
      pot: [],
      operator: null,
      pass: false,
      gameToken: '',
    };
  },
  methods: {
    async initGame() {
      try {
        const response = await universalGet('/api/bigpot/init');
        if (response && response.data && response.data.code === 0) {
          this.numbers = response.data.data.array;
          this.gameToken = response.data.data.game_token;
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
      }
    },
    removeFromPot(index) {
      this.numbers.push(this.pot[index]);
      this.pot.splice(index, 1);
    },
    setOperator(op) {
      this.operator = op;
    },
    async cook() {
      try {
        const response = await axios.post('/api/bigpot/cook', {
          game_token: this.gameToken,
          num: this.pot,
          operator: this.operator,
        });
        if (response.data.code === 0) {
          if (response.data.data.pass === 1) {
            this.pass = true;
          } else {
            this.numbers.push(response.data.data.result);
            this.pot = [];
            this.operator = null;
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
      await this.initGame();
    },
  },
  mounted() {
    this.initGame();
  },
};
</script>

<style scoped>
.page-background {
  background-color: #001f3f;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  height: calc(100vh - 39px);
}

.number-container{
  flex-direction: row;
}

.bigpot-container {
  background-color: #1e1e1e;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
}

.numbers, .pot {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.number {
  background-color: #555;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 20px;
  cursor: pointer;
  box-shadow: 0 4px #333;
  transition: background-color 0.3s, box-shadow 0.1s;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5em;
}

.operators {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.operators.img {
  width: 50px; /* Adjust the width as needed */
  height: auto; /* Maintain aspect ratio */
  scale: 20%;
}

button {
  padding: 10px 20px;
  background-color: #900;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:disabled {
  background-color: #555;
}

button:hover:not(:disabled) {
  background-color: #b00;
}
</style>