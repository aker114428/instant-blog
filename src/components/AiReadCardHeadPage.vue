<script setup>
import { ArrowRightOutlined } from "@ant-design/icons-vue"
import { defineProps, ref } from 'vue';
import { message } from 'ant-design-vue';
import API_ROUTES from "@/api/api";
import { backendGptIP } from "@/api/backend";
/* const store=useStore();
import { useStore } from 'vuex'; */
const props = defineProps({// eslint-disable-line
    text: String
})

let question = ref();
let gpt_response = ref();
let visible=ref(false);


/* function startGpt() {
    if (!store.state.user.is_login) {
        //未登录
        info();
    } else if (!props.text){
        message.info('文本内容为空');
    } else if (!question.value){
        message.info('输入的问题为空');
    } else {
        visible.value=true;
        console.log('text:' + props.text)
        console.log('gpt hat start');
        var myHeaders = new Headers();
        myHeaders.append("Authorization", store.state.user.access);

        var formdata = new FormData();
        
        if (!question.value) {
            gpt_response.value = '输入的问题为空';
        }
        else {
            console.log('问题：' + question.value)
            formdata.append("gpt_message",  question.value);

            var requestOptions = {
                method: 'POST',
                headers: myHeaders,
                body: formdata,
                redirect: 'follow'
            };

            fetch(backendGptIP+API_ROUTES.gptChat, requestOptions)
                .then(response => response.json())
                .then(result => {
                    console.log(result);
                    visible.value=false;
                    gpt_response.value = result["data"];
                })
                .catch(error => {message.info('gpt服务失败，请联系站长');message.info(error)});
        }
    }

} */

//流输入
function startGptStream() {
    if (!props.text) {
        message.info('文本内容为空');
    } else if (!question.value) {
        message.info('输入的问题为空');
    } else {
        visible.value = true;
        
        /* var myHeaders = new Headers();
        myHeaders.append("Authorization", store.state.user.access);

        var formdata = new FormData(); */

        if (!question.value) {
            gpt_response.value = '输入的问题为空';
        }
        else {
            gpt_response.value="";
            let gpt_message = question.value;
            const eventSourceUrl = backendGptIP+API_ROUTES.gptChatStream+`?gpt_message=${gpt_message}`;
            const eventSource = new EventSource(eventSourceUrl);

            eventSource.onmessage = function (event) {
                // 从服务器接收到的数据会在event.data中
                const data = JSON.parse(event.data); // 假设服务器发送的是JSON字符串
                visible.value = false;
                gpt_response.value += data;
            };

            eventSource.onerror = function (error) {
                console.error('EventSource failed:', error);
                eventSource.close(); // 在发生错误时关闭连接
            };
        }
    }

}

</script>

<template>
    <div id="airead">
        <div class="ai-img is-center">
            <img src="../assets/coin.webp" alt="图像">
        </div>
        <h2 class="is-center">AI交互</h2>
        <div class="ai-content"><a-spin v-if="visible"/>{{ gpt_response }}</div>
        <div class="input">
            <a-textarea v-model:value="question" placeholder="有问题尽管问我" :auto-size="{ minRows: 1, maxRows: 5 }" />
            <a-button type="primary" @click="startGptStream"><arrow-right-outlined /></a-button>
        </div>
    </div>
</template>


<style scoped>
#airead {
    background-color: white;

    overflow: hidden;
    padding: 20px 24px;
    border-radius: 8px;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.09);
}

.ai-img {
    overflow: hidden;
    margin: 0 auto;
    width: 60px;
    height: 60px;
    border-radius: 70px;
}

.ai-img img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.is-center {
    text-align: center;
}

.ai-content {
    padding: 20px 24px;
    width: 100%;
    min-height: 60px;
    background-color: rgb(231, 227, 227);
    border-radius: 10px;
    max-height: 50%;
}

.input {
    padding: 20px 0px;
    display: flex;
}
</style>