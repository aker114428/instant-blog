<script setup>
import articleContain from '../components/articleContain.vue'
import Navigation from '../components/navigationView.vue';
import Inforcard from '../components/inforcardView.vue';
import AiReadCard from '@/components/AiReadCard.vue';
import { useRoute } from 'vue-router';
import { ref } from 'vue';
//import { message } from 'ant-design-vue';
import { useStore } from 'vuex';
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import API_ROUTES from "@/api/api";
import { backendIP, backendGptIP } from "@/api/backend";
import router from '@/router'
// highlightjs
import hljs from 'highlight.js';


VMdPreview.use(githubTheme, {
    Hljs: hljs,
});
const store = useStore();
//存储文章内容
//let articleInfo=''
let articleTitle = ref();
let introduction = ref();
let articleText = ref();
let publishTime = ref();
let imgUrl = ref();
let authorId = ref();
let aiInput = ref();
let gpt_response = ref();
//文章id

let notMobile = ref(true);
if (window.matchMedia('(max-width: 500px)').matches) {
    notMobile.value = false; // 如果屏幕宽度小于500px，设置notMobile为false
}

const route = useRoute();
const articleID = parseInt(route.params.articleID);
console.log('article id:' + articleID)

//根据文章ID请求文章数据
var myHeaders = new Headers();
myHeaders.append("Authorization", store.state.user.access);

var formdata = new FormData();
formdata.append("article_id", articleID);

var requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: formdata,
    redirect: 'follow'
};



//获取文章信息
fetch(backendIP + API_ROUTES.getArticleById, requestOptions)
    .then(response => response.json())
    .then(result => {
        result = result["data"];
        articleTitle.value = result['articleTitle'];
        articleText.value = result['articleText'];
        publishTime.value = result['updateTime'].slice(0, 10);
        imgUrl.value = " background-image: url(" + result['articleHeadImg'] + ")";
        authorId.value = result['authorId'];
        introduction.value = result['introduction'];
        //console.log('作者id:'+authorId.value);
        aiInput.value = articleText.value;
        /* 
        var reg1 = new RegExp("#","g"); // 加'g'，删除字符串里所有的"a"
        aiInput.value=articleText.value.replace(reg1,",");
         */

    })
    .catch(error => console.log('error', error));


//点击提炼文章内容
const visible = ref(false);
const showModal = () => {

    
    visible.value = true;
    summarizeArticle();
};

const handleOk = e => {
    console.log(e);
    visible.value = false;
};


//编辑
const editArticle = (ID) => {
    router.push({ name: 'articleModify', params: { articleID: ID } })
}

//概要
function summarizeArticle() {
    var myHeaders = new Headers();
    myHeaders.append("Authorization", store.state.user.access);
    gpt_response.value = '';
    var formdata = new FormData();
    formdata.append("gpt_message", "总结提炼一下给定文章，给出内容概要，尽量简练在200字以内，注意文章采用markdown格式书写,返回的文章概要分点，排版要美观些，文章内容如下：" + aiInput.value);

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: formdata,
        redirect: 'follow'
    };

    fetch(backendGptIP + API_ROUTES.gptChat, requestOptions)
        .then(response => response.json())
        .then(result => {
            console.log(result);
            gpt_response.value = result["data"];
        })
        .catch(error => console.log('error', error));
}

//流输入
/* function summarizeArticle() {
    

    gpt_response.value = "";
    let gpt_message = "总结提炼一下给定文章，给出内容概要，尽量简练在200字以内，注意文章采用markdown格式书写,返回的文章概要分点，排版要美观些，文章内容如下：" + aiInput.value;
    console.log("gpt_message");
    console.log(gpt_message);
    const eventSourceUrl = backendGptIP + API_ROUTES.gptChat + `?gpt_message=${gpt_message}`;
    const eventSource = new EventSource(eventSourceUrl);

    eventSource.onmessage = function (event) {
        // 从服务器接收到的数据会在event.data中
        const data = JSON.parse(event.data); // 假设服务器发送的是JSON字符串
        gpt_response.value += data;
    };

    eventSource.onerror = function (error) {
        console.error('EventSource failed:', error);
        eventSource.close(); // 在发生错误时关闭连接
    };
} */


</script>

<template>
    <header class="article_page_head" :style="imgUrl">
        <div class="navigate">
            <Navigation></Navigation>
        </div>
        <div class="mypage">
            <h1 class="site-title" style="color:white">{{ articleTitle }}</h1>
            <h2 class="site-title" style="color:white">{{ introduction }}</h2>
            <p>更新时间:{{ publishTime }}</p>
        </div>
    </header>

    <a-button v-if="!notMobile" type="primary" class="sticky" @click="showModal">提炼文章内容</a-button>

    <main class="layout">

        <div id="recent-post">
            <articleContain :text="articleText"></articleContain>
        </div>

        <div id="aside-content">
            <Inforcard v-if="authorId" :authorId="authorId"></Inforcard>
            <div class="sticky">
                <AiReadCard v-if="articleText" :text="articleText"></AiReadCard>

                <div id="aside-button">
                    <a-button type="primary" class="button" @click="showModal">提炼文章内容</a-button>
                </div>

                <div v-if="authorId === $store.state.user.id || $store.state.user.username === 'root'">
                    <a-button v-if="notMobile" type="primary" id="edit-button" @click="editArticle(articleID)">编辑</a-button>
                </div>

            </div>

        </div>
        <div>
            <a-modal v-model:visible="visible" width="1000px" title="文章内容概要(需要约10s请耐心等待)" @ok="handleOk">
                <a-spin v-if="!gpt_response" />
                <div id="article-container">
                    <v-md-preview :text="gpt_response" style="overflow: hidden"></v-md-preview>
                </div>
            </a-modal>
        </div>
    </main>
    <div v-if="!notMobile">

        <div style="padding-right:15px;padding-left:15px;">
            <Inforcard v-if="authorId" :authorId="authorId"></Inforcard>
        </div>

        <AiReadCard :text="articleText" id="aicard"></AiReadCard>
    </div>

    <div>
        <a-back-top />

        <strong style="color: rgba(64, 64, 64, 0.6)"> gray </strong>

    </div>
</template>


<style scoped>
.layout {
    display: flex;
    flex: 1 auto;
    margin: 0 auto;
    padding: 30px 15px;
    max-width: 1200px;
    width: 100%;
}

.layout #recent-post {
    width: 74%;
    -webkit-transition: all .3s;
    -moz-transition: all .3s;
    -o-transition: all .3s;
    -ms-transition: all .3s;
    transition: all .3s;
}

#aside-content {
    padding-left: 15px;
    width: 26%;

}

.mypage {
    color: white;
    position: absolute;
    text-align: center;
    top: 45%;
    padding: 0 10px;
    width: 100%;
    mix-blend-mode: difference;

}

.article_page_head {
    height: 60vh;
    background-attachment: fixed;
    background-size: cover;
    color: white;
    position: relative;
    width: 100%;
    background-color: #49b1f5;
    background-position: center center;
    background-repeat: no-repeat;

}

h1 {
    color: white;
    font-size: 3em
}

a {
    color: black !
}

#recent-post {
    display: flex;
}

#aside-button {
    margin-top: 10px;
    display: flex;
    align-content: center;
    justify-content: center;
}

#edit-button {
    width: 100%;
    margin-top: 10px;
}

.sticky {
    margin-top: 20px;
    position: sticky;
    top: 20px;
}

#aside-button .button {
    width: 100%;
}

@media only screen and (max-width: 500px) {
    #aside-content {
        display: none;
    }

    .layout #recent-post {
        width: 100%;
        -webkit-transition: all .3s;
        -moz-transition: all .3s;
        -o-transition: all .3s;
        -ms-transition: all .3s;
        transition: all .3s;
    }

    #aicard {
        padding: 20px;
    }
}
</style>