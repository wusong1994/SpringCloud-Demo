<template>
    <el-row>
        <el-col :span="12">
            <div class="table">
                <div class="container">

                    <!-- 搜索框 -->
                    <div class="handle-box">
                        <el-input v-model="entity.table.params.funcName" placeholder="名称" style="width: 120px" clearable></el-input>
                        <el-input v-model="entity.table.params.funcCode" placeholder="编码" style="width: 120px" clearable></el-input>
                        <el-button type="primary" icon="el-icon-search" @click="$entity_page()" ></el-button>
                    </div>

                    <!-- 数据表格 -->
                    <el-table :data="entity.table.list" ref="entity_table" stripe size="mini" class="table" highlight-current-row @select-all="select" @select="select" @row-click="clickRow">
                        <el-table-column prop="funcName" label="功能点名称" ></el-table-column>
                        <el-table-column prop="funcCode" label="功能点编码"></el-table-column>
                        <el-table-column type="selection" width="55" ></el-table-column>
                    </el-table>

                    <!-- 分页 -->
                    <div class="pagination">
                        <el-pagination background layout="total,prev,pager,next" :total="entity.table.recordsTotal" :page-size="entity.table.pageSize" @current-change="page"></el-pagination>
                    </div>
                </div>
            </div>
        </el-col>
        <el-col :span="12"><sys-role-action ref="sysRoleAction"></sys-role-action></el-col>
    </el-row>

</template>

<script>
    import SysRoleAction from './SysRoleAction'
    export default {
        name: "SysRoleFunc", //将驼峰转换为'/'并作为访问接口的URL, 例：SysUser的URL为/console/sys/user
        components:{SysRoleAction},
        data(){
            return {
                entity:{
                    table:{
                        index: -1, //行的下标
                        list: [],  //数据集合
                        params: {orderBy:'funcCode'}, //查询参数
                        pageSize: 15, //每页大小
                        recordsTotal: 0, //总记录数
                        pageNum:1
                    },
                    form:{
                        data:{rofuId:'',roleId:'',funcId:''},
                        visible:false, //是否显示
                        title:'新增', //标题
                        rules:{} //验证规则
                    }
                }
            }
        },
        methods:{
            page(pageNum){
                this.entity.table.pageNum = pageNum;
                let promise = this.$entity_list({url:'/console/sys/func/pageJoinRole',pageNum:pageNum})
                promise.then(res=>{
                    this.entity.table.list.forEach(e=>{
                        if(e.roleId) this.$refs.entity_table.toggleRowSelection(e,true);
                    })
                })
            },
            //选中行
            select(selection){
                if(!this.entity.table.params.roleId || this.entity.table.list.length<=0){
                    return;
                }
                let ids = [];
                selection.forEach(element=>{
                    ids.push(element.pk);
                })
                this.$axios({url:'/console/sys/roleFunc/batchOperation',method:'post',data:{roleId:this.entity.table.params.roleId,funcIds:ids.join(',')}}).then(res=>{
                    if(res.code==='success'){
                        this.$message.success('授权成功')
                        this.page(this.entity.table.pageNum)
                        this.$refs.sysRoleAction.entity.table.params.funcId = 0;
                        this.$refs.sysRoleAction.entity.table.params.roleId = 0;
                        this.$refs.sysRoleAction.page();
                    }else{
                        this.$message.error(res.message)
                    }
                });
            },
            //单击行
            clickRow(row){
                this.$refs.sysRoleAction.entity.table.params.funcId = row.funcId;
                this.$refs.sysRoleAction.entity.table.params.roleId = row.roleId ? row.roleId : '0';
                this.$refs.sysRoleAction.page();
            },

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
