<template>
    <el-row>
        <el-col :span="12">
            <div class="table">
                <div class="container">

                    <!-- 搜索框 -->
                    <div class="handle-box">
                        <el-button-group>
                            <el-button type="primary" icon="el-icon-circle-plus-outline" @click="generateFunc" >生成功能点</el-button>
                        </el-button-group>

                        <el-input v-model="entity.table.params.funcName" placeholder="名称" style="width: 150px" clearable></el-input>
                        <el-input v-model="entity.table.params.funcCode" placeholder="编码" style="width: 150px" clearable></el-input>
                        <el-input v-model="entity.table.params.funcValue" placeholder="url" style="width: 150px" clearable></el-input>
                        <el-button type="primary" icon="el-icon-search" @click="$entity_page()"></el-button>
                    </div>

                    <!-- 数据表格 -->
                    <el-table :data="entity.table.list" ref="entity_table" stripe size="mini" class="table" highlight-current-row @row-click="selectRow">
                        <el-table-column type="index" width="55" align="center"></el-table-column>

                        <el-table-column prop="funcName" label="功能点名称"></el-table-column>
                        <el-table-column prop="funcCode" label="功能点编码"></el-table-column>
                        <el-table-column prop="funcValue" label="url"></el-table-column>
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
                        <el-form-item label="功能点名称" prop="funcName">
                            <el-input type="text" v-model="entity.form.data.funcName"></el-input>
                        </el-form-item>
                        <el-form-item label="功能点编码" prop="funcCode">
                            <el-input type="text" v-model="entity.form.data.funcCode"></el-input>
                        </el-form-item>
                        <el-form-item label="url" prop="funcValue">
                            <el-input type="text" v-model="entity.form.data.funcValue"></el-input>
                        </el-form-item>
                    </el-form>
                    <span slot="footer" class="dialog-footer">
                <el-button @click="entity.form.visible = false">取 消</el-button>
                <el-button type="primary" @click="$entity_save()">确 定</el-button>
            </span>
                </el-dialog>
            </div>
        </el-col>
        <el-col :span="12">
            <sys-action ref="sysAction"></sys-action>
        </el-col>

    </el-row>

</template>

<script>
    import SysAction from './SysAction'
    export default {
        name: "SysFunc", //将驼峰转换为'/'并作为访问接口的URL, 例：SysUser的URL为/console/sys/user
        components: {SysAction},
        data(){
            return {
                entity:{
                    table:{
                        index: -1, //行的下标
                        list: [],  //数据集合
                        params: {orderBy:'funcCode'}, //查询参数
                        pageSize: 15, //每页大小
                        recordsTotal: 0 //总记录数
                    },
                    form:{
                        data:{funcId:'',funcCode:'',funcName:'',funcValue:''},
                        visible:false, //是否显示
                        title:'新增', //标题
                        rules:{
                            funcCode:[
                                { required: true, message: '请输入功能点编码', trigger: 'blur' },
                                { validator: this.$entity_unique() ,message: '编码已存在', trigger: 'blur'}
                            ]
                        }
                    }
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
                this.$refs.sysAction.entity.table.params.funcId = row.funcId;
                this.$refs.sysAction.$entity_page();
                Object.assign(this.$refs.sysAction.func, row);
            },
            //生成功能点
            generateFunc(){
                this.$confirm('是否生成功能点','提示',{
                    type:'info'
                }).then(()=>{
                    this.$axios.post('/console/sys/func/saveAllApi').then(res=>{
                        if(res.code==='success'){
                            this.$message.success('生成成功');
                            this.$entity_page();
                        }else{
                            this.$message.error(res.message);
                        }
                    })
                }).catch(()=>{})
            }
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
</style>
