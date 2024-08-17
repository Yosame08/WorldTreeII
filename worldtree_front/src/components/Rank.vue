<template>
  <h1>This is Rank.</h1>
  <h2>H2 template</h2>
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
import { defineComponent } from 'vue';
import {fetchRankData} from "@/services/infoService";

export default defineComponent({
  name: 'Rank',
  data() {
    return {
      tableData: [],
    };
  },
  created() {
    this.loadRankData();
  },
  methods: {
    async loadRankData() {
      try {
        const response = await fetchRankData();
        console.log(response);
        if (response.data.code === 3050) {
          this.tableData = response.data.tableData;
        } else {
          this.$router.push('/login');
        }
      } catch (error) {
        console.error('Failed to fetch rank data', error);
        this.$router.push('/login');
      }
    },
    tableRowClassName({ row }) {
      console.log(row);
      if (row.rank === 1) {
        return 'golden-row';
      } else if (row.rank === 2) {
        return 'silver-row';
      } else if (row.rank === 3) {
        return 'bronze-row';
      }
      return '';
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