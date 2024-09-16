<template>
    <el-card class="container" :class="['nim-pile', { inactive: !active }]">
        <div class="counter">
            <span class="number">{{ counter }}</span>
        </div>
        <div class="actions">
            <el-button type="primary" @click="take" :disabled="counter <= 0 || !active">拿一个</el-button>
        </div>
    </el-card>
</template>

<script setup>
import { ref, watch, toRef, defineEmits } from "vue";


const props = defineProps({
    remaining: Number,
    active: {
        type: Boolean,
        default: true
    }
});

const remainingRef = toRef(props, 'remaining');
const originalValue = ref(props.remaining);
const counter = ref(props.remaining);
const emit = defineEmits(['clicked', 'update-remaining']);

function take() {
    if (counter.value > 0) {
        counter.value--;
        emit('clicked');
        emit('update-remaining', counter.value);
    }
}

watch(remainingRef, (newMax) => {
    counter.value = newMax;
    originalValue.value = newMax;
});

function resetCounter() {
    counter.value = originalValue.value;
}
</script>

<style scoped>
.nim-pile {
    cursor: pointer;
    transition: all 0.3s ease;
}

.nim-pile.inactive {
    cursor: not-allowed;
    opacity: 0.5;
    pointer-events: none;
}

.container {
    display: flex;
    margin: 10px;
    justify-content: center;
    align-items: center;
}

.counter {
    font-size: 48px;
    text-align: center;
    margin-bottom: 20px;
}

.actions {
    display: flex;
    flex-direction: row;
    align-items: center;
}

.actions .el-button {
    margin-bottom: 10px;
}
</style>
