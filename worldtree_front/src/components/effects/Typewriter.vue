<template>
  <h1>{{ displayedText }}<span class="cursor">.</span></h1>
</template>

<script>
export default {
  props: {
    text: {
      type: String,
      required: true
    },
    speed: {
      type: Number,
      default: 100
    }
  },
  data() {
    return {
      displayedText: ''
    };
  },
  mounted() {
    this.typeText();
  },
  methods: {
    typeText() {
      let index = 0;
      const interval = setInterval(() => {
        if (index < this.text.length) {
          this.displayedText += this.text[index];
          index++;
        } else {
          clearInterval(interval);
        }
      }, this.speed);
    }
  }
};
</script>

<style scoped>
.cursor {
  display: inline-block;
  width: 1px;
  background-color: black;
  animation: blink 1s step-end infinite;
}

@keyframes blink {
  from, to {
    background-color: transparent;
  }
  50% {
    background-color: black;
  }
}
</style>