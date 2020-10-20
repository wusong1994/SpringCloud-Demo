<template>
    <div class="table">
        <div class="container">

            <!-- 搜索框 -->
            <div class="handle-box">
                <el-input v-model="entity.table.params.loginName" placeholder="登录名" style="width: 120px" clearable></el-input>
                <el-input v-model="entity.table.params.userName" placeholder="用户名称" style="width: 120px" clearable></el-input>
                <el-input v-model="entity.table.params.phone" placeholder="手机号" style="width: 120px" clearable></el-input>
                <el-input v-model="entity.table.params.email" placeholder="邮箱" style="width: 120px" clearable></el-input>
                <el-button type="primary" icon="el-icon-search" @click="page"></el-button>
            </div>

            <!-- 数据表格 -->
            <el-table :data="entity.table.list" ref="entity_table" stripe size="mini" class="table" highlight-current-row @select-all="select" @select="select">
                <el-table-column type="index" width="55" align="center"></el-table-column>
                <el-table-column prop="loginName" label="登录名"></el-table-column>
                <el-table-column prop="userName" label="用户名称"></el-table-column>
                <el-table-column prop="phone" label="手机号"></el-table-column>
                <el-table-column prop="email" label="邮箱"></el-table-column>
                <el-table-column type="selection" width="55" ></el-table-column>
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
        name: "SysUserRole", //将驼峰转换为'/'并作为访问接口的URL, 例：SysUser的URL为/console/sys/user
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
                        data:{usroId:'',userId:'',roleId:''},
                        visible:false, //是否显示
                        title:'新增', //标题
                        rules:{} //验证规则
                    }
                }
            }
        },
        methods:{
            page(pageNum){
                this.$entity_list({url:'/console/sys/user/joinPage',pageNum:pageNum}).then(res=>{
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
                let data = {roleId:this.entity.table.params.roleId,userIds:ids.join(',')}
                this.$axios.post('/console/sys/userRole/batchOperation',data).then(res=>{
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
