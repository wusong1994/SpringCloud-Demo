module.exports = {
    publicPath: './',
    productionSourceMap: false,
    devServer: {
        proxy: {
            '/console':{
                target: process.env.VUE_APP_API_HOST,
                changeOrigin:true,
            },
            '/temp':{
                target: process.env.VUE_APP_API_HOST,
                changeOrigin:true,
            },
            '/perm':{
                target: process.env.VUE_APP_API_HOST,
                changeOrigin:true,
            }
        }
    }
}