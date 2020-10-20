<template>
    <div class="table">
        <div class="container">

            <!-- 搜索框 -->
            <div class="handle-box">
                <el-button type="primary" icon="el-icon-plus" @click="add()"></el-button>
                <el-input v-model="entity.table.params.code" placeholder="编码" style="width: 150px" clearable></el-input>
                <el-input v-model="entity.table.params.name" placeholder="名称" style="width: 150px" clearable></el-input>
                <el-button type="primary" icon="el-icon-search" @click="$entity_page()"></el-button>
                <el-button type="primary"  @click="$dic_cache()">缓存<i class="el-icon-upload el-icon--right" /></el-button>
            </div>

            <!-- 数据表格 -->
            <el-table :data="entity.table.list" ref="entity_table" stripe size="mini" class="table" highlight-current-row id="dic_table">
                <!--<el-table-column type="index" width="55" align="center"></el-table-column>-->
                <el-table-column prop="type" label="类型编码"></el-table-column>
                <el-table-column prop="code" label="编码"></el-table-column>
                <el-table-column prop="name" label="名称"></el-table-column>
                <el-table-column prop="sort" label="序号"></el-table-column>
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
                <el-form-item label="类型编码" prop="type">
                    <el-input type="text" v-model="entity.form.data.type" disabled></el-input>
                </el-form-item>
                <el-form-item label="编码" prop="code">
                    <el-input type="text" v-model="entity.form.data.code"></el-input>
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
</template>

<script>
    import Sortable from 'sortablejs';

    export default {
        name: "SysDic", //将驼峰转换为'/'并作为访问接口的URL, 例：SysUser的URL为/console/sys/user
        data(){
            return {
                entity:{
                    table:{
                        index: -1, //行的下标
                        list: [],  //数据集合
                        params: {
                            orderBy: 'sort',
                        }, //查询参数
                        pageSize: 15, //每页大小
                        recordsTotal: 0 //总记录数
                    },
                    form:{
                        data:{dicId:'',type:'',code:'',name:'',sort:''},
                        visible:false, //是否显示
                        title:'新增', //标题
                        rules:{
                            code: [
                                {required:true, message:'请输入编码', trigger:'blur'},
                                {validator: this.$entity_unique('type'), message:'编码已存在', trigger:'blur'}
                            ],
                            name: [
                                {required:true, message:'请输入名称', trigger:'blur'},
                            ]
                        } //验证规则
                    }
                },
                dicType:{}
            }
        },
        methods:{
            page(pageNum){
                this.$entity_page(pageNum);
            },
            close(){
                this.$entity_close();
            },
            //打开新增编辑框
            add(){
                if(!this.dicType.code){
                    this.$message.error('请选择字典类型');
                    return;
                }
                this.$entity_add();
                this.entity.form.data.type = this.dicType.code;
            },
            //验证编码
            validCode(){
                let valid = (rule,value,callback)=>{
                    let {type,code,pk} = this.entity.form.data;
                    let opts = {url:'/console/sys/dic/list',method:'post',data:{type,code,pk}};
                    this.$axios(opts).then(res=>{
                        if(res.code === 'success'){
                            if(res.data.length>0) callback(new Error('编码已存在'));
                            else callback();
                        }else{
                            callback(new Error(res.message));
                        }
                    })
                }
                return valid;
            },
            //排序
            sort() {
                const table = document.querySelector('#dic_table div.el-table__body-wrapper tbody');
                const self = this;
                Sortable.create(table, {
                    onEnd({oldIndex,newIndex}) {
                        let oldRow = self.entity.table.list[oldIndex];
                        let newRow = self.entity.table.list[newIndex];

                        self.$axios({url:'/console/sys/dic/sort',method: 'post', data:{pk:oldRow.dicId,fieldName:'sort',position:newRow.sort}}).then(res=>{
                           if(res.code !== 'success'){
                               self.$message.error(res.message);
                           }else{
                               let temp = oldRow.sort;
                               oldRow.sort = newRow.sort;
                               newRow.sort = temp;
                               self.$message.success('排序成功');
                           }
                        });
                    }
                })
            }
        },
        created(){

        },
        mounted(){
            this.sort();
        }
    }
</script>

<style scoped>
    .red{
        color: #ff0000;
    }
</style>
