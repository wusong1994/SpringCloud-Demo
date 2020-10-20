<template>
    <div class="table">
        <div class="container">

            <!-- 搜索框 -->
            <div class="handle-box">
                <el-input v-model="entity.table.params.actionName" placeholder="名称" style="width: 120px" clearable></el-input>
                <el-input v-model="entity.table.params.actionCode" placeholder="编码" style="width: 120px" clearable></el-input>
                <el-button type="primary" icon="el-icon-search" @click="$entity_page()"></el-button>
            </div>

            <!-- 数据表格 -->
            <el-table :data="entity.table.list" ref="entity_table" stripe size="mini" class="table" highlight-current-row @select-all="select" @select="select" >
                <el-table-column prop="actionName" label="操作点名称"></el-table-column>
                <el-table-column prop="actionCode" label="操作点编码" :formatter="actionFormatter"></el-table-column>
                <el-table-column type="selection" width="55"></el-table-column>
            </el-table>

            <!-- 分页 -->
            <div class="pagination">
                <el-pagination background layout="total,prev,pager,next" :total="entity.table.recordsTotal" :page-size="entity.table.pageSize" @current-change="page"></el-pagination>
            </div>
        </div>


    </div>
</template>

<script>
    export default {
        name: "SysRoleAction", //将驼峰转换为'/'并作为访问接口的URL, 例：SysUser的URL为/console/sys/user
        data(){
            return {
                entity:{
                    table:{
                        index: -1, //行的下标
                        list: [],  //数据集合
                        params: {orderBy:'method, actionCode',roleId:'0'}, //查询参数
                        pageSize: 15, //每页大小
                        recordsTotal: 0 //总记录数
                    },
                    form:{
                        data:{roacId:'',roleId:'',funcId:'',actionId:''},
                        visible:false, //是否显示
                        title:'新增', //标题
                        rules:{} //验证规则
                    }
                }
            }
        },
        methods:{
            page(pageNum){
                let promise = this.$entity_list({url:'/console/sys/action/pageJoinRole',pageNum:pageNum});
                promise.then(res=>{
                    this.entity.table.list.forEach(e=>{
                        if(e.roleId) this.$refs.entity_table.toggleRowSelection(e,true);
                    })
                })
            },
            actionFormatter(row){
                return row.method + ' ' + row.actionCode
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
                let data = {roleId:this.entity.table.params.roleId,funcId:this.entity.table.params.funcId,actionIds:ids.join(',')}
                this.$axios.post('/console/sys/roleAction/batchOperation',data).then(res=>{
                    if(res.code==='success'){
                        this.$message.success('授权成功');
                    }else{
                        this.$message.error(res.message);
                    }
                })
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
