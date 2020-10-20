<template>
    <div class="sidebar">
        <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" background-color="#324157"
            text-color="#bfcbd9" active-text-color="#20a0ff" unique-opened router @select="onSelect">
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-submenu :index="item.index" :key="item.index">
                        <template slot="title">
                            <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
                        </template>
                        <template v-for="subItem in item.subs">
                            <el-submenu v-if="subItem.subs" :index="subItem.index" :key="subItem.index">
                                <template slot="title">{{ subItem.title }}</template>
                                <el-menu-item v-for="(threeItem,i) in subItem.subs" :key="i" :index="threeItem.index">
                                    {{ threeItem.title }}
                                </el-menu-item>
                            </el-submenu>
                            <el-menu-item v-else :index="subItem.index" :key="subItem.index">
                                {{ subItem.title }}
                            </el-menu-item>
                        </template>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index" :key="item.index">
                        <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
    import bus from '../common/bus';
    export default {
        data(){
            return {
                collapse: false,
                items: [],
                menus: []
            }
        },
        computed:{
            onRoutes(){
                return this.$route.path.replace('/','');
            }
        },
        methods:{
            //选中菜单的事件
            onSelect(index, indexPath){
                let items = this.items;
                let crumbItems = []
                indexPath.forEach(function(ind){
                    let temp = items.find(function(item){
                        return item.index === ind;
                    });
                    if(temp.subs) items = temp.subs;
                    crumbItems.push(temp);
                },this);
                bus.$emit("sidebar-select",crumbItems);
            },
            //加载菜单
            loadMenu(){
                this.$axios.get('/console/sys/menu/user').then(res=>{
                    if(res.code==='success'){
                        this.menus = res.data;
                        this.items = [];
                        this.addRoutes();
                        this.formatterMenu(null, this.items, 0);
                    }else{
                        console.error(res.message);
                    }
                })
            },
            //格式化菜单
            formatterMenu(itemsHolder, items, parentId){
                let tempMenus = this.menus.filter(function(menu){
                    return menu.parentId === parentId;
                });
                if(tempMenus.length<=0 && itemsHolder ){
                    delete itemsHolder.subs;
                }
                tempMenus.forEach(menu=>{
                    let a = {icon:'el-icon-fa ' +menu.icon, index: ''+menu.menuId, title: menu.menuTitle,subs:[]};
                    items.push(a);
                    this.formatterMenu(a, a.subs, menu.menuId);
                })
            },
            addRoutes(){
                let curr_menus = JSON.parse(localStorage.getItem('curr_menus'))
                if(curr_menus) return;
                let children = [];
                this.menus.forEach(el=>{
                    if(el.menuPath){
                        let path = el.menuPath;
                        children.push({
                            path:'/'+el.menuId,
                            component: resolve => require([`../page/${path}.vue`], resolve),
                            meta: { title: el.menuTitle },
                        })
                    }
                })
                let routes = [            {
                    path: '/',
                    component: resolve => require(['./Home.vue'], resolve),
                    meta: { title: '主页' },
                    children: children
                }]
                localStorage.setItem("curr_menus",JSON.stringify(this.menus));
                this.$router.addRoutes(routes);
            }
        },
        mounted(){
            // 通过 Event Bus 进行组件间通信，来折叠侧边栏
            bus.$on('collapse', msg => {
                this.collapse = msg;
            })
            this.loadMenu();
        }
    }
</script>

<style scoped>
    .sidebar{
        display: block;
        position: absolute;
        left: 0;
        top: 60px;
        bottom:0;
        overflow-y: scroll;
    }
    .sidebar::-webkit-scrollbar{
        width: 0;
    }
    .sidebar-el-menu:not(.el-menu--collapse){
        width: 210px;
    }
    .sidebar > ul {
        height:100%;
    }
</style>
