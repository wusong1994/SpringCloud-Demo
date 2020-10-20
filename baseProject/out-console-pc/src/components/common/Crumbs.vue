<template>
    <div class="crumbs">
        <el-breadcrumb separator="/">
            <template v-for="item in crumbItems">
                <el-breadcrumb-item><i class="el-icon-fa" :class="item.icon" v-if="item.icon"></i> {{item.title}}</el-breadcrumb-item>
            </template>
        </el-breadcrumb>
    </div>
</template>

<script>
    import bus from '../common/bus';
    export default {
        data(){
            return {
                "crumbItems":[]
            }
        },
        created(){
            bus.$on('sidebar-select',msg=>{
                this.crumbItems = msg;
            })
        }
    }
</script>

<style scoped>

</style>