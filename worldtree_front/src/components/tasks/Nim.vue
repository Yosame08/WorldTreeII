<template>
    <div>
        <el-row>
            <el-col v-for="(pile, index) in piles" :key="index" :span="4">
                <NimPile :remaining="pile.remaining" :active="activePiles[index]" @clicked="lockOthers(index)"
                    @update-remaining="updateRemaining(index, $event)" />
            </el-col>
        </el-row>
        <el-row justify="center">
            <el-col :span="4">
                <el-button type="danger" @click="reset">重置</el-button>
                <el-button type="success" @click="submit">提交</el-button>
            </el-col>
        </el-row>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import NimPile from "@/components/tasks/NimPile.vue";
import { useRoute } from "vue-router";
import axios from "axios";

let piles = ref([]); // State holding piles info
let originalPiles = ref([]); // Original value for reset
let activePiles = ref([]);

const route = useRoute(); // Get the current route object
let token;
// Fetch data on component mount
onMounted(() => {
    axios.get("/api/subtask/nim/init", {
        player_first: route.query.player_first ? 1 : 0,
    }).then((resp) => {
        piles.value = resp.data.data.array.map(remaining => ({ remaining }));
        originalPiles.value = JSON.parse(JSON.stringify(piles.value)); // Make deep copy
        activePiles.value = piles.value.map(() => true);
        token = resp.data.data.gameToken;
    }).catch((error) => {
        alert(error);
    });
});

function submit() {
    axios.post("/api/subtask/nim/step", {
        gameToken: token,
        array: piles.value
    }).then((resp) => {
        if (resp.data.data.status === "NOT_FINISHED") {
            piles.value = resp.data.data.array.map(remaining => ({ remaining }));
            originalPiles.value = JSON.parse(JSON.stringify(piles.value)); // Make deep copy
            activePiles.value = piles.value.map(() => true);
        } else if (resp.data.data.status === "FINISHED") {
            alert(`${resp.data.data.winner} wins`);
        }
    }).catch((error) => {
        alert(error);
    });
}

// Lock other piles when one is clicked
function lockOthers(clickedIndex) {
    activePiles.value = activePiles.value.map((_, index) => index === clickedIndex);
}

// Update the remaining value of a pile
function updateRemaining(index, newRemaining) {
    piles.value[index].remaining = newRemaining;
}

// Reset the piles and active state
function reset() {
    piles.value = JSON.parse(JSON.stringify(originalPiles.value)); // Reset piles to original
    activePiles.value = piles.value.map(() => true); // Reset all piles to active
}
</script>