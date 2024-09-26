<template>
  <div class="rank-div">
    <el-card class="rank-card">
      <h2>魔塔电报员排行榜</h2>
      <el-table :data="rankData" style="width: 100%">
        <el-table-column prop="rank" label="排行" width="80" align="left"></el-table-column>
        <el-table-column prop="usernameA" label="观察者" width="200" align="center"></el-table-column>
        <el-table-column prop="usernameB" label="操作者" width="200" align="center"></el-table-column>
        <el-table-column prop="mapName" label="地图" width="80" align="center"></el-table-column>
        <el-table-column prop="score" label="得分" width="80" align="center"></el-table-column>
        <el-table-column prop="time" label="剩余时间" align="center"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import {universalGet} from "@/services/universalService";

const rankData = ref([])

const fetchRankData = async () => {
  try {
    const response = await universalGet('/api/subtask/telegram/rank')
    console.log(response)
    rankData.value = response.data.data.map(record => ({
      ...record,
      mapName: getMapName(record.telegramId)
    }))
  } catch (error) {
    console.error('Error fetching rank data:', error)
  }
}

const getMapName = (telegramId) => {
  switch (telegramId) {
    case 1: return '魔塔'
    case 2: return '沙漠'
    case 3: return '草地'
    default: return '未知'
  }
}

onMounted(() => {
  fetchRankData()
  const interval = setInterval(fetchRankData, 300000) // 5 minutes
  onUnmounted(() => clearInterval(interval))
})
</script>

<style scoped>
.rank-div {
  width: 100%;
  height: 100%;
  background-color: #2c3e50;
  color: #ecf0f1;
}

.rank-card {
  background-color: #2c3e50;
  color: #ecf0f1;
  padding: 20px;
}

.el-table th, .el-table td {
  color: #ecf0f1;
}
</style>