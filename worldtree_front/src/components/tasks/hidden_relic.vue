<template>
  <div>
    <p>Downloading file...</p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'FileDownloader',
  mounted() {
    this.downloadFile();
  },
  methods: {
    async downloadFile() {
      try {
        console.log("fetching /api/download/hidden_relic")
        const response = await axios.get('/api/download/hidden_relic', {responseType: 'blob'});
        const blob = new Blob([response.data]);
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'hidden_relic';
        document.body.appendChild(a);
        a.click();
        a.remove();
        window.URL.revokeObjectURL(url);
      } catch (error) {
        console.error('Error downloading file:', error);
      }
    }
  }
};
</script>