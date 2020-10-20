import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios';
import ElementUI from 'element-ui';
import VueClipboard from 'vue-clipboard2'
import 'element-ui/lib/theme-chalk/index.css'; // 默认主题
import 'font-awesome/scss/font-awesome.scss';
import './assets/css/icon.css';
import './components/common/directives'; //弹窗拖拽组件
import "babel-polyfill";
import entity from './components/plugin/entity'
import dic from './components/plugin/dic'

Vue.config.productionTip = false
Vue.use(ElementUI, {size: 'small'});
Vue.use(VueClipboard)
Vue.use(entity);
Vue.use(dic);

//使用拦截器对返回的错误进行相对应的跳转
axios.interceptors.response.use(function (response) {
    if(response.status == 200){
        switch (response.data.code) {
            case "success":
            case "error":
                return response.data;
            case "forbidden":
                Vue.prototype.$message({
                    type: "error",
                    message: response.data.message
                });
                break;
            case "unauthorized":
                localStorage.clear()
                router.push("/login"); break;
            case "not_found":
                router.push("/404"); break;
        }
    }else{
        router.push("/404");
    }

});
Vue.prototype.$axios = axios;


let homeMenu = null
let curr_menus = JSON.parse(localStorage.getItem('curr_menus'))
if(curr_menus){
    let children = [];
    curr_menus.forEach(el=>{
        if(el.home){
            homeMenu = '/'+el.menuId
        }
        if(el.menuPath){
            let path = el.menuPath;
            children.push({
                path:'/'+el.menuId,
                component: resolve => require([`./components/page/${path}.vue`], resolve),
                meta: { title: el.menuTitle },
            })
        }
    })
    let routes = [{
        path: '/',
        component: resolve => require(['./components/common/Home.vue'], resolve),
        meta: { title: '主页' },
        children: children
    }]
    router.addRoutes(routes);
}

//使用钩子函数对路由进行权限跳转
router.beforeEach((to, from, next) => {
    const userId = localStorage.getItem('curr_userId');
    if (!userId && to.path !== '/login') {
        next('/login');
    } else if(to.path =='/') {
        if(homeMenu) next(homeMenu)
        else next()
    }else{
        next()
    }
})

new Vue({
    router,
    render: h => h(App)
}).$mount('#app')