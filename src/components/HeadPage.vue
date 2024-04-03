<script setup>
import pageHeader from '../components/pageHeader.vue';
import recentPostItem from '@/components/recentPostItem.vue';
import {ref} from 'vue';
import API_ROUTES from "@/api/api";
import { backendIP } from "@/api/backend";
import { message } from 'ant-design-vue';
const info = () => {
    message.info('网络出错请刷新');
};

let array=ref();
let num=ref();

let myUrl = backendIP+API_ROUTES.getRandomTenArticles;
fetch(myUrl, {
  method: 'get'
  })
  .then(response => response.json())
  .then(data => {
    if(data["code"]==1){
      info();
    }
    else{
      data=data["data"];
      console.log(data);
      num.value=data['total'];
      array.value=data['items'];
      console.log(array.value)
    }
    
});

console.log("文章数："+num.value);
console.log("文章列表"+array.value);



</script>

<template>
  <header class="full-page" id="page-header">
    <pageHeader></pageHeader>
  </header>

  <main class="layout">
    <div class="first-page-article">
      <recentPostItem v-for="article in array" :title="article['articleTitle']" :pageImg="article['articleHeadImg']"
      :publishTime="article['updateTime']" :briefInfo="article['introduction']" :author="article['authorName']" :key="article['id']" :id="article['id']"></recentPostItem>
    </div>

  </main>
</template>

<script>

</script>


<style scoped>
header.full-page {
  height: 100vh;
  background-attachment: fixed;
  background-image: url('../assets/index.png');
  color: white;
}

#page-header {
  position: relative;
  width: 100%;
  background-position: center center;
  background-size: cover;
  background-repeat: no-repeat;

}

.layout {
  flex: 1 auto;
  margin: 0 auto;
  padding: 40px 15px;
  max-width: 1200px;
  width: 100%;
}

#menus-items a {
  color: white !important;
}
</style>
