<template>
  <div class="seven-segment-display">
    <div v-for="(digit, index) in digits" :key="index" class="segment-digit">
      <div v-for="(segment, sIndex) in segments" :key="sIndex" :class="['segment', segment, { 'on': isSegmentOn(digit, segment) }]"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';

const segments = ['a', 'b', 'c', 'd', 'e', 'f', 'g'];
const digitToSegments = {
  0: ['a', 'b', 'c', 'd', 'e', 'f'],
  1: ['b', 'c'],
  2: ['a', 'b', 'd', 'e', 'g'],
  3: ['a', 'b', 'c', 'd', 'g'],
  4: ['b', 'c', 'f', 'g'],
  5: ['a', 'c', 'd', 'f', 'g'],
  6: ['a', 'c', 'd', 'e', 'f', 'g'],
  7: ['a', 'b', 'c'],
  8: ['a', 'b', 'c', 'd', 'e', 'f', 'g'],
  9: ['a', 'b', 'c', 'd', 'f', 'g']
};

const props = defineProps({
  timerValue: {
    type: Number,
    required: true
  }
});

const digits = ref([0, 0, 0, 0, 0, 0,]);

const isSegmentOn = (digit, segment) => {
  return digitToSegments[digit].includes(segment);
};

const updateDigits = (value) => {
  const milliseconds = value % 1000;
  const seconds = Math.floor((value / 1000) % 60);
  const minutes = Math.floor((value / (1000 * 60)) % 60);

  digits.value = [
    Math.floor(minutes / 10),
    minutes % 10,
    Math.floor(seconds / 10),
    seconds % 10,
    Math.floor(milliseconds / 100),
    Math.floor((milliseconds % 100) / 10)
  ];
};

watch(() => props.timerValue, (newValue) => {
  updateDigits(newValue);
});
</script>

<style>
:root {
  --digit-thickness: 6px;
  --segment-width: 16px;
  --segment-height: 20px;
}

.seven-segment-display {
  display: flex;
  gap: 10px;
}

.segment-digit {
  position: relative;
  width: 30px; /* 调整宽度 */
  height: 60px; /* 调整高度 */
}

.segment {
  position: absolute;
  background-color: #1c1c18; /* 调整颜色 */
  transition: background-color 0.1s;
}

.segment.on {
  background-color: #A00; /* 调整颜色 */
}

.segment.a {
  top: 0;
  left: 7px;
  width: var(--segment-width); /* 使用变量 */
  height: var(--digit-thickness); /* 使用变量 */
}

.segment.b {
  top: var(--digit-thickness);
  right: 0;
  width: var(--digit-thickness); /* 使用变量 */
  height: var(--segment-height); /* 使用变量 */
}

.segment.c {
  bottom: var(--digit-thickness);
  right: 0;
  width: var(--digit-thickness); /* 使用变量 */
  height: var(--segment-height); /* 使用变量 */
}

.segment.d {
  bottom: 0;
  left: 7px;
  width: var(--segment-width); /* 使用变量 */
  height: var(--digit-thickness); /* 使用变量 */
}

.segment.e {
  bottom: var(--digit-thickness);
  left: 0;
  width: var(--digit-thickness); /* 使用变量 */
  height: var(--segment-height); /* 使用变量 */
}

.segment.f {
  top: var(--digit-thickness);
  left: 0;
  width: var(--digit-thickness); /* 使用变量 */
  height: var(--segment-height); /* 使用变量 */
}

.segment.g {
  top: 28px;
  left: 7px;
  width: var(--segment-width); /* 使用变量 */
  height: var(--digit-thickness); /* 使用变量 */
}
</style>