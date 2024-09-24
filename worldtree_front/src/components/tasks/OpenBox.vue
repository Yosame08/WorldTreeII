<template>
  <div class="location-button">
    <button @click="getLocation">获取定位</button>
    <p v-if="latitude && longitude">经度: {{ longitude }}, 纬度: {{ latitude }}</p>
  </div>
</template>

<script>
export default {
  name: 'OpenBox',
  data() {
    return {
      latitude: null,
      longitude: null,
    };
  },
  methods: {
    getLocation() {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(this.showPosition, this.showError);
      } else {
        alert("Geolocation is not supported by this browser.");
      }
    },
    showPosition(position) {
      this.latitude = position.coords.latitude;
      this.longitude = position.coords.longitude;
    },
    showError(error) {
      switch(error.code) {
        case error.PERMISSION_DENIED:
          alert("User denied the request for Geolocation.");
          break;
        case error.POSITION_UNAVAILABLE:
          alert("Location information is unavailable.");
          break;
        case error.TIMEOUT:
          alert("The request to get user location timed out.");
          break;
        case error.UNKNOWN_ERROR:
          alert("An unknown error occurred.");
          break;
      }
    }
  }
};
</script>

<style scoped>
.location-button {
  text-align: center;
  margin-top: 20px;
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