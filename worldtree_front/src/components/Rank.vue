<template>
  <h1>积分排行榜</h1>
  <v-chart :option="chartOptions" autoresize style="height: 300px;"></v-chart>
  <el-table
      :data="tableData"
      style="width: 100%"
      :row-class-name="tableRowClassName"
  >
    <el-table-column prop="rank" label="排名" width="100" />
    <el-table-column prop="username" label="用户" width="180" />
    <el-table-column prop="point" label="积分" width="100"/>
    <el-table-column prop="point" label="个性签名"/>
  </el-table>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { universalGet, universalPost } from "@/services/universalService";
import store from "@/services/storeService";

const tableData = ref([]);
const trendData = ref([]);
const timeNow = ref("");
const chartOptions = ref(undefined);

const loadRankData = async () => {
  try {
    const response = await universalGet('/api/func/rank');
    console.log(response);
    if (response.data.code === 0) {
      store.commit("clearErrorMsg");
      tableData.value = response.data.data;
      await loadTrendData();
      await initEcharts();
    }
  } catch (error) {
  }
};

const loadTrendData = async () => {
  try {
    const topFiveUsers = tableData.value.slice(0, 5);
    const trendPromises = topFiveUsers.map(user =>
        universalPost('/api/func/get_user_trend', {user_id: user.user_id})
    );
    const trendResponses = await Promise.all(trendPromises);
    trendData.value = trendResponses.map((response, index) => {
      if (response.data.code === 0) {
        const userTrendData = response.data.data.map(item => [item.time, item.point]);
        const latestPoint = userTrendData[userTrendData.length - 1];
        const currentTime = new Date().toISOString();
        userTrendData.push([currentTime, latestPoint[1]]);
        return {
          name: tableData.value[index].username,
          type: "line",
          step: "end",
          data: userTrendData
        };
      }
      return null;
    }).filter(item => item !== null);
  } catch (error) {
  }
};

const tableRowClassName = ({row}) => {
  if (row.rank === 1) {
    return 'golden-row';
  } else if (row.rank === 2) {
    return 'silver-row';
  } else if (row.rank === 3) {
    return 'bronze-row';
  }
  return '';
};

const getDateFromString = (str) => {
  const reg = /^(\d+)-(\d+)-(\d+) (\d+):(\d+):(\d+)/;
  const temp = str.match(reg);
  let result = "";
  if (temp) {
    result = new Date(temp[1], temp[2] - 1, temp[3], temp[4], temp[5], temp[6]);
  }
  return result;
};

const initEcharts = async () => {
  let startTime = getDateFromString("2024-09-20 00:00:00");
  let nowTime = new Date();

  chartOptions.value = {
    title: {
      text: "Rank Trend",
      subtext: "subtitle",
    },
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "cross",
      },
    },
    toolbox: {
      show: true,
    },
    formatter: function (params) {
      return params[0].data[0] + "<br/>" + "积分：" + params[0].data[1];
    },
    xAxis: [
      {
        type: "time",
        interval: 1 * 60 * 60 * 1000,
        min: startTime,
        max: nowTime,
        axisLabel: {
          show: true,
          showMinLabel: true,
          showMaxLabel: true,
          formatter: function (value) {
            const date = new Date(value);
            const hours = date.getHours().toString().padStart(2, '0');
            const minutes = date.getMinutes().toString().padStart(2, '0');
            return `${hours}:${minutes}`;
          }
        },
      },
    ],
    yAxis: {
      type: "value",
      axisLabel: {
        formatter: "{value}",
      },
      axisPointer: {
        snap: true,
      },
    },
    series: trendData.value,
  };
};

onMounted(() => {
  loadRankData();
});
</script>

<style>
.el-table .golden-row {
  --el-table-tr-bg-color: #ffd700;
}

.el-table .silver-row {
  --el-table-tr-bg-color: #d8d8d8;
}

.el-table .bronze-row {
  --el-table-tr-bg-color: #cd7f32;
}
</style>