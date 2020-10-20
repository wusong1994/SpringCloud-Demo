<template>
    <div class="table">
        <div class="container">

            <!-- 搜索框 -->
            <div class="handle-box">
                <el-button type="primary" icon="el-icon-plus" @click="$entity_add()"></el-button>
                <el-input v-model="entity.table.params.propKey" placeholder="键" style="width: 150px" clearable></el-input>
                <el-button type="primary" icon="el-icon-search" @click="$entity_page()"></el-button>
            </div>

            <!-- 数据表格 -->
            <el-table :data="entity.table.list" ref="entity_table" stripe size="mini" class="table" highlight-current-row>
                <el-table-column prop="propKey" label="键"></el-table-column>
                <el-table-column prop="propValue" label="值"></el-table-column>
                <el-table-column prop="propName" label="备注"></el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" icon="el-icon-edit" @click="$entity_edit(scope)">编辑</el-button>
                        <el-button type="text" icon="el-icon-delete" class="red" @click="$entity_del(scope)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 分页 -->
            <div class="pagination">
                <el-pagination background layout="total,prev,pager,next" :total="entity.table.recordsTotal" :page-size="entity.table.pageSize" :current-page="entity.table.pageNum" @current-change="page"></el-pagination>
            </div>
        </div>

        <!-- 编辑弹出框 -->
        <el-dialog :title="entity.form.title" :visible.sync="entity.form.visible" @close="close" width="48%">
            <el-form :model="entity.form.data" :rules="entity.form.rules" ref="entity_form" label-width="27%" class="item-form">
                <el-form-item label="键" prop="propKey">
                    <el-input type="text" v-model="entity.form.data.propKey"></el-input>
                </el-form-item>
                <el-form-item label="值" prop="propValue">
                    <el-input type="text" v-model="entity.form.data.propValue"></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="propName">
                    <el-input type="text" v-model="entity.form.data.propName"></el-input>
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
        name: "SysProperties", //将驼峰转换为'/'并作为访问接口的URL, 例：SysUser的URL为/console/sys/user
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
                        data:{propId:null,propKey:null,propName:null,propValue:null},
                        visible:false, //是否显示
                        title:'新增', //标题
                        rules:{
                            propKey:[
                                { required: true, message: '请输入键', trigger: 'blur' },
                                { validator: this.$entity_unique() ,message: '键已存在', trigger: 'blur'}
                            ],
                            propValue:[
                                { required: true, message: '请输入值', trigger: 'blur' }
                            ]
                        } //验证规则
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
