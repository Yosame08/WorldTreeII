<template>
  <div>
    <p>Downloading file...</p>
  </div>
</template>

<script>
export default {
  name: 'FileDownloader',
  mounted() {
    this.redirectToFile();
  },
  methods: {
    redirectToFile() {
      window.location.href = '/static/HL.z02.a9';
    }
  }
};
</script>