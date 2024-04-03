<template>
    <footer>
      <p id="text">滇ICP备2024022589号</p>
    </footer>
  </template>
  
  <script>
  export default {
    name: 'AppFooter',
  }
  </script>
  
  <style scoped>
  /* 这里添加你的CSS样式 */
  footer {
    display: flex;
    align-content: center;
    justify-content: center;
    left: 0;
    bottom: 0;
    width: 100%;
    background-color: #e0e5eb;
    
    padding: 20px 0;
  }

  #text{
    color:black
  }
  </style>
  