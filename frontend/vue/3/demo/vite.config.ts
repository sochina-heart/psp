import {defineConfig, loadEnv} from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'
import {resolve} from 'path'

export default defineConfig(({command, mode}) => {
    // 根据当前工作目录中的 `mode` 加载 .env 文件
    // 设置第三个参数为 '' 来加载所有环境变量，而不管是否有 `VITE_` 前缀。
    const env = loadEnv(mode, process.cwd(), '')
    return {
        // vite 配置
        define: {
            __APP_ENV__: env.APP_ENV,
        },
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
            port: Number(env.VITE_APP_PORT),
            //自动打开浏览器
            open: false,
            proxy: {
                [`${env.VITE_APP_BASE_URL}`]: {
                    // 代理目标地址
                    target: "http://localhost:18888",
                    // 允许跨域
                    changeOrigin: true,
                    // 开启websockets代理
                    ws: true,
                    // 验证SSL证书
                    secure: false,
                    // 重写path路径
                    rewrite: (path) => path.replace(new RegExp('^' + env.VITE_APP_BASE_URL), ""),
                },
            },
        },
        resolve: {
            alias: {
                '@': resolve('src')
            }
        }
    }
})
