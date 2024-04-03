<script setup>
import Navigation from '../components/navigationView.vue';
import AiReadCard from '@/components/AiReadCard.vue';
import router from '@/router'
import { message } from 'ant-design-vue';
import { useStore } from 'vuex';
import { useRoute } from 'vue-router';
//import richTextEditor from '../components/richTextEditor.vue';
import { ref } from 'vue';
import API_ROUTES from "@/api/api";
import { backendIP,backendGptIP } from "@/api/backend";
import uploadPhoto from '@/components/uploadPhoto.vue';

//富文本部分
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';

// highlightjs
import hljs from 'highlight.js';

VMdEditor.use(githubTheme, {
    Hljs: hljs,
});

VMdPreview.use(githubTheme, {
    Hljs: hljs,
});



const store = useStore();
const loading = ref(false);
const visible = ref(false);//上传文章
const dvisible = ref(false);//文章润色
const dvisible2 = ref(false);//文章续写
const wvisible = ref(true);//加载图标
const ivisible = ref(false)//修改图片
//点击上传文章响应函数
const showModal = () => {
    if (!text.value) {
        message.info('文章内容为空');
    }
    else {
        console.log('show modal')
        visible.value = true;
    }

};
//点击确定响应函数
const handleOk = () => {
    loading.value = true;
    console.log('text' + text.value);
    console.log('title' + articleTitle.value);
    setTimeout(() => {
        loading.value = false;
        visible.value = false;
        publishArticle()
    }, 2000);
};
const handleCancel = () => {
    visible.value = false;

};

//获取编辑的文章信息
let text = ref('');
let articleTitle = ref('');
let polishedText = ref('');
let imgUrl = ref('');
let imgUrlStyle = ref('');
let introduction = ref('');

let publishTime = ref();



const route = useRoute();
const articleID = parseInt(route.params.articleID);
console.log('article id:' + articleID)
getArticle(articleID)
//点击富文本编辑器里的save
function saveFile(text, html) {
    console.log('successfully saved')
    console.log(text)
    console.log(html)
}

//根据id获取文章内容

function getArticle(id) {
    var myHeaders = new Headers();
    myHeaders.append("Authorization", store.state.user.access);

    var formdata = new FormData();
    formdata.append("article_id", id);

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: formdata,
        redirect: 'follow'
    };

    fetch(backendIP + API_ROUTES.getArticleById, requestOptions)
        .then(response => response.json())
        .then(result => {
            result = result["data"];
            articleTitle.value = result['articleTitle'];
            text.value = result['articleText'];
            introduction.value=result['introduction'];
            publishTime.value = result['updateTime'].slice(0, 10);
            imgUrl.value=result['articleHeadImg'];
            imgUrlStyle.value = " background-image: url(" + result['articleHeadImg'] + ")";

            //console.log('作者id:'+authorId.value);


        })
        .catch(error => console.log('error', error));
}

//上传文章
function publishArticle() {
    var myHeaders = new Headers();
    myHeaders.append("Authorization", store.state.user.access);

    var formdata = new FormData();
    formdata.append("articleTitle", articleTitle.value);
    formdata.append("articleText", text.value);
    formdata.append("introduction", introduction.value);
    formdata.append("authorName", store.state.user.username);
    formdata.append("authorId", store.state.user.id);
    formdata.append("id", articleID);
    formdata.append("articleHeadImg", imgUrl.value);

    
    console.log("introduction");
    console.log(introduction.value);

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: formdata,
        redirect: 'follow'
    };

    fetch(backendIP + API_ROUTES.updataArticle, requestOptions)
        .then(response => response.json())
        .then(result => {
            console.log(result);
            console.log("successfully published")

            router.push({ name: 'article', params: { articleID: articleID } })
        })
        .catch(error => console.log('error', error));
}
//文章润色
function articlePolishing() {
    if (!text.value) {
        message.info('文章内容为空');
    }
    else {
        wvisible.value = true;
        dvisible.value = true;
        var myHeaders = new Headers();
        myHeaders.append("Authorization", store.state.user.access);

        var formdata = new FormData();
        
        formdata.append("gpt_message", "润色一下给定文章，使文章表述更清晰，内容更规整有序。注意文章采用markdown格式书写，排版要美观，文章内容如下：" + text.value);

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
                wvisible.value = false;
                polishedText.value = result["data"];
            })
            .catch(error => console.log('error', error));
    }
}
//续写
function articleCompletion(){
    if (!text.value) {
        message.info('文章内容为空');
    }
    else {
        wvisible.value=true;
        dvisible2.value = true;
        var myHeaders = new Headers();
        myHeaders.append("Authorization", store.state.user.access);

        var formdata = new FormData();
        
        formdata.append("gpt_message", "补充续写一下给定文章，内容更完整。注意文章采用markdown格式书写，排版要美观，文章内容如下：" + text.value);

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
                wvisible.value=false;
                polishedText.value = text.value+'\n'+ result["data"];
            })
            .catch(error => console.log('error', error));
    }
}
//修改图片
function changeImg() {
    ivisible.value = true;
}
//处理图片上传
const handleUpdate = (value) => {
    
    imgUrl.value = value;
};

function changeImgOk() {
    imgUrlStyle.value = " background-image: url(" + imgUrl.value + ")";
    ivisible.value = false;
}

const onClose = () => {
    dvisible.value = false;
    dvisible2.value = false;
}

function adopt() {
    text.value = polishedText.value;
    onClose();
}
</script>

<template>
    <header class="navigation">
        <navigation></navigation>
    </header>

    <main class="layout">
        <div id="recent-post">
            <div id="post">
                <div id="headimg" :style="imgUrlStyle">
                    <h1 class="site-title" style="color:white">{{ articleTitle }}</h1>
                </div>
                <div id="article-container">
                    <v-md-editor v-model="text" height="600px" @save="saveFile"></v-md-editor>
                </div>
            </div>
        </div>

        <div id="aside-content">
            <div class="sticky">
                <AiReadCard :text="text"></AiReadCard>

                <div id="aside-button">
                    <a-button type="primary" class="button" @click="articlePolishing">一键润色</a-button>
                    <a-button type="primary" class="submit-button" @click="articleCompletion">AI续写</a-button>
                    <a-button type="primary" class="submit-button" @click="changeImg">修改封图</a-button>
                    <a-button type="primary" class="submit-button" @click="showModal">上传修改的文章</a-button>
                </div>
            </div>

        </div>

        <div class="submit-modal">
            <a-modal v-model:visible="visible" title="文章标题和简介" @ok="handleOk">
                <template #footer>
                    <a-button key="back" @click="handleCancel">取消</a-button>
                    <a-button key="submit" type="primary" :loading="loading" @click="handleOk">确定</a-button>
                </template>
                <a-input-group compact>
                    <a-input v-model:value="articleTitle" id="input" placeholder="文章标题"/>
                    <a-input v-model:value="introduction" placeholder="文章简介"/>
                </a-input-group>
            </a-modal>
        </div>
        <a-drawer title="文章润色结果(需要约10s请耐心等待)" size='large' placement="left" :visible="dvisible" @close="onClose">
            <template #extra>
                <a-button style="margin-right: 8px" @click="onClose">取消</a-button>
                <a-button type="primary" @click="adopt">采纳</a-button>
            </template>
            <a-spin v-if="wvisible" />
            <div class="text-contain">
                
                <v-md-preview v-if="!wvisible" :text="polishedText" style="overflow: hidden"></v-md-preview>
            </div>
        </a-drawer>
        <a-drawer title="文章续写结果(需要约10s请耐心等待)" size='large' placement="left" :visible="dvisible2" @close="onClose">
            <template #extra>
                <a-button style="margin-right: 8px" @click="onClose">取消</a-button>
                <a-button type="primary" @click="adopt">采纳</a-button>
            </template>
            <a-spin v-if="wvisible" />
            <div class="text-contain">
                
                <v-md-preview v-if="!wvisible" :text="polishedText" style="overflow: hidden"></v-md-preview>
            </div>
            
        </a-drawer>
        <div class="submit-modal">
            <a-modal v-model:visible="ivisible" title="选择文章封图" @ok="handleOk">
                <template #footer>
                    <a-button key="back" @click="handleCancel">取消</a-button>
                    <a-button key="submit" type="primary" :loading="loading" @click="changeImgOk">确定</a-button>
                </template>
                <a-input-group compact>
                    <a-input v-model:value="imgUrl" />
                    <uploadPhoto @update="handleUpdate"></uploadPhoto>
                </a-input-group>
            </a-modal>
        </div>
    </main>
</template>



<style scoped>
.layout {

    display: flex;
    flex: 1 auto;
    margin: 0 auto;
    padding: 40px 15px;
    max-width: 1300px;
    width: 100%;
}

.layout #recent-post {
    width: 80%;
}

#aside-content {
    padding-left: 15px;
    width: 20%;

}

span a {
    color: black;
}

#aside-button {
    margin-top: 10px;
    display: flex;
    flex-direction: column;
    align-content: center;
    justify-content: center;

}

#aside-button .button {
    width: 100%;
}

.sticky {
    position: sticky;
    top: 20px;
}

.submit-button {
    margin-top: 10px;
}

.submit-modal {
    margin-top: 100px;
}

/* 富文本 */
#post {
    align-self: flex-start;
    -ms-flex-item-align: start;
    padding: 50px 40px;

    border-radius: 8px;
    background-color: white;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.09);

}



#article-container {
    overflow-wrap: break-word;
}

.v-md-editor {
    height: 100% !important;
}

.text-contain {
    display: flex;
}

#headimg {
    height: 50vh;
    background-size: cover;
    color: white;
    position: relative;
    width: 100%;
    background-color: #49b1f5;
    background-position: center center;
    background-repeat: no-repeat;

    display: flex;
    justify-content: center;
}

.site-title {
    align-self: center;

}

#input{
    margin-bottom: 15px;
}
</style>