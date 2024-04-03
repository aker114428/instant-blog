<template>
  <div class="padding" id="container">

    <input type="file" @change="handleFileChange" accept="image/*" />
    <button v-if="selectedFile" @click="uploadImage">上传图片</button>
    <div v-if="isUploading" id="spin">
      <a-spin  />
    </div>
    

  </div>
</template>

<script setup>
import { ref } from 'vue';
import { backendIP } from "@/api/backend";
import { useStore } from 'vuex';
import API_ROUTES from "@/api/api";
const selectedFile = ref(null);
const imageUrl = ref('');
let store = useStore();
const emit = defineEmits(['update']);
const isUploading=ref(false);
const handleFileChange = (event) => {
  selectedFile.value = event.target.files[0];

};

const sendToParent = () => {
  // 使用 $emit 触发自定义事件 'update', 并传递 userInput 的值给父组件
  emit('update', imageUrl.value);
  console.log("上传的图片url子组件");
  console.log(imageUrl.value);
};

const maxSize = 4 * 1024 * 1024; // 设置最大文件大小，例如2MB

const uploadImage = () => {
  if (!selectedFile.value) {
    alert('请选择一个文件上传。');
    return;
  }

  if (selectedFile.value.size > maxSize) {
    // 如果文件大小超过限制
    alert('文件大小不能超过4MB'); 
    // 可以选择清空文件输入框或进行其他操作
    return;
  }

  var myHeaders = new Headers();
  myHeaders.append("Authorization", store.state.user.access);
  myHeaders.append("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)");

  isUploading.value=true;
  var formdata = new FormData();
  formdata.append("file", selectedFile.value, selectedFile.value.name);

  var requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: formdata,
    redirect: 'follow'
  };

  fetch(backendIP + API_ROUTES.uploadPhoto, requestOptions)
    .then(response => response.json())
    .then(result => {
      console.log("接收data");
      console.log(result);
      imageUrl.value = result['data'];
      console.log(result['data']);
      sendToParent();
      isUploading.value=false;
      alert('文件上传成功!');

    })
    .catch(error => {
      console.log('error', error);
      isUploading.value=false;
      alert('上传失败，请重试。');
    });
};
</script>

<style scoped>
.padding {
  padding-top: 8px;

}

#container {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

#spin{
  padding-left: 5px;
}

</style>
