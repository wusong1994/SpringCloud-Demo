<template>
    <el-row>
        <el-col :span="6">
            <div class="table">
                <div class="container">
                    <div class="handle-box">
                        <el-button type="primary" icon="el-icon-plus" @click="addDir()"></el-button>
                    </div>
                    <el-tree :props="props" :load="loadNode" @node-click="clickNode" :highlight-current="true" lazy :filter-node-method="filterNode" ref="tree" node-key="path" :expand-on-click-node="false">
                        <span slot-scope="{ node, data }" class="custom-tree-node">
                            <span style="align-content: center">{{node.label}}</span>
                            <span style="float:right;" v-if="data.path!='/'">
                                <el-button type="text" icon="el-icon-edit" @click.stop="editDir(node)">重命名</el-button>
                                <el-button type="text" icon="el-icon-delete" class="red" @click.stop="removeDir(node)">删除</el-button>
                            </span>
                        </span>
                    </el-tree>
                </div>

                <!-- 编辑弹出框 -->
                <el-dialog :title="form.dir.title" :visible.sync="form.dir.visible" @close="closeDir" width="30%">
                    <el-form :model="form.dir.data" :rules="form.dir.rules" ref="entity_form" label-width="27%" class="item-form">
                        <el-form-item label="路径" prop="parentPath" v-if="form.dir.data.parentPath">
                            <el-input type="text"  v-model="form.dir.data.parentPath" disabled></el-input>
                        </el-form-item>
                        <el-form-item label="目录名称" prop="name">
                            <el-input type="text" v-model="form.dir.data.name"></el-input>
                        </el-form-item>
                    </el-form>
                    <span slot="footer" class="dialog-footer">
                        <el-button @click="form.dir.visible = false">取 消</el-button>
                        <el-button type="primary" @click="saveDir()">确 定</el-button>
                    </span>
                </el-dialog>
            </div>
        </el-col>
        <el-col :span="18">
            <div class="table">
                <div class="container">
                    <!-- 搜索框 -->
                    <div class="handle-box">
                        <el-button type="primary" icon="el-icon-upload2" @click="addUpload"></el-button>
                        <!--<el-button type="danger"  icon="el-icon-delete" @click="code_disabled=false">批量删除</el-button>-->
                        <el-input placeholder="输入文件名前缀匹配" v-model="form.file.prefix" style="width: 150px" clearable>
                        </el-input>
                        <el-button type="primary" icon="el-icon-search" @click="queryFile()"></el-button>
                    </div>

                    <!-- 数据表格 -->
                    <el-table :data="list.file" ref="file_table" stripe size="mini" class="table" highlight-current-row  >
                        <el-table-column type="selection" width="30"></el-table-column>
                        <el-table-column prop="name" label="名称">
                            <template slot-scope="scope">
                                <i :class="getIcon(scope.row)"></i>
                                <span style="margin-left: 10px">{{scope.row.name}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="contentType" label="类型"  width="200"></el-table-column>
                        <el-table-column prop="formatSize" label="大小" width="150"></el-table-column>
                        <el-table-column prop="lastModified" label="日期" width="200"></el-table-column>
                        <el-table-column label="操作" width="200" align="center">
                            <template slot-scope="scope">
                                <el-button type="text" icon="el-icon-document-copy" v-clipboard:copy="scope.row.path" v-clipboard:success="onCopy" v-clipboard:error="onError">复制地址</el-button>
                                <el-button  type="text" icon="el-icon-download" @click="download(scope.row.path)">下载</el-button>
                                <el-button type="text" icon="el-icon-delete" class="red" @click="removeFile(scope.row.path)">删除</el-button>
                            </template>
                        </el-table-column>
                    </el-table>

                    <!-- 文件上传弹出框 -->
                    <el-dialog title="上传文件" :visible.sync="form.file.visible" @close="closeFile" width="30%">
                        <el-upload
                                class="upload-demo"
                                :action="form.file.action"
                                :on-success="onSuccess"
                                :on-error="onError"
                                :on-remove="onRemove"
                                :multiple="true"
                                :file-list="form.file.list">
                            <el-button slot="trigger" size="small" type="primary">点击上传</el-button>
                            <div class="el-upload el-upload--text" style="margin-left: 10px">
                                <el-button  size="small"  type="success" @click="onFinish()">确定</el-button>
                            </div>
                            <div slot="tip" class="el-upload__tip">
                                文件的命名规范如下：<br>
                                1.使用 UTF-8 编码；<br>
                                2.区分大小写；<br>
                                3.长度必须在 1-1023 字节之间；<br>
                                4.不能以 / 或者 \ 字符开头。
                            </div>
                        </el-upload>
                    </el-dialog>
                </div>
            </div>
        </el-col>
    </el-row>
</template>

<script>
    export default {
        name: "Oss.vue",
        data(){
            return {
                form:{
                    dir:{
                        title: "新增目录",
                        insert:true,
                        node:null,
                        url:null,
                        visible: false,
                        data:{parentPath:null,path:null,name:null,directory:null,leaf:null},
                        rules:{
                            name: [
                                { required: true, message: '请填写目录名称', trigger: 'blur' },
                            ]
                        }
                    },
                    file:{
                        prefix:null,
                        visible:false,
                        action:null,
                        list:[]
                    }
                },
                props: {
                    label: "name",
                    isLeaf: "leaf"
                },
                list:{
                    file:[]
                },
                filterText: ''
            }
        },
        watch:{
            filterText(val) {
                this.$refs.tree.filter(val);
            }
        },
        methods:{
            //打开目录编辑框
            addDir(type){
                let node = this.$refs.tree.getCurrentNode();
                if(node==null){
                    this.$message.error("请选择节点");
                    return
                }
                this.form.dir.visible = true

                this.form.dir.data.parentPath = node.path
                this.form.dir.data.directory = true
                this.form.dir.data.leaf = false

                this.form.dir.title = '新增目录'
                this.form.dir.insert = true
                this.form.dir.url = '/console/base/oss/mkdir';
            },
            editDir(node){
                this.form.dir.node = null
                this.form.dir.visible = true

                this.form.dir.data.parentPath = null
                Object.assign(this.form.dir.data,node.data)
                this.form.dir.data.name = null
                this.form.dir.title = '重命名'
                this.form.dir.insert = false
                this.form.dir.url = '/console/base/oss/rename'
                this.form.dir.node = node
            },
            //创建目录
            saveDir(){
                let url = this.form.dir.url
                if(this.form.dir.insert){
                    this.form.dir.data.path = this.form.dir.data.parentPath + (this.form.dir.data.parentPath.endsWith('/')?'':'/') +this.form.dir.data.name
                    url += this.form.dir.data.path
                }else{
                    url += this.form.dir.data.path +"?name="+ this.form.dir.data.name
                }

                this.$axios.post(url).then(res=>{
                    if(res.code == 'success' && res.data==true){
                        this.$message.success(this.form.dir.title+"目录成功")
                        if(this.form.dir.insert){
                            let key = this.$refs.tree.getCurrentKey()
                            let data = {}

                            Object.assign(data,this.form.dir.data)
                            data.parentPath = null
                            this.$refs.tree.append(data,key)
                        }else{
                            let node = this.form.dir.node
                            node.data.path = this.form.dir.data.path.substring(0,this.form.dir.data.path.lastIndexOf('/')+1 ) + this.form.dir.data.name
                            node.data.name = this.form.dir.data.name
                            this.form.dir.node = null
                        }

                        this.form.dir.visible = false
                    }else{
                        this.$message.error(this.form.dir.title+"目录失败")
                    }
                })
            },
            //删除目录
            removeDir(node){
                this.$axios.delete('/console/base/oss/delete'+node.data.path).then(res=>{
                    if(res.code == 'success' && res.data==true){
                        this.$message.success("删除目录成功")
                        this.$refs.tree.remove(node)
                    }else{
                        this.$message.error("删除目录失败")
                    }
                })
            },
            queryFile(){
                let node = this.$refs.tree.getCurrentNode()
                if(node==null){
                    this.$message.error("请选择节点")
                    return
                }
                this.loadFile(node.path,this.form.file.prefix)
            },

            download(path){
                let link = document.createElement('a');
                link.style.display = 'none';
                link.href = '/console/base/oss/download'+path;
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            },
            addUpload(){
                let node = this.$refs.tree.getCurrentNode()
                if(node==null){
                    this.$message.error("请选择节点")
                    return
                }
                this.form.file.action = '/console/base/oss/multiple/upload'+node.path
                this.form.file.visible = true
            },
            onSuccess(res, file, fileList){
                if(res.code != 'success'){
                    this.$message.error(file.name +'上传失败')
                }
            },
            onError(err, file, fileList){
                this.$message.error(file.name +'上传失败')
            },
            onRemove(file){
                let node = this.$refs.tree.getCurrentNode()
                let path = node.path + '/' + file.name
                this.removeFile(path,true)
            },
            onFinish(){
                this.form.file.visible = false
                this.form.file.list = []
                this.queryFile()
            },
            removeFile(path,noRefresh){
                this.$axios.delete('/console/base/oss/delete'+path).then(res=>{
                    if(res.code == 'success' && res.data==true){
                        this.$message.success("删除文件成功")
                        if(!noRefresh) this.queryFile()
                    }else{
                        this.$message.error("删除文件失败")
                    }
                })
            },
            onCopy: function (e) {
                this.$message.success("复制成功")
            },
            onError: function (e) {
                this.$message.success("该浏览器不支持")
            },
            loadNode(node, resolve){
                if (node.level === 0) {
                    resolve([{name:'bucket',path:'/',directory:true,leaf:false}])
                }else{
                    this.loadDir(node.data.path, resolve)
                }
            },
            clickNode(data){
                this.loadFile(data.path)
            },
            filterNode(value, data){
                if (!value) return true;
                return data.label.indexOf(value) !== -1;
            },
            //加载目录
            loadDir(path, resolve){
                path = path? path :''
                this.$axios.get("/console/base/oss/dir/list"+path).then(res=>{
                    if(res.code != 'success'){
                        this.$messge.error("加载目录失败")
                        return
                    }
                    return resolve(res.data);
                })
            },
            //加载文件
            loadFile(path,prefix){
                path = path? path :''
                let url = '/console/base/oss/file/list'+path
                if(prefix) url += '?prefix='+prefix
                this.$axios.get(url).then(res=>{
                    if(res.code != 'success'){
                        this.$messge.error("加载文件失败")
                    }else{
                        this.list.file = res.data
                    }
                })
            },
            //获取图标
            getIcon(row){
                let icon = 'el-icon-fa '
                let type = row.contentType? row.contentType.substring(0,row.contentType.indexOf('/')):null
                let suffix = row.name.substring(row.name.lastIndexOf('.')).toLowerCase()
                switch (type) {
                    case 'image': icon += 'fa-file-picture-o';break
                    case 'text': icon += 'fa-file-text-o';break
                    case 'video': icon += 'fa-file-video-o';break
                    case 'audio': icon += 'fa-file-audio-o';break
                    default:
                        switch (suffix) {
                            case '.doc':
                            case '.docx':
                                icon += 'fa-file-word-o';break
                            case '.xls':
                            case '.xlsx':
                                icon += 'fa-file-excel-o';break
                            case '.pdf':
                                icon += 'fa-file-pdf-o';break
                            case '.pdm':
                                icon += 'fa-file-powerpoint-o';break
                            case '.rar':
                            case '.zip':
                                icon += 'fa-file-archive-o';break
                            default:
                                icon += 'fa-file-o';break
                        }
                }
                return icon
            },
            closeDir(){
                for(let key of Object.keys(this.form.dir.data)){
                    this.form.dir.data[key] = null;
                }
            },
            closeFile(){

            }
        }
    }
</script>

<style scoped>
    .red{
        color: #ff0000;
    }
    .custom-tree-node {
        width: 100%;
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 14px;
        padding-right: 8px;
    }
    /deep/ .el-upload--text{
        border: 0px;
        width: auto;
        height: auto;
    }
</style>