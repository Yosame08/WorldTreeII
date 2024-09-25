<template>
  <div>
    <el-card v-for="(card, index) in cards" :key="index" class="box-card">
      <div slot="header" class="clearfix" style="margin: 20px;">
        <span>{{ card.title }}</span>
      </div>
      <div v-if="card.pass">已找到该地点</div>
      <div v-else>
        <el-button type="primary" @click="checkOrientation(index)">获取定位</el-button>
        <p v-if="card.dire !== null && card.dist !== null">
          方向: {{ directions[card.dire] }}, 距离: {{ distances[card.dist] }}
        </p>
      </div>
    </el-card>
  </div>
</template>

<script>
import {universalGet, universalPost} from "@/services/universalService";

export default {
  name: 'OpenBox',
  data() {
    return {
      cards: [],
      descriptions: ['这里', '是', '题目', '的', '描述', '不应该有这张卡片？'],
      directions: ['东', '南', '西', '北'],
      distances: ['近', '不远', '远'],
    };
  },
  created() {
    this.initOrientation();
  },
  methods: {
    async initOrientation() {
      try {
        const response = await universalGet('/api/subtask/orientation/init');
        if (response.data.code === 0) {
          this.cards = response.data.data.map((item, index) => ({
            title: `${this.descriptions[index]}`,
            pass: item.pass,
            dire: null,
            dist: null,
          }));
        } else {
          alert(response.data.message);
        }
      } catch (error) {
        console.error(error);
      }
    },
    async checkOrientation(index) {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(async (position) => {
          const pos = {
            level: index,
            lng: position.coords.longitude,
            lat: position.coords.latitude,
          };
          try {
            const response = await universalPost('/api/subtask/orientation/check', pos);
            if (response.data.code === 0) {
              const { dire, dist } = response.data.data;
              console.log(response.data.data);
              this.cards[index].dire = dire;
              this.cards[index].dist = dist;
            } else {
              alert(response.data.message);
            }
          } catch (error) {
            console.error(error);
          }
        }, (error) => {
          alert("获取定位失败");
        });
      } else {
        alert("Geolocation is not supported by this browser.");
      }
    },
  },
};
</script>

<style scoped>
.box-card {
  margin: 10px;
}
button {
  padding: 10px 20px;
  font-size: 16px;
}
p {
  margin-top: 10px;
  font-size: 14px;
}
</style>