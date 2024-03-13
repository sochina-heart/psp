import axios, {AxiosInstance, AxiosRequestConfig} from "axios";
import {handleMessage} from "./status"; // 引入状态码文件
import { ElMessage } from "element-plus"; // 引入el 提示框，这个项目里用什么组件库这里引什么

// 设置接口超时时间
axios.defaults.timeout = 60000;
// axios.defaults.baseURL = "/api" || "";  // 自定义接口地址

interface requestType {
    url: string
    params?: any
}

const service: AxiosInstance = axios.create({
    baseURL:import.meta.env.VITE_APP_BASE_URL,
    timeout:30000,
});


// 响应拦截
service.interceptors.response.use(
    (response) => {
        return response.data;
    },
    (error) => {
        const { response } = error.data;
        if (response) {
            // 请求已发出，但是不在2xx的范围
            handleMessage(response.code); // 传入响应码，匹配响应码对应信息
            return Promise.reject("error");
        } else {
            ElMessage.warning("网络连接异常,请稍后再试!");
        }
    }
);

// 封装 请求并导出
export function request(data: any) {
    return new Promise((resolve, reject) => {
        const promise = axios(data);
        //处理返回
        promise
            .then((res: any) => {
                resolve(res.data);
            })
            .catch((err: any) => {
                reject(err.data);
            });
    });
}

/**
 * @description GET
 */
const GET = ({url,params}: requestType) => {
    return service({
        url,
        method:"GET",
        params
    } as AxiosRequestConfig)
}

/**
 * @description POST
 */
const POST = ({url,params}: requestType) => {
    return service({
        url,
        method:"POST",
        data:params
    } as AxiosRequestConfig)
}
/**
 * @description PUT
 */
const PUT = ({url,params}: requestType) => {
    return service({
        url,
        method:"PUT",
        data:params
    } as AxiosRequestConfig)
}

/**
 * @description DELETE
 */
const DELETE = ({url,params}: requestType) => {
    return service({
        url,
        method: 'DELETE',
        data:params
    } as AxiosRequestConfig)
}

/**
 * @description PATCH
 */
const PATCH = ({url,params}: requestType) => {

    return new Promise((resolve, reject) => {
        service
            .put(url, params)
            .then((res: any) => {
                if (res && res.status == 200) {
                    resolve(res)
                }
            })
            .catch((error: any) => {
                reject(error)
            })
    })
}
export { GET, POST, PUT, DELETE, PATCH }
