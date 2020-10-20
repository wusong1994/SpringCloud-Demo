<template>
    <div class="table">
        <div class="container">

            <!-- 搜索框 -->
            <div class="handle-box">
                <el-input v-model="entity.table.params.actionName" placeholder="名称" style="width: 150px" clearable></el-input>
                <el-input v-model="entity.table.params.actionCode" placeholder="编码" style="width: 150px" clearable></el-input>
                <el-button type="primary" icon="el-icon-search" @click="$entity_page()"></el-button>
            </div>

            <!-- 数据表格 -->
            <el-table :data="entity.table.list" ref="entity_table" stripe size="mini" class="table" highlight-current-row>
                <el-table-column type="index" width="55" align="center"></el-table-column>
                <el-table-column prop="actionName" label="操作点名称"></el-table-column>
                <el-table-column prop="actionCode" label="操作点编码" :formatter="actionFormatter"></el-table-column>
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
                <el-form-item label="功能点" prop="funcName">
                    <el-input type="text" v-model="func.funcName" disabled></el-input>
                </el-form-item>
                <el-form-item label="操作点名称" prop="actionName">
                    <el-input type="text" v-model="entity.form.data.actionName"></el-input>
                </el-form-item>
                <el-form-item label="方式" prop="method">
                    <el-select v-model="entity.form.data.method" clearable>
                        <el-option
                                v-for="item in $dic_list('REQUEST_METHOD')"
                                :key="item.code"
                                :label="item.name"
                                :value="item.code">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="操作点编码" prop="actionCode">
                    <el-input type="text" v-model="entity.form.data.actionCode"></el-input>
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
        name: "SysAction", //将驼峰转换为'/'并作为访问接口的URL, 例：SysUser的URL为/console/sys/user
        data(){
            return {
                entity:{
                    table:{
                        index: -1, //行的下标
                        list: [],  //数据集合
                        params: {orderBy:'method, actionCode'}, //查询参数
                        pageSize: 15, //每页大小
                        recordsTotal: 0 //总记录数
                    },
                    form:{
                        data:{actionId:'',funcId:'',method:'',actionCode:'',actionName:'',funcName:''},
                        visible:false, //是否显示
                        title:'新增', //标题
                        rules:{
                            actionCode: [
                                { validator: this.$entity_unique('funcId','method'), message: '编码已存在', trigger: 'blur'},
                                { required: true, message: '请输入编码', trigger: 'blur' }
                            ],
                            method: [
                                { required: true, message: '请选择方式', trigger: 'blur' },
                            ]
                        }
                    }
                },
                func:{funcId:''}//功能点
            }
        },
        methods:{
            page(pageNum){
                this.$entity_page(pageNum);
            },
            close(){
                this.$entity_close();
            },
            //添加
            add(){
                if(!this.func.pk){
                    this.$message.error('请选择功能点');
                    return;
                }
                this.$entity_add();
                this.entity.form.data.funcId = this.func.pk;
            },
            //添加默认的增删该查操作点
            addDefault(){
                if(!this.func.pk){
                    this.$message.error('请选择功能点');
                    return;
                }
                this.$confirm('是否添加默认的操作点(增删改查)','提示',{
                    type:'info'
                }).then(()=>{
                    this.$axios.post('/console/sys/action/addDefault/'+this.func.pk).then(res=>{
                        if(res.code==='success'){
                            this.$message.success('添加成功');
                            this.$entity_page();
                        }else{
                            this.$message.error(res.message);
                        }
                    })
                }).catch(()=>{})
            },
            actionFormatter(row){
                return row.method + ' ' + row.actionCode
            }
        },
        created(){
        }
    }
</script>

<style scoped>
    .red{
        color: #ff0000;
    }
</style>
