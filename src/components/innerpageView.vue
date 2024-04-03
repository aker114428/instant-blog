
<template>
    <div class="recent-posts" id="recent-posts">
        <div class="item" v-for="article in array" :key="article['文章id']">
            <recentPostItem :title="article['articleTitle']" :pageImg="article['articleHeadImg']"
                :publishTime="article['updateTime']" :briefInfo="article['introduction']" :author="article['authorName']"
                :key="article['id']" :id="article['id']"></recentPostItem>
                
            <div v-if="authorID === $store.state.user.id || username === 'root'" class="buttons">
                <a-button v-if="notMobile" type="primary" id="edit" @click="editArticle(article['id'])">编辑</a-button>
                <a-button v-if="notMobile" danger id="delete" @click="deleteArticle(article['id'])">删除</a-button>
            </div>
        </div>

    </div>
</template>

<script>
import { useStore } from 'vuex';
import { message } from 'ant-design-vue';
import API_ROUTES from "@/api/api";
import { backendIP } from "@/api/backend";
import router from '@/router'
import recentPostItem from './recentPostItem.vue';
import { ref } from 'vue';
export default {
    components: {
        recentPostItem
    },
    setup() {
        let store = useStore();
        const token=store.state.user.access;
        const userID=store.state.user.id;
        const username=store.state.user.username;
        let notMobile = ref(true);
        if (window.matchMedia('(max-width: 500px)').matches) {
            notMobile.value = false; // 如果屏幕宽度小于500px，设置notMobile为false
        }
        return {
            token,userID,store,username,notMobile
        }
    },
    props: {
        authorID: Number,
    },
    data() {
        return {
            array: []
        }
    },
    methods: {
        //删除文章
        deleteArticle(articleId) {
            
            console.log('userID:'+this.userID)
            console.log('token: '+this.token)

            
            debugger//eslint-disable-line
            var myHeaders = new Headers();
            myHeaders.append("Authorization", this.store.state.user.access);

            var formdata = new FormData();
            debugger//eslint-disable-line
            formdata.append("article_id", articleId);

            var requestOptions = {
                method: 'POST',
                headers: myHeaders,
                body: formdata,
                redirect: 'follow'
            };

            fetch(backendIP+API_ROUTES.deleteArticle, requestOptions)
                .then(response => response.json())
                .then(result => {
                    message.info(result["message"]);
                    console.log('')
                })
                .catch(error => console.log('error', error));

            this.initArticle();
        },
        initArticle() {
            //获取作者所有文章
            
            var myHeaders = new Headers();
            myHeaders.append("Authorization",this.store.state.user.access);

            var formdata = new FormData();
            formdata.append("user_id", this.authorID);

            var requestOptions = {
                method: 'POST',
                headers: myHeaders,
                body: formdata,
                redirect: 'follow'
            };

            fetch(backendIP+API_ROUTES.getUserArticles, requestOptions)
                .then(response => response.json())
                .then(result => {
                    console.log(result);
                    result=result['data']
                    this.array = result['items'];
                    console.log(this.array);
                })
                .catch(error => console.log('error', error));

            console.log('authorId:' + this.authorID);

        },
        editArticle(ID){
            router.push({ name: 'articleModify', params: { articleID: ID } })
        }
    },
    mounted() {
        this.initArticle();
    }
}
</script>
<style scoped>
.recent-post {
    width: 100%
}

.buttons {
    display: flex;
    justify-content: flex-end;
}

.buttons button {
    width: 100px;
    margin: 10px;
}
</style>