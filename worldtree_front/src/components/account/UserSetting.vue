<template>
  <div>
    <!-- User Settings Card -->
    <el-card class="user-settings">
      <h2>User Settings</h2>

      <!-- Avatar Upload -->
      <div class="avatar-section">
        <el-upload class="avatar-uploader" action="" :show-file-list="false" :before-upload="beforeAvatarUpload"
          :on-change="handleAvatarChange">
          <img v-if="imageUrl" :src="imageUrl" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon">
            <Plus />
          </el-icon>
        </el-upload>
        <el-button @click="clearAvatar" type="danger">Clear Avatar</el-button>
      </div>

      <!-- Signature Editor -->
      <div class="signature-editor">
        <h3>User Signature</h3>
        <quill-editor v-model="signature" ref="quillEditor" :options="editorOptions"></quill-editor>
      </div>

      <!-- Save Button -->
      <div class="action-buttons">
        <el-button type="primary" @click="saveSettings">Save Settings</el-button>
      </div>
    </el-card>

    <!-- QR Code Card -->
    <el-card class="qr-code-card">
      <h2>User QR Code</h2>
      <div v-if="qrCodeImage">
        <img :src="qrCodeImage" alt="User QR Code" />
      </div>
      <div v-else>
        <el-button @click="fetchQRCode">Get your identity code</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import axios from 'axios'
import QRCode from 'qrcode'
import {set_info} from "@/services/userService";

const imageUrl = ref('') // For image preview
const avatarBase64 = ref('') // Base64-encoded avatar
const signature = ref('') // Signature HTML content
const qrCodeImage = ref('') // QR Code image URL

const editorOptions = {
  theme: 'snow',
}

// Handle avatar file selection and convert it to base64
const handleAvatarChange = (file) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    imageUrl.value = e.target.result // Set image for preview
    avatarBase64.value = e.target.result.split(',')[1] // Store base64 (without metadata prefix)
  }
  reader.readAsDataURL(file.raw) // Convert to base64
}

// Avatar validation before upload
const beforeAvatarUpload = (file) => {
  if (file.type !== 'image/jpeg' && file.type !== 'image/png') {
    ElMessage.error('Avatar picture must be in JPG or PNG format!')
    return false
  } else if (file.size / 1024 / 1024 > 2) {
    ElMessage.error('Avatar picture size cannot exceed 2MB!')
    return false
  }
  return true
}

// Clear avatar
const clearAvatar = () => {
  imageUrl.value = ''
  avatarBase64.value = ''
}

// Get the HTML content of the signature from Quill
const getSignatureContent = () => {
  const quillEditor = document.querySelector('.ql-editor')
  return quillEditor ? quillEditor.innerHTML : ''
}

// Save user settings (avatar as base64 and signature)
const saveSettings = async () => {
  const signatureContent = getSignatureContent() // Extract HTML from Quill editor

  const payload = {
    avatar: avatarBase64.value, // Base64-encoded avatar
    signature: signatureContent, // Extracted signature HTML content
  }

  try {
    const { data } = set_info(payload)
    ElMessage.success('Settings saved successfully!')
  } catch (error) {
    console.error('Error saving settings:', error)
    ElMessage.error('Failed to save settings.')
  }
}

// Fetch the QR code string and generate the QR code image
const fetchQRCode = () => {
  console.log("Fetching QR Code......")
  axios.get('/api/user/qrcode', { token: sessionStorage.getItem("token") }).then(
    (data) => {
      console.log(data);
      generateQRCode(data.data.data.qrCodeString);
    }
  )
}

// Generate QR code using the fetched string
const generateQRCode = (qrString) => {
  QRCode.toDataURL(qrString)
    .then((url) => {
      qrCodeImage.value = url // Set the QR code image source
    })
    .catch((error) => {
      console.error('Error generating QR code:', error)
    })
}

fetchQRCode();
</script>

<style scoped>
.user-settings {
  margin-top: 20px;
  margin-left: 50px;
  margin-right: 50px;
}

.avatar-section {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.avatar-uploader {
  margin-right: 20px;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
}

.signature-editor {
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  justify-content: flex-end;
}

.qr-code-card {
  margin-top: 20px;
  margin-left: 50px;
  margin-right: 50px;
  text-align: center;
}
</style>
