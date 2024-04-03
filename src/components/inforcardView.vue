

<template>
    <div class="card-widget card-info">
        <div class="is-center">
            <div class="avatar-img" @click="toAuthorPage">
                <img :src="imgUrl" alt="头像">
            </div>
            <div class="author-info__name">{{ authorName }}</div>
            <div class="author-info__description">{{ authorIntroduce }}</div>
        </div>
        <div class="card-info-data site-data is-center">
            <div style="color:rgb(44, 116, 209)">
                <div class="headline">文章</div>
                <div class="length-num">{{ articleNum }}</div>
            </div>
        </div>

        <div class="card-info-social-icons is-center">
            <a-button v-if="!isFollowed" type="primary" @click="follow">关注</a-button>
            <a-button v-if="isFollowed" type="primary" @click="restoreFollow"
                style="background-color: rgb(150, 180, 180);">取消关注</a-button>

        </div>
    </div>
</template>

<script>
//import {ref} from 'vue';
import { useStore } from 'vuex';
import { message } from 'ant-design-vue';
import API_ROUTES from "@/api/api";
import { backendIP } from "@/api/backend";

import router from '@/router'
export default {
    setup() {
        let store = useStore();
        // 返回值会暴露给模板和其他的选项式 API 钩子
        return {
            store
        }
    },
    props: {
        authorId: Number
    },
    data() {
        return {
            articleNum: 0,
            authorName: '',
            authorIntroduce: '',
            imgUrl: '',
            isFollowed: false
        }
    },
    methods: {
        toAuthorPage(){
            router.push({ name: 'userprofile', params: { userID: this.authorId } })
        },
        judgeFollow() {
            //判断关注
            if (this.store.state.user.is_login) {
                if (this.authorId !== this.store.state.user.id) {
                   
                    var myHeaders = new Headers();
                    myHeaders.append("Authorization", this.store.state.user.access);

                    var formdata = new FormData();
                    
                    formdata.append("id", this.authorId);

                    var requestOptions = {
                        method: 'POST',
                        headers: myHeaders,
                        body: formdata,
                        redirect: 'follow'
                    };

                    fetch(backendIP+API_ROUTES.isFollowed, requestOptions)
                        .then(response => response.json())
                        .then(result => {
                            console.log(result)
                            this.isFollowed = result["data"];
                            console.log('判断是否关注' + this.isFollowed)
                        })
                        .catch(error => console.log('error', error));
                }

            }

        },
        follow() {
            if (!this.store.state.user.is_login) {
                message.info('请先登录');
            }
            else if (this.authorId === this.store.state.user.id) {
                message.info('不能关注自己');
            }
            else {
               
                var myHeaders = new Headers();
                myHeaders.append("Authorization", this.store.state.user.access);

                var formdata = new FormData();
               
                formdata.append("followUserId", this.authorId);

                var requestOptions = {
                    method: 'POST',
                    headers: myHeaders,
                    body: formdata,
                    redirect: 'follow'
                };

                fetch(backendIP+API_ROUTES.follow, requestOptions)
                    .then(response => response.json())
                    .then(result => {
                        console.log(result);
                        message.info(result['message'])
                        this.isFollowed=true;
                    })
                    .catch(error => console.log('error', error));
            }

        },
        restoreFollow() {//取消关注
            
            var myHeaders = new Headers();
            myHeaders.append("Authorization", this.store.state.user.access);

            var formdata = new FormData();
            
            formdata.append("unfollowUserId", this.authorId);

            var requestOptions = {
                method: 'POST',
                headers: myHeaders,
                body: formdata,
                redirect: 'follow'
            };

            fetch(backendIP+API_ROUTES.unfollow, requestOptions)
                .then(response => response.json())
                .then(result => {
                    console.log(result);
                    this.isFollowed=false;
                    message.info(result['message'])
                })
                .catch(error => console.log('error', error));
        }
    },
    created() {
        this.judgeFollow();

        console.log(this.authorId)
        console.log('aaaaaa')
        
        var myHeaders = new Headers();
        myHeaders.append("Authorization", this.store.state.user.access);

        var formdata = new FormData();
        formdata.append("id", this.authorId);

        var requestOptions = {
            method: 'POST',
            headers: myHeaders,
            body: formdata,
            redirect: 'follow'
        };

        fetch(backendIP+API_ROUTES.UserInfoByID, requestOptions)
            .then(response => response.json())
            .then(result => {
                console.log(result);
                result=result['data'];
                this.articleNum = result['articleNumber'];
                this.authorName = result['username'];
                this.authorIntroduce = result['selfIntroduction'];
                this.imgUrl = result['userPhoto']
                console.log(this.articleNum)
            })
            .catch(error => console.log('error', error));


    }
}



</script>

<style scoped>
.card-widget {
    background-color: white;

    top: 20px;
    overflow: hidden;
    padding: 20px 24px;
    border-radius: 8px;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.09);
}

.is-center {
    text-align: center;
}

.avatar-img {
    overflow: hidden;
    margin: 0 auto;
    width: 110px;
    height: 110px;
    border-radius: 70px;
}

.avatar-img img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.card-info .author-info__name {
    font-weight: 500;
    font-size: 1.57em;
}

#aside-content .card-info .author-info__description {
    margin-top: -.42em;
}

#aside-content .card-info .card-info-data {
    margin: 14px 0 4px;
}

.site-data {
    display: flex;
    width: 100%;
    justify-content: space-evenly;
}

.card-info-social-icons .social-icon {
    color: black;
    font-weight: 900;
    color: var(--font-color);
    font-size: 1.4em;
}

#follow {
    background-color: rgb(187, 187, 233);
    border: none;
    color: white;
}
</style>