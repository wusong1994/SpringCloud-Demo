export default {
    install(Vue, options){

        /**
         * 将驼峰转下划线
         * @param {str} 字符串
         * @param [separate] 分隔符，默认为'_'
         * @returns {void | string | *}
         */
        let toLowerLine = function(str, separate) {
            if(str.match(/[A-Z]/)['index']==0) str = str.substring(0,1).toLowerCase() + str.substring(1);
            separate = separate ? separate : '_';
            let temp = str.replace(/[A-Z]/, function (match) {
                return separate + match.toLowerCase();
            });
            return temp;
        }

        /**
         * 获取BaseCtrl的ajax参数
         * @param vm Vue实例
         * @param method 对应BaseCtrl的方法名称[info,list,page,insert,update,delete]
         * @returns {{url: string, method: string, data}}
         */
        let getAjaxOptions = function(vm,method){
            let name = vm.$options.name;
            if(!name ) console.error('name must be not null!');
            name = toLowerLine(name,'/');
            let url = '/console/' + name;

            let type = 'get';
            let data = {};
            switch (method) {
                case 'page':
                    url += '/' + method;
                    type = 'post';
                    data = vm.entity.table.params?vm.entity.table.params:{};
                    data.pageSize = vm.entity.table.pageSize?vm.entity.table.pageSize :15;
                    data.pageNum = vm.entity.table.pageNum?vm.entity.table.pageNum:1;
                    break;
                case 'info':
                    if(vm.entity.form.data.pk) url += "/" + vm.entity.form.data.pk;
                    type = 'get';
                    break;
                case 'insert':
                    type = 'post';
                    data = vm.entity.form.data;
                    break;
                case 'update':
                    type = 'put';
                    data = vm.entity.form.data;
                    break;
                case 'delete':
                    if(vm.entity.form.data.pk) url += "/" + vm.entity.form.data.pk;
                    type = 'delete';
                    break;
                case 'exist':
                    url += '/' + method;
                    type = 'post';
                    break
            }

            return {
                url:url,
                method:type,
                data:data
            }
        }

        /**
         * 将格式为yyyy-MM-dd HH:mm:ss的日期转换为yyyy-MM-ddTHH:mm:ss
         * @param data
         * @returns {*}
         */
        let convertDatetime = function(data){
            if(data==null || typeof data != 'object') return data;
            let regex = /^\d{4}-\d{2}-\d{2} \d{2}\:\d{2}:\d{2}$/;
            for(let key of Object.keys(data)){
                if(regex.test(data[key])){
                    data[key] = data[key].replace(" ", "T");
                }
            }
            return data;
        }

        /**
         * 获取列表并返回Promise
         * @param [url] api接口，默认为/page
         * @param [method] ajax方式，默认为post
         * @param [pageNum] 页码，默认为1
         * @returns {PromiseLike<T | never> | Promise<T | never>}
         */
        Vue.prototype.$entity_list = function(opts){
            if(!opts || typeof opts != 'object') opts = {};
            let {url,method,pageNum} = opts;

            let ajaxOptions = getAjaxOptions(this,'page');
            if(url) ajaxOptions.url = url;
            if(method) ajaxOptions.method = method;

            pageNum = pageNum ? pageNum : 1;
            ajaxOptions.data.pageNum = pageNum;
            this.entity.table.pageNum = pageNum;

            let params = this.entity.table.params?this.entity.table.params : {};
            Object.assign(ajaxOptions.data, params);
            return this.$axios(ajaxOptions).then(res=>{
                if(res.code==="success"){
                    this.entity.table.list = res.data.content;
                    this.entity.table.recordsTotal = res.data.totalElements;
                } else{
                    this.$message.error(res.message);
                }
                return res;
            }).catch(()=>{});
        }

        /**
         * 分页查询
         * @param [pageNum] 页码
         * @returns {PromiseLike<T|never>|Promise<T|never>}
         */
        Vue.prototype.$entity_page = function (pageNum) {
            return this.$entity_list({pageNum:pageNum});
        }

        Vue.prototype.$entity_add = function(){
            if(this.entity.table)  this.entity.table.index = -1;
            this.entity.form.visible = true;
            this.entity.form.title = '新增';
            if(this.$refs.entity_table){//清除table高亮行
                this.$refs.entity_table.setCurrentRow();
            }
        }

        /**
         * 显示编辑框并对表单赋值
         * @param [$index] 行的下标
         * @param [row] 行数据
         * @param [url] api接口，默认为/{id}
         * @param [method] ajax方式，默认为get
         * @param [data] 访问数据
         */
        Vue.prototype.$entity_edit = function(opts){

            if(!opts || typeof opts != 'object') opts = {};
            let {$index,row,url,method,data} = opts;
            if(!row){
                row = {};
                Object.assign(row,this.entity.form.data);
                if(!row.pk){
                    this.$message({
                        type:'error',
                        message:'请选择要编辑的行',
                        duration:1700
                    });
                    return;
                }
            }
            if(!row.pk) console.error('pk must be not null!');

            this.entity.form.visible = true;

            if(this.entity.table) this.entity.table.index = $index;
            this.entity.form.title = '编辑';

            let ajaxOptions = getAjaxOptions(this,"info");
            ajaxOptions.url = ajaxOptions.url+ "/" +row.pk;
            if(url) ajaxOptions.url = url;
            if(method) ajaxOptions.method = method;
            if(data) ajaxOptions.data = data;

            return this.$axios(ajaxOptions).then(res=>{
                if(res.code=="success"){
                    Object.assign(this.entity.form.data, res.data)
                }else{
                    this.$message({
                        type:"error",
                        message:res.message
                    });
                }
                return res;
            }).catch(()=>{});
        }

        /**
         * 保存
         * @param [url] api接口，默认为 /save
         * @param [method] ajax方式，默认为 put
         * @param [refresh] 是否刷新页面，默认为true
         * @returns {Promise<T | never | boolean>}
         */
        Vue.prototype.$entity_save = function(opts){
            if(!opts || typeof opts != 'object') opts = {};
            let {url,method,refresh} = opts;
            refresh = refresh==undefined?true:refresh;
            let validFunc = function () {
                let that = this;
                return new Promise(function (resolve, reject){
                    that.$refs.entity_form.validate((valid)=>{
                        if(valid){
                            resolve();
                        }else{
                            reject();
                        }
                    });
                })
            }

            return validFunc.call(this).then(()=>{
                let type = this.entity.form.data.pk ? "update" : "insert"
                let ajaxOptions = getAjaxOptions(this, type);
                if(url) ajaxOptions.url = url;
                if(method) ajaxOptions.method = method;

                return this.$axios(ajaxOptions).then(res=>{
                    let message = '保存成功';
                    let type = 'success';
                    if(res.code == 'success'){
                        if(refresh){
                            this.$entity_page(this.entity.table.pageNum);
                        }
                        this.entity.form.visible = false;
                    }else{
                        message = res.message?res.message:message;
                        type = 'error';
                    }
                    this.$message({
                        type:type,
                        message:message,
                        duration:1700
                    });
                    return res;
                }).catch(err=>{return err});
            }).catch(res=>{
                return false;
            })
        }

        /**
         * 删除
         * @param [$index] 行的下标
         * @param [row] 行数据
         * @param [url] api接口，默认为 /delete/{id}
         * @param [method] ajax方式，默认为 delete
         * @param [data] 参数
         * @param [tips] 删除提示，true-提示，false-不提示，默认为true
         * @param [tipsMsg] 删除提示信息，默认为 “删除不可恢复，是否确定删除？”
         * @param [refresh] 是否刷新页面，默认为true
         * @returns {PromiseLike<T | never> | Promise<T | never>}
         */
        Vue.prototype.$entity_del = function(opts){
            if(!opts || typeof opts != 'object') opts = {};
            let {$index,row,url,method,data,tips,tipsMsg,refresh} = opts;
            refresh = refresh==undefined?true:refresh;
            if(typeof tips == 'undefined') tips = true;
            let pk = null;
            if(($index || $index ==0) && row){
                pk = row.pk
            }else{
                if(!this.entity.form.data.pk){
                    this.$message({
                        type:'error',
                        message:'请选择要删除的行',
                        duration:1700
                    });
                    return;
                }
                pk = this.entity.form.data.pk
            }

            let delFunc = function () {
                if(!pk){
                    console.error('please choose row for delete!');
                    return;
                }

                let ajaxOptions = getAjaxOptions(this,"delete");
                if(url) ajaxOptions.url = url
                else ajaxOptions.url += '/'+pk;
                if(method) ajaxOptions.method = method;
                if(data) ajaxOptions.data = data;

                return this.$axios(ajaxOptions).then(res=>{
                    let message = '删除成功';
                    let type = 'success';
                    if(res.code != 'success'){
                        message = res.message?res.message:'删除失败';
                        type = 'error';
                    }else{
                        if(this.entity.table.list.length ==1){
                            this.entity.table.pageNum--;
                        }
                        if(refresh) this.$entity_page(this.entity.table.pageNum);
                    }
                    this.$message({
                        type: type,
                        message: message,
                        duration: 1700
                    });
                    this.entity.form.visible = false;
                    return res;
                }).catch(err=>{return err});
            }

            if(tips){
                tipsMsg = tipsMsg?tipsMsg:'删除不可恢复，是否确定删除？';
                return this.$confirm(tipsMsg,'提示',{
                    type:'warning'
                }).then(()=>{
                    return delFunc.call(this);
                }).catch(()=>{})
            }else{
                return delFunc.call(this);
            }
        }

        /**
         * 关闭编辑框事件
         */
        Vue.prototype.$entity_close = function(){
            //清空form
            for(let key of Object.keys(this.entity.form.data)){
                this.entity.form.data[key] = null;
            }
        }

        /**
         * 表格开关change事件
         * @param scope
         * @param field
         * @returns {Promise<T | never>}
         */
        Vue.prototype.$entity_switch = function(scope,field){
            let data = {pk:scope.row.pk}
            data[field] = scope.row[field]
            let ajaxOptions = getAjaxOptions(this, "update");
            ajaxOptions.data = data

            return this.$axios(ajaxOptions).then(res=>{
                let message = '保存成功';
                let type = 'success';
                if(res.code != 'success'){
                    message = res.message?res.message:message;
                    type = 'error';
                }
                this.$message({
                    type:type,
                    message:message,
                    duration:1700
                });
                return res;
            }).catch(err=>{return err});
        }

        /**
         * 判断值是否唯一
         * @param arr entity.form.data的字段名称，会将该字段作为参数进行访问
         * @returns {unique}
         */
        Vue.prototype.$entity_unique = function(...arr){
            let ajaxOptions = getAjaxOptions(this,"exist");

            let unique =(rule,value,callback)=>{
                ajaxOptions.data[rule.field] = value;
                if(!value) callback();
                for(let i=0;i<arr.length;i++){
                    ajaxOptions.data[arr[i]] = this.entity.form.data[arr[i]];
                }
                if(this.entity.form.data.pk){
                    ajaxOptions.data.pk = this.entity.form.data.pk;
                }
                this.$axios(ajaxOptions).then(res=>{
                    if(!res.data){
                        callback();
                    }else{
                        callback(new Error(rule.field+' must be unique'));
                    }
                }).catch(()=>{})
            }
            return unique;
        }
    }
}