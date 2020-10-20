<template>
    <div class="table">
        <div class="container">

            <!-- 搜索框 -->
            <div class="handle-box">
                <el-button type="primary" icon="el-icon-plus" @click="add"></el-button>
                <!--                <el-input v-model="entity.table.params.menuTitle" placeholder="标题"></el-input>
                                <el-button type="primary" icon="el-icon-search" @click="$entity_page()"></el-button>-->
            </div>

            <!-- 数据表格 -->
            <el-table :data="treeData.lists" ref="entity_table" stripe size="mini" class="table" highlight-current-row current-row-key="menuId" row-key="menuId" id="menu_table">
                <el-table-column prop="menuTitle" label="标题" column-key="menuTitle"></el-table-column>
                <el-table-column prop="menuPath" label="路径"></el-table-column>
                <el-table-column prop="icon" label="图标"></el-table-column>
                <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" icon="el-icon-plus" @click="addSub(scope)">添加子菜单</el-button>
                        <el-button type="text" icon="el-icon-edit" @click="edit(scope)">编辑</el-button>
                        <el-button type="text" icon="el-icon-delete" class="red" @click="del(scope)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--拖拽表格-->
        </div>

        <!-- 编辑弹出框 -->
        <el-dialog :title="entity.form.title" :visible.sync="entity.form.visible" width="48%"  @close="close">
            <el-form :model="entity.form.data" :rules="entity.form.rules" ref="entity_form" label-width="27%" class="item-form">
                <el-form-item label="父菜单" prop="parentTitle" >
                    <el-input type="text" v-model="entity.form.data.parentTitle" disabled></el-input>
                </el-form-item>
                <el-form-item label="标题" prop="menuTitle">
                    <el-input type="text" v-model="entity.form.data.menuTitle"></el-input>
                </el-form-item>
                <el-form-item label="路径" prop="menuPath">
                    <el-input type="text" v-model="entity.form.data.menuPath"></el-input>
                </el-form-item>
                <el-form-item label="图标" prop="icon">
                    <el-input type="text" v-model="entity.form.data.icon" ></el-input>
                </el-form-item>
                <el-form-item label="功能点" prop="funcIds" >
                    <el-select v-model="funcIdArr" multiple placeholder="请选择" style="width: 100%">
                        <el-option
                                v-for="item in funcList"
                                :key="item.funcId"
                                :label="item.funcCode"
                                :value="item.funcId">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item v-if="firstMenu" label="主页" prop="home">
                    <el-switch
                            v-model="entity.form.data.home"
                            active-color="#13ce66"
                            inactive-color="#ff4949">
                    </el-switch>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="entity.form.visible = false">取 消</el-button>
                <el-button type="primary" @click="save()">确 定</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>
    import Sortable from 'sortablejs'
    export default {
        name: "SysMenu", //将驼峰转换为'/'并作为访问接口的URL, 例：SysUser的URL为/console/sys/user
        components:{
        },
        data(){
            return {
                entity:{
                    form:{
                        data:{menuId:null,parentTitle:null,parentId:null,funcIds:null,menuTitle:null,menuPath:null,icon:null,sort:null,home:null},
                        visible:false, //是否显示
                        title:'新增', //标题
                        rules:{
                            menuTitle:[{required:true, message:'请输入标题', trigger:'blur'}]
                        }
                    }
                },
                treeData:{
                    index:0,
                    lists:[],
                },
                funcList:[],
                firstMenu:false
            }
        },
        computed:{
            funcIdArr:{
                get(){
                    if(this.entity.form.data.funcIds){
                        return this.entity.form.data.funcIds.split(',').map(Number)
                    }else{
                        return []
                    }
                },
                set(value){
                    this.entity.form.data.funcIds = value.join(',');
                }
            }
        },
        methods:{
            close(){
                this.$entity_close()
            },
            listMenu(){
                return this.$axios.post('/console/sys/menu/tree',{parentId: 0}).then(res=>{
                    if(res.code==='success'){
                        this.treeData.lists = res.data;
                        this.treeData.index = 0;
                        this.setMenuIndex(this.treeData.lists)
                    }else{
                        this.$message.error(res.message);
                    }
                })
            },
            setMenuIndex(menus){
                menus.forEach(menu=>{
                    menu.index = this.treeData.index ++
                    if(menu.children && menu.children.length>0){
                        this.setMenuIndex(menu.children)
                    }
                })
            },
            add(){
                this.$entity_add();
                this.entity.form.data.parentId = 0;
                this.entity.form.data.parentTitle = '根菜单'
                this.firstMenu = true
                this.entity.form.data.selective = true
            },
            edit(scope){
                this.$entity_edit(scope).then(res=>{
                    if(res.data.parentId === 0){
                        this.entity.form.data['parentTitle'] = '根菜单'
                        this.firstMenu = true
                    }else{
                        let menu = this.getMenu(this.treeData.lists, res.data.parentId);
                        this.entity.form.data['parentTitle'] = menu.menuTitle;
                        this.firstMenu = false
                    }
                    this.entity.form.data.selective = false;
                })
            },
            getMenu(menus,value,key){
                key = key ? key : 'menuId'
                let subMenus = [];
                let menu = menus.find(function(el){
                    if(el[key] === value){
                        return true;
                    }else{
                        if(el.children) subMenus = subMenus.concat(el.children)
                        return false;
                    }
                })
                if(!menu){
                    return this.getMenu(subMenus, value, key);
                }else{
                    return menu;
                }
            },
            //保存
            save(){
                this.$entity_save({refresh:false}).then(res=>{
                    this.listMenu();
                })
            },
            //删除
            del(scope){

                let data = {url:'/console/sys/menu/deleteTree/'+scope.row.pk,refresh: false}
                this.$confirm('删除不可恢复，是否确定删除？','提示',{
                    type:'warning'
                }).then(()=>{
                    this.$axios.delete('/console/sys/menu/deleteTree/'+scope.row.pk).then(res=>{
                        if(res.code==='success'){
                            this.$message.success('删除成功');
                            this.listMenu()
                        } else{
                            this.$message.error(res.message);
                        }

                    })
                }).catch(()=>{})
            },
            addSub(scope){
                this.$entity_add();
                this.entity.form.data.parentId = scope.row.menuId;
                this.entity.form.data.parentTitle = scope.row.menuTitle;
                this.firstMenu = false
            },
            moveDown(scope){
                console.log(scope)
            },
            //查询func
            listFunc(){
                this.$axios.post('/console/sys/func/list',{orderBy:'funcCode'}).then(res=>{
                    if(res.code==='success') this.funcList = res.data;
                })
            },
            sort(){
                const table = document.querySelector('#menu_table div.el-table__body-wrapper tbody');
                const self = this;
                Sortable.create(table, {
                    onEnd({oldIndex,newIndex}) {
                        let oldMenu = self.getMenu(self.treeData.lists,oldIndex,"index")
                        let newMenu = self.getMenu(self.treeData.lists,newIndex,"index")
                        if(oldMenu.parentId == newMenu.parentId){
                            self.$axios.post("/console/sys/menu/sort",{pk:oldMenu.pk,parentId:oldMenu.parentId,fieldName:"sort",position:newMenu.sort}).then(res=>{
                                self.listMenu()
                            })
                        }else{
                            self.$axios.post("/console/sys/menu/move",{pk:oldMenu.pk,parentId:newMenu.parentId,position:newMenu.sort}).then(res=>{
                                self.listMenu()
                            })
                        }
                    }
                })
            }
        },
        created(){
            this.listMenu().then(()=>{
                this.sort();
            });
            this.listFunc();
        }

    }
</script>

<style scoped>
    /deep/.red{
        color: #ff0000;
    }
    /deep/ .tree-column{
        text-align: left;
    }
</style>
