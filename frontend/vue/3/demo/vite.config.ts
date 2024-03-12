import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'
import {resolve} from 'path'

export default defineConfig({
    plugins: [
        vue(),
        AutoImport({
            resolvers: [ElementPlusResolver()],
        }),
        Components({
            resolvers: [ElementPlusResolver()],
        }),],
    server: {
        host: true,
        // 设置端口号
        port: 8080,
        //自动打开浏览器
        open: true,
        proxy: {
            "/api": {
                // 代理目标地址
                target: "http://127.0.0.1",
                // 允许跨域
                changeOrigin: true,
                // 开启websockets代理
                ws: true,
                // 验证SSL证书
                secure: false,
                // 重写path路径
                rewrite: (path) => path.replace(/^\/api/, ""),
            },
        },
    },
    resolve: {
        alias: {
            '@': resolve('src')
        }
    }
})
