<template>
    <div class="table">
        <div class="container">

            <!-- 搜索框 -->
            <div class="handle-box">
                <el-button type="primary" icon="el-icon-plus" @click="openAddDialog()"></el-button>
                <el-input v-model="entity.table.params.loginName" placeholder="登录名" style="width: 150px" clearable></el-input>
                <el-input v-model="entity.table.params.userName" placeholder="用户名称" style="width: 150px" clearable></el-input>
                <el-select v-model="entity.table.params.status" placeholder="请选择状态" clearable>
                    <el-option
                            v-for="item in $dic_list('USER_STATUS')"
                            :key="item.code"
                            :label="item.name"
                            :value="item.code">
                    </el-option>
                </el-select>
                <el-input v-model="entity.table.params.phone" placeholder="手机号" style="width: 150px" clearable></el-input>
                <el-button type="primary" icon="el-icon-search" @click="$entity_page()"></el-button>
            </div>

            <!-- 数据表格 -->
            <el-table :data="entity.table.list" ref="entity_table" stripe size="mini" class="table" highlight-current-row>
                <el-table-column type="index" width="55" align="center"></el-table-column>
                <el-table-column prop="loginName" label="登录名"></el-table-column>
                <el-table-column prop="userName" label="用户名称"></el-table-column>
                <el-table-column prop="status" label="状态" :formatter="statusFormatter"></el-table-column>
                <el-table-column prop="phone" label="手机号"></el-table-column>
                <el-table-column prop="email" label="邮箱"></el-table-column>
                <el-table-column prop="createDate" label="创建时间"></el-table-column>
                <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" icon="el-icon-refresh-right" @click="resetPwd(scope)">重置密码</el-button>
                        <el-button type="text" icon="el-icon-edit" @click="edit(scope)">编辑</el-button>
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
                <el-form-item label="登录名" prop="loginName">
                    <el-input type="text" v-model="entity.form.data.loginName"></el-input>
                </el-form-item>
                <el-form-item label="用户名称" prop="userName">
                    <el-input type="text" v-model="entity.form.data.userName"></el-input>
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-switch v-model="userStatus"></el-switch>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                    <el-input type="text" v-model="entity.form.data.phone"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input type="text" v-model="entity.form.data.email"></el-input>
                </el-form-item>
                <el-form-item label="角色" prop="role" v-if="entity.form.data.loginName!='admin'">
                    <el-select v-model="list.userRole" style="width: 100%" multiple>
                        <el-option
                                v-for="item in list.role"
                                :key="item.roleId"
                                :label="item.roleName"
                                :value="item.roleId">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="entity.form.visible = false">取 消</el-button>
                <el-button type="primary" @click="$entity_save()">确 定</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>
    export default {
        name: "SysUser", //将驼峰转换为'/'并作为访问接口的URL, 例：SysUser的URL为/console/sys/user
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
                        data:{userId:'',status:'',userName:'',loginName:'',phone:'',email:'',password:'',userImage:'',createDate:'',operDate:'',roleIds:null},
                        visible:false, //是否显示
                        title:'新增', //标题
                        rules:{
                            loginName:[
                                { required: true, message: '请输入登录名', trigger: 'blur' },
                                { validator: this.$entity_unique() ,message: '登录名已存在', trigger: 'blur'}
                            ]
                        }
                    }
                },
                list:{
                    userRole:[],
                    role:[]
                }
            }
        },
        computed:{
            //用户状态
            userStatus:{
                get(){
                    return this.entity.form.data.status ==='1' ? true : false;
                },
                set(status){
                    this.entity.form.data.status = status ? '1' : '0';
                }
            }
        },
        watch:{
            'list.userRole':function (value,oldValue) {
                this.entity.form.data.roleIds = value.join(",")
            }
        },
        methods:{
            page(pageNum){
                this.$entity_page(pageNum);
            },
            close(){
                this.$entity_close();
            },
            edit(scope){
                this.list.userRole = []
                this.$entity_edit(scope).then(()=>{
                    this.loadUserRole(scope.row.pk)
                })

            },
            //重置密码
            resetPwd(scope){
                let {row} = scope;
                if(!row) this.$message.error('请选择行');
                this.$confirm('是否重置密码？','提示',{
                    type:'warning'
                }).then(()=>{
                    this.$axios.put('/console/sys/user/resetPwd',{pk: row.pk}).then(res=>{
                        if(res.code=="success"){
                            this.$message.success('重置密码成功');
                        }else{
                            this.$message.error(res.message);
                        }
                    })
                }).catch(()=>{})
            },
            //打开新增编辑框
            openAddDialog(){
                this.list.userRole = []
                this.$entity_add();
                this.entity.form.data.status = '1';
            },
            //状态格式化
            statusFormatter(row){
                return this.$dic_name('USER_STATUS',row.status);
            },
            loadUserRole(userId){
                return this.$axios.post('/console/sys/userRole/list',{userId:userId}).then(res=>{
                    if(res.code!='success') return
                    this.list.userRole = res.data.map(el=>el.roleId)
                })
            },
            loadRole(){
                this.$axios.post('/console/sys/role/list',{}).then(res=>{
                    if(res.code!='success') return
                    this.list.role = res.data
                })
            }
        },
        created(){
            this.$entity_page();
            this.loadRole()
        }
    }
</script>

<style scoped>
    .red{
        color: #ff0000;
    }
</style>
