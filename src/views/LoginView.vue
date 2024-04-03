<template>
    <div class="container">

        <div class="card">

            <div class="welcom">
                <div>
                    <a-icon #="loginicon">
                        <LoginOutlined />
                    </a-icon>
                </div>
                <div>
                    欢迎登陆
                </div>

            </div>
            <div class="login-form-container">
                <a-row type="flex" justify="center" align="middle">
                    <a-col xs={24} sm={12} md={8} lg={6}>
                        <a-form name="basic" @submit.prevent="login">
                            <a-form-item  name="username" rules="[{required: true, message: '请输入用户名！'}]">
                                <span class="label-width">用户名：</span>
                                <a-input v-model:value="username" class="equal-width"/>
                            </a-form-item>
                            <a-form-item  name="password" rules="[{required: true, message: '请输入密码！'}]">
                                <span class="label-width">密码：</span>
                                <a-input-password v-model:value="password" class="equal-width" />
                            </a-form-item>
                            
                                <a ><router-link to="/register">没有账户，前往注册</router-link></a>
                           
                            <div class="error-message">{{ error_message }}</div>
                            <a-form-item>
                                <a-button type="primary" html-type="submit">登录</a-button>
                            </a-form-item>
                        </a-form>
                    </a-col>
                </a-row>
            </div>

        </div>
    </div>
</template> 
  
<script>
//import ContentBase from '../components/ContentBase.vue';
//import ContentBaseNcard from '@/components/ContentBaseNcard.vue';
import { ref } from 'vue';
import { useStore } from 'vuex';
import router from '@/router/index';
import { LoginOutlined } from '@ant-design/icons-vue';


export default {
    name: 'LoginView',
    components: { LoginOutlined },
    setup() {

        const onFinish = values => {
            console.log('Success:', values);
        };
        const onFinishFailed = errorInfo => {
            console.log('Failed:', errorInfo);
        };
        const store = useStore();
        let username = ref('');
        let password = ref('');
        let remember = ref(true);
        let error_message = ref('');

        const login = () => {
            //登录前把error message清空
            error_message.value = '';
            store.dispatch('login', {
                username: username.value,
                password: password.value,
                success() {
                    console.log("success")
                    //（1）实现登录成功后跳转
                    router.push({ name: "home" });
                },
                error() {
                    //（2）错误的话，显示错误信息
                    // html里面直接放这个响应式变量即可，为空的时候反正也不会显示
                    console.log("failed");
                    error_message.value = "用户名或密码错误";
                }
            });
        }

        return {
            onFinish,
            onFinishFailed,
            username, password, remember, login, error_message
        };
    }
}
</script >

<style scoped>
.login-form-container {
    margin-top: 20px;
    
}

.error-message {
    color: red;
}

button {
    margin-top: 5px;
    width: 100%;
}

.welcom {
    display: flex;
    align-items: center;
    justify-content: center;
    text-align: center;
    font-size: 24px;
    margin-top: 4vh;
}

#loginicon {
    display: flex !important;

}

.container {
    display: flex;
    justify-content: center;
    /* 水平居中 */
    align-items: center;
    /* 竖直居中 */
    height: 100vh;
    /* 或者任何指定的高度 */
}

.card {

    width: 80vh;
    background-color: white;
    box-sizing: border-box;
    
    margin-top: 20px;
    box-shadow: 0 3px 8px 6px rgba(7,17,27,0.09);
    border-radius: 20px 20px;
    overflow: hidden;
}

.equal-width {
  width: 250px; /* 或者根据需要设置的宽度 */
  
}

.label-width{
    display: inline-block; /* 或者使用 block，根据需要选择 */
    width: 60px; /* 设置所需的宽度 */
}


</style>