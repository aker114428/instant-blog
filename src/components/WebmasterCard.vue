<script setup>
import { ref } from 'vue';
import { GithubOutlined,MailFilled } from '@ant-design/icons-vue';
let imgUrl = ref("https://source-warehouse.oss-cn-guangzhou.aliyuncs.com/img/34746474_p0.jpg");
let authorName = ref("站长");
let authorIntroduce = ref("遇到问题请联系我");

</script>
<template>
    <div class="card-widget card-info container">
        <div class="is-center">
            <div class="avatar-img">
                <img :src="imgUrl" alt="头像">
            </div>
            <div class="author-info__name">{{ authorName }}</div>
            <div class="author-info__description">{{ authorIntroduce }}</div>
        </div>
        

        <div class="is-center">
            
            <a  id="follow" target="_blank" rel="noopener" href="https://github.com/aker114428"><i
                class="fab fa-github"></i><GithubOutlined /><span>Follow Me</span></a>

            <div>
                <MailFilled />1144280511@qq.com
            </div>

        </div>

        
    </div>
</template>

<style scoped>
.card-widget {
    background-color: white;
    width: 100%;
    height: 40vh;
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
    width: 14vh;
    height: 14vh;
    border-radius: 70px;
}

.avatar-img img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.card-info .author-info__name {
    font-weight: 500;
    font-size: 1.3em;
    padding: 3px;
}

.author-info__description {
    padding: 3px;
    margin-top: 20px;
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
    display: block;
    margin-top: 14px;
    background-color: rgb(56, 56, 165);
    
    color: white;
    text-align: center;
    line-height: 2.4;
}
</style>