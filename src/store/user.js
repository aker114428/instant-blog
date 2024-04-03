import $ from "jquery";
import API_ROUTES from "@/api/api";
import { backendIP } from "@/api/backend";

const ModuleUser = {
    state: {
        username: "",
        id: 1,//这里要给个初值
        description: "",
        photo: "",
        followerCount: 0,
        fanCount: 0,
        postCount: 0,
        access: "",
        is_login: false,
    },
    getters: {
    },
    mutations: {
        //(3)根据获取的用户信息，存到全局变量state中
        updateUser(state, data) {
            state.id = data["id"];
            state.username = data["username"];
            state.photo = data["userPhoto"];
            state.description = data["selfIntroduction"];
            state.followerCount = data["followNumber"];
            state.fanCount = data["fanNumber"];
            state.postCount = data["articleNumber"];
            state.access = data.access;
            state.is_login = data.is_login;
            // 存储到本地, 用于页面刷新时的登录状态保持
            localStorage.setItem("access", data.access);
        },
        updateAccess(state, data) {
            state.access = data.access
        },
        logout(state) {
            state.id = "";
            state.username = "";
            state.photo = "";
            state.description = "";
            state.followerCount = "";
            state.fanCount = "";
            state.postCount = "";
            state.access = "";
            state.is_login = false;
            // 清除本地存储
            localStorage.removeItem("access");
        }
    },
    actions: {
        //调用后端接口，获取用户信息
        getUserInfo(context, access) {
            $.ajax({
                url: backendIP + API_ROUTES.UserInfo,
                type: "POST",
                headers: {
                    "Authorization": access // 注意：根据Bearer token类型，前面添加'Bearer '前缀
                },
                data: {
                    access_token: access,
                },
                success(resp) {
                    console.log("登錄成功獲取用戶信息");
                    console.log("獲取到的用戶信息");
                    console.log(resp);
                    if (resp.code == 0) {
                        //调用mutation的updateUser函数，把获取的用户信息，存到全局变量state中

                        context.commit("updateUser", {
                            ...(resp.data), //将resp解构(一些用户信息)
                            access: access,//再补充一些用户信息
                            is_login: true,
                        })
                    }
                    else {
                        console.log("token过期");
                        this.state.id = "";
                        this.state.username = "";
                        this.state.photo = "";
                        this.state.description = "";
                        this.state.followerCount = "";
                        this.state.fanCount = "";
                        this.state.postCount = "";
                        this.state.access = "";
                        this.state.is_login = false;
                        // 清除本地存储
                        this.localStorage.removeItem("access");
                    }
                }
            });
        },
        login(context, data) {
            //data是我们传递的值，如username和password
            //根据账号密码，获取JWT token，保存access值
            $.ajax({
                url: backendIP + API_ROUTES.login,
                type: "POST",
                data: {
                    username: data.username,
                    password: data.password
                },
                success(resp) {
                    console.log("登录响应的内容", resp);

                    if (resp.code == 0) {
                        const access = resp.data;
                        context.dispatch('getUserInfo', access); // 登陆成功后，调用getUserInfo函数，获取用户信息

                        data.success() // 如果登录成功调用组件传来的回调函数（跳转首页等）
                    }
                    //如果登录失败调用组件传来的回调函数
                    else {
                        data.error(resp.result)
                    }
                },

            });
        },
        register(context, data) {
            $.ajax({
                url: backendIP + API_ROUTES.register,
                type: "POST",
                data: {
                    username: data.username,
                    password: data.password,

                },
                success(resp) {
                    if (resp.code == 0) {
                        data.success()
                    }
                    else {
                        data.error(resp.message)
                        console.log("注册失败信息：")
                        console.log(resp)
                    }
                }
            })
        }
    },
    modules: {
    }
};

export default ModuleUser;