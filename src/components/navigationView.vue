<script setup>
import searchView from './SearchView.vue';
import { useStore } from 'vuex';
import { HomeOutlined, SolutionOutlined, LoginOutlined, LogoutOutlined, EditOutlined } from '@ant-design/icons-vue';
import { ref } from 'vue';
const store = useStore();
const logout = () => {
    //action里面的成员才是用dispatch
    //store.dispatch("logout");

    //mutation里面的成员用commit！！！！
    store.commit("logout");
    console.log("test")
};

let notMobile = ref(true);
if (window.matchMedia('(max-width: 500px)').matches) {
    notMobile.value = false; // 如果屏幕宽度小于500px，设置notMobile为false
}
</script>


<template>
    <nav class="container">
        <div id="blog-info">
            <div class="blog-img">
                <img src="../assets/coin.webp" alt="图像">
            </div>
            <router-link to="/"><span class="site-name">Instant-Blog</span></router-link>
        </div>

        <div id="menus">
            <span id="search-button" v-if="notMobile">
                <searchView></searchView>
            </span>
            <span id="menus-items">
                <span v-if="notMobile">
                    <home-outlined /><router-link to="/">首页</router-link>
                </span>
                <span v-if="!$store.state.user.is_login">
                    <span v-if="notMobile"><solution-outlined /><router-link to="/login">个人主页</router-link></span>
                    <span v-if="notMobile"><edit-outlined /> <router-link to="/login">创作中心</router-link></span>
                    <span><login-outlined /> <router-link to="/login">登录</router-link></span>
                    <span><logout-outlined /><router-link to="/register">注册</router-link></span>
                </span>

                <span v-else>
                    <span v-if="notMobile">
                        <solution-outlined />
                        <router-link :to="{ name: 'userprofile', params: { userID: $store.state.user.id } }">个人主页
                        </router-link>
                    </span>
                    <span v-if="notMobile"><edit-outlined /> <router-link to="/writecenter">创作中心</router-link></span>
                    <span><login-outlined /> <router-link
                            :to="{ name: 'userprofile', params: { userID: $store.state.user.id } }">{{
                                $store.state.user.username }}</router-link></span>
                    <span><logout-outlined /><router-link class="nav-link" :to="{ name: 'home' }"
                            @click="logout">退出</router-link></span>
                </span>


            </span>

        </div>

    </nav>
</template>


<style scoped>
.container {
    display: flex;
    height: 60px;
    margin: 0;
    padding: 0;
    align-items: center;


}

.blog-img {
    display: flex;
    align-items: center;
    overflow: hidden;
    width: 30px;
    height: 30px;
    border-radius: 70px;
}

.blog-img img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

#blog-info {
    font-size: 1.5em;
    font-weight: bold;
    display: flex;
    width: 25%;
    margin: 0 .5em;
}

.site-name {
    color: #fff;
    mix-blend-mode: difference;
}

#menus {
    display: flex;
    justify-content: flex-end;
    width: 70%;
    align-items: center;
    font-size: 1.1em
}

span {
    margin: 0 .5em;
}

#menus-items a {
    color: #fff;
    mix-blend-mode: difference;

}

.change-color {
    mix-blend-mode: difference;
}

@media only screen and (max-width: 500px) {
    .remove {
        display: none;
    }

    #blog-info {
    font-size: 1.1em;
    font-weight: bold;
    display: flex;
    width: 50%;
    margin: 0 .5em;
}

}
</style>