<template>
    <div class="container">
        <div class="card">
            <div class="welcom" >
                欢迎注册
            </div>
            <div class="login-form-container">
                <a-row type="flex" justify="center" align="middle">
                    <a-col xs={24} sm={12} md={8} lg={6}>
                        <a-form name="basic" @submit.prevent="register">
                            <a-form-item name="username">
                                <span class="label-width">用户名：</span>
                                <a-input v-model:value="username" class="equal-width" />
                            </a-form-item>
                            <a-form-item name="password" rules="[{ required: true, message: '请输入密码！' }]">
                                <span class="label-width">密码：</span>
                                <a-input-password v-model:value="password" class="equal-width" />
                            </a-form-item>
                            <a-form-item name="confirmPassword" rules="[{ required: true, message: '请再次输入密码！' }]">
                                <span class="label-width">确认密码：</span>
                                <a-input-password v-model:value="password2" class="equal-width" />
                            </a-form-item>
                            <div class="error-message">{{ error_message }}</div>
                            <a ><router-link to="/login">已有账户，前往登录</router-link></a>
                            <a-form-item>
                                <a-button type="primary" html-type="submit">注册</a-button>
                            </a-form-item>
                        </a-form>
                    </a-col>
                </a-row>
            </div>
        </div>
    </div>
</template>

<script>
import { ref } from 'vue';
import { useStore } from 'vuex';
import router from '@/router/index';
import { message } from 'ant-design-vue';
export default {
    name: 'RegisterView',
    setup() {
        const store = useStore();
        let username = ref('');
        let password = ref('');
        let password2 = ref(''); // 确认密码
        let error_message = ref('');

        const register = () => {
            error_message.value = ''; // 清空错误信息
            if (password.value !== password2.value) {
                error_message.value = "两次输入的密码不一致";
                return;
            }
            if (username.value == '') {
                error_message.value = "请输入用户名";
                return;
            }
            if (password.value == '') {
                error_message.value = "请输入密码";
                return;
            }
            // 调用store里面的注册函数
            store.dispatch('register', {
                username: username.value,
                password: password.value,
                success() {
                    console.log("success");
                    message.info('注册成功');
                    router.push({ name: "login" }); // 注册成功后跳转登录界面
                },
                error(mesg) {
                    error_message.value = mesg; // 显示错误信息
                    console.log(mesg);
                },
            });
        }

        return { username, password, password2, register, error_message,message };
    }
}
</script>

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
    margin-top: 30px;
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
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.09);
    border-radius: 20px 20px;
    overflow: hidden;
}

.equal-width {
    width: 30vh;
    /* 或者根据需要设置的宽度 */

}

.label-width {
    display: inline-block;
    /* 或者使用 block，根据需要选择 */
    width: 80px;
    /* 设置所需的宽度 */
}
</style>
