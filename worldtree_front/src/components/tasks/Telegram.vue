<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage, ElDialog } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import QRCode from 'qrcode'

const entries = ref([{ code: '', meaning: '' }])
const qrCodeImage = ref(null)
const dialogVisible = ref(false)

// 添加新条目
const addEntry = () => {
  entries.value.push({ code: '', meaning: '' })
}

// 删除条目
const removeEntry = (index) => {
  entries.value.splice(index, 1)
}

// 生成二维码
const generateQRCode = () => {
  let qrString = '[WorldTreeII]codewords='
  entries.value.forEach(entry => {
    if (entry.code && entry.meaning) {
      qrString += `${entry.code}\`${entry.meaning}\``
    }
  })
  console.log(qrString)

  QRCode.toDataURL(qrString, { width: 500 })
      .then(url => {
        qrCodeImage.value = url
        dialogVisible.value = true
      })
      .catch(error => {
        console.error('Error generating QR code:', error)
        ElMessage.error('Error generating QR code')
      })
}

// 屏蔽右滑返回
const preventSwipeBack = (event) => {
  if (event.touches[0].clientX < 50) {
    event.preventDefault()
  }
}

onMounted(() => {
  window.addEventListener('touchstart', preventSwipeBack, { passive: false })
})

onUnmounted(() => {
  window.removeEventListener('touchstart', preventSwipeBack)
})
</script>

<template>
  <el-row justify="center" class="image-row">
    <el-col :span="4" v-for="i in 5" :key="i">
      <img :src="require(`@/assets/telegram/Code${i}.png`)" :alt="`Code${i}`" class="code-image" />
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="8">
      <h2>编码(1-5)</h2>
      <div v-for="(entry, index) in entries" :key="index" class="entry">
        <el-input v-model="entry.code" placeholder="1-5"
                  @input="value => entry.code = value.replace(/[^1-5]/g, '')"/>
      </div>
    </el-col>
    <el-col :span="12">
      <h2>含义</h2>
      <div v-for="(entry, index) in entries" :key="index" class="entry">
        <el-input v-model="entry.meaning" placeholder="输入含义"/>
      </div>
    </el-col>
    <el-col :span="4">
      <h2>删除</h2>
      <div v-for="(entry, index) in entries" :key="index" class="entry">
        <el-button type="danger" @click="removeEntry(index)">
          <el-icon>
            <Delete/>
          </el-icon>
        </el-button>
      </div>
    </el-col>
  </el-row>
  <el-button type="primary" @click="addEntry">
    <el-icon>
      <Plus/>
    </el-icon>
    添加条目
  </el-button>
  <el-button type="primary" @click="generateQRCode">生成/更新二维码</el-button>
  <br>
  <img v-if="qrCodeImage" :src="qrCodeImage" alt="QR Code" class="QR-code"/>
</template>

<style scoped>
.el-row {
  margin-bottom: 20px;
}

.el-col {
  padding: 10px;
}

.entry {
  margin-bottom: 10px;
}

.code-image {
  width: 90px;
}

.QR-code {
  width: 100%;
}
</style>