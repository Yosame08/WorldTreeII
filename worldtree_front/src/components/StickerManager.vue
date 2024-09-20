<script setup>
import { ref, onMounted } from 'vue';
import { universalGet, universalPost } from "@/services/universalService";

const stickers = ref([{
    "stk_id": 1,
    "show": true,
    "x": 0.566,
    "y": 0.788,
},
{
    "stk_id": 4,
    "show": true,
    "x": 0.6,
    "y": 0.1,
},
{
    "stk_id": 8,
    "show": false,
    "x": 0.6,
    "y": 0.1,
},]);

// Fetch stickers from API
const fetchStickers = async () => {
    try {
        const response = await universalGet('/api/get_stickers');
        if (response.data.code === 0) {
            stickers.value = response.data.data;
        }
    } catch (error) {
        console.error('Failed to fetch stickers:', error);
    }
};

onMounted(() => {
    fetchStickers();
});

// Handle dragging a sticker
const dragSticker = (sticker, event) => {
    event.preventDefault(); // 防止选中图片
    console.log("Dragging sticker:", sticker.stk_id); // Add this line for debugging

    const stickerElement = event.target;

    const startX = event.clientX;
    const startY = event.clientY;

    const initialX = sticker.x * window.innerWidth;
    const initialY = sticker.y * window.innerHeight;

    const onMouseMove = (e) => {
        const deltaX = e.clientX - startX;
        const deltaY = e.clientY - startY;

        // Calculate new position in percentages
        const newX = (initialX + deltaX) / window.innerWidth;
        const newY = (initialY + deltaY) / window.innerHeight;

        sticker.x = newX;
        sticker.y = newY;
    };

    const onMouseUp = async () => {
        document.removeEventListener('mousemove', onMouseMove);
        document.removeEventListener('mouseup', onMouseUp);

        try {
            await universalPost('/api/modify_stickers', {
                stk_id: sticker.stk_id,
                show: true,
                x: sticker.x,
                y: sticker.y,
            });
        } catch (error) {
            console.error('Failed to update sticker position:', error);
        }

        console.log('Sticker position updated:', { id: sticker.stk_id, x: sticker.x, y: sticker.y });
    };

    document.addEventListener('mousemove', onMouseMove);
    document.addEventListener('mouseup', onMouseUp);
};

// Remove a sticker
const removeSticker = async (sticker) => {
    sticker.show = false; // Locally update the state
    try {
        await universalPost('/api/modify_stickers', {
            stk_id: sticker.stk_id,
            show: false,
        });
    } catch (error) {
        console.error('Failed to remove sticker:', error);
    }
};

const showBag = ref(false);  // 控制背包弹出状态

// 显示背包中的贴纸
const showStickerFromBag = async (sticker) => {
    sticker.show = true; // 本地显示贴纸
    try {
        await universalPost('/api/modify_stickers', {
            stk_id: sticker.stk_id,
            show: true,
        });
    } catch (error) {
        console.error('Failed to show sticker from bag:', error);
    }
};

</script>

<template>
    <!-- Render stickers and buttons -->
    <div class="sticker-container">
        <div v-for="sticker in stickers" :key="sticker.stk_id" v-show="sticker.show"
            @mousedown="dragSticker(sticker, $event)">

            <img :style="{ left: `${sticker.x * 100}%`, top: `${sticker.y * 100}%` }" :src="require('@/assets/hex.png')"
                class="sticker" />
            <!-- Remove button -->
            <button :style="{ left: `${sticker.x * 100}%`, top: `${sticker.y * 100}%` }" class="remove-btn"
                @click="removeSticker(sticker)">X</button>
        </div>
    </div>

    <!-- Backpack icon in bottom-right corner -->
    <button class="backpack-btn" @click="showBag = true">背包</button>

    <!-- Backpack drawer -->
    <div v-if="showBag" class="backpack-drawer">
        <div class="header-container">
            <h3>未显示的贴纸</h3>
            <button class="close-btn" @click="showBag = false">关闭</button>
        </div>
        <div v-for="sticker in stickers" :key="sticker.stk_id">
            <div v-if="!sticker.show" class="sticker-in-bag">
                <img :src="require('@/assets/hex.png')" class="sticker-small" />
                <button @click="showStickerFromBag(sticker)">显示</button>
            </div>
        </div>
    </div>
</template>

<style scoped>
.sticker-container {
    position: absolute;
    z-index: 0;
    width: 100%;
    height: 100%;
}

.sticker {
    position: absolute;
    width: 50px;
    height: 50px;
    cursor: pointer;
}

.remove-btn {
    position: absolute;
    top: -10px;
    right: -10px;
    width: 24px;
    height: 24px;
    background-color: red;
    color: white;
    border: none;
    border-radius: 50%;
    cursor: pointer;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 16px;
    font-weight: bold;
    line-height: 1;
    padding: 0;
    z-index: 1000;
    /* 确保按钮在最上层 */
}

.backpack-btn {
    position: fixed;
    bottom: 20px;
    right: 20px;
    width: 60px;
    height: 60px;
    background-color: #f0a500;
    color: white;
    border: none;
    border-radius: 50%;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    z-index: 200;
}

.backpack-drawer {
    position: fixed;
    bottom: 0;
    right: 0;
    width: 300px;
    height: 50%;
    background-color: #fff;
    box-shadow: -2px -2px 10px rgba(0, 0, 0, 0.3);
    z-index: 201;
    padding: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
    overflow-y: auto;
}

.sticker-in-bag {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
}

.sticker-small {
    width: 30px;
    height: 30px;
    margin-right: 10px;
}

.header-container {
    display: flex;
    align-items: center;
    /* 垂直居中对齐 */
}

.close-btn {
    margin-left: 10px;
    background-color: red;
    color: white;
    border: none;
    cursor: pointer;
    height: 25px;
    border-radius: 10px;
}
</style>