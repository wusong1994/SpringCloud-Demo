export default {
    install(Vue, options){

        let dic_map = null;

        /**
         * 缓存DIC
         * @returns {PromiseLike<T | never> | Promise<T | never>}
         */
        Vue.prototype.$dic_cache = function(tip){
            if(tip==undefined) tip = true;
            return this.$axios.get('/console/sys/dic/cache').then(res=>{
                if(res.code === 'success'){
                    if(tip) this.$message.success('缓存成功')
                    dic_map = res.data;
                    localStorage.setItem('dic_map',JSON.stringify(res.data))
                }else{
                    if(tip) this.$message.error('缓存失败');
                }
            })
        }

        /**
         * 获取名称
         * @param type
         * @param code
         * @returns {*}
         */
        Vue.prototype.$dic_name = function(type,code){
            if(!dic_map){
                let t  = localStorage.getItem('dic_map');
                if(!t) return code;
                else dic_map = JSON.parse(t);
            }
            let arr = dic_map[type];
            if(!arr || arr.length <=0){
                return code;
            }
            for(let i=0;i<arr.length;i++){
                if(arr[i].code == code){
                    return arr[i].name;
                }
            }
            return code;
        }

        /**
         * 获取列表
         * @param type
         * @returns {*}
         */
        Vue.prototype.$dic_list = function(type){
            if(!dic_map){
                let t  = localStorage.getItem('dic_map');
                if(!t) return [];
                else dic_map = JSON.parse(t);
            }
            let arr = dic_map[type];
            if(!arr || arr.length <=0){
                return [];
            }
            return arr;
        }
    }
}