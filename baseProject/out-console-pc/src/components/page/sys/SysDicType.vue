<template>
    <el-row>
        <el-col :span="8">
            <div class="table">
                <div class="container">

                    <!-- 搜索框 -->
                    <div class="handle-box">
                        <el-button type="primary" icon="el-icon-plus" @click="code_disabled=false;$entity_add()"></el-button>
                        <el-input v-model="entity.table.params.code" placeholder="编码" style="width: 150px" clearable></el-input>
                        <el-input v-model="entity.table.params.name" placeholder="名称" style="width: 150px" clearable></el-input>
                        <el-button type="primary" icon="el-icon-search" @click="$entity_page()"></el-button>
                    </div>

                    <!-- 数据表格 -->
                    <el-table :data="entity.table.list" ref="entity_table" stripe size="mini" class="table" highlight-current-row @row-click="selectRow">
                        <el-table-column type="index" width="55" align="center"></el-table-column>
                        <el-table-column prop="code" label="编码"></el-table-column>
                        <el-table-column prop="name" label="名称"></el-table-column>
                        <el-table-column label="操作" width="180" align="center">
                            <template slot-scope="scope">
                                <el-button type="text" icon="el-icon-edit" @click="code_disabled=true;$entity_edit(scope)">编辑</el-button>
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
                <el-dialog :title="entity.form.title" :visible.sync="entity.form.visible" @close="close" width="30%">
                    <el-form :model="entity.form.data" :rules="entity.form.rules" ref="entity_form" label-width="27%" class="item-form">
                        <el-form-item label="编码" prop="code">
                            <el-input type="text" v-model="entity.form.data.code" :disabled="code_disabled"></el-input>
                        </el-form-item>
                        <el-form-item label="名称" prop="name">
                            <el-input type="text" v-model="entity.form.data.name"></el-input>
                        </el-form-item>
                    </el-form>
                    <span slot="footer" class="dialog-footer">
                <el-button @click="entity.form.visible = false">取 消</el-button>
                <el-button type="primary" @click="$entity_save()">确 定</el-button>
            </span>
                </el-dialog>
            </div>
        </el-col>
        <el-col :span="16">
            <sys-dic ref="sysDic"></sys-dic>
        </el-col>
    </el-row>

</template>

<script>
    import SysDic from './SysDic'
    export default {
        name: "SysDicType", //将驼峰转换为'/'并作为访问接口的URL, 例：SysUser的URL为/console/sys/user
        components: {SysDic},
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
                        data:{dityId:'',code:'',name:''},
                        visible:false, //是否显示
                        title:'新增', //标题
                        rules:{
                            code:[
                                { required: true, message: '请输入编码', trigger: 'blur' },
                                { validator: this.$entity_unique() ,message: '编码已存在', trigger: 'blur'}
                            ],
                            name: [
                                { required: true, message: '请输入名称', trigger: 'blur' },
                            ]
                        }
                    }
                },
                code_disabled:false
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
                this.$refs.sysDic.entity.table.params.type = row.code;
                this.$refs.sysDic.$entity_page();
                Object.assign(this.$refs.sysDic.dicType, row);
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
</style>
