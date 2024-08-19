<template>
  <h1>This is Rank.</h1>
  <v-chart :option="chartOptions" autoresize style="height: 300px;"></v-chart>
  <el-table
      :data="tableData"
      style="width: 100%"
      :row-class-name="tableRowClassName"
  >
    <el-table-column prop="rank" label="排名" width="180" />
    <el-table-column prop="name" label="用户名" width="180" />
    <el-table-column prop="point" label="积分" />
  </el-table>
</template>

<script>
import { fetchRankData } from "@/services/infoService";

export default ({
  name: 'Rank',
  data() {
    return {
      tableData: [],
      trendData: [],
      timeNow: "",
      chartOptions: undefined,
      token: "placeholder",
    };
  },
  created() {
    this.loadRankData();
  },
  methods: {
    async loadRankData() {
      try {
        const response = await fetchRankData(this.token);
        if (response.data.code === 3050) {
          this.tableData = response.data.tableData;
          this.trendData = response.data.trendData;
          this.timeNow = response.data.timeNow;
          await this.initEcharts();
        } else {
          this.$router.push('/login');
        }
      } catch (error) {
        console.error('Failed to fetch rank data', error);
        this.$router.push('/login');
      }
    },
    tableRowClassName({ row }) {
      if (row.rank === 1) {
        return 'golden-row';
      } else if (row.rank === 2) {
        return 'silver-row';
      } else if (row.rank === 3) {
        return 'bronze-row';
      }
      return '';
    },
    getDateFromString(str) {
      const reg = /^(\d+)-(\d+)-(\d+) (\d+):(\d+):(\d+)/;
      const temp = str.match(reg);
      let result = "";
      if (temp) {
        result = new Date(temp[1], temp[2] - 1, temp[3], temp[4], temp[5], temp[6]);
      }
      return result;
    },
    async initEcharts() {
      this.trendData = this.trendData.map(item => {
        const lastPoint = item.data[item.data.length - 1];
        const newPoint = [ this.timeNow, lastPoint[1] ];
        return {
          ...item,
          type: "line",
          step: "end",
          data: [...item.data, newPoint]
        };
      });

      let startTime = this.getDateFromString("2024-08-18 10:00:00");
      let nowTime = this.getDateFromString(this.timeNow);
      this.chartOptions = {
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
          console.log(params);
          return params[0].data[0] + "<br/>" + "积分：" + params[0].data[1];
        },
        xAxis: [
          {
            // data: diffDate,
            //设置类别
            type: "time",
            interval: 1 * 60 * 60 * 1000, // 固定x轴时间间隔 间隔24小时，也就是一天
            // 自己想固定间隔多长时间可以改成自己的间隔时间
            min: startTime, // 开始时间时间戳
            max: nowTime, // 结束时间时间戳 如果实际的最大日期不确定，也可以不设定这个属性
            // x轴的字
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
        series: this.trendData,
      };
    },
  },
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