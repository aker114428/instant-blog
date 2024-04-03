<script setup>
import { ref } from 'vue';
import recentPostItem from './recentPostItem.vue';
import { message } from 'ant-design-vue';
import API_ROUTES from "@/api/api";
import { backendIP } from "@/api/backend";
import { useStore } from 'vuex';
const value = ref('');
const store = useStore();

const onSearch = searchValue => {
  console.log('use value', searchValue);
  console.log('or use this.value', value.value);
  if (!searchValue) {
    message.info('搜索内容为空')
  }
  else {
    search(searchValue)
    showModal();
  }

};

const visible = ref(false);
const showModal = () => {
  visible.value = true;
};
const handleOk = e => {
  console.log(e);
  visible.value = false;
};

//文章列表
let array = ref()
function search(searchValue) {
  var myHeaders = new Headers();
  
  myHeaders.append("Authorization", store.state.user.access);

  var formdata = new FormData();
  formdata.append("keyword", searchValue);

  var requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: formdata,
    redirect: 'follow'
  };

  fetch(backendIP+API_ROUTES.searchArticles, requestOptions)
    .then(response => response.json())
    .then(result => {
      result=result["data"]
      array.value = result["items"]
      console.log(array.value);
    })
    .catch(error => console.log('error', error));
}
</script>
<template>
  <a-space direction="vertical">
    <a-input-search v-model="value" placeholder="input search text" style="width: 200px" @search="onSearch">
    </a-input-search>
  </a-space>
  <div>
    <a-modal v-model:visible="visible" width="1000px" title="搜索结果" @ok="handleOk" class="layout">
      <div class="item" v-for="article in array" :key="article['文章id']">
        <recentPostItem  :title="article['articleTitle']" :pageImg="article['articleHeadImg']"
      :publishTime="article['updateTime']" :briefInfo="article['introduction']" :author="article['authorName']" :key="article['id']" :id="article['id']"></recentPostItem>
        </div>
    </a-modal>
  </div>
</template>
  
<style scoped>
.layout{
  max-height: 80vh;
  overflow-block: scroll;
}
</style>