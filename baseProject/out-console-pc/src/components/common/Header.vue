<template>
    <div class="header">
        <!-- 折叠按钮 -->
        <div class="collapse-btn" @click="collapseChage">
            <i class="el-icon-menu"></i>
        </div>
        <div class="logo">后台管理系统</div>
        <div class="header-right">
            <div class="header-user-con">
                <!-- 全屏显示 -->
                <!--<div class="btn-fullscreen" @click="handleFullScreen">-->
                    <!--<el-tooltip effect="dark" :content="fullscreen?`取消全屏`:`全屏`" placement="bottom">-->
                        <!--<i class="el-icon-rank"></i>-->
                    <!--</el-tooltip>-->
                <!--</div>-->
                <!-- 消息中心 -->
                <!--<div class="btn-bell">
                    <el-tooltip effect="dark" :content="message?`有${message}条未读消息`:`消息中心`" placement="bottom">
                        <router-link to="/tabs">
                            <i class="el-icon-bell"></i>
                        </router-link>
                    </el-tooltip>
                    <span class="btn-bell-badge" v-if="message"></span>
                </div>-->
                <!-- 用户头像 -->
                <div class="user-avator"><img :src="user_info.data.userImage" ></div>
                <!-- 用户名下拉菜单 -->
                <el-dropdown class="user-name" trigger="click" @command="handleCommand">
                    <span class="el-dropdown-link">
                        {{user_info.data.userName}} <i class="el-icon-caret-bottom"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="personInfo">个人信息</el-dropdown-item>
                        <el-dropdown-item command="updatePwd">修改密码</el-dropdown-item>
                        <el-dropdown-item divided  command="logout">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
        </div>

        <!-- 个人信息 -->
        <el-dialog title="个人信息" :visible.sync="user_info.visible" width="30%">


            <el-form :model="user_info.data" :rules="user_info.rules" ref="user_info" label-width="27%" class="item-form">

                <el-form-item label="头像" prop="userImage">
                    <img :src="user_info.data.userImage" id="pick-avatar" class="userImage-avator">
                    <avatar-cropper @uploaded="handleUploaded" trigger="#pick-avatar" upload-url="/console/base/oss/upload" />
                    <el-input v-model="user_info.data.userImage" style="display:none"></el-input>
                </el-form-item>
                <el-form-item label="登录名" prop="loginName">
                    <el-input v-model="user_info.data.loginName" :disabled="true" ></el-input>
                </el-form-item>
                <el-form-item label="昵称" prop="userName">
                    <el-input v-model="user_info.data.userName"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="user_info.data.email"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="cancelSave()">取 消</el-button>
                <el-button type="primary" @click="savePerson()">确 定</el-button>
            </span>
        </el-dialog>
        <!-- 修改密码 -->
        <el-dialog title="修改密码" :visible.sync="update_pwd.visible" width="30%">
            <el-form :model="update_pwd.data" :rules="update_pwd.rules" ref="update_pwd" label-width="27%" class="item-form">
                <el-form-item label="旧密码" prop="oldPwd">
                    <el-input type="password" v-model="update_pwd.data.oldPwd"></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="newPwd">
                    <el-input type="password" v-model="update_pwd.data.newPwd"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPwd">
                    <el-input type="password" v-model="update_pwd.data.confirmPwd"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="update_pwd.visible = false">取 消</el-button>
                <el-button type="primary" @click="updatePwd()">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<script>
    import bus from '../common/bus';
    import AvatarCropper from "vue-avatar-cropper";

    export default {
        data() {
            return {
                collapse: false,
                fullscreen: false,
                message: 2,
                update_pwd:{
                    visible:false,
                    data:{oldPwd:'',newPwd:'',confirmPwd:''},
                    rules:{
                        oldPwd:[{ required: true, message: '请输入旧密码', trigger: 'blur' }],
                        newPwd:[{ required: true, message: '请输入新密码', trigger: 'blur' }],
                        confirmPwd:[{ required: true, message: '请再次输入密码', trigger: 'blur' },
                            { validator: this.validConfirmPwd, trigger: 'blur' }
                        ],
                    }
                },
                user_info:{
                    visible:false,
                    data:{}
                }
            }
        },
        components: {
            AvatarCropper
        },
        methods:{
            // 用户名下拉菜单选择事件
            handleCommand(command) {
                switch (command) {
                    case 'logout':
                        this.logout();
                        break;
                    case 'personInfo':
                        this.user_info.visible = true;
                        break;
                    case 'updatePwd':
                        this.update_pwd.visible = true;
                        break;
                }
            },
            //退出登录
            logout(){
                localStorage.clear();
                this.$axios.get('/console/sys/user/logout');
                this.$router.push('/login');
            },
            //验证确认密码
            validConfirmPwd(rule,value,callback){
                if(value === '') callback(new Error('请再次输入密码'))
                else if(value !== this.update_pwd.data.newPwd) callback(new Error('两次输入密码不一致'))
                else callback();
            },
            //更新密码
            updatePwd(){
                this.$refs.update_pwd.validate(valid=>{
                    if(!valid) return;
                    this.$axios.post('/console/sys/user/updatePwd', this.update_pwd.data).then(res=>{
                        if(res.code == "success"){
                            this.$alert("修改密码成功，请重新登录", {callback:()=>{
                                    this.update_pwd.visible = false;
                                    this.logout();
                                }})
                        } else {
                            this.$message.error(res.message);
                        }
                    })
                })
            },
            //保存用户信息
            savePerson(){
                this.$axios.put('/console/sys/user/updateMyself', this.user_info.data).then(res=>{
                    if(res.code=="success"){
                        localStorage.setItem('curr_user',JSON.stringify(res.data));
                        this.setUserInfo();
                        this.user_info.visible = false;
                        this.$message.success("修改成功");
                    }else{
                        this.$message.error(res.message);
                    }
                })
            },
            //取消保存用户
            cancelSave(){
                this.user_info.visible = false;
                this.setUserInfo();
            },
            //上传头像
            handleUploaded(res){
                if(res.code!=="success") return;
                this.user_info.data.userImage = res.data;
            },
            // 侧边栏折叠
            collapseChage(){
                this.collapse = !this.collapse;
                bus.$emit('collapse', this.collapse);
            },
            // 全屏事件
            handleFullScreen(){
                let element = document.documentElement;
                if (this.fullscreen) {
                    if (document.exitFullscreen) {
                        document.exitFullscreen();
                    } else if (document.webkitCancelFullScreen) {
                        document.webkitCancelFullScreen();
                    } else if (document.mozCancelFullScreen) {
                        document.mozCancelFullScreen();
                    } else if (document.msExitFullscreen) {
                        document.msExitFullscreen();
                    }
                } else {
                    if (element.requestFullscreen) {
                        element.requestFullscreen();
                    } else if (element.webkitRequestFullScreen) {
                        element.webkitRequestFullScreen();
                    } else if (element.mozRequestFullScreen) {
                        element.mozRequestFullScreen();
                    } else if (element.msRequestFullscreen) {
                        // IE11
                        element.msRequestFullscreen();
                    }
                }
                this.fullscreen = !this.fullscreen;
            },
            //设置用户信息
            setUserInfo(){
                let user = localStorage.getItem('curr_user');
                user = JSON.parse(user);
                if(!user.userImage) user.userImage = require('../../assets/img/img.jpg');
                if(!user.userName) user.userName = user.loginName;
                this.user_info.data = {userName:user.userName,loginName:user.loginName,userImage:user.userImage,email:user.email,phone:user.phone};
            }
        },
        created(){
            this.setUserInfo();
        },
        mounted(){
            if(document.body.clientWidth < 1500){
                this.collapseChage();
            }
        }
    }
</script>
<style scoped>
    .header {
        position: relative;
        box-sizing: border-box;
        width: 100%;
        height: 60px;
        font-size: 22px;
        color: #fff;
    }
    .collapse-btn{
        float: left;
        padding: 0 21px;
        cursor: pointer;
        line-height: 60px;
    }
    .header .logo{
        float: left;
        width:250px;
        line-height: 60px;
    }
    .header-right{
        float: right;
        padding-right: 50px;
    }
    .header-user-con{
        display: flex;
        height: 60px;
        align-items: center;
    }
    .btn-fullscreen{
        transform: rotate(45deg);
        margin-right: 5px;
        font-size: 24px;
    }
    .btn-bell, .btn-fullscreen{
        position: relative;
        width: 30px;
        height: 30px;
        text-align: center;
        border-radius: 15px;
        cursor: pointer;
    }
    .btn-bell-badge{
        position: absolute;
        right: 0;
        top: -2px;
        width: 8px;
        height: 8px;
        border-radius: 4px;
        background: #f56c6c;
        color: #fff;
    }
    .btn-bell .el-icon-bell{
        color: #fff;
    }
    .user-name{
        margin-left: 10px;
    }
    .user-avator{
        margin-left: 20px;
    }
    .user-avator img{
        display: block;
        width:40px;
        height:40px;
        border-radius: 50%;
    }
    .userImage-avator {
        width: 80px;
        height: 80px;
        border-radius: 8%;
    }
    .el-dropdown-link{
        color: #fff;
        cursor: pointer;
    }
    .el-dropdown-menu__item{
        text-align: center;
    }
</style>
