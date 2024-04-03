const API_ROUTES = {
  //账户控制
    login: '/user/login',
    register: '/user/register',
    UserInfo: '/user/get_current_user_info',
    UserInfoByID: '/user/get_user_info_by_id',
    editProfile: '/user/modify_user_info',

    isFollowed: '/user/is_followed',
    follow: '/user/follow_user',
    unfollow: '/user/unfollow_user',
    followList: '/user/follow_list',
    

    //文章控制
    creatArticle: '/article/create_article',
    getUserArticles: '/article/show_articles',
    getArticleById: '/article/show_article_by_id',
    getRandomTenArticles: '/article/get_random_ten_articles',
    updataArticle: '/article/update_article_by_id',
    deleteArticle: '/article/delete_article_by_id',
    searchArticles: '/article/search_article_by_keyword',
    //GPT控制
    gptChat: '/chat',
    gptChatStream: '/chat_stream',

    //图片上传
    uploadPhoto: '/upload_photo'

  };
  
  export default API_ROUTES;