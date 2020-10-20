<template>
    <el-row>
        <el-col :span="8">
            <div class="container">
                <el-tabs v-model="tabs.role">
                    <el-tab-pane label="角色管理" name="first">
                        <div class="table">
                            <div class="container">

                                <!-- 搜索框 -->
                                <div class="handle-box">
                                    <el-button type="primary" icon="el-icon-plus" @click="$entity_add()"></el-button>
                                    <el-input v-model="entity.table.params.roleName" placeholder="名称" style="width: 120px" clearable></el-input>
                                    <el-button type="primary" icon="el-icon-search" @click="$entity_page()"></el-button>
                                </div>

                                <!-- 数据表格 -->
                                <el-table :data="entity.table.list" ref="entity_table" stripe size="mini" class="table" highlight-current-row @row-click="selectRow">
                                    <el-table-column type="index" width="55" align="center"></el-table-column>
                                    <el-table-column prop="roleName" label="名称"></el-table-column>
                                    <el-table-column label="操作" width="180" align="center">
                                        <template slot-scope="scope">
                                            <el-button type="text" icon="el-icon-edit" @click="$entity_edit(scope)">编辑</el-button>
                                            <el-button type="text" icon="el-icon-delete" class="red" @click="$entity_del(scope)">删除</el-button>
                                        </template>
                                    </el-table-column>
                                </el-table>

                                <!-- 分页 -->
                                <div class="pagination">
                                    <el-pagination background layout="total,prev,pager,next" :total="entity.table.recordsTotal" :page-size="entity.table.pageSize" @current-change="page"></el-pagination>
                                </div>
                            </div>

                            <!-- 编辑弹出框 -->
                            <el-dialog :title="entity.form.title" :visible.sync="entity.form.visible" @close="close" width="48%">
                                <el-form :model="entity.form.data" :rules="entity.form.rules" ref="entity_form" label-width="27%" class="item-form">
                                    <el-form-item label="名称" prop="roleName">
                                        <el-input type="text" v-model="entity.form.data.roleName"></el-input>
                                    </el-form-item>
                                    <el-form-item label="备注" prop="roleRemark">
                                        <el-input type="text" v-model="entity.form.data.roleRemark"></el-input>
                                    </el-form-item>
                                </el-form>
                                <span slot="footer" class="dialog-footer">
                            <el-button @click="entity.form.visible = false">取 消</el-button>
                            <el-button type="primary" @click="$entity_save()">确 定</el-button>
                        </span>
                            </el-dialog>
                        </div>
                    </el-tab-pane>
                </el-tabs>
            </div>

        </el-col>
        <el-col :span="16">

            <div class="container">
                <el-tabs v-model="tabs.permission">
                    <el-tab-pane label="权限分配" name="first"><sys-role-func ref="sysRoleFunc"></sys-role-func></el-tab-pane>
                    <el-tab-pane label="用户授权" name="second"><sys-user-role ref="sysUserRole"></sys-user-role></el-tab-pane>
                </el-tabs>
            </div>

            <!--<sys-role-func ref="sysRoleFunc"></sys-role-func>-->

        </el-col>
    </el-row>

</template>

<script>
    import SysRoleFunc from './SysRoleFunc'
    import SysUserRole from './SysUserRole'

    export default {
        name: "SysRole", //将驼峰转换为'/'并作为访问接口的URL, 例：SysUser的URL为/console/sys/user
        components:{SysRoleFunc,SysUserRole},
        data(){
            return {
                entity:{
                    table:{
                        index: -1, //行的下标
                        list: [],  //数据集合
                        params: {}, //查询参数
                        pageSize: 15, //每页大小
                        recordsTotal: 0 //总记录数
                    },
                    form:{
                        data:{roleId:'',roleName:'',roleRemark:''},
                        visible:false, //是否显示
                        title:'新增', //标题
                        rules:{
                            roleName: [
                                { required: true, message: '请输入角色名称', trigger: 'blur' }
                            ]
                        }
                    }
                },
                tabs:{
                    role:'first',
                    permission:'first',
                }
            }
        },
        methods:{
            page(pageNum){
                this.$entity_page(pageNum);
            },
            close(){
                this.$entity_close();
            },
            //选择行
            selectRow(row){
                this.$refs.sysRoleFunc.entity.table.params.roleId = row.roleId;
                this.$refs.sysRoleFunc.page();

                this.$refs.sysUserRole.entity.table.params.roleId = row.roleId;
                this.$refs.sysUserRole.page();
            },
        },
        created(){
            this.$entity_page();
        }
    }
</script>

<style scoped>
    .red{
        color: #ff0000;
    }
    /deep/ .el-tabs__header{
        margin: 0 0 8px;
    }
    /deep/ .container{
        padding: 8px;
    }
</style>
