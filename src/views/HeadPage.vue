<script setup>
import { defineAsyncComponent, ref } from 'vue';


import API_ROUTES from "@/api/api";
import { backendIP } from "@/api/backend";
import { message } from 'ant-design-vue';
import AiReadCardHeadPage from '@/components/AiReadCardHeadPage.vue';
import WebmasterCard from '@/components/WebmasterCard.vue';

//懒加载
const PageHeader = defineAsyncComponent(() => import('../components/pageHeader.vue'));
const RecentPostItem = defineAsyncComponent(() => import('@/components/recentPostItem.vue'));



const info = (mesg) => {
  message.info(mesg);
};

let notMobile = ref(true);
if (window.matchMedia('(max-width: 500px)').matches) {
  notMobile.value = false; // 如果屏幕宽度小于500px，设置notMobile为false
}

let array = ref();
let num = ref();

let myUrl = backendIP + API_ROUTES.getRandomTenArticles;
fetch(myUrl, {
  method: 'get'
})
  .then(response => response.json())
  .then(data => {
    if (data["code"] == 1) {
      info("获取文章首页失败");
    }
    else {
      data = data["data"];
      console.log(data);
      num.value = data['total'];
      array.value = data['items'];
      console.log(array.value)
    }

  });

console.log("文章数：" + num.value);
console.log("文章列表" + array.value);



</script>

<template>
  <header class="full-page" id="page-header">
    <PageHeader></PageHeader>
  </header>

  <main class="layout">

    <div class="first-page-article">
      <RecentPostItem v-for="article in array" :title="article['articleTitle']" :pageImg="article['articleHeadImg']"
        :publishTime="article['updateTime']" :briefInfo="article['introduction']" :author="article['authorName']"
        :key="article['id']" :id="article['id']"></RecentPostItem>
    </div>

    <div id="cards" v-if="notMobile">
      <div id="master-card">
        <WebmasterCard></WebmasterCard>
      </div>
      <div id="aicard">
        <AiReadCardHeadPage text="无文章内容"></AiReadCardHeadPage>
      </div>
    </div>

    <!-- 移动端 -->
    <div v-if="!notMobile">
      <AiReadCardHeadPage text="无文章内容" class="card-mobile"></AiReadCardHeadPage>
      <WebmasterCard class="card-mobile"></WebmasterCard>
    </div>



  </main>
</template>

<script>

</script>


<style scoped>
header.full-page {
  height: 100vh;
  background-attachment: fixed;
  background-image: url('../assets/index.webp');
  color: white;
}

#page-header {
  position: relative;
  width: 100%;
  background-position: center center;
  background-size: cover;
  background-repeat: no-repeat;

}

.first-page-article {
  width: 65%;
}

.layout {

  display: flex;
  flex-wrap: wrap;
  justify-content: center;


}

#menus-items a {
  color: white !important;
}

#cards {
  width: 20%;
  height: 30vh;
  position: sticky;
  top: 0px;

  padding-top: 20px;
  padding-left: 40px;
}



#aicard {
  width: 35vh;
  
}

#master-card{
  width: 35vh;
  padding-bottom: 20px;
}

.sticky {
    margin-top: 20px;
    position: sticky;
    top: 20px;
}

@media only screen and (max-width: 500px) {

  .card-mobile {

    margin-top: 20px;

  }

  .first-page-article {
    width: 130vh;
    padding-right: 10px;
    padding-left: 10px;
  }
}
</style>
