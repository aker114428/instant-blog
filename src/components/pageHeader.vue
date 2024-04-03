<script setup>
import navigation from './navigationView.vue'
import { DownOutlined } from '@ant-design/icons-vue';
import { ref, onMounted } from 'vue';
const text = '一款快捷AI生成式轻博客网站'; // 要显示的文本

const typedText = ref(''); // 用于存储当前已显示的文本部分
const typingSpeed = 150; // 打字速度，单位是毫秒
const deletingSpeed = 100; // 删除速度，单位是毫秒

onMounted(() => {
  let action = typeChar; // 初始动作设置为打字
  let charIndex = 0; // 当前字符的索引

  function typeChar() {
    if (charIndex < text.length) {
      typedText.value += text[charIndex];
      charIndex++;
      setTimeout(typeChar, typingSpeed);
    } else {
      // 打字完成后准备回退
      setTimeout(deleteChar, 5000); // 在开始删除之前暂停一段时间
    }
  }

  function deleteChar() {
    if (charIndex > 0) {
      typedText.value = typedText.value.slice(0, -1); // 移除最后一个字符
      charIndex--;
      setTimeout(deleteChar, deletingSpeed);
    } else {
      // 文字删除完毕，准备重新打字
      setTimeout(typeChar, 500); // 在重新开始打字之前暂停一段时间
    }
  }

  action();
});


</script>

<template>
    <navigation></navigation>

    <div id="site-info">
        <h1 id="site-title" style="color:white">Instant-Blog</h1>
        <div id="site-subtitle">
            {{ typedText }}<span class="cursor"></span>
            
        </div>
    </div>

    <div id="scroll-down">
        <down-outlined />
    </div>
</template>

<style scoped>
#site-info {
    color: white;
    position: absolute;
    text-align: center;
    top: 43%;
    padding: 0 10px;
    width: 100%;
    background: var(--global-bg);
}

.title {

    text-shadow: 2px 2px 4px rgba(0, 0, 0, .15);
    line-height: 1.5;
}

#scroll-down {
    bottom: 0;
    text-align: center;
    position: absolute;
    width: 100%;
    color: var(--light-grey);
    font-size: 30px;
}

h1 {
    color: white;
    font-size: 3em
}

.cursor {
  border-right: 2px solid black; /* 光标的样式 */
  display: inline-block; /* 使光标不占用额外空间 */
  margin-left: 2px; /* 在光标和文字之间留一点空隙 */
  height: 1em; /* 光标的高度 */
  animation: blink 1s step-start infinite; /* 闪烁动画 */
}

@keyframes blink {
  from, to { border-color: transparent }
  50% { border-color: black }
}
</style>