import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            component: resolve => require(['../components/common/Home.vue'], resolve),
            meta: { title: '主页' },
        },
        {
            path: '/login',
            component: resolve => require(['../components/page/index/Login.vue'], resolve)
        },
        {
            path: '/404',
            component: resolve => require(['../components/page/index/404.vue'], resolve)
        }
    ]
})
