<template>
  <h1>This is Rank.</h1>
  <v-chart :option="chartOptions" autoresize style="height: 300px;"></v-chart>
  <el-table
      :data="tableData"
      style="width: 100%"
      :row-class-name="tableRowClassName"
  >
    <el-table-column prop="rank" label="排名" width="100" />
    <el-table-column prop="name" label="用户" width="180" />
    <el-table-column prop="point" label="积分" width="100"/>
    <el-table-column prop="point" label="个性签名"/>
  </el-table>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { universalGet } from "@/services/universalService";
import store from "@/services/storeService";

const tableData = ref([]);
const trendData = ref([]);
const timeNow = ref("");
const chartOptions = ref(undefined);

const loadRankData = async () => {
  try {
    const response = await universalGet('/api/rank');
    console.log(response);
    if (response.data.code === 0) {
      store.commit("clearErrorMsg");
      tableData.value = response.data.tableData;
      trendData.value = response.data.trendData;
      timeNow.value = response.data.timeNow;
      await initEcharts();
    }
  } catch (error) {}
};

const tableRowClassName = ({ row }) => {
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
  console.log(trendData.value);
  trendData.value = trendData.value.map(item => {
    const lastPoint = item.data[item.data.length - 1];
    const newPoint = [ timeNow.value, lastPoint[1] ];
    return {
      ...item,
      type: "line",
      step: "end",
      data: [...item.data, newPoint]
    };
  });

  let startTime = getDateFromString("2024-08-18 10:00:00");
  let nowTime = getDateFromString(timeNow.value);
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
          showMaxLabel: true
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